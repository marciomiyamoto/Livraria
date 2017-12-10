package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.venda.CupomTroca;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosCupomTroca implements IStrategy{

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof CupomTroca) {
			CupomTroca cupom = (CupomTroca)entidade;
			if(cupom.getAtivo() == null || cupom.getCodigo() == null || cupom.getIdCliente() == null || cupom.getValor() == null ||
				cupom.getCodigo().equals("") || cupom.getIdCliente() == 0 || cupom.getValor() == 0) {
					return "Dados obrigatórios: ativo, código, valor e idCliente";
				}
		}
		return null;
	}
}
