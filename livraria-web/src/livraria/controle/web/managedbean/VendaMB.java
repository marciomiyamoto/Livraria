package livraria.controle.web.managedbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.context.RequestContext;

import dominio.livro.Autor;
import dominio.livro.Categoria;
import dominio.livro.Dimensoes;
import dominio.livro.Editora;
import dominio.livro.GrupoDePrecificacao;
import dominio.livro.Livro;
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
	
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		livro = new Livro();
		livros = new ArrayList<Livro>();
		livroSelecionado = new Livro();
		
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		carregaLivros();
	}
	
	public void carregaLivros() {
		Livro livro = new Livro();
		Autor autor = new Autor();
		Editora editora = new Editora();
		GrupoDePrecificacao grupo = new GrupoDePrecificacao();
		Dimensoes dimensoes = new Dimensoes();
		List<Categoria> categorias = new ArrayList<Categoria>();
		Categoria cat = new Categoria();
		
		livro.setAutor(autor);
		livro.setEditora(editora);
		livro.setGrupoPrec(grupo);
		livro.setDimensoes(dimensoes);
		livro.setCategorias(categorias);
		livro.setCatAtivInativacao(cat);
		RequestContext req = RequestContext.getCurrentInstance();
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(livro);
		// PREENCHENDO LISTA DE LIVROS
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				livros.add(i, (Livro) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		req.update("home:dgLivros");
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
	
}
