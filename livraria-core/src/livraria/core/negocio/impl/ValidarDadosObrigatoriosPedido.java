package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.venda.Pagamento;
import dominio.venda.Pedido;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosPedido implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			// VERIFICA SE TODOS OS DADOS OBRIGAT�RIOS EXISTEM
			if(pedido.getCliente() == null || pedido.getValorTotal() == null || pedido.getValorTotalComDescontos() == null || pedido.getEndEntrega() == null ||
					pedido.getCustoFrete() == null || pedido.getStatusPedido() == null || pedido.getPagamentos() == null || pedido.getItens() == null) {
				return "Todos os dados s�o obrigat�rios";
			}
			// VERIFICA SE O VALOR DO PEDIDO EST� CORRETO
			if(pedido.getValorTotal() == 0 || pedido.getValorTotalComDescontos() == 0) {
				return "Valor total ou final incorreto";
			}
			// VERIFICA SE O VALOR DOS PAGAMENTOS N�O EST� COM VALOR INV�LIDO
			for(Pagamento p : pedido.getPagamentos()) {
				if(p.getValor() <= 0) {
					return "Pagamento com valor inv�lido";
				}
			}
		}
		return null;
	}

}
