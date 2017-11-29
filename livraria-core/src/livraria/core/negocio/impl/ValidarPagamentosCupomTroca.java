package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.venda.Pagamento;
import dominio.venda.Pedido;
import livraria.core.IStrategy;

public class ValidarPagamentosCupomTroca implements IStrategy{

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			// VERIFICA SE EXISTE AO MENOS UMA FORMA DE PAGAMENTO
			if(pedido.getPagamentos() != null && !pedido.getPagamentos().isEmpty()) {
				double valorTemp = 0;
				// PERCORRE A LISTA DE PAGAMENTOS E VERIFICA SE A QTDE DE CUPONS NAO SUPERA O VALOR TOTAL DA COMPRA DESNECESSARIAMENTE
				for(Pagamento p : pedido.getPagamentos()) {
					if(p.getFormaPgto().getCupomTroca() != null) {
						if(valorTemp >= pedido.getValorTotalComDescontos() && p.getFormaPgto().getCupomTroca() != null) {
							return "Deve ser utilizado somente a quantidade de cupons para cobrir o valor da compra";
						}
						valorTemp += p.getValor();
					}
				}
			} else {
				return "Erro! Selecione uma forma de pagamento";
			}
		}
		return null;
	}

}
