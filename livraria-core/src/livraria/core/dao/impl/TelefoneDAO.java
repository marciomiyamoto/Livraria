package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.Telefone;
import dominio.TipoTelefone;
import dominio.livro.Autor;

public class TelefoneDAO extends AbstractJdbcDAO {
	
	protected TelefoneDAO(String tabela, String idTabela) {
		super("Telefone", "id");
	}
	
	public TelefoneDAO(Connection conn) {
		super(conn, "Telefone", "id");
	}
	
	public TelefoneDAO() {
		super("Telefone", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Telefone tel = (Telefone)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Telefone ");
		sql.append("(ddd, numero, id_tipo) ");
		sql.append("VALUES(?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setInt(1, tel.getDdd());
			pst.setInt(2, tel.getNumero());
			pst.setInt(3, tel.getTipo().getId());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				tel.setId(generatedKeys.getInt(1));
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
		Telefone tel = (Telefone) entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Telefone SET ");
		sql.append("ddd = ?, ");
		sql.append("numero = ?, ");
		sql.append("id_tipo = ?");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, tel.getDdd());
			pst.setInt(2, tel.getNumero());
			pst.setInt(3, tel.getTipo().getId());
			pst.setInt(4, tel.getId());
			
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
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Telefone tel = (Telefone)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Telefone ");
		sql.append("WHERE 1 =1 ");
		
		if(tel.getDdd() != null && tel.getDdd() != 0) {
			sql.append("AND ddd = ? ");
		}
		if(tel.getNumero() != null && tel.getNumero() != 0) {
			sql.append("AND numero = ? ");
		}
		if(tel.getTipo().getId() != null && tel.getTipo().getId() != 0) {
			sql.append("AND id_tipo = ? ");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			
			if(tel.getDdd() != null && tel.getDdd() != 0) {
				pst.setInt(i, tel.getDdd());
				i++;
			}
			if(tel.getNumero() != null && tel.getNumero() != 0) {
				pst.setInt(i, tel.getNumero());
				i++;
			}
			if(tel.getTipo().getId() != null && tel.getTipo().getId() != 0) {
				pst.setInt(i, tel.getTipo().getId());
				i++;
			}
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> telefones = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				tel = new Telefone();
				TipoTelefone tipo = new TipoTelefone();
				tel.setId(rs.getInt("id"));
				tel.setDtCadastro(rs.getDate("dtCadastro"));
				tel.setDdd(rs.getInt("ddd"));
				tel.setNumero(rs.getInt("numero"));
				tipo.setId(rs.getInt("id_tipo"));
				tel.setTipo(tipo);
				telefones.add(tel);
			}
			rs.close();
			return telefones;
		} catch (SQLException ex) {
			System.out.println("\n--- SQLException ---\n");
			while( ex != null ) {
				System.out.println("Mensagem: " + ex.getMessage());
				System.out.println("SQLState: " + ex.getSQLState());
				System.out.println("ErrorCode: " + ex.getErrorCode());
				ex = ex.getNextException();
				System.out.println("");
			}
		}
		return null;
	}

}
