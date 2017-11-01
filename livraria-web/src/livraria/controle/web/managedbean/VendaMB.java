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

import org.primefaces.context.RequestContext;

import dominio.Genero;
import dominio.Telefone;
import dominio.cliente.Cartao;
import dominio.cliente.Cliente;
import dominio.endereco.Endereco;
import dominio.livro.Estoque;
import dominio.livro.Livro;
import dominio.livro.Registro;
import dominio.venda.CupomPromocional;
import dominio.venda.CustoFrete;
import dominio.venda.ItemPedido;
import dominio.venda.Pagamento;
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
public class VendaMB {
	
	private Livro livro;
	private List<Livro> livros;
	private Livro livroSelecionado;
	private Estoque estoque;
	private List<Estoque> estoques;
	private Estoque estoqueSelecionado;
	private ItemPedido itemPedido;
	private List<ItemPedido> itens;
	private Pedido pedido;
	private Integer qtdeItemPedido;
	private CustoFrete frete;
	private Pagamento pagamento;
	private List<Pagamento> pagamentos;
	private CupomPromocional cupomPromocional;
	private Double descontoCupomPromocional;
	
	private Cliente cliente;
	private List<Cliente> clientes;
	
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		livro = new Livro();
		livros = new ArrayList<Livro>();
		livroSelecionado = new Livro();
		estoque = new Estoque();
		estoques = new ArrayList<Estoque>();
		estoqueSelecionado = new Estoque();
		itemPedido = new ItemPedido();
		pedido = new Pedido();
		itens = new ArrayList<ItemPedido>();
		pagamento = new Pagamento();
		pagamentos = new ArrayList<Pagamento>();
		qtdeItemPedido = 1;
		cupomPromocional = new CupomPromocional();
		descontoCupomPromocional = 0.0;
		
