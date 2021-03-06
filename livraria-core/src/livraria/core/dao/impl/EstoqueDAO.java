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
		abrirConexao();
		PreparedStatement pst = null;
		Estoque estoque = (Estoque)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Estoque ");
		sql.append("(id_livro) ");
		sql.append("VALUES(?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setInt(1, estoque.getLivro().getId());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				estoque.setId(generatedKeys.getInt(1));
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
		Estoque estoque = (Estoque)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Estoque ");
		sql.append("WHERE 1=1 ");
		
		if(estoque.getId() != null && estoque.getId() != 0) {
			sql.append("AND id = ? ");
		}
		if(estoque.getLivro() != null && estoque.getLivro().getId() != null && estoque.getLivro().getId() != 0) {
			sql.append("AND id_livro = ? ");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(estoque.getId() != null && estoque.getId() != 0) {
				pst.setInt(i, estoque.getId());
				i++;
			}
			if(estoque.getLivro() != null && estoque.getLivro().getId() != null && estoque.getLivro().getId() != 0) {
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
				
				estoqueTemp.setLivro(livro);
				estoqueTemp.setId(rs.getInt("id"));
				estoqueTemp.setDtCadastro(rs.getDate("dtcadastro"));
				
				estoqueTemp.getLivro().setId(rs.getInt("id_livro"));
				estoqueTemp.setLivro((Livro)livroDAO.consultar(estoqueTemp.getLivro()).get(0));
				
				registro.setIdEstoque(estoqueTemp.getId());
				registros = (List<Registro>)(List<?>)regDAO.consultar(registro);
				estoqueTemp.setRegistros(registros);
				
				estoques.add(estoqueTemp);
			}
			rs.close();
			return estoques;
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
