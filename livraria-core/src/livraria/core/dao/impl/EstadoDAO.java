package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.Genero;
import dominio.endereco.Estado;
import dominio.endereco.Pais;

public class EstadoDAO extends AbstractJdbcDAO {

	protected EstadoDAO(String tabela, String idTabela) {
		super("Estado", "id");
	}
	
	public EstadoDAO(Connection conn) {
		super(conn, "Estado", "id");
	}
	
	public EstadoDAO() {
		super("Estado", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		Estado estado = (Estado)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Estado ");
		sql.append("(nome, id_pais) ");
		sql.append("VALUES(?, ?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, estado.getNome());
			pst.setInt(2, estado.getPais().getId());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				estado.setId(generatedKeys.getInt(1));
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
		Estado estado = (Estado)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Estado SET ");
		sql.append("nome = ?, ");
		sql.append("id_pais = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, estado.getNome());
			pst.setInt(2, estado.getPais().getId());
			pst.setInt(2, estado.getId());
			
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
		Estado estado = (Estado)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Estado ");
		sql.append("WHERE 1=1 ");
		
		if(estado.getPais().getId() != null && estado.getPais().getId() != 0) {
			sql.append("AND id_pais = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(estado.getPais().getId() != null && estado.getPais().getId() != 0) {
				pst.setInt(i, estado.getPais().getId());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> estados = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				estado = new Estado();
				Pais pais = new Pais();
				pais.setId(rs.getInt("id_pais"));
				estado.setId(rs.getInt("id"));
				estado.setDtCadastro(rs.getDate("dtCadastro"));
				estado.setNome(rs.getString("nome"));
				estado.setPais(pais);
				estados.add(estado);
			}
			rs.close();
			return estados;
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
