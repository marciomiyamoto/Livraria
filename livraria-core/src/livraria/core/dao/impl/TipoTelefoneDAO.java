package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.TipoTelefone;
import dominio.livro.Autor;

public class TipoTelefoneDAO extends AbstractJdbcDAO {

	protected TipoTelefoneDAO(String tabela, String idTabela) {
		super("TipoTelefone", "id");
	}
	
	public TipoTelefoneDAO(Connection conn) {
		super(conn, "TipoTelefone", "id");
	}
	
	public TipoTelefoneDAO() {
		super("TipoTelefone", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		TipoTelefone tipo = (TipoTelefone)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO TipoTelefone ");
		sql.append("(nome) ");
		sql.append("VALUES(?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, tipo.getNome());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				tipo.setId(generatedKeys.getInt(1));
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
		sql.append("SELECT * FROM TipoTelefone");
		
		try {
			pst = connection.prepareStatement(sql.toString());			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> tipos = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				TipoTelefone tipo = new TipoTelefone();
				tipo.setId(rs.getInt("id"));
				tipo.setDtCadastro(rs.getDate("dtCadastro"));
				tipo.setNome(rs.getString("nome"));
				tipos.add(tipo);
			}
			rs.close();
			return tipos;
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
