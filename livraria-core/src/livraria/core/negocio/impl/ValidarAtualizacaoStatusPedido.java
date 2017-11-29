package livraria.core.negocio.impl;

import java.sql.SQLException;

import dominio.EntidadeDominio;
import dominio.venda.EnumStatusPedido;
import dominio.venda.Pedido;
import livraria.core.IStrategy;
import livraria.core.dao.impl.PedidoDAO;

public class ValidarAtualizacaoStatusPedido implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			// RECUPERANDO O STATUS ATUAL DO PEDIDO
			PedidoDAO pedidoDao = new PedidoDAO();
			Pedido pedidoTemp = new Pedido();
			pedidoTemp.setId(pedido.getId());
			try {
				pedidoTemp = (Pedido)pedidoDao.consultar(pedidoTemp).get(0);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// VERIFICA SE O PEDIDO EST� COM STATUS EM PROCESSAMENTO, CASO ESTEJA, SER� ATUALIZADO NA VALIDA��O ValidarPagamentosPedido
			if(pedidoTemp.getStatusPedido().equals(EnumStatusPedido.EM_PROCESSAMENTO.getValue()) && !pedido.getStatusPedido().equals(EnumStatusPedido.EM_PROCESSAMENTO.getValue())) {
				return "Status EM PROCESSAMENTO n�o deve ser alterado at� o pagamento ser validado";
			// VERIFICA SE O PEDIDO EST� COM STATUS APROVADO E SER� ATUALIZADO PARA STATUS EM TRANSPORTE
			} else if(pedidoTemp.getStatusPedido().equals(EnumStatusPedido.APROVADO.getValue()) && !pedido.getStatusPedido().equals(EnumStatusPedido.EM_TRANSPORTE.getValue())) {
				return "Status APROVADO deve ser atualizado para EM TRANSPORTE";
			// VERIFICA SE O PEDIDO EST� COM STATUS EM TRANSPORTE E SER� ATUALIZADO PARA STATUS EM ENTREGUE
			} else if(pedidoTemp.getStatusPedido().equals(EnumStatusPedido.EM_TRANSPORTE.getValue()) && !pedido.getStatusPedido().equals(EnumStatusPedido.ENTREGUE.getValue())) {
				return "Status EM TRANSPORTE deve ser atualizado para ENTREGUE";
			// VERIFICA SE O PEDIDO EST� COM STATUS ENTREGUE E SER� ATUALIZADO PARA STATUS EM TROCA
			} else if(pedidoTemp.getStatusPedido().equals(EnumStatusPedido.ENTREGUE.getValue()) && !pedido.getStatusPedido().equals(EnumStatusPedido.EM_TROCA.getValue())) {
				return "Status ENTREGUE deve ser atualizado para EM TROCA";
			// VERIFICA SE O PEDIDO EST� COM STATUS EM TROCA E SER� ATUALIZADO PARA STATUS TROCADO
			} else if(pedidoTemp.getStatusPedido().equals(EnumStatusPedido.EM_TROCA.getValue()) && !pedido.getStatusPedido().equals(EnumStatusPedido.TROCADO.getValue())) {
				return "Status EM TROCA deve ser atualizado para TROCADO";
			}
		}
		return null;
	}
}
