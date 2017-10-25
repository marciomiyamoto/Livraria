package livraria.core.negocio.impl;

import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Categoria;
import dominio.livro.Livro;
import dominio.livro.Subcategoria;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosLivro implements IStrategy{

	@Override
	public String processar(EntidadeDominio entidade) {
		
		if(entidade instanceof Livro) {
			Livro livro = (Livro)entidade;
			
			if(livro.getAutor() != null && livro.getCategorias() != null && livro.getSubcategorias() != null &&
					 livro.getDimensoes() != null && livro.getEdicao() != null && livro.getGrupoPrec().getId() != null &&
					 livro.getAno() != null && livro.getNumPaginas() != null && livro.getEditora().getId() != null && 
					 livro.getAutor() != null && livro.getDimensoes().getAltura() != null && livro.getDimensoes().getLargura() != null && 
					 livro.getDimensoes().getPeso() != null && livro.getDimensoes().getProfundidade() != null) {
				
				String titulo = livro.getTitulo();
				String codBarras = livro.getCodBarras();
				String sinopse = livro.getSinopse();
				String autor = livro.getAutor().getNome();
				List<Categoria> categorias = livro.getCategorias();
				List<Subcategoria> subcategorias = livro.getSubcategorias();
				int ano = livro.getAno();
				int edicao = livro.getEdicao();
				int paginas = livro.getNumPaginas();
				int idEditora = livro.getEditora().getId();
				int idGrupoPrec = livro.getGrupoPrec().getId();
				double altura = livro.getDimensoes().getAltura();
				double largura = livro.getDimensoes().getLargura();
				double peso = livro.getDimensoes().getPeso();
				double profundidade = livro.getDimensoes().getProfundidade();
				
				if(ano == 0 || edicao == 0 || paginas == 0 || 
						 idEditora == 0 || idGrupoPrec == 0 ||
						altura == 0 || largura == 0 || peso == 0 || profundidade == 0 || 
						titulo == null || codBarras == null || sinopse == null || autor == null ||
						categorias.isEmpty() || subcategorias.isEmpty()) {
					return "Todos os campos são de preenchimento obrigatório!";
				}
				
				if(titulo.trim().equals("") || codBarras.trim().equals("") || sinopse.trim().equals("") || autor.trim().equals("")) {
					return "Todos os campos são de preenchimento obrigatório!";
				}
				
			} else {
				return "Todos os campos são de preenchimento obrigatório!";
			}
		}
		return null;
	}
	
	

}
