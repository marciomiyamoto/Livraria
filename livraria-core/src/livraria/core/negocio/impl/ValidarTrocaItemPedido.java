package livraria.core.negocio.impl;

import java.sql.SQLException;

import dominio.EntidadeDominio;
import dominio.venda.EnumStatusItemPedido;
import dominio.venda.ItemPedido;
import dominio.venda.Pedido;
import livraria.core.IStrategy;
import livraria.core.dao.impl.ItemPedidoDAO;

public class ValidarTrocaItemPedido implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof ItemPedido) {
			ItemPedido item = (ItemPedido)entidade;
			// RECUPERANDO STATUS ATUAL DO ITEMPEDIDO
			ItemPedido itemTemp = new ItemPedido();
			ItemPedidoDAO itemDao = new ItemPedidoDAO();
			itemTemp.setId(item.getId());
			try {
				itemTemp = (ItemPedido)itemDao.consultar(itemTemp).get(0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// VERIFICA SE O ITEMPEDIDO ESTÁ COM UM DOS STATUS VÁLIDOS 
			if(!itemTemp.getStatus().equals(EnumStatusItemPedido.EM_PROCESSAMENTO.getValue()) &&
					!itemTemp.getStatus().equals(EnumStatusItemPedido.APROVADO.getValue()) &&
					!itemTemp.getStatus().equals(EnumStatusItemPedido.REPROVADO.getValue()) &&
					!itemTemp.getStatus().equals(EnumStatusItemPedido.EM_TRANSPORTE.getValue()) &&
					!itemTemp.getStatus().equals(EnumStatusItemPedido.ENTREGUE.getValue()) && 
					!itemTemp.getStatus().equals(EnumStatusItemPedido.CANCELADO.getValue()) && 
					!itemTemp.getStatus().equals(EnumStatusItemPedido.TROCADO.getValue()) && 
					!itemTemp.getStatus().equals(EnumStatusItemPedido.EM_TROCA.getValue())) {
				return "Status inválido! O status de item só deverá ser alterado, caso esteja com um dos status válidos no sistema";
			} else {
				// VERIFICA SE O ITEMPEDIDO ESTÁ COM STATUS ATUAL COMO ENTREGUE, CASO ESTEJA, SERÁ ATUALIZADO PARA STATUS EM TROCA
				if(itemTemp.getStatus().equals(EnumStatusItemPedido.ENTREGUE.getValue()) && !item.getStatus().equals(EnumStatusItemPedido.EM_TROCA.getValue())) {
					return "Status de item só deverá ser alterado para EM TROCA caso o item esteja com status atual ENTREGUE";
				// VERIFICA SE O ITEMPEDIDO ESTÁ COM STATUS ATUAL COMO EM TROCA, CASO ESTEJA, SERÁ ATUALIZADO PARA STATUS TROCADO
				} else if(itemTemp.getStatus().equals(EnumStatusItemPedido.EM_TROCA) && !item.getStatus().equals(EnumStatusItemPedido.TROCADO.getValue())) {
					return "Status de item só deverá ser alterado para TROCADO caso o item esteja com status atual EM TROCA";
				}
			}
		}
		return null;
	}

}