		cliente = new Cliente();
		clientes = new ArrayList<Cliente>();
		
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		carregaEstoquesLivros();
		carregarClientes();
	}
	
	public void carregaEstoquesLivros() {
		Livro livro = new Livro();
		List<Registro> registros = new ArrayList<Registro>();
		
		estoque.setLivro(livro);
		estoque.setRegistros(registros);
		
		RequestContext req = RequestContext.getCurrentInstance();
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(estoque);
		// PREENCHENDO LISTA DE LIVROS
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				estoques.add(i, (Estoque) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// CARREGANDO QTDE EM ESTOQUE
		for(int i = 0; i < estoques.size(); i++) {
			if(estoques.get(i).getRegistros() != null && estoques.get(i).getRegistros().size() != 0) {
				estoques.get(i).calcularQtdeEstoque();
				estoques.get(i).getLivro().setPrecoVenda(estoques.get(i).calculaValorVenda());
			}
		}
		req.update("home:dgLivros");
	}
	
	public void carregarClientes() {
		List<Endereco> listEndEntrega = new ArrayList<Endereco>();
		Endereco endResidencial = new Endereco();
		Endereco endCobranca = new Endereco();
		List<Cartao> listCartoes = new ArrayList<Cartao>();
		Cartao cartaoPreferencial = new Cartao();
		List<Telefone> listTelefones = new ArrayList<Telefone>();
		Genero genero = new Genero();
		
		cliente.setEndsEntrega(listEndEntrega);
		cliente.setEnderecoResidencial(endResidencial);
		cliente.setEndCobranca(endCobranca);
		cliente.setCartoes(listCartoes);
		cliente.setCartaoPreferencial(cartaoPreferencial);
		cliente.setTelefones(listTelefones);
		cliente.setGenero(genero);
		
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cliente);
		// PREENCHENDO LISTA DE CLIENTES
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				clientes.add(i, (Cliente) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void carregarClienteSelecionado() {
		for(Cliente c : clientes) {
			if(c.getId().equals(cliente.getId())) {
				cliente = c;
				if(c.getEndPreferencial() != null) {
					pedido.setEndEntrega(c.getEndPreferencial());
				}
				break;
			}
		}
	}
	
	public void adicionarItemCarrinho() {
		boolean flgItemAdicionado = false;
		ItemPedido itemPedido = new ItemPedido();
		
		int indPedido = 0;
		// VERIFICA SE O ITEM ADICIONADO JÁ EXISTE NO CARRINHO
		for(int i = 0; i < itens.size(); i++) {
			if(itens.get(i).getEstoque().getId().equals(estoqueSelecionado.getId())) {
				flgItemAdicionado = true;
				indPedido = i;
				break;
			}
		}
		// CASO EXISTA, SÓ SERÁ INCREMENTADA A QTDE DO PRODUTO EXISTENTE
		// CASO CONTRÁRIO, SERÁ ADICIONADO O NOVO PRODUTO NO CARRINHO
		if(flgItemAdicionado) {
			itens.get(indPedido).setQtde(itens.get(indPedido).getQtde() + qtdeItemPedido);
		} else {
			itemPedido.setQtde(qtdeItemPedido);
			itemPedido.setEstoque(estoqueSelecionado);
			itens.add(itemPedido);
			pedido.setItens(itens);
		}
		calculaValorTotalCarrinho();
		qtdeItemPedido = 1;
	}

	public void calculaValorTotalCarrinho() {
		double valorTotalPedido = 0.0;
		if(pedido.getValorTotal() == null) {
			pedido.setValorTotal(0.0);
		}
		for(ItemPedido i : itens) {
			double valorTotalItem = 0;
			valorTotalItem = i.getQtde() * i.getEstoque().getLivro().getPrecoVenda();
			valorTotalPedido += valorTotalItem;
		}
		pedido.setValorTotal(valorTotalPedido);
	}
	
	public void removerItemCarrinho(ItemPedido item) {
		RequestContext req = RequestContext.getCurrentInstance();
		pedido.getItens().remove(item);
		calculaValorTotalCarrinho();
		req.update("carrinho:itens");
		req.update("carrinho:valor");
	}
	
	public void aplicarCupomPromocional() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cupomPromocional);
		
		if(rs.getMsg() == null && rs.getEntidades().size() != 0) {
			CupomPromocional cupomTemp = new CupomPromocional();
			cupomTemp = (CupomPromocional) rs.getEntidades().get(0);
			if(cupomTemp.getCodigo().equals(cupomPromocional.getCodigo()) && cupomTemp.getAtivo() && 
					!cupomTemp.equals(pedido.getCupomPromocional())) {
				RequestContext req = RequestContext.getCurrentInstance();
				descontoCupomPromocional = pedido.getValorTotal() * cupomTemp.getPorcentagemDesconto();
				pedido.setValorTotal(pedido.getValorTotal() - descontoCupomPromocional);
				pedido.setCupomPromocional(cupomTemp);
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cupom promocional aplicado com sucesso!"));
				req.update("pedido:total");
				return;
			}
		} 
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cupom inválido!" + rs.getMsg()));
	}

	
	// GETTERS E SETTERS
	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public List<Livro> getLivros() {
		return livros;
	}

	public void setLivros(List<Livro> livros) {
		this.livros = livros;
	}

	public Livro getLivroSelecionado() {
		return livroSelecionado;
	}

	public void setLivroSelecionado(Livro livroSelecionado) {
		this.livroSelecionado = livroSelecionado;
	}

	public Estoque getEstoque() {
		return estoque;
	}

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		VendaMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}

	public List<Estoque> getEstoques() {
		return estoques;
	}

	public void setEstoques(List<Estoque> estoques) {
		this.estoques = estoques;
	}

	public Estoque getEstoqueSelecionado() {
		return estoqueSelecionado;
	}

	public void setEstoqueSelecionado(Estoque estoqueSelecionado) {
		this.estoqueSelecionado = estoqueSelecionado;
	}

	public ItemPedido getItemPedido() {
		return itemPedido;
	}

	public void setItemPedido(ItemPedido itemPedido) {
		this.itemPedido = itemPedido;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Integer getQtdeItemPedido() {
		return qtdeItemPedido;
	}

	public void setQtdeItemPedido(Integer qtdeItemPedido) {
		this.qtdeItemPedido = qtdeItemPedido;
	}

	public List<ItemPedido> getItens() {
		return itens;
	}

	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public CustoFrete getFrete() {
		return frete;
	}

	public void setFrete(CustoFrete frete) {
		this.frete = frete;
	}

	public Pagamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}

	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public CupomPromocional getCupomPromocional() {
		return cupomPromocional;
	}

	public void setCupomPromocional(CupomPromocional cupomPromocional) {
		this.cupomPromocional = cupomPromocional;
	}

	public Double getDescontoCupomPromocional() {
		return descontoCupomPromocional;
	}

	public void setDescontoCupomPromocional(Double descontoCupomPromocional) {
		this.descontoCupomPromocional = descontoCupomPromocional;
	}
	
}
