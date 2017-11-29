package livraria.core.negocio.impl;

import java.sql.SQLException;
import java.util.Date;

import dominio.EntidadeDominio;
import dominio.cliente.Cartao;
import dominio.livro.EnumTipoRegistroEstoque;
import dominio.livro.Registro;
import dominio.venda.CupomTroca;
import dominio.venda.EnumStatusPedido;
import dominio.venda.ItemBloqueioPedido;
import dominio.venda.ItemPedido;
import dominio.venda.Pagamento;
import dominio.venda.Pedido;
import livraria.core.IStrategy;
import livraria.core.dao.impl.ItemBloqueioPedidoDAO;
import livraria.core.dao.impl.RegistroDAO;

public class ValidarPagamentosPedido implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			// VERIFICA SE O STATUS DO PEDIDO ESTÁ EM PROCESSAMENTO, CASO ESTEJA, VERIFICA MEIOS DE PAGAMENTO
			if(pedido.getStatusPedido().equals(EnumStatusPedido.EM_PROCESSAMENTO.getValue())) {
				// PERCORRE A LISTA DE PAGAMENTOS PARA VALIDAR OS PAGAMENTOS
				for(Pagamento pg : pedido.getPagamentos()) {
					// CASO SEJA CARTÃO
					if(pg.getFormaPgto().getCartao() != null) {
						Cartao cartao = pg.getFormaPgto().getCartao();
						Date date = new Date();
						// CASO A DATA DE VEENCIMENTO SEJA ANTERIOR A DATA ATUAL
						if(cartao.getDtVencimento().before(date)) {
							pedido.setStatusPedido(EnumStatusPedido.REPROVADO.getValue());
						}
						// CASO ESTEJA FALTANDO ALGUM DOS DADOS OBRIGATÓRIOS
						if(cartao.getCodSeguranca() == null || cartao.getNomeImpresso() == null || cartao.getNumero() == null || cartao.getBandeira() == null) {
							pedido.setStatusPedido(EnumStatusPedido.REPROVADO.getValue());
						}
						// CASO ALGUM DOS DADOS OBRIGATÓRIOS ESTEJA EM BRANCO
						if(cartao.getCodSeguranca() == 0 || cartao.getNomeImpresso().equals("") || cartao.getNumero() == 0) {
							pedido.setStatusPedido(EnumStatusPedido.REPROVADO.getValue());
						}
					// CASO SEJA CUPOM DE TROCA
					} else if(pg.getFormaPgto().getCupomTroca() != null) {
						CupomTroca cupom = pg.getFormaPgto().getCupomTroca();
						// CASO O CUPOM DE TROCA ESTEJA INATIVO
						if(!cupom.getAtivo()) {
							pedido.setStatusPedido(EnumStatusPedido.REPROVADO.getValue());
						}
						// CASO O CUPOM DE TROCA NÃO SEJA DO CLIENTE 
						if(cupom.getIdCliente().equals(pedido.getCliente().getId())) {
							pedido.setStatusPedido(EnumStatusPedido.REPROVADO.getValue());
						}
					}
				}
				// CASO OS PAGAMENTOS PASSEM EM TODAS AS VALIDAÇÕES, TERÁ STATUS APROVADO
				if(!pedido.getStatusPedido().equals(EnumStatusPedido.REPROVADO.getValue())) {
					pedido.setStatusPedido(EnumStatusPedido.APROVADO.getValue());
					// REGISTRA ITENS DE PEDIDO NO REGISTRO DE ESTOQUE
					RegistroDAO regDao = new RegistroDAO();
					for(ItemPedido item : pedido.getItens()) {
						Registro registro = new Registro();
						registro.setIdEstoque(item.getEstoque().getId());
						registro.setQtde(item.getQtde());
						registro.setTipoRegistro(EnumTipoRegistroEstoque.SAIDA.getValue());
						registro.setValorVenda(item.getValorUnitario());
						try {
							regDao.salvar(registro);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				// REMOVE ITENS BLOQUEADOS DO PEDIDO
				ItemBloqueioPedidoDAO itemDao = new ItemBloqueioPedidoDAO();
				for(ItemBloqueioPedido item : pedido.getItensBloqueados()) {
					try {
						itemDao.excluir(item);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
}
