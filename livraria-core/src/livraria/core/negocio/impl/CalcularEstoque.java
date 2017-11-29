package livraria.core.negocio.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.EnumTipoRegistroEstoque;
import dominio.livro.Estoque;
import dominio.livro.Registro;
import dominio.venda.ItemBloqueioPedido;
import livraria.core.IStrategy;
import livraria.core.aplicacao.Resultado;
import livraria.core.dao.impl.ItemBloqueioPedidoDAO;

public class CalcularEstoque implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		
		if(entidade instanceof Estoque) {
			Estoque estoque = (Estoque)entidade;
			ItemBloqueioPedido itemBloq = new ItemBloqueioPedido();
			List<ItemBloqueioPedido> itensBloq = new ArrayList<ItemBloqueioPedido>();
			ItemBloqueioPedidoDAO itemBloqDao = new ItemBloqueioPedidoDAO();
			int qtdeTotal;
			// VERIFICA REGISTRO DE ESTOQUE DE LIVRO E CALCULA A DIFERENÇA ENTRE ENTRADAS E SAÍDAS 
			if(!estoque.getRegistros().isEmpty()) {
				int qtdeEntrada = 0;
				int qtdeSaida = 0;
				for(Registro r : estoque.getRegistros()) {
					if(r.getTipoRegistro().equals(EnumTipoRegistroEstoque.ENTRADA.getValue())) {
						qtdeEntrada += r.getQtde();
					}
					else if(r.getTipoRegistro().equals(EnumTipoRegistroEstoque.SAIDA.getValue())) {
						qtdeSaida += r.getQtde();
					}
				}
				qtdeTotal = qtdeEntrada - qtdeSaida;
				// VERIFICA ITENS BLOQUEADOS POR CARRINHOS CONCORRENTES OU PEDIDOS EM PROCESSAMENTO 
				itemBloq.setIdEstoque(estoque.getId());
				try {
					itensBloq = (List<ItemBloqueioPedido>)(List<?>)itemBloqDao.consultar(itemBloq);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for(ItemBloqueioPedido i : itensBloq) {
					qtdeTotal -= i.getQtde();
				}
				estoque.setQtdeTotal(qtdeTotal);
			}
		}

		return null;
	}

}
