package livraria.core.controle.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dominio.EntidadeDominio;
import dominio.Genero;
import dominio.Telefone;
import dominio.TipoTelefone;
import dominio.Usuario;
import dominio.analise.LivroPeriodoVendas;
import dominio.cliente.BandeiraCartao;
import dominio.cliente.Cartao;
import dominio.cliente.Cliente;
import dominio.cliente.ClienteEnd;
import dominio.endereco.Cidade;
import dominio.endereco.Endereco;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.endereco.TipoEndereco;
import dominio.livro.Autor;
import dominio.livro.Categoria;
import dominio.livro.Dimensoes;
import dominio.livro.Editora;
import dominio.livro.Estoque;
import dominio.livro.GrupoDePrecificacao;
import dominio.livro.Livro;
import dominio.livro.LogLivro;
import dominio.livro.Registro;
import dominio.livro.Subcategoria;
import dominio.venda.CupomPromocional;
import dominio.venda.CupomTroca;
import dominio.venda.ItemBloqueioPedido;
import dominio.venda.ItemPedido;
import dominio.venda.Pedido;
import livraria.core.IDAO;
import livraria.core.IFachada;
import livraria.core.IStrategy;
import livraria.core.aplicacao.Resultado;
import livraria.core.dao.impl.AutorDAO;
import livraria.core.dao.impl.BandeiraCartaoDAO;
import livraria.core.dao.impl.CartaoDAO;
import livraria.core.dao.impl.CategoriaDAO;
import livraria.core.dao.impl.CidadeDAO;
import livraria.core.dao.impl.ClienteDAO;
import livraria.core.dao.impl.ClienteEndDAO;
import livraria.core.dao.impl.CupomPromocionalDAO;
import livraria.core.dao.impl.CupomTrocaDAO;
import livraria.core.dao.impl.DimensoesDAO;
import livraria.core.dao.impl.EditoraDAO;
import livraria.core.dao.impl.EnderecoDAO;
import livraria.core.dao.impl.EstadoDAO;
import livraria.core.dao.impl.EstoqueDAO;
import livraria.core.dao.impl.GeneroDAO;
import livraria.core.dao.impl.GraficoLivroPeriodoVendaDAO;
import livraria.core.dao.impl.GrupoDePrecificacaoDAO;
import livraria.core.dao.impl.ItemBloqueioPedidoDAO;
import livraria.core.dao.impl.ItemPedidoDAO;
import livraria.core.dao.impl.LivroDAO;
import livraria.core.dao.impl.LogLivroDAO;
import livraria.core.dao.impl.PaisDAO;
import livraria.core.dao.impl.PedidoDAO;
import livraria.core.dao.impl.RegistroDAO;
import livraria.core.dao.impl.SubcategoriaDAO;
import livraria.core.dao.impl.TelefoneDAO;
import livraria.core.dao.impl.TipoEnderecoDAO;
import livraria.core.dao.impl.TipoTelefoneDAO;
import livraria.core.dao.impl.UsuarioDAO;
import livraria.core.negocio.impl.AssociarMotivoAtivacao;
import livraria.core.negocio.impl.AssociarMotivoInativacao;
import livraria.core.negocio.impl.GerarCodigoCupomTroca;
import livraria.core.negocio.impl.ValidarAtualizacaoStatusPedido;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosCartao;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosCliente;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosConfigLivro;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosCupomTroca;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosEndereco;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosLivro;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosPedido;
import livraria.core.negocio.impl.ValidarEstoqueItemPedido;
import livraria.core.negocio.impl.ValidarEstoqueItensFinalizacaoPedido;
import livraria.core.negocio.impl.ValidarFrete;
import livraria.core.negocio.impl.ValidarPagamentosCupomTroca;
import livraria.core.negocio.impl.ValidarPagamentosPedido;
import livraria.core.negocio.impl.ValidarSenhaAntigaCliente;
import livraria.core.negocio.impl.ValidarSenhaCadastroCliente;
import livraria.core.negocio.impl.ValidarTrocaItemPedido;
import livraria.core.negocio.impl.ValidarValorMinMaxCartaoCredito;
import livraria.core.negocio.impl.ValidarValorTotalPagamentos;

