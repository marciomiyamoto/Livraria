package livraria.controle.web.managedbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import dominio.cliente.Cliente;
import dominio.livro.Estoque;
import dominio.livro.Livro;
import dominio.livro.Registro;
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
	private Cliente cliente;
	private CustoFrete frete;
	private Pagamento pagamento;
	private List<Pagamento> pagamentos;
	
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
		cliente = new Cliente();
		pagamento = new Pagamento();
		pagamentos = new ArrayList<Pagamento>();
		qtdeItemPedido = 1;
		
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		carregaEstoquesLivros();
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
	
	public void adicionarItemCarrinho() {
		boolean flgItemAdicionado = false;
		ItemPedido itemPedido = new ItemPedido();
		
		int indPedido = 0;
		// VERIFICA SE O ITEM ADICIONADO J� EXISTE NO CARRINHO
		for(int i = 0; i < itens.size(); i++) {
			if(itens.get(i).getEstoque().getId().equals(estoqueSelecionado.getId())) {
				flgItemAdicionado = true;
				indPedido = i;
				break;
			}
		}
		// CASO EXISTA, S� SER� INCREMENTADA A QTDE DO PRODUTO EXISTENTE
		// CASO CONTR�RIO, SER� ADICIONADO O NOVO PRODUTO NO CARRINHO
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
	
}
