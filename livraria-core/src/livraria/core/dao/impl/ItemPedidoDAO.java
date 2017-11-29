package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Estoque;
import dominio.venda.ItemPedido;

public class ItemPedidoDAO extends AbstractJdbcDAO {
	
	protected ItemPedidoDAO(String tabela, String idTabela) {
		super("ItemPedido", "id");
	}
	
	public ItemPedidoDAO(Connection conn) {
		super(conn, "ItemPedido", "id");
	}
	
	public ItemPedidoDAO() {
		super("ItemPedido", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		ItemPedido item = (ItemPedido)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO ItemPedido ");
		sql.append("(qtde, valorUnitario, id_pedido, id_estoque) ");
		sql.append("VALUES(?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setInt(1, item.getQtde());
			pst.setDouble(2, item.getValorUnitario());
			pst.setInt(3, item.getIdPedido());
			pst.setInt(4, item.getEstoque().getId());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				item.setId(generatedKeys.getInt(1));
			}
			connection.commit();
			
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
//		
	}

	@Override
	public EntidadeDominio visualizar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		ItemPedido item = (ItemPedido)entidade;
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ItemPedido ");
		sql.append("WHERE 1=1 ");
		
		if(item.getIdPedido() != null && item.getIdPedido() != 0) {
			sql.append("AND id_pedido = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());			
			int i = 1;
			
			if(item.getIdPedido() != null && item.getIdPedido() != 0) {
				pst.setInt(i, item.getIdPedido());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> itens = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				item = new ItemPedido();
				Estoque estoque = new Estoque();
				EstoqueDAO estDao = new EstoqueDAO();
				
				item.setId(rs.getInt("id"));
				item.setDtCadastro(rs.getDate("dtCadastro"));
				item.setQtde(rs.getInt("qtde"));
				item.setValorUnitario(rs.getDouble("valorUnitario"));
				
				estDao.ctrlTransacao = false;
				estoque.setId(rs.getInt("id_estoque"));
				estoque = (Estoque)estDao.consultar(estoque).get(0);
				item.setEstoque(estoque);
				
				itens.add(item);
			}
			rs.close();
			return itens;
		} catch (SQLException ex) {
			System.out.println("\n--- SQLException ---\n");
			while( ex != null ) {
				System.out.println("Mensagem: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("ErrorCode: " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		} finally {
			try {
				pst.close();
				if(ctrlTransacao)
					connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
