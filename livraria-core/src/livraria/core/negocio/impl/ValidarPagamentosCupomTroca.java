package livraria.core.negocio.impl;

import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.venda.CupomTroca;
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
				List<CupomTroca> cupons = new ArrayList<CupomTroca>();
				for(Pagamento p : pedido.getPagamentos()) {
					if(p.getFormaPgto().getCupomTroca() != null) {
						if(valorTemp >= pedido.getValorTotalComDescontos() && p.getFormaPgto().getCupomTroca() != null) {
							return "Deve ser utilizado somente a quantidade de cupons para cobrir o valor da compra";
						}
						// VERIFICA SE O CUPOM JÁ FOI UTILIZADO
						if(!p.getFormaPgto().getCupomTroca().getAtivo()) {
							return "Cupom já foi utilizado em outra compra";
						}
						cupons.add(p.getFormaPgto().getCupomTroca());
						valorTemp += p.getValor();
					}
				}
				// VERIFICA SE EXISTE CUPONS DUPLICADOS
				List<String> codUsados = new ArrayList<String>();
				for(CupomTroca c : cupons) {
					String codigo = c.getCodigo();
					if(codUsados.contains(codigo)) {
						return "Cupom de código " + codigo + " duplicado";
					}
					codUsados.add(codigo);
				}
			} else {
				return "Erro! Selecione uma forma de pagamento";
			}
		}
		return null;
	}

}
