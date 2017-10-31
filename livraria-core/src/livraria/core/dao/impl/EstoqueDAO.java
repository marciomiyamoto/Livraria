package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.livro.Autor;
import dominio.livro.Categoria;
import dominio.livro.Dimensoes;
import dominio.livro.Editora;
import dominio.livro.Estoque;
import dominio.livro.GrupoDePrecificacao;
import dominio.livro.Livro;
import dominio.livro.Registro;

public class EstoqueDAO extends AbstractJdbcDAO {

	protected EstoqueDAO(String tabela, String idTabela) {
		super("Estoque", "id");
	}
	
	public EstoqueDAO(Connection conn) {
		super(conn, "Estoque", "id");
	}
	
	public EstoqueDAO() {
		super("Estoque", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
//		if(connection == null) {
//			abrirConexao();
//		}
//		PreparedStatement pst = null;
//		Estado estado = (Estado)entidade;
//		StringBuilder sql = new StringBuilder();
//		
//		sql.append("INSERT INTO Estado ");
//		sql.append("(nome, id_pais) ");
//		sql.append("VALUES(?, ?)");
//		
//		try {
//			connection.setAutoCommit(false);
//			
//			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
//			pst.setString(1, estado.getNome());
//			pst.setInt(2, estado.getPais().getId());
//			
//			pst.executeUpdate();
//			ResultSet generatedKeys = pst.getGeneratedKeys();
//			if(null != generatedKeys && generatedKeys.next()) {
//				estado.setId(generatedKeys.getInt(1));
//			}
//			
//			connection.commit();
//		} catch(SQLException e) {
//			try {
//				connection.rollback();
//			} catch (SQLException el){
//				el.printStackTrace();
//			}
//		} finally {
//			if(ctrlTransacao) {
//				try {
//					pst.close();
//					if(ctrlTransacao) {
//						connection.close();
//					}
//				} catch(SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
	}

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
//		if(connection == null) {
//			abrirConexao();
//		}
//		PreparedStatement pst = null;
//		Estado estado = (Estado)entidade;
//		StringBuilder sql = new StringBuilder();
//		
//		sql.append("UPDATE Estado SET ");
//		sql.append("nome = ?, ");
//		sql.append("id_pais = ? ");
//		sql.append("WHERE id = ?");
//		try {
//			connection.setAutoCommit(false);
//			
//			pst = connection.prepareStatement(sql.toString());
//			pst.setString(1, estado.getNome());
//			pst.setInt(2, estado.getPais().getId());
//			pst.setInt(2, estado.getId());
//			
//			pst.executeUpdate();
//			connection.commit();
//		} catch(SQLException e) {
//			try {
//				connection.rollback();
//			} catch(SQLException e1) {
//				e1.printStackTrace();
//			}
//			e.printStackTrace();
//		} finally {
//			if(ctrlTransacao) {
//				try {
//					pst.close();
//					if(ctrlTransacao)
//						connection.close();
//				} catch(SQLException e) {
//					e.printStackTrace();
//				}
//			}
//		}
		
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
		Estoque estoque = (Estoque)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Estoque ");
		sql.append("WHERE 1=1 ");
		
		if(estoque.getLivro().getId() != null && estoque.getLivro().getId() != 0) {
			sql.append("AND id_livro = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(estoque.getLivro().getId() != null && estoque.getLivro().getId() != 0) {
				pst.setInt(i, estoque.getLivro().getId());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> estoques = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				Estoque estoqueTemp = new Estoque();
				Livro livro = new Livro();
				Autor autor = new Autor();
				Editora editora = new Editora();
				GrupoDePrecificacao grupo = new GrupoDePrecificacao();
				Dimensoes dimensoes = new Dimensoes();
				List<Categoria> categorias = new ArrayList<Categoria>();
				Categoria cat = new Categoria();
				LivroDAO livroDAO = new LivroDAO();
				List<Registro> registros = new ArrayList<>();
				RegistroDAO regDAO = new RegistroDAO();
				Registro registro = new Registro();
				
				livro.setAutor(autor);
				livro.setEditora(editora);
				livro.setGrupoPrec(grupo);
				livro.setDimensoes(dimensoes);
				livro.setCategorias(categorias);
				livro.setCatAtivInativacao(cat);
//				livro = estoque.getLivro();
				
				estoqueTemp.setLivro(livro);
				estoqueTemp.setId(rs.getInt("id"));
				estoqueTemp.setDtCadastro(rs.getDate("dtcadastro"));
				
				estoqueTemp.getLivro().setId(rs.getInt("id_livro"));
				livroDAO.ctrlTransacao = false;
				estoqueTemp.setLivro((Livro)livroDAO.consultar(estoqueTemp.getLivro()).get(0));
//				estoque.setLivro(livro);
				
				registro.setIdEstoque(estoqueTemp.getId());
				regDAO.ctrlTransacao = false;
				registros = (List<Registro>)(List<?>)regDAO.consultar(registro);
				estoqueTemp.setRegistros(registros);
				
				estoques.add(estoqueTemp);
			}
			rs.close();
			return estoques;
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
