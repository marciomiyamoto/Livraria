package livraria.core.negocio.impl;

import java.sql.SQLException;

import dominio.EntidadeDominio;
import dominio.livro.Estoque;
import dominio.venda.ItemPedido;
import livraria.core.IStrategy;
import livraria.core.dao.impl.EstoqueDAO;

public class ValidarEstoqueItemPedido implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof ItemPedido) {
			Estoque estoqueTemp = new Estoque();
			EstoqueDAO estDAO = new EstoqueDAO();
			ItemPedido item = (ItemPedido)entidade;
			CalcularEstoque calcEst = new CalcularEstoque();
			// ATRIBUI ID DO ESTOQUE A VARIAVEL TEMPORARIA PARA CONSULTA 
			estoqueTemp.setId(item.getEstoque().getId());
			try {
				estoqueTemp = (Estoque)estDAO.consultar(estoqueTemp).get(0);
				// CALCULA QTDE EM ESTQUE
				calcEst.processar(estoqueTemp);
				// CASO A QTDE EM ESTOQUE SEJA MAIOR OU IGUAL A QTDE ADICIONADA NO CARRINHO, RETORNA DISPONIVEL
				// CASO CONTRÁRIO, RETORNA INDISPONIVEL
				if(estoqueTemp.getQtdeTotal() != null && estoqueTemp.getQtdeTotal() >= item.getQtde()) {
					return "Disponivel";
				} else {
					return "Indisponivel";
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
