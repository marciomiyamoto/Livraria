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

import org.primefaces.context.RequestContext;
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
public class LivroMB {
	
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
	private Boolean filtroStatus;
	private Categoria filtroCatAtIn;
	private String justificativa;
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		livro = new Livro();
		livros = new ArrayList<Livro>();
		livroSelecionado = new Livro();
		autor = new Autor();
		dimensoes = new Dimensoes();
		categoria = new Categoria();
		catAtivInativacao = new Categoria();
		categorias = new ArrayList<Categoria>();
		catAtivInativas = new ArrayList<Categoria>();
		categoriasSelecionadas = new ArrayList<Categoria>();
		catPickList = new DualListModel<Categoria>(categorias, categoriasSelecionadas);
		subcategoria = new Subcategoria();
		subcategorias = new ArrayList<Subcategoria>();
		subcategoriasSelecionadas = new ArrayList<Subcategoria>();
		subCatPickList = new DualListModel<Subcategoria>(subcategorias, subcategoriasSelecionadas);
		catString = new StringBuilder();
		subcatString = new StringBuilder();
		editora = new Editora();
		editoras = new ArrayList<Editora>();
		grupoDePrecificacao = new GrupoDePrecificacao();
		gruposDePrecificacao = new ArrayList<GrupoDePrecificacao>();
		status = null;
		filtroStatus = null;
		filtroCatAtIn = new Categoria();
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		popularCategorias();
		popularSubcategorias();
		popularEditoras();
		popularGruposDePrecificacao();
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
	public void carregaCatFiltro() {
		catAtivInativas = new ArrayList<Categoria>();
		if(filtroStatus != null) {
			if(filtroStatus == true) {
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
			} else {
				categoria.setTipo("inativacao");
				command = commands.get("CONSULTAR");
				Resultado rs = command.execute(categoria);
				
				for(int i = 0; i < rs.getEntidades().size(); i++) {
					try {
						catAtivInativas.add(i, (Categoria) rs.getEntidades().get(i));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}	
			}
		}
	}
	public void carregaCatAtivas() {
		catAtivInativas = new ArrayList<Categoria>();
		if(status == true) {
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
		} else {
			categoria.setTipo("inativacao");
			command = commands.get("CONSULTAR");
			Resultado rs = command.execute(categoria);
			
			for(int i = 0; i < rs.getEntidades().size(); i++) {
				try {
					catAtivInativas.add(i, (Categoria) rs.getEntidades().get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
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
	
	public void concatenaCategoriasSubcategorias() {
		String virgula = "";
		catString = new StringBuilder();
		subcatString = new StringBuilder();
		for(Categoria cat : livroSelecionado.getCategorias()) {
			catString.append(virgula);
			virgula = ", ";
			catString.append(cat.getNome());
		}
		virgula = "";
		for(Subcategoria subcat : livroSelecionado.getSubcategorias()) {
			subcatString.append(virgula);
			virgula = ", ";
			subcatString.append(subcat.getNome());
		}
		status = null;
		catAtivInativas.clear();
	}
	
	public void salvarLivro() {
		livro.setCategorias(catPickList.getTarget());
		livro.setSubcategorias(subCatPickList.getTarget());
		livro.setDimensoes(dimensoes);
		livro.setAutor(autor);
		livro.setEditora(editora);
		livro.setGrupoPrec(grupoDePrecificacao);
		
		command = commands.get("SALVAR");
		Resultado rs = command.execute(livro);
		if(rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Livro cadastrado com sucesso!"));
		}
	}
	
	public void buscarLivros() {
		livro.setCategorias(categorias);
		livro.setCatAtivInativacao(filtroCatAtIn);
		livro.setSubcategorias(subcategorias);
		livro.setDimensoes(dimensoes);
		livro.setAutor(autor);
		livro.setEditora(editora);
		livro.setGrupoPrec(grupoDePrecificacao);
		livro.setAtivo(filtroStatus);
		livros = new ArrayList<Livro>();
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
		// FILTRANDO POR CATEGORIAS
		List<Livro> livrosFiltrados = new ArrayList<Livro>();
		if(categoria.getId() != null && categoria.getId() != 0) {
			for(Livro l : livros) {
				for(Categoria c : l.getCategorias()) {
					if(c.getId().equals(categoria.getId()))
						livrosFiltrados.add(l);
				}
			}
			livros = livrosFiltrados;
		}
		// FILTRANDO POR CATEGORIAS
		livrosFiltrados = new ArrayList<Livro>();
		if(subcategoria.getId() != null && subcategoria.getId() != 0) {
			for(Livro l : livros) {
				for(Subcategoria s : l.getSubcategorias()) {
					if(s.getId().equals(subcategoria.getId()))
						livrosFiltrados.add(l);
				}
			}
			livros = livrosFiltrados;
		}
		req.update("busca:dgLivros");
	}
	
	public String editarLivro() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getRequestMap().put("livro", livroSelecionado);
        return "editar.xhtml";
	}
	
	public void excluirLivro() {
		RequestContext req = RequestContext.getCurrentInstance();
		command = commands.get("EXCLUIR");
		Resultado rs = command.execute(livroSelecionado);
		if(rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Livro excluído com sucesso!"));
		}
		buscarLivros();
		req.update("busca:dgLivros");
	}
	
	public void salvarConfigLivro() {
		for(Categoria c : catAtivInativas) {
			if(c.getId() == catAtivInativacao.getId()){
				catAtivInativacao = c;
				break;
			}
		}
		Livro livroConfig = new Livro();
		livroConfig = livroSelecionado;
		livroConfig.setAtivo(status);
		livroConfig.setCatAtivInativacao(catAtivInativacao);
		livroConfig.setJustificativa(justificativa);
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(livroConfig);
		if(rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Livro atualizado com sucesso!"));
		}
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

	public List<Categoria> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}

	public List<Categoria> getCategoriasSelecionadas() {
		return categoriasSelecionadas;
	}

	public void setCategoriasSelecionadas(List<Categoria> categoriasSelecionadas) {
		this.categoriasSelecionadas = categoriasSelecionadas;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}

	public GrupoDePrecificacao getGrupoDePrecificacao() {
		return grupoDePrecificacao;
	}

	public void setGrupoDePrecificacao(GrupoDePrecificacao grupoDePrecificacao) {
		this.grupoDePrecificacao = grupoDePrecificacao;
	}

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		LivroMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}

	public Subcategoria getSubcategoria() {
		return subcategoria;
	}

	public void setSubcategoria(Subcategoria subcategoria) {
		this.subcategoria = subcategoria;
	}

	public List<Subcategoria> getSubcategorias() {
		return subcategorias;
	}

	public void setSubcategorias(List<Subcategoria> subcategorias) {
		this.subcategorias = subcategorias;
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

	public List<Subcategoria> getSubcategoriasSelecionadas() {
		return subcategoriasSelecionadas;
	}

	public void setSubcategoriasSelecionadas(List<Subcategoria> subcategoriasSelecionadas) {
		this.subcategoriasSelecionadas = subcategoriasSelecionadas;
	}

	public List<Editora> getEditoras() {
		return editoras;
	}

	public void setEditoras(List<Editora> editoras) {
		this.editoras = editoras;
	}

	public List<GrupoDePrecificacao> getGruposDePrecificacao() {
		return gruposDePrecificacao;
	}

	public void setGruposDePrecificacao(List<GrupoDePrecificacao> gruposDePrecificacao) {
		this.gruposDePrecificacao = gruposDePrecificacao;
	}

	public StringBuilder getCatString() {
		return catString;
	}

	public void setCatString(StringBuilder catString) {
		this.catString = catString;
	}

	public StringBuilder getSubcatString() {
		return subcatString;
	}

	public void setSubcatString(StringBuilder subcatString) {
		this.subcatString = subcatString;
	}

	public List<Categoria> getCatAtivInativas() {
		return catAtivInativas;
	}

	public void setCatAtivInativas(List<Categoria> catAtivInativas) {
		this.catAtivInativas = catAtivInativas;
	}

	public Categoria getCatAtivInativacao() {
		return catAtivInativacao;
	}

	public void setCatAtivInativacao(Categoria catAtivInativacao) {
		this.catAtivInativacao = catAtivInativacao;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Livro getLivroSelecionado() {
		return livroSelecionado;
	}

	public void setLivroSelecionado(Livro livroSelecionado) {
		this.livroSelecionado = livroSelecionado;
	}

	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public Boolean getFiltroStatus() {
		return filtroStatus;
	}

	public void setFiltroStatus(Boolean filtroStatus) {
		this.filtroStatus = filtroStatus;
	}

	public Categoria getFiltroCatAtIn() {
		return filtroCatAtIn;
	}

	public void setFiltroCatAtIn(Categoria filtroCatAtIn) {
		this.filtroCatAtIn = filtroCatAtIn;
	}

}
