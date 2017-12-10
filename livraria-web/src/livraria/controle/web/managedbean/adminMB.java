package livraria.controle.web.managedbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import dominio.livro.EnumTipoRegistroEstoque;
import dominio.livro.Registro;
import dominio.venda.CupomTroca;
import dominio.venda.EnumStatusItemPedido;
import dominio.venda.EnumStatusPedido;
import dominio.venda.ItemPedido;
import dominio.venda.Pedido;
import livraria.controle.web.command.ICommand;
import livraria.controle.web.command.impl.AlterarCommand;
import livraria.controle.web.command.impl.ConsultarCommand;
import livraria.controle.web.command.impl.ExcluirCommand;
import livraria.controle.web.command.impl.SalvarCommand;
import livraria.controle.web.command.impl.VisualizarCommand;
import livraria.controle.web.util.JavaMailApp;
import livraria.core.aplicacao.Resultado;
import livraria.core.dao.impl.ItemPedidoDAO;

@ManagedBean
@SessionScoped
public class adminMB {

	private Pedido pedidoSelecionado;
	private List<Pedido> pedidos;
	private int statusTemp;
	private ItemPedido itemTroca;
	private Boolean flgDevolverItens;
	
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		pedidoSelecionado = new Pedido();
		pedidos = new ArrayList<Pedido>();
		itemTroca = new ItemPedido();
		flgDevolverItens = null;
		
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
	}
	
	public void carregarPedidos() {
		Pedido pedido = new Pedido();
		List<Pedido> pedidos = new ArrayList<Pedido>();
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(pedido);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				pedidos.add(i, (Pedido) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.pedidos = pedidos;
		pedidos = null;
	}
	
	public void atualizarPedido() {
		if(statusTemp != pedidoSelecionado.getStatusPedido()) {
			pedidoSelecionado.setStatusPedido(statusTemp);
			
			command = commands.get("ALTERAR");
			Resultado rs = command.execute(pedidoSelecionado);
			
			if(rs.getMsg() == null) {
				Pedido pedido = (Pedido)rs.getEntidades().get(0);
				statusTemp = pedido.getStatusPedido();
				
				commands.get("ALTERAR");
				for(ItemPedido i : pedidoSelecionado.getItens()) {
					i.setStatus(statusTemp);
					rs = command.execute(i);
				}
				if(rs.getMsg() == null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Pedido atualizado com sucesso!"));
					return;
				}
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		}
	}

	public void verificarPagamento() {
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(pedidoSelecionado);
		if(rs.getMsg() == null) {
			Pedido pedido = (Pedido)rs.getEntidades().get(0);
			if(pedido.getStatusPedido().equals(EnumStatusPedido.APROVADO.getValue())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Pedido aprovado"));
			} else if (pedido.getStatusPedido().equals(EnumStatusPedido.REPROVADO.getValue())) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Pedido reprovado"));
			}
			statusTemp = pedido.getStatusPedido();
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		}
	}
	
	public void confirmarTrocaItem() {
		int statusTemp = itemTroca.getStatus();
		Pedido pedido = new Pedido();
		// ALTERANDO STATUS DE ITEM PARA TROCADO
		itemTroca.setStatus(EnumStatusItemPedido.TROCADO.getValue());
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(itemTroca);
		if(rs.getMsg() != null) {
			itemTroca.setStatus(statusTemp);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			// ALTERANDO STATUS DE PEDIDO PARA TROCADO
			pedido.setId(itemTroca.getIdPedido());
			pedido.setStatusPedido(EnumStatusPedido.TROCADO.getValue());
			command = commands.get("ALTERAR");
			rs = command.execute(pedido);
			if(rs.getMsg() != null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
			} else {
				// GERANDO E SALVANDO CUPOM DE TROCA
				CupomTroca cupom = new CupomTroca();
				cupom.setAtivo(true);
				cupom.setValor(itemTroca.getValorUnitario());
				cupom.setIdCliente(pedidoSelecionado.getCliente().getId());
				command = commands.get("SALVAR");
				rs = command.execute(cupom);
				if(rs.getMsg() != null) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
				} else {
					cupom = (CupomTroca)rs.getEntidades().get(0);
					// ENVIANDO EMAIL COM O CUPOM DE TROCA
					JavaMailApp mail = new JavaMailApp();
					mail.enviarEmail(pedidoSelecionado, cupom);
					// CASO O ADMIN ESCOLHA DEVOLVER O ITEM PARA O ESTOQUE, SERÁ REGISTRADO NO ESTOQUE
					if(flgDevolverItens != null && flgDevolverItens) {
						Registro registro = new Registro();
						registro.setIdEstoque(itemTroca.getEstoque().getId());
						registro.setQtde(itemTroca.getQtde());
						registro.setTipoRegistro(EnumTipoRegistroEstoque.ENTRADA.getValue());
						registro.setValorCompra(itemTroca.getValorUnitario() - (itemTroca.getEstoque().getLivro().getGrupoPrec().getMargemDeLucro() * itemTroca.getValorUnitario() / 10));
						command = commands.get("SALVAR");
						rs = command.execute(registro);
						if(rs.getMsg() != null) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
						} else {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item trocado com sucesso!"));
						}
					} else {
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item trocado com sucesso!"));
					}
				}
			}
		}
		statusTemp = pedido.getStatusPedido();
		flgDevolverItens = null;
	}
	
	public void confirmarTrocaPedido() {
		// ALTERANDO STATUS DE PEDIDO PARA TROCADO
		int statusPedidoTemp = pedidoSelecionado.getStatusPedido();
		pedidoSelecionado.setStatusPedido(EnumStatusPedido.TROCADO.getValue());
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(pedidoSelecionado);
		if(rs.getMsg() != null) {
			pedidoSelecionado.setStatusPedido(statusPedidoTemp);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			// GERANDO E SALVANDO CUPOM TROCA
			CupomTroca cupom = new CupomTroca();
			cupom.setAtivo(true);
			cupom.setValor(pedidoSelecionado.getValorTotalComDescontos());
			cupom.setIdCliente(pedidoSelecionado.getCliente().getId());
			command = commands.get("SALVAR");
			rs = command.execute(cupom);
			if(rs.getMsg() != null) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
			} else {
				cupom = (CupomTroca)rs.getEntidades().get(0);
				// ENVIANDO EMAIL COM O CUPOM DE TROCA
				JavaMailApp mail = new JavaMailApp();
				mail.enviarEmail(pedidoSelecionado, cupom);
				// CASO O ADMIN ESCOLHA DEVOLVER ITENS PARA O ESTOQUE, SERÁ REGISTRADO NO ESTOQUE
				if(flgDevolverItens != null && flgDevolverItens) {
					for(ItemPedido item : pedidoSelecionado.getItens()) {
						Registro registro = new Registro();
						registro.setIdEstoque(item.getEstoque().getId());
						registro.setQtde(item.getQtde());
						registro.setTipoRegistro(EnumTipoRegistroEstoque.ENTRADA.getValue());
						registro.setValorCompra(item.getValorUnitario() - (item.getEstoque().getLivro().getGrupoPrec().getMargemDeLucro() * item.getValorUnitario() / 100));
						command = commands.get("SALVAR");
						rs = command.execute(registro);
						if(rs.getMsg() != null) {
							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
							return;
						}
					}
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item trocado com sucesso!"));
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Item trocado com sucesso!"));
				}
			}
		}
		statusTemp = pedidoSelecionado.getStatusPedido();
		flgDevolverItens = null;
	}
	
	// GETTERS E SETTERS
	public Pedido getPedidoSelecionado() {
		return pedidoSelecionado;
	}

	public void setPedidoSelecionado(Pedido pedidoSelecionado) {
		this.pedidoSelecionado = pedidoSelecionado;
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		adminMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}

	public int getStatusTemp() {
		return statusTemp;
	}

	public void setStatusTemp(int statusTemp) {
		this.statusTemp = statusTemp;
	}

	public ItemPedido getItemTroca() {
		return itemTroca;
	}

	public void setItemTroca(ItemPedido itemTroca) {
		this.itemTroca = itemTroca;
	}

	public Boolean getFlgDevolverItens() {
		return flgDevolverItens;
	}

	public void setFlgDevolverItens(Boolean flgDevolverItens) {
		this.flgDevolverItens = flgDevolverItens;
	}
	
}
