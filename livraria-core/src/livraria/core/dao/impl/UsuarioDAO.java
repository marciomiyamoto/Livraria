package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.Usuario;
import dominio.livro.Autor;

public class UsuarioDAO extends AbstractJdbcDAO {
	
	protected UsuarioDAO(String tabela, String idTabela) {
		super("Usuario", "id");
	}
	
	public UsuarioDAO(Connection conn) {
		super(conn, "Usuario", "id");
	}
	
	public UsuarioDAO() {
		super("Usuario", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Usuario usuario = (Usuario)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Usuario ");
		sql.append("(usuario, senha) ");
		sql.append("VALUES(?, ?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, usuario.getUsuario());
			pst.setString(2, usuario.getSenha());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				usuario.setId(generatedKeys.getInt(1));
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
		Usuario usuario = (Usuario) entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Usuario SET ");
		sql.append("usuario = ?, ");
		sql.append("senha = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, usuario.getUsuario());
			pst.setString(2, usuario.getSenha());
			pst.setInt(3, usuario.getId());
			
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
		Usuario usuario = (Usuario)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Usuario");
		sql.append(" WHERE 1 =1 ");
		
		if(usuario.getUsuario() != null && !usuario.getUsuario().equals("")) {
			sql.append("AND usuario = ? ");
		}
		if(usuario.getSenha() != null && !usuario.getSenha().equals("")) {
			sql.append("AND senha = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			
			if(usuario.getUsuario() != null && !usuario.getUsuario().equals("")) {
				pst.setString(i, usuario.getUsuario());
				i++;
			}
			if(usuario.getSenha() != null && !usuario.getSenha().equals("")) {
				pst.setString(i, usuario.getSenha());
				i++;
			}
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> usuarios = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				usuario = new Usuario();
				usuario.setId(rs.getInt("id"));
				usuario.setDtCadastro(rs.getDate("dtCadastro"));
				usuario.setUsuario(rs.getString("usuario"));
				usuario.setSenha(rs.getString("senha"));
				usuarios.add(usuario);
			}
			rs.close();
			return usuarios;
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
