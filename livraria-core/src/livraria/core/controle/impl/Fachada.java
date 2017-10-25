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
import dominio.livro.GrupoDePrecificacao;
import dominio.livro.Livro;
import dominio.livro.LogLivro;
import dominio.livro.Subcategoria;
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
import livraria.core.dao.impl.DimensoesDAO;
import livraria.core.dao.impl.EditoraDAO;
import livraria.core.dao.impl.EnderecoDAO;
import livraria.core.dao.impl.EstadoDAO;
import livraria.core.dao.impl.GeneroDAO;
import livraria.core.dao.impl.GrupoDePrecificacaoDAO;
import livraria.core.dao.impl.LivroDAO;
import livraria.core.dao.impl.LogLivroDAO;
import livraria.core.dao.impl.PaisDAO;
import livraria.core.dao.impl.SubcategoriaDAO;
import livraria.core.dao.impl.TelefoneDAO;
import livraria.core.dao.impl.TipoEnderecoDAO;
import livraria.core.dao.impl.TipoTelefoneDAO;
import livraria.core.dao.impl.UsuarioDAO;
import livraria.core.negocio.impl.AssociarMotivoAtivacao;
import livraria.core.negocio.impl.AssociarMotivoInativacao;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosConfigLivro;
import livraria.core.negocio.impl.ValidarDadosObrigatoriosLivro;

public class Fachada implements IFachada {
	
	/** 
	 * Mapa de DAOS, será indexado pelo nome da entidade 
	 * O valor é uma instância do DAO para uma dada entidade; 
	 */
	private Map<String, IDAO> daos;
	
	/**
	 * Mapa para conter as regras de negócio de todas operações por entidade;
	 * O valor é um mapa que de regras de negócio indexado pela operação
	 */
	private Map<String, Map<String, List<IStrategy>>> rns;
	
	private Resultado resultado;
	
	
	public Fachada(){
		/* Intânciando o Map de DAOS */
		daos = new HashMap<String, IDAO>();
		/* Intânciando o Map de Regras de Negócio */
		rns = new HashMap<String, Map<String, List<IStrategy>>>();
		
		/* Criando instâncias dos DAOs a serem utilizados*/
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
//		EstoqueDAO estoqueDAO = new EstoqueDAO();
		
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
//		daos.put(Estoque.class.getName(), estoqueDAO);
		
		/* Criando instâncias de regras de negócio a serem utilizados*/		
		ValidarDadosObrigatoriosLivro vDadosObrigatoriosLivro = new ValidarDadosObrigatoriosLivro();
		AssociarMotivoAtivacao associarMotivoAtivacao = new AssociarMotivoAtivacao();
		AssociarMotivoInativacao associarMotivoInativacao = new AssociarMotivoInativacao();
		ValidarDadosObrigatoriosConfigLivro vDadosObrigatoriosConfigLivro = new ValidarDadosObrigatoriosConfigLivro();
//		ValidarDadosObrigatoriosEstoque vDadosObrigatoriosEstoque = new ValidarDadosObrigatoriosEstoque();
		
		/* Criando uma lista para conter as regras de negócio de livro
		 * quando a operação for salvar
		 */
		List<IStrategy> rnsSalvarLivro = new ArrayList<IStrategy>();
		/* Adicionando as regras a serem utilizadas na operação salvar do estoque*/
		rnsSalvarLivro.add(vDadosObrigatoriosLivro);
		
		/* Criando uma lista para conter as regras de negócio de livro
		 * quando a operação for alterar
		 */
		List<IStrategy> rnsAlterarLivro = new ArrayList<IStrategy>();
		/* Adicionando as regras a serem utilizadas na operação salvar do estoque*/
		rnsAlterarLivro.add(vDadosObrigatoriosLivro);
		rnsAlterarLivro.add(associarMotivoAtivacao);
		rnsAlterarLivro.add(associarMotivoInativacao);
		rnsAlterarLivro.add(vDadosObrigatoriosConfigLivro);
//		rnsAlterarEstoque.add(vDadosObrigatoriosEstoque);
//		rnsAlterarEstoque.add(vQtdeEstoque);
		
		/* Cria o mapa que poderá conter todas as listas de regras de negócio específica 
		 * por operação  do livro
		 */
		Map<String, List<IStrategy>> rnsLivro = new HashMap<String, List<IStrategy>>();
		/*
		 * Adiciona a listra de regras na operação salvar no mapa do livro 
		 */
		rnsLivro.put("SALVAR", rnsSalvarLivro);
		/*
		 * Adiciona a listra de regras na operação alterar no mapa do livro 
		 */
		rnsLivro.put("ALTERAR", rnsAlterarLivro);
		
		/* Adiciona o mapa com as regras indexadas pelas operações no mapa geral indexado 
		 * pelo nome da entidade
		 */
		rns.put(Livro.class.getName(), rnsLivro);
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
				resultado.setMsg("Não foi possível realizar o registro!");
				
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
				resultado.setMsg("Não foi possível realizar o registro!");
				
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
				resultado.setMsg("Não foi possível realizar o registro!");
				
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
				resultado.setMsg("Não foi possível realizar a consulta!");
				
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
				resultado.setMsg("Não foi possível realizar a consulta!");
				
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
