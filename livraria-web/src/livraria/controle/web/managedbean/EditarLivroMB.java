package livraria.controle.web.managedbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.model.DualListModel;

import dominio.livro.Autor;
import dominio.livro.Categoria;
import dominio.livro.Dimensoes;
import dominio.livro.Editora;
import dominio.livro.GrupoDePrecificacao;
import dominio.livro.Livro;
import dominio.livro.Subcategoria;
import livraria.controle.web.command.ICommand;
import livraria.controle.web.command.impl.AlterarCommand;
import livraria.controle.web.command.impl.ConsultarCommand;
import livraria.controle.web.command.impl.ExcluirCommand;
import livraria.controle.web.command.impl.SalvarCommand;
import livraria.controle.web.command.impl.VisualizarCommand;
import livraria.core.aplicacao.Resultado;

@ManagedBean
@ViewScoped
public class EditarLivroMB {

	private Livro livro;
	private List<Livro> livros;
	private Livro livroSelecionado;
	private Autor autor;
	private Dimensoes dimensoes;
	private Categoria categoria;
	private Categoria catAtivInativacao;
	private StringBuilder catString;
	private List<Categoria> categorias;
	private List<Categoria> catAtivInativas;
	private List<Categoria> categoriasSelecionadas;
	private DualListModel<Categoria> catPickList;
	private DualListModel<Subcategoria> subCatPickList;
	private Subcategoria subcategoria;
	private StringBuilder subcatString;
	private List<Subcategoria> subcategorias;
	private List<Subcategoria> subcategoriasSelecionadas;
	private Editora editora;
	private List<Editora> editoras;
	private GrupoDePrecificacao grupoDePrecificacao;
	private List<GrupoDePrecificacao> gruposDePrecificacao;
	private Boolean status;
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		livros = new ArrayList<Livro>();
		livroSelecionado = new Livro();
		autor = new Autor();
		dimensoes = new Dimensoes();
		categoria = new Categoria();
		catAtivInativacao = new Categoria();
		categorias = new ArrayList<Categoria>();
		catAtivInativas = new ArrayList<Categoria>();
		categoriasSelecionadas = new ArrayList<Categoria>();
		subcategoria = new Subcategoria();
		subcategorias = new ArrayList<Subcategoria>();
		subcategoriasSelecionadas = new ArrayList<Subcategoria>();
		catString = new StringBuilder();
		subcatString = new StringBuilder();
		editora = new Editora();
		editoras = new ArrayList<Editora>();
		grupoDePrecificacao = new GrupoDePrecificacao();
		gruposDePrecificacao = new ArrayList<GrupoDePrecificacao>();
		status = null;
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		popularCategorias();
		popularCatAtivInativas();
		popularSubcategorias();
		popularEditoras();
		popularGruposDePrecificacao();
		
		ExternalContext ec =FacesContext.getCurrentInstance().getExternalContext();
	       this.livro =  (Livro)ec.getRequestMap().get("livro");
	       
	    categorias.removeAll(livro.getCategorias());
	    subcategorias.removeAll(livro.getSubcategorias());
	    catPickList = new DualListModel<Categoria>(categorias, livro.getCategorias());
	    subCatPickList = new DualListModel<Subcategoria>(subcategorias, livro.getSubcategorias());
	}
	
	
	private void popularCategorias() {
		categoria.setTipo("venda");
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(categoria);
		
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				categorias.add(i, (Categoria) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	private void popularCatAtivInativas() {
		categoria.setTipo("ativacao");
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(categoria);
		
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				catAtivInativas.add(i, (Categoria) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
		categoria.setTipo("inativacao");
		command = commands.get("CONSULTAR");
		rs = command.execute(categoria);
		
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				catAtivInativas.add(i, (Categoria) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	public void popularSubcategorias() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(subcategoria);
		
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				subcategorias.add(i, (Subcategoria) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	public void popularEditoras() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(editora);
		
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				editoras.add(i, (Editora) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	public void popularGruposDePrecificacao() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(grupoDePrecificacao);
		
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				gruposDePrecificacao.add(i, (GrupoDePrecificacao) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}	
	}
	
	public void alterarLivro() {
		livro.setCategorias(catPickList.getTarget());
		livro.setSubcategorias(subCatPickList.getTarget());
		
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(livro);
		if(rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Livro editado com sucesso!"));
		}
	}
	
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

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Dimensoes getDimensoes() {
		return dimensoes;
	}

	public void setDimensoes(Dimensoes dimensoes) {
		this.dimensoes = dimensoes;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Categoria getCatAtivInativacao() {
		return catAtivInativacao;
	}

	public void setCatAtivInativacao(Categoria catAtivInativacao) {
		this.catAtivInativacao = catAtivInativacao;
	}

	public StringBuilder getCatString() {
		return catString;
	}

	public void setCatString(StringBuilder catString) {
		this.catString = catString;
	}

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Categoria> getCatAtivInativas() {
		return catAtivInativas;
	}

	public void setCatAtivInativas(List<Categoria> catAtivInativas) {
		this.catAtivInativas = catAtivInativas;
	}

	public List<Categoria> getCategoriasSelecionadas() {
		return categoriasSelecionadas;
	}

	public void setCategoriasSelecionadas(List<Categoria> categoriasSelecionadas) {
		this.categoriasSelecionadas = categoriasSelecionadas;
	}

	public DualListModel<Categoria> getCatPickList() {
		return catPickList;
	}

	public void setCatPickList(DualListModel<Categoria> catPickList) {
		this.catPickList = catPickList;
	}

	public DualListModel<Subcategoria> getSubCatPickList() {
		return subCatPickList;
	}

	public void setSubCatPickList(DualListModel<Subcategoria> subCatPickList) {
		this.subCatPickList = subCatPickList;
	}

	public Subcategoria getSubcategoria() {
		return subcategoria;
	}

	public void setSubcategoria(Subcategoria subcategoria) {
		this.subcategoria = subcategoria;
	}

	public StringBuilder getSubcatString() {
		return subcatString;
	}

	public void setSubcatString(StringBuilder subcatString) {
		this.subcatString = subcatString;
	}

	public List<Subcategoria> getSubcategorias() {
		return subcategorias;
	}

	public void setSubcategorias(List<Subcategoria> subcategorias) {
		this.subcategorias = subcategorias;
	}

	public List<Subcategoria> getSubcategoriasSelecionadas() {
		return subcategoriasSelecionadas;
	}

	public void setSubcategoriasSelecionadas(List<Subcategoria> subcategoriasSelecionadas) {
		this.subcategoriasSelecionadas = subcategoriasSelecionadas;
	}

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}

	public List<Editora> getEditoras() {
		return editoras;
	}

	public void setEditoras(List<Editora> editoras) {
		this.editoras = editoras;
	}

	public GrupoDePrecificacao getGrupoDePrecificacao() {
		return grupoDePrecificacao;
	}

	public void setGrupoDePrecificacao(GrupoDePrecificacao grupoDePrecificacao) {
		this.grupoDePrecificacao = grupoDePrecificacao;
	}

	public List<GrupoDePrecificacao> getGruposDePrecificacao() {
		return gruposDePrecificacao;
	}

	public void setGruposDePrecificacao(List<GrupoDePrecificacao> gruposDePrecificacao) {
		this.gruposDePrecificacao = gruposDePrecificacao;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		EditarLivroMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}
	
}
