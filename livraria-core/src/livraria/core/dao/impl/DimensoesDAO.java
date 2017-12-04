package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Autor;
import dominio.livro.Dimensoes;

public class DimensoesDAO extends AbstractJdbcDAO {
	
	protected DimensoesDAO(String tabela, String idTabela) {
		super("Dimensoes", "id");
	}
	
	public DimensoesDAO(Connection conn) {
		super(conn, "Dimensoes", "id");
	}
	
	public DimensoesDAO() {
		super("Dimensoes", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		Dimensoes dimensoes = (Dimensoes)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Dimensoes");
		sql.append("(largura,  ");
		sql.append("altura,  ");
		sql.append("peso,  ");
		sql.append("profundidade) ");
		sql.append("VALUES(?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setDouble(1, dimensoes.getLargura());
			pst.setDouble(2, dimensoes.getAltura());
			pst.setDouble(3, dimensoes.getPeso());
			pst.setDouble(4, dimensoes.getProfundidade());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				dimensoes.setId(generatedKeys.getInt(1));
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
		Dimensoes dimensoes = (Dimensoes) entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Dimensoes SET ");
		sql.append("altura = ?, ");
		sql.append("largura = ?, ");
		sql.append("peso = ?, ");
		sql.append("profundidade = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setDouble(1, dimensoes.getAltura());
			pst.setDouble(2, dimensoes.getLargura());
			pst.setDouble(3, dimensoes.getPeso());
			pst.setDouble(4, dimensoes.getProfundidade());
			pst.setInt(5, dimensoes.getId());
			
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
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Dimensoes");
		
		try {
			pst = connection.prepareStatement(sql.toString());			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> dimensoes = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				Dimensoes dim = new Dimensoes();
				dim.setAltura(rs.getDouble("altura"));
				dim.setLargura(rs.getDouble("largura"));
				dim.setPeso(rs.getDouble("peso"));
				dim.setProfundidade(rs.getDouble("profundidade"));
				dimensoes.add(dim);
			}
			rs.close();
			return dimensoes;
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
