package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.livro.Livro;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosConfigLivro implements IStrategy {
	
	@Override
	public String processar(EntidadeDominio entidade) {

		if(entidade instanceof Livro) {
			Livro livro = (Livro)entidade;
			if(livro.getCatAtivInativacao().getId() == null || livro.getCatAtivInativacao().getId() == 0 ||
					livro.getCatAtivInativacao().getTipo() == null || livro.getCatAtivInativacao().getTipo().trim().equals("") ||
					livro.getAtivo() == null || 
					livro.getJustificativa() == null || livro.getJustificativa().trim().equals("")) {
				
				return "Status, Categoria de ativação / inativação e Justificativa são de preenchimento obrigatório!";
			}
		}
		return null;
	}
}
