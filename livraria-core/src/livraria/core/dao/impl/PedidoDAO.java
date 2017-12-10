package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.cliente.Cliente;
import dominio.endereco.Endereco;
import dominio.venda.CupomPromocional;
import dominio.venda.CupomTroca;
import dominio.venda.CustoFrete;
import dominio.venda.ItemBloqueioPedido;
import dominio.venda.ItemPedido;
import dominio.venda.Pagamento;
import dominio.venda.Pedido;

public class PedidoDAO extends AbstractJdbcDAO {
	
	protected PedidoDAO(String tabela, String idTabela) {
		super("Pedido", "id");
	}
	
	public PedidoDAO(Connection conn) {
		super(conn, "Pedido", "id");
	}
	
	public PedidoDAO() {
		super("Pedido", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		Pedido pedido = (Pedido)entidade;
		StringBuilder sql = new StringBuilder();
		
		// SALVANDO FRETE
		CustoFreteDAO custoFreteDAO = new CustoFreteDAO();
		custoFreteDAO.ctrlTransacao = false;
		custoFreteDAO.salvar(pedido.getCustoFrete());
		
		// SALVANDO ENDERECO DE ENTREGA
		EnderecoDAO endDao = new EnderecoDAO();
		endDao.ctrlTransacao = false;
		endDao.salvar(pedido.getEndEntrega());
		
		// SALVANDO CARTÕES DE CRÉDITO
		CartaoDAO cartaoDao = new CartaoDAO();
		cartaoDao.ctrlTransacao = false;
		for(Pagamento pgto : pedido.getPagamentos()) {
			if(pgto.getFormaPgto().getCartao() != null) {
				cartaoDao.salvar(pgto.getFormaPgto().getCartao());
			}
		}
		
		// SALVANDO PEDIDO
		
		sql.append("INSERT INTO Pedido ");
		sql.append("(valorTotal, status, id_cliente, id_cupomPromocional, id_frete, id_endEntrega, valorTotalComDescontos) ");
		sql.append("VALUES(?,?,?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setDouble(1, pedido.getValorTotal());
			pst.setInt(2, pedido.getStatusPedido());
			pst.setInt(3, pedido.getCliente().getId());
			if(pedido.getCupomPromocional() != null) {
				pst.setInt(4, pedido.getCupomPromocional().getId());
			} else {
				pst.setNull(4, Types.INTEGER);
			}
			pst.setInt(5, pedido.getCustoFrete().getId());
			pst.setInt(6, pedido.getEndEntrega().getId());
			pst.setDouble(7, pedido.getValorTotalComDescontos());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				pedido.setId(generatedKeys.getInt(1));
			}
			generatedKeys.close();
			connection.commit();
			
			// SALVANDO LISTA DE ITENS PEDIDO
			ItemPedidoDAO itemPedidoDao = new ItemPedidoDAO();
			itemPedidoDao.ctrlTransacao = false;
			for(ItemPedido item : pedido.getItens()) {
				item.setIdPedido(pedido.getId());
				itemPedidoDao.salvar(item);
			}
			
			// SALVANDO LISTA DE PAGAMENTOS
			PagamentoDAO pagamentoDao = new PagamentoDAO();
			pagamentoDao.ctrlTransacao = false;
			for(Pagamento pgto : pedido.getPagamentos()) {
				pgto.setIdPedido(pedido.getId());
				pagamentoDao.salvar(pgto);
			}
			
			// ATUALIZANDO LISTA DE ITENS BLOQUEADOS COM O ID DO PEDIDO
			ItemBloqueioPedidoDAO itemDao = new ItemBloqueioPedidoDAO();
			itemDao.ctrlTransacao = false;
			for(ItemBloqueioPedido i : pedido.getItensBloqueados()) {
				i.setIdPedido(pedido.getId());
				itemDao.alterar(i);
			}
			
			// ATUALIZANDO STATUS LISTA DE CUPONS DE TROCA UTILIZADOS
			CupomTrocaDAO cupDao = new CupomTrocaDAO();
			cupDao.ctrlTransacao = false;
			for(Pagamento pgto : pedido.getPagamentos()) {
				if(pgto.getFormaPgto().getCupomTroca() != null) {
					CupomTroca cupom = pgto.getFormaPgto().getCupomTroca();
					cupom.setAtivo(false);
					cupDao.alterar(cupom);
				}
			}
			
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("\n--- SQLException ---\n");
				while( ex != null ) {
					System.out.println("Mensagem: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("ErrorCode: " + ex.getErrorCode());
					ex = ex.getNextException();
					System.out.println("");
				}
				e.printStackTrace();
			}
		} finally {
			if(ctrlTransacao) {
				try {
					pst.close();
					if(ctrlTransacao) {
						connection.close();
					}
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		Pedido pedido = (Pedido)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Pedido SET ");
		sql.append("status = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, pedido.getStatusPedido());
			pst.setInt(2, pedido.getId());
			
			pst.executeUpdate();
			connection.commit();
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("\n--- SQLException ---\n");
				while( ex != null ) {
					System.out.println("Mensagem: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("ErrorCode: " + ex.getErrorCode());
					ex = ex.getNextException();
					System.out.println("");
				}
				e.printStackTrace();
			}
		} finally {
			if(ctrlTransacao) {
				try {
					pst.close();
					if(ctrlTransacao) {
						connection.close();
					}
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public EntidadeDominio visualizar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		Pedido pedido = (Pedido)entidade;
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Pedido ");
		sql.append("WHERE 1=1 ");
		
		if(pedido.getId() != null && pedido.getId() != 0) {
			sql.append("AND id = ? ");
		}
		if(pedido.getCliente() != null && pedido.getCliente().getId() != null && pedido.getCliente().getId() != 0) {
			sql.append("AND id_cliente = ? ");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			
			if(pedido.getId() != null && pedido.getId() != 0) {
				pst.setInt(i, pedido.getId());
				i++;
			}
			if(pedido.getCliente() != null && pedido.getCliente().getId() != null && pedido.getCliente().getId() != 0) {
				pst.setInt(i, pedido.getCliente().getId());
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> pedidos = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				pedido = new Pedido();
				Cliente cliente = new Cliente();
				ClienteDAO cliDao = new ClienteDAO();
				Endereco end = new Endereco();
				EnderecoDAO endDao = new EnderecoDAO();
				CustoFrete custoFrete = new CustoFrete();
				CustoFreteDAO custoFreDao = new CustoFreteDAO();
				Pagamento pgto = new Pagamento();
				List<Pagamento> pgtos = new ArrayList<Pagamento>();
				PagamentoDAO pgtoDao = new PagamentoDAO();
				ItemPedido itemPedido = new ItemPedido();
				List<ItemPedido> itensPedido = new ArrayList<ItemPedido>();
				ItemPedidoDAO itemPedDao = new ItemPedidoDAO();
				ItemBloqueioPedido itemBloqueio = new ItemBloqueioPedido();
				List<ItemBloqueioPedido> itensBloq = new ArrayList<ItemBloqueioPedido>();
				ItemBloqueioPedidoDAO itemBloqDao = new ItemBloqueioPedidoDAO();
				CupomPromocional cupomPromo = new CupomPromocional();
				CupomPromocionalDAO cupomPromoDao = new CupomPromocionalDAO();
				
				pedido.setId(rs.getInt("id"));
				pedido.setDtCadastro(rs.getDate("dtCadastro"));
				pedido.setStatusPedido(rs.getInt("status"));
				pedido.setValorTotal(rs.getDouble("valorTotal"));
				pedido.setValorTotalComDescontos(rs.getDouble("valorTotalComDescontos"));
				
				cliente.setId(rs.getInt("id_cliente"));
				cliente = (Cliente)cliDao.consultar(cliente).get(0);
				pedido.setCliente(cliente);
				
				end.setId(rs.getInt("id_endEntrega"));
				end = (Endereco)endDao.consultar(end).get(0);
				pedido.setEndEntrega(end);
				
				custoFrete.setId(rs.getInt("id_frete"));
				custoFrete = (CustoFrete)custoFreDao.consultar(custoFrete).get(0);
				pedido.setCustoFrete(custoFrete);
				
				pgto.setIdPedido(pedido.getId());
				pgtos = (List<Pagamento>)(List<?>)pgtoDao.consultar(pgto);
				pedido.setPagamentos(pgtos);
				
				itemPedido.setIdPedido(pedido.getId());
				itensPedido = (List<ItemPedido>)(List<?>)itemPedDao.consultar(itemPedido);
				pedido.setItens(itensPedido);
				
				itemBloqueio.setIdPedido(pedido.getId());
				itensBloq = (List<ItemBloqueioPedido>)(List<?>)itemBloqDao.consultar(itemBloqueio);
				pedido.setItensBloqueados(itensBloq);
				
				cupomPromo.setId(rs.getInt("id_cupomPromocional"));
				if(cupomPromo.getId() != null && cupomPromo.getId() != 0) {
					cupomPromo = (CupomPromocional)cupomPromoDao.consultar(cupomPromo).get(0);
					pedido.setCupomPromocional(cupomPromo);
				}
				
				pedidos.add(pedido);
			}
			rs.close();
			return pedidos;
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				System.out.println("\n--- SQLException ---\n");
				while( ex != null ) {
					System.out.println("Mensagem: " + ex.getMessage());
					System.out.println("SQLState: " + ex.getSQLState());
					System.out.println("ErrorCode: " + ex.getErrorCode());
					ex = ex.getNextException();
					System.out.println("");
				}
				e.printStackTrace();
			}
		} finally {
			if(ctrlTransacao) {
				try {
					pst.close();
					if(ctrlTransacao) {
						connection.close();
					}
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

}
