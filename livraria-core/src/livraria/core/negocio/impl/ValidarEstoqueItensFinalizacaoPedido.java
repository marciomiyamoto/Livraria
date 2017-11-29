package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.venda.ItemPedido;
import dominio.venda.Pedido;
import livraria.core.IStrategy;

public class ValidarEstoqueItensFinalizacaoPedido implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			// VERIFICA SE A LISTA DE ITENS NÃO ESTÁ VAZIA
			if(!pedido.getItens().isEmpty()) {
				ValidarEstoqueItemPedido vEstoque = new ValidarEstoqueItemPedido();
				// VERIFICA SE EXISTE ALGUM ITEM SEM ESTOQUE E RETORNA MENSAGEM DE ERRO, CASO EXISTA
				for(ItemPedido item : pedido.getItens()) {
					if(vEstoque.processar(item).equals("Indisponivel\n")) {
						return "Erro! Item ID " + item.getEstoque().getId() + " sem estoque";
					}
				}
			} else {
				return "Erro! Pedido sem itens!";
			}
		}
		return null;
	}
}
