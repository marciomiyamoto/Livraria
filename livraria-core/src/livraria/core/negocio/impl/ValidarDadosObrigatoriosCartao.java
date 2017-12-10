package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.cliente.Cartao;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosCartao implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Cartao) {
			Cartao cartao = (Cartao)entidade;
			// VERIFICA SE TODOS OS DADOS OBRIGAT�RIOS DE CART�O EST�O DIFERENTES DE NULL
			if(cartao.getBandeira().getId() != null && cartao.getCodSeguranca() != null && cartao.getDtVencimento() != null &&
					cartao.getIdCliente() != null && cartao.getNomeImpresso() != null && cartao.getNumero() != null &&
					cartao.getTipoCartao() != null) {
				// VERIFICA SE TODOS OS CAMPOS OBRIGAT�RIOS DE CART�O EST�O PREENCHIDOS
				if(cartao.getCodSeguranca() != null && cartao.getBandeira().getId() != 0 &&
						cartao.getDtVencimento() != null && cartao.getIdCliente() != 0 && cartao.getNomeImpresso() != "" && 
						cartao.getNumero() != 0 && cartao.getTipoCartao() != 0) {
					return null;
				}
			}
			return "Todo cart�o de cr�dito associado a um cliente dever� ser composto pelos seguintes campos de preenchimento obrigat�rio: "
					+ "N� do Cat�o, Nome impresso no Cart�o, Bandeira do Cart�o e C�digo de Seguran�a.";
		}
		return null;
	}
}
