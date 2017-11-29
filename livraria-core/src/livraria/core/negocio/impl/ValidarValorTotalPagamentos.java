package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.venda.Pagamento;
import dominio.venda.Pedido;
import livraria.core.IStrategy;

public class ValidarValorTotalPagamentos implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			// VERIFICA SE EXISTE AO MENOS UMA FORMA DE PAGAMENTO
			if(pedido.getPagamentos() != null && !pedido.getPagamentos().isEmpty()) {
				double valor = 0;
				// PERCORRE A LISTA DE PAGAMENTOS E VERIFICA SE NÃO EXISTEM PAGAMENTOS EM EXCESSO
				for(Pagamento p : pedido.getPagamentos()) {
					if(valor >= pedido.getValorTotalComDescontos() && p.getFormaPgto() != null) {
						return "Formas de pagamento em excesso!";
					} 
					valor += p.getValor();
				}
				// VERIFICA SE O VALOR DO PAGAMENTO É INFERIOR AO VALOR FINAL DO PEDIDO
				if(valor < pedido.getValorTotalComDescontos()) {
					return "Pagamentos com valor inferior ao total do pedido";
				}
			} else {
				return "Erro! Selecione uma forma de pagamento"; 
			}
		}
		return null;
	}
}
