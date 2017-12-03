package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.Telefone;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosTelefone implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Telefone) {
			Telefone tel = (Telefone)entidade;
			// VERIFICA SE TODOS OS DADOS OBRIGAT�RIOS DE TELEFONE EST�O DIFERENTES DE NULL
			if(tel.getDdd() != null && tel.getNumero() != null && tel.getTipo() != null) {
				// VERIFICA SE TODOS OS DADOS OBRIGAT�RIOS DE CART�O EST�O PREENCHIDOS
				if(tel.getDdd() != 0 && tel.getNumero() != 0 && tel.getTipo().getId() != 0) {
					return null;
				}
			}
			return "Todo telefone deve ser composto pelo tipo, DDD e n�mero";
		}
		return null;
	}
}
