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
			// VERIFICA SE TODOS OS DADOS OBRIGATÓRIOS EXISTEM
			if(pedido.getCliente() == null || pedido.getValorTotal() == null || pedido.getValorTotalComDescontos() == null || pedido.getEndEntrega() == null ||
					pedido.getCustoFrete() == null || pedido.getStatusPedido() == null || pedido.getPagamentos() == null || pedido.getItens() == null) {
				return "Todos os dados são obrigatórios";
			}
			// VERIFICA SE O VALOR DO PEDIDO ESTÁ CORRETO
			if(pedido.getValorTotal() == 0 || pedido.getValorTotalComDescontos() == 0) {
				return "Valor total ou final incorreto";
			}
			// VERIFICA SE O VALOR DOS PAGAMENTOS NÃO ESTÁ COM VALOR INVÁLIDO
			for(Pagamento p : pedido.getPagamentos()) {
				if(p.getValor() <= 0) {
					return "Pagamento com valor inválido";
				}
			}
		}
		return null;
	}

}
