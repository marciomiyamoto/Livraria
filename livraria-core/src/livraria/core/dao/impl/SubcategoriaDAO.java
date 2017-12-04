package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Categoria;
import dominio.livro.Subcategoria;

public class SubcategoriaDAO extends AbstractJdbcDAO {

	protected SubcategoriaDAO(String tabela, String idTabela) {
		super("Subcategoria", "id");
	}
	
	public SubcategoriaDAO(Connection conn) {
		super(conn, "Subcategoria", "id");
	}
	
	public SubcategoriaDAO() {
		super("Subcategoria", "id");
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
		abrirConexao();
		Subcategoria sub = (Subcategoria)entidade;
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Subcategoria ");
		sql.append("WHERE 1 = 1 ");
		
//		if(sub.getIdCategoria() != null && sub.getIdCategoria() != 0) {
//			sql.append("AND id_categoria = ?");
//		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			
//			if(sub.getIdCategoria() != null && sub.getIdCategoria() != 0) {
//				pst.setInt(1, sub.getIdCategoria());
//				i++;
//			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> subcategorias = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				Subcategoria subcat = new Subcategoria();
				subcat.setId(rs.getInt("id"));
				subcat.setDtCadastro(rs.getDate("dtCadastro"));
				subcat.setNome(rs.getString("nome"));
				subcategorias.add(subcat);
			}
			rs.close();
			return subcategorias;
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
