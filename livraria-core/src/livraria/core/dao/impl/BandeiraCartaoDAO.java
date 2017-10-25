package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.cliente.BandeiraCartao;

public class BandeiraCartaoDAO extends AbstractJdbcDAO {

	protected BandeiraCartaoDAO(String tabela, String idTabela) {
		super("BandeiraCartao", "id");
	}
	
	public BandeiraCartaoDAO(Connection conn) {
		super(conn, "BandeiraCartao", "id");
	}
	
	public BandeiraCartaoDAO() {
		super("BandeiraCartao", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		BandeiraCartao bandeira = (BandeiraCartao)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO BandeiraCartao ");
		sql.append("(nome, bin) ");
		sql.append("VALUES(?, ?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, bandeira.getNome());
			pst.setInt(2, bandeira.getBin());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				bandeira.setId(generatedKeys.getInt(1));
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
		BandeiraCartao bandeira = (BandeiraCartao) entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE BandeiraCartao SET ");
		sql.append("nome = ?, ");
		sql.append("bin = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, bandeira.getNome());
			pst.setInt(2, bandeira.getBin());
			pst.setInt(3, bandeira.getId());
			
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
		sql.append("SELECT * FROM BandeiraCartao");
		
		try {
			pst = connection.prepareStatement(sql.toString());			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> bandeiras = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				BandeiraCartao bandeira = new BandeiraCartao();
				bandeira.setId(rs.getInt("id"));
				bandeira.setDtCadastro(rs.getDate("dtCadastro"));
				bandeira.setNome(rs.getString("nome"));
				bandeira.setBin(rs.getInt("bin"));
				bandeiras.add(bandeira);
			}
			rs.close();
			return bandeiras;
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
		return null;
	}
}
