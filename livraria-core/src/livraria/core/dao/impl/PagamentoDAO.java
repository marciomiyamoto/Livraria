package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.venda.ItemPedido;
import dominio.venda.Pagamento;

public class PagamentoDAO extends AbstractJdbcDAO {

	protected PagamentoDAO(String tabela, String idTabela) {
		super("Pagamento", "id");
	}
	
	public PagamentoDAO(Connection conn) {
		super(conn, "Pagamento", "id");
	}
	
	public PagamentoDAO() {
		super("Pagamento", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Pagamento pagamento = (Pagamento)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Pagamento ");
		sql.append("(valor, status, id_cupomTroca, id_cartao, id_pedido) ");
		sql.append("VALUES(?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setDouble(1, pagamento.getValor());
			pst.setInt(2, pagamento.getStatus());
			if(pagamento.getFormaPgto().getCupomTroca() != null) {
				pst.setInt(3, pagamento.getFormaPgto().getCupomTroca().getId());
			} else {
				pst.setNull(3, Types.INTEGER);
			}
			if(pagamento.getFormaPgto().getCartao() != null) {
				pst.setInt(4, pagamento.getFormaPgto().getCartao().getId());
			} else {
				pst.setNull(4, Types.INTEGER);
			}
			pst.setInt(5, pagamento.getIdPedido());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				pagamento.setId(generatedKeys.getInt(1));
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
