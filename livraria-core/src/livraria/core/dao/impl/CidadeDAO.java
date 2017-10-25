package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.endereco.Cidade;
import dominio.endereco.Estado;
import dominio.endereco.Pais;

public class CidadeDAO extends AbstractJdbcDAO {
	
	protected CidadeDAO(String tabela, String idTabela) {
		super("Cidade", "id");
	}
	
	public CidadeDAO(Connection conn) {
		super(conn, "Cidade", "id");
	}
	
	public CidadeDAO() {
		super("Cidade", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Cidade cidade = (Cidade)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Cidade ");
		sql.append("(nome, id_estado) ");
		sql.append("VALUES(?, ?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, cidade.getNome());
			pst.setInt(2, cidade.getEstado().getId());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				cidade.setId(generatedKeys.getInt(1));
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
		Cidade cidade = (Cidade)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Cidade SET ");
		sql.append("nome = ?, ");
		sql.append("id_estado = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, cidade.getNome());
			pst.setInt(2, cidade.getEstado().getId());
			pst.setInt(2, cidade.getId());
			
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
		Cidade cidade = (Cidade)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Cidade ");
		sql.append("WHERE 1=1 ");
		
		if(cidade.getEstado().getId() != null && cidade.getEstado().getId() != 0) {
			sql.append("AND id_estado = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(cidade.getEstado().getId() != null && cidade.getEstado().getId() != 0) {
				pst.setInt(i, cidade.getEstado().getId());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> cidades = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				cidade = new Cidade();
				Estado estado = new Estado();
				estado.setId(rs.getInt("id_estado"));
				cidade.setId(rs.getInt("id"));
				cidade.setDtCadastro(rs.getDate("dtCadastro"));
				cidade.setNome(rs.getString("nome"));
				cidade.setEstado(estado);
				cidades.add(cidade);
			}
			rs.close();
			return cidades;
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
