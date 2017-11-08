package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.EnumTipoRegistroEstoque;
import dominio.livro.Registro;
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
		if(connection == null) {
			abrirConexao();
		}
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
			
			// SALVANDO NOVO REGISTRO NO ESTOQUE
			RegistroDAO registroDao = new RegistroDAO();
			registroDao.ctrlTransacao = false;
			for(ItemPedido item : pedido.getItens()) {
				Registro registro = new Registro();
				registro.setIdEstoque(item.getEstoque().getId());
				registro.setQtde(item.getQtde());
				registro.setTipoRegistro(EnumTipoRegistroEstoque.SAIDA.getValue());
				registro.setValorVenda(item.getEstoque().getLivro().getPrecoVenda());
				registroDao.salvar(registro);
			}
			
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException el){
				el.printStackTrace();
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
//		if(connection == null) {
//			abrirConexao();
//		}
//		PreparedStatement pst = null;
//		Genero genero = (Genero)entidade;
//		StringBuilder sql = new StringBuilder();
//		
//		sql.append("UPDATE Genero SET ");
//		sql.append("nome = ? ");
//		sql.append("WHERE id = ?");
//		try {
//			connection.setAutoCommit(false);
//			
//			pst = connection.prepareStatement(sql.toString());
//			pst.setString(1, genero.getNome());
//			pst.setInt(2, genero.getId());
//			
//			pst.executeUpdate();
//			connection.commit();
//		} catch(SQLException e) {
//			try {
//				connection.rollback();
//			} catch(SQLException e1) {
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		} finally {
//			if(ctrlTransacao) {
//				try {
//					pst.close();
//					if(ctrlTransacao)
//						connection.close();
//				} catch(SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
	}

	@Override
	public EntidadeDominio visualizar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
//		if(connection == null || connection.isClosed()) {
//			abrirConexao();
//		}
//		PreparedStatement pst = null;
//		StringBuilder sql = new StringBuilder();
//		sql.append("SELECT * FROM Genero");
//		
//		try {
//			pst = connection.prepareStatement(sql.toString());			
//			ResultSet rs = pst.executeQuery();
//			List<EntidadeDominio> generos = new ArrayList<EntidadeDominio>()	;
//			while(rs.next() ) {
//				Genero genero = new Genero();
//				genero.setId(rs.getInt("id"));
//				genero.setDtCadastro(rs.getDate("dtCadastro"));
//				genero.setNome(rs.getString("nome"));
//				generos.add(genero);
//			}
//			rs.close();
//			return generos;
//		} catch (SQLException ex) {
//			System.out.println("\n--- SQLException ---\n");
//			while( ex != null ) {
//				System.out.println("Mensagem: " + ex.getMessage());
//				System.out.println("SQLState: " + ex.getSQLState());
//				System.out.println("ErrorCode: " + ex.getErrorCode());
//				ex = ex.getNextException();
//				System.out.println("");
//			}
//		} finally {
//			try {
//				pst.close();
//				if(ctrlTransacao)
//					connection.close();
//			} catch(SQLException e) {
//				e.printStackTrace();
//			}
//		}
		return null;
	}

}
