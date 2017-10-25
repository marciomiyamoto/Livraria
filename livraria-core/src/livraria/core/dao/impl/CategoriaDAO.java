package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Categoria;

public class CategoriaDAO extends AbstractJdbcDAO {
	
	protected CategoriaDAO(String tabela, String idTabela) {
		super("Categoria", "id");
	}
	
	public CategoriaDAO(Connection conn) {
		super(conn, "Categoria", "id");
	}
	
	public CategoriaDAO() {
		super("Categoria", "id");
	}

	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		
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
		Categoria categoria = (Categoria)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Categoria");
		
		if(categoria.getTipo() != null & !categoria.getTipo().equals("")) {
			sql.append(" WHERE TIPO = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			
			if(categoria.getTipo() != null & !categoria.getTipo().equals("")) {
				pst.setString(1, categoria.getTipo());
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> categorias = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				Categoria cat = new Categoria();
				cat.setId(rs.getInt("id"));
				cat.setDtCadastro(rs.getDate("dtCadastro"));
				cat.setNome(rs.getString("nome"));
				cat.setTipo(rs.getString("tipo"));
				categorias.add(cat);
			}
			rs.close();
			return categorias;
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
