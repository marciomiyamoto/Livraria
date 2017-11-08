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
import dominio.endereco.Cidade;
import dominio.endereco.Endereco;
import dominio.endereco.EnumEndereco;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.endereco.TipoEndereco;
import dominio.livro.Dimensoes;
import dominio.livro.Estoque;
import dominio.livro.Livro;
import dominio.livro.Registro;
import dominio.venda.CupomPromocional;
import dominio.venda.CupomTroca;
import dominio.venda.CustoFrete;
import dominio.venda.EnumStatusPedido;
import dominio.venda.EnumStatusPgto;
import dominio.venda.EnumTipoEnvio;
import dominio.venda.FormaPgto;
import dominio.venda.Frete;
import dominio.venda.ItemPedido;
import dominio.venda.Pagamento;
import dominio.venda.Pedido;
import livraria.controle.web.command.ICommand;
import livraria.controle.web.command.impl.AlterarCommand;
import livraria.controle.web.command.impl.ConsultarCommand;
import livraria.controle.web.command.impl.ExcluirCommand;
import livraria.controle.web.command.impl.SalvarCommand;
import livraria.controle.web.command.impl.VisualizarCommand;
import livraria.controle.web.util.CorreiosWS;
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
	private Pagamento pagamento;
	private List<Pagamento> pagamentos;
	private CupomPromocional cupomPromocional;
	private CupomTroca cupomTroca;
	private Double descontoCupomPromocional;
	private List<Endereco> listEndEntrega;
	private Endereco novoEndereco;
	private Pais paisSelecionado;
	private List<Pais> paises;
	private Estado estadoSelecionado;
	private List<Estado> estados;
	private Cidade cidadeSelecionada;
	private List<Cidade> cidades;
	private Boolean flgSalvarEnd;
	private String selFormaPgto;
	private Cartao cartao;
	private Cartao cartaoSelecionado;
	private List<Pagamento> cartoesPtgo;
	private List<Pagamento> cuponsTrocaPgto;
	private String cepDestino;
	private CustoFrete custoFretePAC;
	private CustoFrete custoFreteSedex;
	private CustoFrete custoFreteSedex10;
	private Frete frete;
	private String radioSelFrete;
	
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
		cupomTroca = new CupomTroca();
		listEndEntrega = new ArrayList<Endereco>();
		novoEndereco = new Endereco();
		paisSelecionado = new Pais();
		paises = new ArrayList<Pais>();
		estadoSelecionado = new Estado();
		estados = new ArrayList<Estado>();
		cidadeSelecionada = new Cidade();
		cidades = new ArrayList<Cidade>();
		cartao = new Cartao();
		cartaoSelecionado = new Cartao();
		cartoesPtgo = new ArrayList<Pagamento>();
		cuponsTrocaPgto = new ArrayList<Pagamento>();
		custoFretePAC = new CustoFrete();
		custoFreteSedex = new CustoFrete();
		custoFreteSedex10 = new CustoFrete();
		frete = new Frete();
		
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
		popularPaises();
	}
	
	private void popularPaises() {
		Pais pais = new Pais();
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(pais);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				paises.add(i, (Pais) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void popularEstados() {
		Estado estado = new Estado();
		estados.clear();
		cidades.clear();
		estado.setPais(paisSelecionado);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(estado);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				estados.add(i, (Estado) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void popularCidades() {
		Cidade cidade = new Cidade();
		cidades.clear();
		cidade.setEstado(estadoSelecionado);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cidade);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				cidades.add(i, (Cidade) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
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
				for(Endereco e : cliente.getEndsEntrega()) {
					listEndEntrega.add(e);
				}
				if(c.getEndPreferencial() != null) {
					pedido.setEndEntrega(c.getEndPreferencial());
					listEndEntrega.add(c.getEndPreferencial());
				} else {
					pedido.setEndEntrega(listEndEntrega.get(0));
				}
				if(c.getCartaoPreferencial() != null) {
					cartao = c.getCartaoPreferencial();
				} else {
					cartao = c.getCartoes().get(0);
				}
				break;
			}
		}
		// CARREGAR VALORES DE FRETE COM O ENDEREÇO DE PEDIDO DO CLIENTE
		cepDestino = pedido.getEndEntrega().getCep();
		calcularFrete();
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
	
	public void calcularFrete() {
		CorreiosWS correios = new CorreiosWS();
		Livro livroTemp = new Livro();
		Frete fretePacTemp = new Frete();
		Frete freteSedexTemp = new Frete();
		Frete freteSedex10Temp = new Frete();
		
		for(ItemPedido item : itens) {
			Dimensoes dim = new Dimensoes();
			dim.setAltura(item.getEstoque().getLivro().getDimensoes().getAltura());
			dim.setLargura(item.getEstoque().getLivro().getDimensoes().getLargura());
			dim.setProfundidade(item.getEstoque().getLivro().getDimensoes().getProfundidade());
			// O PESO TOTAL DO PEDIDO SERÁ CALCULADO PELA SOMA DO PESO DE TODOS OS ITENS
			if(dim.getPeso() != null) {
				dim.setPeso(dim.getPeso() + item.getEstoque().getLivro().getDimensoes().getPeso() * item.getQtde());
			} else {
				dim.setPeso(item.getEstoque().getLivro().getDimensoes().getPeso() * item.getQtde());
			}
			livroTemp.setDimensoes(dim);
		}
		// PAC
		fretePacTemp.setTipoEnvio(EnumTipoEnvio.PAC.getValue());
		fretePacTemp.setCepOrigem("08725280");
		fretePacTemp.setCepDestino(cepDestino);
		fretePacTemp.setLivro(livroTemp);
		custoFretePAC = correios.calcularFrete(fretePacTemp);
		// SEDEX
		freteSedexTemp.setTipoEnvio(EnumTipoEnvio.SEDEX.getValue());
		freteSedexTemp.setCepOrigem("08725280");
		freteSedexTemp.setCepDestino(cepDestino);
		freteSedexTemp.setLivro(livroTemp);
		custoFreteSedex = correios.calcularFrete(freteSedexTemp);
		// SEDEX10
		freteSedex10Temp.setTipoEnvio(EnumTipoEnvio.SEDEX10.getValue());
		freteSedex10Temp.setCepOrigem("08725280");
		freteSedex10Temp.setCepDestino(cepDestino);
		freteSedex10Temp.setLivro(livroTemp);
		custoFreteSedex10 = correios.calcularFrete(freteSedex10Temp);
	}
	
	public void atribuirFretePedido() {
		if(radioSelFrete.equals("pac")) {
			pedido.setCustoFrete(custoFretePAC);
		} else if(radioSelFrete.equals("sedex")) {
			pedido.setCustoFrete(custoFreteSedex);
		} else if(radioSelFrete.equals("sedex10")) {
			pedido.setCustoFrete(custoFreteSedex10);
		}
		calculaValorTotalCarrinho();
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
		pedido.setValorTotalComDescontos(valorTotalPedido);
		// CALCULAR DESCONTO DE CUPOM PROMOCIONAL, CASO EXISTA
		if(pedido.getCupomPromocional() != null && pedido.getCupomPromocional().getPorcentagemDesconto() != 0 && pedido.getCupomPromocional().getPorcentagemDesconto() != null) {
			descontoCupomPromocional = pedido.getValorTotal() * pedido.getCupomPromocional().getPorcentagemDesconto();
			pedido.setValorTotalComDescontos(pedido.getValorTotal() - descontoCupomPromocional);
		}
		// ADICIONAR VALOR DE FRETE
		if(pedido.getCustoFrete() != null) {
//			pedido.setValorTotal(pedido.getValorTotal() + pedido.getCustoFrete().getValor());
			pedido.setValorTotalComDescontos(pedido.getValorTotalComDescontos() + pedido.getCustoFrete().getValor());
		}
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
				pedido.setCupomPromocional(cupomTemp);
				calculaValorTotalCarrinho();
				cupomPromocional.setCodigo("");
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cupom promocional aplicado com sucesso!"));
				req.update("pedido:total");
				return;
			}
		} 
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cupom inválido!" + rs.getMsg()));
	}
	
	public void adicionaNovoEndereco() {
		for(Pais p : paises) {
			if(p.getId().equals(paisSelecionado.getId())) {
				paisSelecionado = p;
				break;
			}
		}
		for(Estado e : estados) {
			if(e.getId().equals(estadoSelecionado.getId())) {
				estadoSelecionado = e;
				break;
			}
		}
		for(Cidade c : cidades) {
			if(c.getId().equals(cidadeSelecionada.getId())) {
				cidadeSelecionada = c;
				break;
			}
		}
		TipoEndereco entrega = new TipoEndereco();
		estadoSelecionado.setPais(paisSelecionado);
		cidadeSelecionada.setEstado(estadoSelecionado);
		novoEndereco.setCidade(cidadeSelecionada);
		entrega.setId(EnumEndereco.ENTREGA.getValue());
		novoEndereco.setTipo(entrega);
		pedido.setEndEntrega(novoEndereco);
		listEndEntrega.add(novoEndereco);
	}
	
	public void carregarDadosCartao() {
		if(cartaoSelecionado.getId() != null && cartaoSelecionado.getId() != 0) {
			for(Cartao c : cliente.getCartoes()) {
				if(c.getId().equals(cartaoSelecionado.getId())) {
					cartao = c;
					break;
				}
			}
		}
	}
	
	public void adicionarPagamento() {
		FormaPgto formaPgto = new FormaPgto();
		Pagamento pgtoTemp = new Pagamento();
		Cartao cartaoTemp = new Cartao();
		CupomTroca cupomTrocaTemp = new CupomTroca();
		
		if(selFormaPgto.equals("cartao")) {
			cartaoTemp.setBandeira(cartao.getBandeira());
			cartaoTemp.setCodSeguranca(cartao.getCodSeguranca());
			cartaoTemp.setDtVencimento(cartao.getDtVencimento());
			cartaoTemp.setIdCliente(cartao.getIdCliente());
			cartaoTemp.setNomeImpresso(cartao.getNomeImpresso());
			cartaoTemp.setNumero(cartao.getNumero());
			
			formaPgto.setCartao(cartaoTemp);
			pgtoTemp.setFormaPgto(formaPgto);
			pgtoTemp.setValor(pagamento.getValor());
			cartoesPtgo.add(pgtoTemp);
			
			pgtoTemp.setStatus(EnumStatusPgto.PENDENTE.getValue());
			pagamentos.add(pgtoTemp);
			RequestContext.getCurrentInstance().execute("PF('dialogPgto').hide()");
		} else if(selFormaPgto.equals("cupom")) {
			if(cupomTroca.getId() != null && cupomTroca.getId() != 0) {
				cupomTrocaTemp.setAtivo(cupomTroca.getAtivo());
				cupomTrocaTemp.setCodigo(cupomTroca.getCodigo());
				cupomTrocaTemp.setIdCliente(cliente.getId());
				cupomTrocaTemp.setDtCadastro(cupomTroca.getDtCadastro());
				cupomTrocaTemp.setId(cupomTroca.getId());
				cupomTrocaTemp.setValor(cupomTroca.getValor());
				
				formaPgto.setCupomTroca(cupomTrocaTemp);
				pgtoTemp.setFormaPgto(formaPgto);
				pgtoTemp.setValor(cupomTrocaTemp.getValor());
				cuponsTrocaPgto.add(pgtoTemp);
				
				pgtoTemp.setStatus(EnumStatusPgto.PENDENTE.getValue());
				pagamentos.add(pgtoTemp);
				RequestContext.getCurrentInstance().execute("PF('dialogPgto').hide()");
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cupom inválido!"));
			}
		}
		pagamento.setValor(0);
		cartao = cliente.getCartaoPreferencial();
		selFormaPgto = "";
		cupomTroca.setCodigo("");
		cupomTroca.setId(0);
	}
	
	public void validarCupomTroca() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cupomTroca);
		
		if(rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			if(rs.getEntidades().size() > 0) {
				CupomTroca cupomTemp = (CupomTroca)rs.getEntidades().get(0);
				if(cupomTroca.getCodigo().equals(cupomTemp.getCodigo()) && cupomTemp.getAtivo().equals(true)) {
					cupomTroca = cupomTemp;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cupom válido!"));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cupom inválido!"));
			}
		}
	}
	
	public void removerCupomPromocional() {
		pedido.setCupomPromocional(null);
		calculaValorTotalCarrinho();
	}
	
	public void removerCartaoPgto(Pagamento pgto) {
		List<Pagamento> pgtosTemp = new ArrayList<Pagamento>();
		List<Pagamento> cartoesTemp = new ArrayList<Pagamento>();
		List<Pagamento> cuponsTemp = new ArrayList<Pagamento>();
		
		for(Pagamento p : pagamentos) {
			if(p.getFormaPgto().getCartao() != null && pgto.getFormaPgto().getCartao() != null) {
				if(p.getFormaPgto().getCartao().equals(pgto.getFormaPgto().getCartao())) {
					pgtosTemp.add(p);
				}
			} else if(p.getFormaPgto().getCupomTroca() != null && pgto.getFormaPgto().getCupomTroca() != null) {
				if(p.getFormaPgto().getCupomTroca().getId().equals(pgto.getFormaPgto().getCupomTroca().getId())) {
					pgtosTemp.add(p);
				}
			}
			if(pgto.getFormaPgto().getCartao() != null && pgto.getFormaPgto().getCupomTroca() == null && p.getFormaPgto().getCartao() != null &&
					pgto.getFormaPgto().getCartao().equals(p.getFormaPgto().getCartao())) {
				cartoesTemp.add(p);
			}
			if(pgto.getFormaPgto().getCartao() == null && pgto.getFormaPgto().getCupomTroca() != null && p.getFormaPgto().getCupomTroca() != null &&
					pgto.getFormaPgto().getCupomTroca().getId().equals(p.getFormaPgto().getCupomTroca().getId())) {
				cuponsTemp.add(p);
			}
		}
		pagamentos.removeAll(pgtosTemp);
		cartoesPtgo.removeAll(cartoesTemp);
		cuponsTrocaPgto.removeAll(cuponsTemp);
	}
	
	public void finalizarPedido() {
		pedido.getEndEntrega().setId(null);
		pedido.getEndEntrega().setDtCadastro(null);
		pedido.setCliente(cliente);
		pedido.setPagamentos(pagamentos);
		pedido.setStatusPedido(EnumStatusPedido.EM_PROCESSAMENTO.getValue());
		
		command = commands.get("SALVAR");
		Resultado rs = command.execute(pedido);
		// add salvar endereço, caso flgSalvarEnd == true
		
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

	public List<Endereco> getListEndEntrega() {
		return listEndEntrega;
	}

	public void setListEndEntrega(List<Endereco> listEndEntrega) {
		this.listEndEntrega = listEndEntrega;
	}

	public Endereco getNovoEndereco() {
		return novoEndereco;
	}

	public void setNovoEndereco(Endereco novoEndereco) {
		this.novoEndereco = novoEndereco;
	}

	public Pais getPaisSelecionado() {
		return paisSelecionado;
	}

	public void setPaisSelecionado(Pais paisSelecionado) {
		this.paisSelecionado = paisSelecionado;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public Estado getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(Estado estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public Cidade getCidadeSelecionada() {
		return cidadeSelecionada;
	}

	public void setCidadeSelecionada(Cidade cidadeSelecionada) {
		this.cidadeSelecionada = cidadeSelecionada;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public Boolean getFlgSalvarEnd() {
		return flgSalvarEnd;
	}

	public void setFlgSalvarEnd(Boolean flgSalvarEnd) {
		this.flgSalvarEnd = flgSalvarEnd;
	}

	public String getSelFormaPgto() {
		return selFormaPgto;
	}

	public void setSelFormaPgto(String selFormaPgto) {
		this.selFormaPgto = selFormaPgto;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public CupomTroca getCupomTroca() {
		return cupomTroca;
	}

	public void setCupomTroca(CupomTroca cupomTroca) {
		this.cupomTroca = cupomTroca;
	}

	public Cartao getCartaoSelecionado() {
		return cartaoSelecionado;
	}

	public void setCartaoSelecionado(Cartao cartaoSelecionado) {
		this.cartaoSelecionado = cartaoSelecionado;
	}

	public List<Pagamento> getCartoesPtgo() {
		return cartoesPtgo;
	}

	public void setCartoesPtgo(List<Pagamento> cartoesPtgo) {
		this.cartoesPtgo = cartoesPtgo;
	}

	public List<Pagamento> getCuponsTrocaPgto() {
		return cuponsTrocaPgto;
	}

	public void setCuponsTrocaPgto(List<Pagamento> cuponsTrocaPgto) {
		this.cuponsTrocaPgto = cuponsTrocaPgto;
	}

	public String getCepDestino() {
		return cepDestino;
	}

	public void setCepDestino(String cepDestino) {
		this.cepDestino = cepDestino;
	}

	public CustoFrete getCustoFretePAC() {
		return custoFretePAC;
	}

	public void setCustoFretePAC(CustoFrete custoFretePAC) {
		this.custoFretePAC = custoFretePAC;
	}

	public CustoFrete getCustoFreteSedex() {
		return custoFreteSedex;
	}

	public void setCustoFreteSedex(CustoFrete custoFreteSedex) {
		this.custoFreteSedex = custoFreteSedex;
	}

	public CustoFrete getCustoFreteSedex10() {
		return custoFreteSedex10;
	}

	public void setCustoFreteSedex10(CustoFrete custoFreteSedex10) {
		this.custoFreteSedex10 = custoFreteSedex10;
	}

	public Frete getFrete() {
		return frete;
	}

	public void setFrete(Frete frete) {
		this.frete = frete;
	}

	public String getRadioSelFrete() {
		return radioSelFrete;
	}

	public void setRadioSelFrete(String radioSelFrete) {
		this.radioSelFrete = radioSelFrete;
	}
	
}
