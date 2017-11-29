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

import dominio.Genero;
import dominio.venda.EnumStatusPedido;
import dominio.venda.Pedido;
import livraria.controle.web.command.ICommand;
import livraria.controle.web.command.impl.AlterarCommand;
import livraria.controle.web.command.impl.ConsultarCommand;
import livraria.controle.web.command.impl.ExcluirCommand;
import livraria.controle.web.command.impl.SalvarCommand;
import livraria.controle.web.command.impl.VisualizarCommand;
import livraria.core.aplicacao.Resultado;

@ManagedBean
@SessionScoped
public class adminMB {

	private Pedido pedidoSelecionado;
	private List<Pedido> pedidos;
	private int statusTemp;
	
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		pedidoSelecionado = new Pedido();
		pedidos = new ArrayList<Pedido>();
		
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		carregarPedidos();
	}
	
	public void carregarPedidos() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(pedidoSelecionado);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				pedidos.add(i, (Pedido) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void atualizarPedido() {
		if(statusTemp != pedidoSelecionado.getStatusPedido()) {
			pedidoSelecionado.setStatusPedido(statusTemp);
			
			command = commands.get("ALTERAR");
			Resultado rs = command.execute(pedidoSelecionado);
			
			if(rs.getMsg() == null) {
				Pedido pedido = (Pedido)rs.getEntidades().get(0);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Pedido atualizado com sucesso!"));
				statusTemp = pedido.getStatusPedido();
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
			}
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
	
}
