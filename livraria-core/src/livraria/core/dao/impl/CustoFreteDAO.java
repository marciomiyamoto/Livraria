package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.venda.CustoFrete;
import dominio.venda.Frete;

public class CustoFreteDAO extends AbstractJdbcDAO {

	protected CustoFreteDAO(String tabela, String idTabela) {
		super("CustoFrete", "id");
	}
	
	public CustoFreteDAO(Connection conn) {
		super(conn, "CustoFrete", "id");
	}
	
	public CustoFreteDAO() {
		super("CustoFrete", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		CustoFrete custoFrete = (CustoFrete)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Frete ");
		sql.append("(valor, prazoEntrega, cepOrigem, cepDestino, tipoEntrega) ");
		sql.append("VALUES(?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setDouble(1, custoFrete.getValor());
			pst.setInt(2, custoFrete.getPrazoEntrega());
			pst.setString(3, custoFrete.getFrete().getCepOrigem());
			pst.setString(4, custoFrete.getFrete().getCepDestino());
			pst.setString(5, custoFrete.getFrete().getTipoEnvio());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				custoFrete.setId(generatedKeys.getInt(1));
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
		CustoFrete custoFrete = (CustoFrete)entidade;
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Frete ");
		sql.append("WHERE 1=1 ");
		
		if(custoFrete.getId() != null && custoFrete.getId() != 0) {
			sql.append("AND id = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(custoFrete.getId() != null && custoFrete.getId() != 0) {
				pst.setInt(i, custoFrete.getId());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> custoFretes = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				custoFrete = new CustoFrete();
				Frete frete = new Frete();
				
				frete.setCepDestino(rs.getString("cepDestino"));
				frete.setCepOrigem(rs.getString("cepOrigem"));
				frete.setTipoEnvio(rs.getString("tipoEntrega"));
				
				custoFrete.setId(rs.getInt("id"));
				custoFrete.setDtCadastro(rs.getDate("dtCadastro"));
				custoFrete.setValor(rs.getDouble("valor"));
				custoFrete.setPrazoEntrega(rs.getInt("prazoEntrega"));
				custoFrete.setFrete(frete);
				
				custoFretes.add(custoFrete);
			}
			rs.close();
			return custoFretes;
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
