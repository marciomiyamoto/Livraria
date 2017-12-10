package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.cliente.Cartao;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosCartao implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Cartao) {
			Cartao cartao = (Cartao)entidade;
			// VERIFICA SE TODOS OS DADOS OBRIGATÓRIOS DE CARTÃO ESTÃO DIFERENTES DE NULL
			if(cartao.getBandeira().getId() != null && cartao.getCodSeguranca() != null && cartao.getDtVencimento() != null &&
					cartao.getIdCliente() != null && cartao.getNomeImpresso() != null && cartao.getNumero() != null &&
					cartao.getTipoCartao() != null) {
				// VERIFICA SE TODOS OS CAMPOS OBRIGATÓRIOS DE CARTÃO ESTÃO PREENCHIDOS
				if(cartao.getCodSeguranca() != null && cartao.getBandeira().getId() != 0 &&
						cartao.getDtVencimento() != null && cartao.getIdCliente() != 0 && cartao.getNomeImpresso() != "" && 
						cartao.getNumero() != 0 && cartao.getTipoCartao() != 0) {
					return null;
				}
			}
			return "Todo cartão de crédito associado a um cliente deverá ser composto pelos seguintes campos de preenchimento obrigatório: "
					+ "Nº do Catão, Nome impresso no Cartão, Bandeira do Cartão e Código de Segurança.";
		}
		return null;
	}
}
