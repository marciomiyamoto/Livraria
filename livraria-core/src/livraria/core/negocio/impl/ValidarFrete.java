package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.venda.Pedido;
import livraria.core.IStrategy;

public class ValidarFrete implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			// VERIFCA SE O CLIENTE ESCOLHEU UMA FORMA DE FRETE
			if(pedido.getCustoFrete() != null) {
				// VERIFICA SE HOUVE ALGUM ERRO DURANTE A ESCOLHA DO FRETE
				if(!pedido.getCustoFrete().getErro().equals("0")) {
					return "Erro ao escolher o frete";
				}
				// VERIFICA SE O VALOR DO FRETE ESTÁ INVÁLIDO
				if(pedido.getCustoFrete().getValor() == null) {
					return "Valor inválido para o frete";
				}
			} else {
				return "Escolha uma forma de frete";
			}
		}
		return null;
	}

}
