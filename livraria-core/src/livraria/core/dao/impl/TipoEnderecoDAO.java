package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.endereco.TipoEndereco;
import dominio.livro.Autor;

public class TipoEnderecoDAO extends AbstractJdbcDAO {
	
	protected TipoEnderecoDAO(String tabela, String idTabela) {
		super("TipoEndereco", "id");
	}
	
	public TipoEnderecoDAO(Connection conn) {
		super(conn, "TipoEndereco", "id");
	}
	
	public TipoEnderecoDAO() {
		super("TipoEndereco", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		TipoEndereco tipo = (TipoEndereco)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO TipoEndereco ");
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
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		TipoEndereco tipo = (TipoEndereco) entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE TipoEndereco SET ");
		sql.append("nome = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, tipo.getNome());
			pst.setInt(2, tipo.getId());
			
			pst.executeUpdate();
			connection.commit();
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch(SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			if(ctrlTransacao) {
				try {
					pst.close();
					if(ctrlTransacao)
						connection.close();
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
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM TipoEndereco");
		
		try {
			pst = connection.prepareStatement(sql.toString());			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> tipos = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				TipoEndereco tipo = new TipoEndereco();
				tipo.setId(rs.getInt("id"));
				tipo.setDtCadastro(rs.getDate("dtCadastro"));
				tipo.setNome(rs.getString("nome"));
				tipos.add(tipo);
			}
			rs.close();
			return tipos;
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