public class Fachada implements IFachada {
	
	/** 
	 * Mapa de DAOS, ser� indexado pelo nome da entidade 
	 * O valor � uma inst�ncia do DAO para uma dada entidade; 
	 */
	private Map<String, IDAO> daos;
	
	/**
	 * Mapa para conter as regras de neg�cio de todas opera��es por entidade;
	 * O valor � um mapa que de regras de neg�cio indexado pela opera��o
	 */
	private Map<String, Map<String, List<IStrategy>>> rns;
	
	private Resultado resultado;
	
	
	public Fachada(){
		/* Int�nciando o Map de DAOS */
		daos = new HashMap<String, IDAO>();
		/* Int�nciando o Map de Regras de Neg�cio */
		rns = new HashMap<String, Map<String, List<IStrategy>>>();
		
		/* Criando inst�ncias dos DAOs a serem utilizados*/
		CategoriaDAO categoriaDAO = new CategoriaDAO();
		SubcategoriaDAO subcategoriaDAO = new SubcategoriaDAO();
		EditoraDAO editoraDAO = new EditoraDAO();
		GrupoDePrecificacaoDAO grupoDePrecificacaoDAO = new GrupoDePrecificacaoDAO();
		DimensoesDAO dimensoesDAO = new DimensoesDAO();
		AutorDAO autorDAO = new AutorDAO();
		LivroDAO livroDAO = new LivroDAO();
		LogLivroDAO logLivroDAO = new LogLivroDAO();
		GeneroDAO generoDAO = new GeneroDAO();
		TipoTelefoneDAO tipoTelefoneDAO = new TipoTelefoneDAO();
		BandeiraCartaoDAO bandeiraCartaoDAO = new BandeiraCartaoDAO();
		PaisDAO paisDAO = new PaisDAO();
		EstadoDAO estadoDAO = new EstadoDAO();
		CidadeDAO cidadeDAO = new CidadeDAO();
		TipoEnderecoDAO tipoEndDAO = new TipoEnderecoDAO();
		EnderecoDAO endDAO = new EnderecoDAO();
		TelefoneDAO telDAO = new TelefoneDAO();
		ClienteDAO cliDAO = new ClienteDAO();
		CartaoDAO carDAO = new CartaoDAO();
		ClienteEndDAO cliEndDAO = new ClienteEndDAO();
		UsuarioDAO userDAO = new UsuarioDAO();
		EstoqueDAO estoqueDAO = new EstoqueDAO();
		RegistroDAO regDAO = new RegistroDAO();
		CupomPromocionalDAO cupPromDAO = new CupomPromocionalDAO();
		CupomTrocaDAO cupTrocaDAO = new CupomTrocaDAO();
		PedidoDAO pedidoDAO = new PedidoDAO();
		ItemBloqueioPedidoDAO itemBloqPedDAO = new ItemBloqueioPedidoDAO();
		ItemPedidoDAO itemPedDao = new ItemPedidoDAO();
		GraficoLivroPeriodoVendaDAO graficoLivPerVenDao = new GraficoLivroPeriodoVendaDAO();
		
		/* Adicionando cada dao no MAP indexando pelo nome da classe */
//		daos.put(Bebida.class.getName(), bebidaDAO);
		daos.put(Categoria.class.getName(), categoriaDAO);
		daos.put(Subcategoria.class.getName(), subcategoriaDAO);
		daos.put(Editora.class.getName(),editoraDAO);
		daos.put(GrupoDePrecificacao.class.getName(), grupoDePrecificacaoDAO);
		daos.put(Autor.class.getName(), autorDAO);
		daos.put(Dimensoes.class.getName(), dimensoesDAO);
		daos.put(Livro.class.getName(), livroDAO);
		daos.put(LogLivro.class.getName(), logLivroDAO);
		daos.put(Genero.class.getName(), generoDAO);
		daos.put(TipoTelefone.class.getName(), tipoTelefoneDAO);
		daos.put(BandeiraCartao.class.getName(), bandeiraCartaoDAO);
		daos.put(Pais.class.getName(), paisDAO);
		daos.put(Estado.class.getName(), estadoDAO);
		daos.put(Cidade.class.getName(), cidadeDAO);
		daos.put(TipoEndereco.class.getName(), tipoEndDAO);
		daos.put(Endereco.class.getName(), endDAO);
		daos.put(Telefone.class.getName(), telDAO);
		daos.put(Cliente.class.getName(), cliDAO);
		daos.put(Cartao.class.getName(), carDAO);
		daos.put(ClienteEnd.class.getName(), cliEndDAO);
		daos.put(Usuario.class.getName(), userDAO);
		daos.put(Estoque.class.getName(), estoqueDAO);
		daos.put(Registro.class.getName(), regDAO);
		daos.put(CupomPromocional.class.getName(), cupPromDAO);
		daos.put(CupomTroca.class.getName(), cupTrocaDAO);
		daos.put(Pedido.class.getName(), pedidoDAO);
		daos.put(ItemBloqueioPedido.class.getName(), itemBloqPedDAO);
		daos.put(ItemPedido.class.getName(), itemPedDao);
		daos.put(LivroPeriodoVendas.class.getName(), graficoLivPerVenDao);
		
		/* Criando inst�ncias de regras de neg�cio a serem utilizados*/		
		ValidarDadosObrigatoriosLivro vDadosObrigatoriosLivro = new ValidarDadosObrigatoriosLivro();
		AssociarMotivoAtivacao associarMotivoAtivacao = new AssociarMotivoAtivacao();
		AssociarMotivoInativacao associarMotivoInativacao = new AssociarMotivoInativacao();
		ValidarDadosObrigatoriosConfigLivro vDadosObrigatoriosConfigLivro = new ValidarDadosObrigatoriosConfigLivro();
		ValidarEstoqueItemPedido vEstoqueItemPedido = new ValidarEstoqueItemPedido();
		ValidarEstoqueItensFinalizacaoPedido vItensFinalPed = new ValidarEstoqueItensFinalizacaoPedido();
		ValidarPagamentosCupomTroca vPagCupTroca = new ValidarPagamentosCupomTroca();
		ValidarValorMinMaxCartaoCredito vValorMinCartCred = new ValidarValorMinMaxCartaoCredito();
		ValidarValorTotalPagamentos vValorTotalPgtos = new ValidarValorTotalPagamentos();
		ValidarFrete vFrete = new ValidarFrete();
		ValidarDadosObrigatoriosPedido vDadosObrigPedido = new ValidarDadosObrigatoriosPedido();
		ValidarPagamentosPedido vPagPedidos = new ValidarPagamentosPedido();
		ValidarAtualizacaoStatusPedido vAtualStatPedido = new ValidarAtualizacaoStatusPedido();
		ValidarSenhaCadastroCliente vSenhaCliente = new ValidarSenhaCadastroCliente();
		ValidarDadosObrigatoriosCliente vDadosCliente = new ValidarDadosObrigatoriosCliente();
		ValidarSenhaAntigaCliente vSenhaAntigaCli = new ValidarSenhaAntigaCliente();
		ValidarDadosObrigatoriosEndereco vDadosEnd = new ValidarDadosObrigatoriosEndereco();
		ValidarDadosObrigatoriosCartao vDadosCartao = new ValidarDadosObrigatoriosCartao();
		ValidarTrocaItemPedido vTrocaItemPed = new ValidarTrocaItemPedido();
		GerarCodigoCupomTroca gerarCodCupTroca = new GerarCodigoCupomTroca();
		ValidarDadosObrigatoriosCupomTroca vDadosCuptroca = new ValidarDadosObrigatoriosCupomTroca();
		
// LIVRO
		/* Criando uma lista para conter as regras de neg�cio de livro
		 * quando a opera��o for salvar
		 */
		List<IStrategy> rnsSalvarLivro = new ArrayList<IStrategy>();
		/* Adicionando as regras a serem utilizadas na opera��o salvar do estoque*/
		rnsSalvarLivro.add(vDadosObrigatoriosLivro);
		
		/* Criando uma lista para conter as regras de neg�cio de livro
		 * quando a opera��o for alterar
		 */
		List<IStrategy> rnsAlterarLivro = new ArrayList<IStrategy>();
		/* Adicionando as regras a serem utilizadas na opera��o salvar do estoque*/
		rnsAlterarLivro.add(vDadosObrigatoriosLivro);
		rnsAlterarLivro.add(associarMotivoAtivacao);
		rnsAlterarLivro.add(associarMotivoInativacao);
		rnsAlterarLivro.add(vDadosObrigatoriosConfigLivro);
//		rnsAlterarEstoque.add(vDadosObrigatoriosEstoque);
//		rnsAlterarEstoque.add(vQtdeEstoque);
		
		/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
		 * por opera��o  do livro
		 */
		Map<String, List<IStrategy>> rnsLivro = new HashMap<String, List<IStrategy>>();
		/*
		 * Adiciona a listra de regras na opera��o salvar no mapa do livro 
		 */
		rnsLivro.put("SALVAR", rnsSalvarLivro);
		/*
		 * Adiciona a listra de regras na opera��o alterar no mapa do livro 
		 */
		rnsLivro.put("ALTERAR", rnsAlterarLivro);
		
		/* Adiciona o mapa com as regras indexadas pelas opera��es no mapa geral indexado 
		 * pelo nome da entidade
		 */
		rns.put(Livro.class.getName(), rnsLivro);
		
// ITEMPEDIDO
		/* Criando uma lista para conter as regras de neg�cio de ItemPedido
		 * quando a opera��o for consultar
		 */
		List<IStrategy> rnsConsultarItemPedido = new ArrayList<IStrategy>();
		rnsConsultarItemPedido.add(vEstoqueItemPedido);
		
		/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
		 * por opera��o  do ItemPedido
		 */
		Map<String, List<IStrategy>> rnsItemPed = new HashMap<String, List<IStrategy>>();
		rnsItemPed.put("CONSULTAR", rnsConsultarItemPedido);
		
		/* Criando uma lista para conter as regras de neg�cio de ItemPedido
		 * quando a opera��o for alterar
		 */
		List<IStrategy> rnsAlterarItemPedido = new ArrayList<IStrategy>();
		rnsAlterarItemPedido.add(vTrocaItemPed);
		
		/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
		 * por opera��o  do ItemPedido
		 */
		rnsItemPed.put("ALTERAR", rnsAlterarItemPedido);
		
		/* Adiciona o mapa com as regras indexadas pelas opera��es no mapa geral indexado 
		 * pelo nome da entidade
		 */
		rns.put(ItemPedido.class.getName(), rnsItemPed);
		
// PEDIDO
		// SALVAR
		/* Criando uma lista para conter as regras de neg�cio de Pedido
		 * quando a opera��o for salvar
		 */
		List<IStrategy> rnsSalvarPedido = new ArrayList<IStrategy>();
		rnsSalvarPedido.add(vDadosObrigPedido);
		rnsSalvarPedido.add(vFrete);
		rnsSalvarPedido.add(vItensFinalPed);
		rnsSalvarPedido.add(vPagCupTroca);
		rnsSalvarPedido.add(vValorMinCartCred);
		rnsSalvarPedido.add(vValorTotalPgtos);
		
		/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
		 * por opera��o  do ItemPedido
		 */
		Map<String, List<IStrategy>> rnsPedido = new HashMap<String, List<IStrategy>>();
		rnsPedido.put("SALVAR", rnsSalvarPedido);
		
		// ALTERAR
		/* Criando uma lista para conter as regras de neg�cio de Pedido
		 * quando a opera��o for ALTERAR
		 */
		List<IStrategy> rnsAtualizarPedido = new ArrayList<IStrategy>();
		rnsAtualizarPedido.add(vAtualStatPedido);
		rnsAtualizarPedido.add(vPagPedidos);
		
		rnsPedido.put("ALTERAR", rnsAtualizarPedido);
		
		rns.put(Pedido.class.getName(), rnsPedido);
		
// CLIENTE
		// SALVAR
		/* Criando uma lista para conter as regras de neg�cio de Cliente
		 * quando a opera��o for salvar
		 */
		List<IStrategy> rnsSalvarCliente = new ArrayList<IStrategy>();
		rnsSalvarCliente.add(vSenhaCliente);
		rnsSalvarCliente.add(vDadosCliente);
		
		/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
		 * por opera��o  do Cliente
		 */
		Map<String, List<IStrategy>> rnsCliente = new HashMap<String, List<IStrategy>>();
		rnsCliente.put("SALVAR", rnsSalvarCliente);
		
		// ALTERAR
		/* Criando uma lista para conter as regras de neg�cio de Cliente
		 * quando a opera��o for ALTERAR
		 */
		List<IStrategy> rnsAtualizarCliente = new ArrayList<IStrategy>();
		rnsAtualizarCliente.add(vSenhaAntigaCli);
		rnsAtualizarCliente.add(vSenhaCliente);
		rnsAtualizarCliente.add(vDadosCliente);
		
		rnsCliente.put("ALTERAR", rnsAtualizarCliente);
		
		rns.put(Cliente.class.getName(), rnsCliente);
		
// ENDERE�O
	// SALVAR
	/* Criando uma lista para conter as regras de neg�cio de Endere�o
	 * quando a opera��o for salvar
	 */
	List<IStrategy> rnsSalvarEndereco = new ArrayList<IStrategy>();
	rnsSalvarEndereco.add(vDadosEnd);
	
	/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
	 * por opera��o  do Endere�o
	 */
	Map<String, List<IStrategy>> rnsEndereco = new HashMap<String, List<IStrategy>>();
	rnsEndereco.put("SALVAR", rnsSalvarEndereco);
	
	// ALTERAR
	/* Criando uma lista para conter as regras de neg�cio de Endere�o
	 * quando a opera��o for ALTERAR
	 */
	List<IStrategy> rnsAtualizarEndereco = new ArrayList<IStrategy>();
	rnsAtualizarEndereco.add(vDadosEnd);
	
	rnsEndereco.put("ALTERAR", rnsAtualizarEndereco);
	
	rns.put(Endereco.class.getName(), rnsEndereco);
	
// CART�O
	// SALVAR
	/* Criando uma lista para conter as regras de neg�cio de Cart�o
	 * quando a opera��o for salvar
	 */
	List<IStrategy> rnsSalvarCartao = new ArrayList<IStrategy>();
	rnsSalvarCartao.add(vDadosCartao);
	
	/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
	 * por opera��o  do Cart�o
	 */
	Map<String, List<IStrategy>> rnsCartao = new HashMap<String, List<IStrategy>>();
	rnsCartao.put("SALVAR", rnsSalvarCartao);
	
	// ALTERAR
	/* Criando uma lista para conter as regras de neg�cio de Cart�o
	 * quando a opera��o for ALTERAR
	 */
	List<IStrategy> rnsAtualizarCartao = new ArrayList<IStrategy>();
	rnsAtualizarCartao.add(vDadosCartao);
	
	rnsCartao.put("ALTERAR", rnsAtualizarCartao);
	
	rns.put(Cartao.class.getName(), rnsCartao);
	
// CUPOM TROCA
	// SALVAR
	/* Criando uma lista para conter as regras de neg�cio de Cupom de Troca
	 * quando a opera��o for salvar
	 */
	List<IStrategy> rnsSalvarCupomTroca = new ArrayList<IStrategy>();
	rnsSalvarCupomTroca.add(gerarCodCupTroca);
	rnsSalvarCupomTroca.add(vDadosCuptroca);
	
	/* Cria o mapa que poder� conter todas as listas de regras de neg�cio espec�fica 
	 * por opera��o  do Cupom Troca
	 */
	Map<String, List<IStrategy>> rnsCupomTroca = new HashMap<String, List<IStrategy>>();
	rnsCupomTroca.put("SALVAR", rnsSalvarCupomTroca);
	
		
	rns.put(CupomTroca.class.getName(), rnsCupomTroca);
}
	
	
	@Override
	public Resultado salvar(EntidadeDominio entidade) {
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();	
		
		String msg = executarRegras(entidade, "SALVAR");
		
		
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.salvar(entidade);
				List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
				entidades.add(entidade);
				resultado.setEntidades(entidades);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("N�o foi poss�vel realizar o registro!");
				
			}
		}else{
			resultado.setMsg(msg);
					
			
		}
		
		return resultado;
	}

	@Override
	public Resultado alterar(EntidadeDominio entidade) {
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();	
		
		String msg = executarRegras(entidade, "ALTERAR");
	
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.alterar(entidade);
				List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
				entidades.add(entidade);
				resultado.setEntidades(entidades);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("N�o foi poss�vel realizar o registro!");
				
			}
		}else{
			resultado.setMsg(msg);
					
			
		}
		
		return resultado;

	}

	@Override
	public Resultado excluir(EntidadeDominio entidade) {
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();	
		
		String msg = executarRegras(entidade, "EXCLUIR");
		
		
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				dao.excluir(entidade);
				List<EntidadeDominio> entidades = new ArrayList<EntidadeDominio>();
				entidades.add(entidade);
				resultado.setEntidades(entidades);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("N�o foi poss�vel realizar o registro!");
				
			}
		}else{
			resultado.setMsg(msg);
					
			
		}
		
		return resultado;

	}

	@Override
	public Resultado consultar(EntidadeDominio entidade) {
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();	
		
		String msg = executarRegras(entidade, "CONSULTAR");
		
		
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				
				resultado.setEntidades(dao.consultar(entidade));
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("N�o foi poss�vel realizar a consulta!");
				
			}
		}else{
			resultado.setMsg(msg);
			
		}
		
		return resultado;

	}
	
	@Override
	public Resultado visualizar(EntidadeDominio entidade) {
		
		resultado = new Resultado();
		String nmClasse = entidade.getClass().getName();	
		
		String msg = executarRegras(entidade, "VISUALIZAR");
		
		
		if(msg == null){
			IDAO dao = daos.get(nmClasse);
			try {
				List<EntidadeDominio> ent = new ArrayList<EntidadeDominio>(1);
				ent.add( dao.visualizar(entidade));
				resultado.setEntidades(ent);
			} catch (SQLException e) {
				e.printStackTrace();
				resultado.setMsg("N�o foi poss�vel realizar a consulta!");
				
			}
		}else{
			resultado.setMsg(msg);
			
		}
		
		return resultado;

	}

	
	private String executarRegras(EntidadeDominio entidade, String operacao){
		String nmClasse = entidade.getClass().getName();		
		StringBuilder msg = new StringBuilder();
		
		Map<String, List<IStrategy>> regrasOperacao = rns.get(nmClasse);
		
		
		if(regrasOperacao != null){
			List<IStrategy> regras = regrasOperacao.get(operacao);
			
			if(regras != null){
				for(IStrategy s: regras){			
					String m = s.processar(entidade);			
					
					if(m != null){
						msg.append(m);
						msg.append("\n");
					}			
				}	
			}			
			
		}
		
		if(msg.length()>0)
			return msg.toString();
		else
			return null;
	}


}
