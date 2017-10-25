package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.livro.Livro;
import livraria.core.IStrategy;

public class AssociarMotivoInativacao implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {

		if(entidade instanceof Livro) {
			Livro livro = (Livro)entidade;
			if(livro.getCatAtivInativacao().getTipo() != null && !livro.getCatAtivInativacao().getTipo().trim().equals("")){
				if(livro.getCatAtivInativacao().getTipo().equals("inativacao")) {
					if(livro.getCatAtivInativacao().getId() == 0 || 
							livro.getJustificativa() == null || livro.getJustificativa().trim().equals("")) {
						return "Todo livro deve ter uma justificativa e uma categoria de INATIVAÇÃO associada!";
					}
					if(livro.getAtivo()) {
						return "Categoria inválida! Deve ser selecionada uma categoria de INATIVAÇÃO.";
					}
				}
			}
		}
		return null;
	}

}
