package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.venda.ItemBloqueioPedido;

public class ItemBloqueioPedidoDAO extends AbstractJdbcDAO {

	protected ItemBloqueioPedidoDAO(String tabela, String idTabela) {
		super("ItemBloqueioPedido", "id");
	}
	
	public ItemBloqueioPedidoDAO(Connection conn) {
		super(conn, "ItemBloqueioPedido", "id");
	}
	
	public ItemBloqueioPedidoDAO() {
		super("ItemBloqueioPedido", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		ItemBloqueioPedido item = (ItemBloqueioPedido)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO ItemBloqueioPedido ");
		sql.append("(hora, qtde, id_estoque) ");
		sql.append("VALUES(?, ?, ?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setTime(1, item.getHora());
			pst.setInt(2, item.getQtde());
			pst.setInt(3, item.getIdEstoque());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				item.setId(generatedKeys.getInt(1));
			}
			generatedKeys.close();
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
	public void alterar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		ItemBloqueioPedido item = (ItemBloqueioPedido)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE ItemBloqueioPedido SET ");
		sql.append("hora = ?, ");
		sql.append("qtde = ?, ");
		sql.append("id_estoque = ?, ");
		sql.append("id_pedido = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setTime(1, item.getHora());
			pst.setInt(2, item.getQtde());
			pst.setInt(3, item.getIdEstoque());
			if(item.getIdPedido() != null && item.getIdPedido() != 0) {
				pst.setInt(4, item.getIdPedido());
			} else {
				pst.setNull(4, Types.INTEGER);
			}
			pst.setInt(5, item.getId());
			
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
		PreparedStatement pst = null;
		ItemBloqueioPedido item = (ItemBloqueioPedido)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM ItemBloqueioPedido ");
		sql.append("WHERE 1=1 ");
		
		if(item.getId() != null && item.getId() != 0) {
			sql.append("AND id = ?");
		}
		if(item.getIdEstoque() != null && item.getIdEstoque() != 0) {
			sql.append("AND id_estoque = ?");
		}
		if(item.getIdPedido() != null && item.getIdPedido() != 0) {
			sql.append("AND id_pedido = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(item.getId() != null && item.getId() != 0) {
				pst.setInt(i, item.getId());
				i++;
			}
			if(item.getIdEstoque() != null && item.getIdEstoque() != 0) {
				pst.setInt(i, item.getIdEstoque());
				i++;
			}
			if(item.getIdPedido() != null && item.getIdPedido() != 0) {
				pst.setInt(i, item.getIdPedido());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> itens = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				item = new ItemBloqueioPedido();
				item.setId(rs.getInt("id"));
				item.setDtCadastro(rs.getDate("dtCadastro"));
				item.setHora(rs.getTime("hora"));
				item.setQtde(rs.getInt("qtde"));
				item.setIdEstoque(rs.getInt("id_estoque"));
				item.setIdPedido(rs.getInt("id_pedido"));
				itens.add(item);
			}
			rs.close();
			return itens;
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
			try {
				if(pst != null) {
					pst.close();
				}
				if(ctrlTransacao) {
					connection.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
