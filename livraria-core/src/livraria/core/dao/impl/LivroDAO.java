package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Autor;
import dominio.livro.Categoria;
import dominio.livro.Dimensoes;
import dominio.livro.Editora;
import dominio.livro.Estoque;
import dominio.livro.GrupoDePrecificacao;
import dominio.livro.Livro;
import dominio.livro.Subcategoria;

public class LivroDAO extends AbstractJdbcDAO {
	
	protected LivroDAO(String tabela, String idTabela) {
		super("Livro", "id");
	}
	
	public LivroDAO(Connection conn) {
		super(conn, "Livro", "id");
	}
	
	public LivroDAO() {
		super("Livro", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Livro livro = (Livro)entidade;
		Autor autor = livro.getAutor();
		AutorDAO autDAO = new AutorDAO();
		Dimensoes dimensoes = livro.getDimensoes();
		DimensoesDAO dimDAO = new DimensoesDAO();
		EstoqueDAO estDao = new EstoqueDAO();
		Estoque estoque = new Estoque();
		StringBuilder sql = new StringBuilder();
		
		// SALVANDO DIMENSOES
		dimDAO.ctrlTransacao = false;
		dimDAO.salvar(dimensoes);
		
		// SALVANDO AUTOR
		autDAO.ctrlTransacao = false;
		autDAO.salvar(autor);
		
		// SALVANDO LIVRO
		if(connection == null) {
			abrirConexao();
		}
		sql.append("INSERT INTO Livro");
		sql.append("(titulo,  ");
		sql.append("ano,  ");
		sql.append("edicao,  ");
		sql.append("numPaginas, ");
		sql.append("codBarras, ");
		sql.append("ativo, ");
		sql.append("sinopse, ");
		sql.append("id_editora, ");
		sql.append("id_autor, ");
		sql.append("id_grupoprecificacao, ");
		sql.append("id_dimensoes, ");
		sql.append("justificativa, ");
		sql.append("id_catativinativacao) ");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, livro.getTitulo());
			pst.setInt(2, livro.getAno());
			pst.setInt(3, livro.getEdicao());
			pst.setInt(4, livro.getNumPaginas());
			pst.setString(5, livro.getCodBarras());
			pst.setBoolean(6, false);
			pst.setString(7, livro.getSinopse());
			pst.setInt(8, livro.getEditora().getId());
			pst.setInt(9, livro.getAutor().getId());
			pst.setInt(10, livro.getGrupoPrec().getId());
			pst.setInt(11, livro.getDimensoes().getId());
			pst.setString(12, "Novo livro");
			pst.setInt(13, 7);
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				livro.setId(generatedKeys.getInt(1));
			}
			connection.commit();
			// SALVANDO LIVRO_CATEGORIA
			for(Categoria cat : livro.getCategorias()) {
				salvarCategoria(livro, cat);
			}
			// SALVANDO LIVRO_SUBCATEGORIA
			for(Subcategoria subcat : livro.getSubcategorias()) {
				salvarSubcategoria(livro, subcat);
			}
			// SALVANDO ESTOQUE
			estoque.setLivro(livro);
			estDao.salvar(estoque);
			
			
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch(SQLException el) {
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

	private void salvarSubcategoria(Livro livro, Subcategoria subcat) {
		
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.setLength(0);
		sql.append("INSERT INTO Livro_subcategoria ");
		sql.append("(id_livro, ");
		sql.append("id_subcategoria) ");
		sql.append("VALUES(?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, livro.getId());
			pst.setInt(2, subcat.getId());
			
			pst.executeUpdate();
			connection.commit();
		}catch(SQLException e) {
			try {
				connection.rollback();
			} catch(SQLException el) {
				el.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void salvarCategoria(Livro livro, Categoria cat) {
		
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();		
		sql.append("INSERT INTO Livro_categoria ");
		sql.append("(id_livro, ");
		sql.append("id_categoria) ");
		sql.append("VALUES(?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, livro.getId());
			pst.setInt(2, cat.getId());
			
			pst.executeUpdate();
			connection.commit();
		}catch(SQLException e) {
			try {
				connection.rollback();
			} catch(SQLException el) {
				el.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Livro livro = (Livro) entidade;
		Autor autor = livro.getAutor();
		AutorDAO autDAO = new AutorDAO();
		Dimensoes dimensoes = livro.getDimensoes();
		DimensoesDAO dimDAO = new DimensoesDAO();
		StringBuilder sql = new StringBuilder();
		
		// SALVANDO DIMENSOES
		dimDAO.ctrlTransacao = false;
		dimDAO.alterar(dimensoes);
		
		// SALVANDO AUTOR
		autDAO.ctrlTransacao = false;
		autDAO.alterar(autor);
		
		// SALVANDO LIVRO
		sql.append("UPDATE Livro SET ");
		sql.append("titulo = ?, ");
		sql.append("ano = ?, ");
		sql.append("edicao = ?, ");
		sql.append("numpaginas = ?, ");
		sql.append("codbarras = ?, ");
		sql.append("sinopse = ?, ");
		sql.append("id_editora = ?, ");
		sql.append("id_grupoprecificacao = ?, ");
		sql.append("id_catAtivInativacao = ?, ");
		sql.append("ativo = ?, ");
		sql.append("justificativa = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, livro.getTitulo());
			pst.setInt(2, livro.getAno());
			pst.setInt(3, livro.getEdicao());
			pst.setInt(4, livro.getNumPaginas());
			pst.setString(5, livro.getCodBarras());
			pst.setString(6, livro.getSinopse());
			pst.setInt(7, livro.getEditora().getId());
			pst.setInt(8, livro.getGrupoPrec().getId());
			pst.setInt(9, livro.getCatAtivInativacao().getId());
			pst.setBoolean(10, livro.getAtivo());
			pst.setString(11, livro.getJustificativa());
			pst.setInt(12, livro.getId());
			
			pst.executeUpdate();
			connection.commit();
			
			//EXCLUINDO CATEGORIAS ASSOCIADAS A LIVRO
			excluirCategorias(livro);
			
			//EXCLUINDO SUBCATEGORIAS ASSOCIADAS A LIVRO
			excluirSubcategorias(livro);
			
			// INSERINDO NOVAS CATEGORIAS
			for(Categoria cat : livro.getCategorias()) {
				salvarCategoria(livro, cat);
			}
			// INSERINDO NOVAS SUBCATEGORIAS
			for(Subcategoria subcat : livro.getSubcategorias()) {
				salvarSubcategoria(livro, subcat);
			}
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
	public void excluir(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Livro livro = (Livro) entidade;
		StringBuilder sql = new StringBuilder();
		
		//EXCLUINDO CATEGORIAS ASSOCIADAS A LIVRO
		excluirCategorias(livro);
		
		//EXCLUINDO SUBCATEGORIAS ASSOCIADAS A LIVRO
		excluirSubcategorias(livro);
		
		// EXCLUINDO LIVRO
		sql.append("DELETE FROM Livro ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, livro.getId());
			
			pst.executeUpdate();
			connection.commit();
			// EXCLUINDO AUTOR
			AutorDAO autDao = new AutorDAO();
			autDao.ctrlTransacao = false;
			autDao.excluir(livro.getAutor());
			
			// EXLUINDO DIMENSOES
			DimensoesDAO dimDao = new DimensoesDAO();
			dimDao.ctrlTransacao = false;
			dimDao.excluir(livro.getDimensoes());
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
	
	private void excluirCategorias(Livro livro) {
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE Livro_categoria ");
		sql.append(" WHERE id_livro = ?");
		try {
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, livro.getId());
			
			pst.executeUpdate();
			connection.commit();
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch(SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} 
	}
	private void excluirSubcategorias(Livro livro) {
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE Livro_subcategoria ");
		sql.append(" WHERE id_livro = ?");
		try {
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, livro.getId());
			
			pst.executeUpdate();
			connection.commit();
		} catch(SQLException e) {
			try {
				connection.rollback();
			} catch(SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
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
		Livro filtro = (Livro)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT l.id AS id, l.dtcadastro AS dtcadastro, l.titulo AS titulo, ");
		sql.append("l.ano AS ano, l.edicao AS edicao, l.numpaginas AS numpaginas, ");
		sql.append("l.codbarras AS codbarras, l.ativo AS ativo, l.sinopse AS sinopse, ");
		sql.append("l.justificativa as justificativa, ");
		sql.append("c.id as id_catAtivInativacao, c.dtCadastro as catAtivInativacao_dt, c.nome as catAtivInativacao, c.tipo as catTipo, ");
		sql.append("a.id as id_autor, a.dtCadastro as autor_dt, a.nome AS autor, ");
		sql.append("e.id as id_editora, e.dtCadastro as editora_dt, e.nome AS editora, ");
		sql.append("g.id as id_grupo, g.dtCadastro as grupo_dt, g.nome AS grupodeprecificacao, g.MARGEMDELUCRO AS margemdelucro, ");
		sql.append("d.id as id_dimensoes, d.dtCadastro as dimensoes_dt, d.altura AS altura, d.largura AS largura, d.peso AS peso, d.profundidade AS profundidade ");
		sql.append("FROM livro l ");
		sql.append("JOIN autor a ON l.id_autor = a.id ");
		sql.append("JOIN editora e ON l.ID_EDITORA = e.id ");
		sql.append("JOIN grupodeprecificacao g ON l.id_grupoprecificacao = g.id ");
		sql.append("JOIN dimensoes d ON l.id_dimensoes = d.id ");
		sql.append("JOIN categoria c ON l.id_catativinativacao = c.id ");
		sql.append("WHERE 1=1 ");
		
		if(filtro.getId() != null && filtro.getId() != 0) {
			sql.append("AND l.id = ? ");
		}
		if(filtro.getTitulo() != null && !filtro.getTitulo().equals("")) {
			sql.append("AND titulo = ? ");
		}
		if(filtro.getAno() != null && filtro.getAno() != 0) {
			sql.append("AND ano = ? ");
		}
		if(filtro.getEdicao() != null && filtro.getEdicao() != 0) {
			sql.append("AND edicao = ? ");
		}
		if(filtro.getNumPaginas() != null && filtro.getNumPaginas() != 0) {
			sql.append("AND numpaginas = ? ");
		}
		if(filtro.getCodBarras() != null && !filtro.getCodBarras().equals("")) {
			sql.append("AND codbarras = ? ");
		}
		if(filtro.getAtivo() != null ) {
			sql.append("AND ativo = ? ");
		}
		if(filtro.getSinopse() != null && !filtro.getSinopse().equals("")) {
			sql.append("AND sinopse = ? ");
		}
		if(filtro.getAutor().getNome() != null && !filtro.getAutor().getNome().equals("")) {
			sql.append("AND a.nome = ? ");
		}
		if(filtro.getEditora().getId() != null && filtro.getEditora().getId() != 0) {
			sql.append("AND e.id = ? ");
		}
		if(filtro.getGrupoPrec().getId() != null && filtro.getGrupoPrec().getId() != 0) {
			sql.append("AND g.id = ? ");
		}
		if(filtro.getDimensoes().getAltura() != null && filtro.getDimensoes().getAltura() != 0) {
			sql.append("AND altura = ? ");
		}
		if(filtro.getDimensoes().getLargura() != null && filtro.getDimensoes().getLargura() != 0) {
			sql.append("AND largura = ? ");
		}
		if(filtro.getDimensoes().getPeso() != null && filtro.getDimensoes().getPeso() != 0) {
			sql.append("AND peso = ? ");
		}
		if(filtro.getDimensoes().getProfundidade() != null && filtro.getDimensoes().getProfundidade() != 0) {
			sql.append("AND profundidade = ? ");
		}
		if(filtro.getJustificativa() != null && !filtro.getJustificativa().equals("")) {
			sql.append("AND justificativa = ? ");
		}
		if(filtro.getCatAtivInativacao().getId() != null && filtro.getCatAtivInativacao().getId() != 0) {
			sql.append("AND  c.id = ?" );
		}
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			
			if(filtro.getId() != null && filtro.getId() != 0) {
				pst.setInt(i, filtro.getId());
			}
			if(filtro.getTitulo() != null && !filtro.getTitulo().equals("")) {
				pst.setString(i, filtro.getTitulo());
				i++;
			}
			if(filtro.getAno() != null && filtro.getAno() != 0) {
				pst.setInt(i, filtro.getAno());
				i++;
			}
			if(filtro.getEdicao() != null && filtro.getEdicao() != 0) {
				pst.setInt(i, filtro.getEdicao());
				i++;
			}
			if(filtro.getNumPaginas() != null && filtro.getNumPaginas() != 0) {
				pst.setInt(i, filtro.getNumPaginas());
				i++;
			}
			if(filtro.getCodBarras() != null && !filtro.getCodBarras().equals("")) {
				pst.setString(i, filtro.getCodBarras());
				i++;
			}
			if(filtro.getAtivo() != null ) {
				pst.setBoolean(i, filtro.getAtivo());
				i++;
			}
			if(filtro.getSinopse() != null && !filtro.getSinopse().equals("")) {
				pst.setString(i, filtro.getSinopse());
				i++;
			}
			if(filtro.getAutor().getNome() != null && !filtro.getAutor().getNome().equals("")) {
				pst.setString(i, filtro.getAutor().getNome());
				i++;
			}
			if(filtro.getEditora().getId() != null && filtro.getEditora().getId() != 0) {
				pst.setInt(i, filtro.getEditora().getId());
				i++;
			}
			if(filtro.getGrupoPrec().getId() != null && filtro.getGrupoPrec().getId() != 0) {
				pst.setInt(i, filtro.getGrupoPrec().getId());
				i++;
			}
			if(filtro.getDimensoes().getAltura() != null && filtro.getDimensoes().getAltura() != 0) {
				pst.setDouble(i, filtro.getDimensoes().getAltura());
				i++;
			}
			if(filtro.getDimensoes().getLargura() != null && filtro.getDimensoes().getLargura() != 0) {
				pst.setDouble(i, filtro.getDimensoes().getLargura());
				i++;
			}
			if(filtro.getDimensoes().getPeso() != null && filtro.getDimensoes().getPeso() != 0) {
				pst.setDouble(i, filtro.getDimensoes().getPeso());
				i++;
			}
			if(filtro.getDimensoes().getProfundidade() != null && filtro.getDimensoes().getProfundidade() != 0) {
				pst.setDouble(i, filtro.getDimensoes().getProfundidade());
				i++;
			}
			if(filtro.getJustificativa() != null && !filtro.getJustificativa().equals("")) {
				pst.setString(i, filtro.getJustificativa());
				i++;
			}
			if(filtro.getCatAtivInativacao().getId() != null && filtro.getCatAtivInativacao().getId() != 0) {
				pst.setInt(i, filtro.getCatAtivInativacao().getId());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> livros = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				Autor autor = new Autor();
				autor.setId(rs.getInt("id_autor"));
				autor.setDtCadastro(rs.getDate("autor_dt"));
				autor.setNome(rs.getString("autor"));
				
				Editora editora = new Editora();
				editora.setId(rs.getInt("id_editora"));
				editora.setDtCadastro(rs.getDate("editora_dt"));
				editora.setNome(rs.getString("editora"));
				
				GrupoDePrecificacao grupo = new GrupoDePrecificacao();
				grupo.setId(rs.getInt("id_grupo"));
				grupo.setDtCadastro(rs.getDate("grupo_dt"));
				grupo.setNome(rs.getString("grupodeprecificacao"));
				grupo.setMargemDeLucro(rs.getDouble("margemdelucro"));
				
				Dimensoes dimensoes = new Dimensoes();
				dimensoes.setId(rs.getInt("id_dimensoes"));
				dimensoes.setDtCadastro(rs.getDate("dimensoes_dt"));
				dimensoes.setAltura(rs.getDouble("altura"));
				dimensoes.setLargura(rs.getDouble("largura"));
				dimensoes.setPeso(rs.getDouble("peso"));
				dimensoes.setProfundidade(rs.getDouble("profundidade"));
				
				Categoria catAtivInativacao = new Categoria();
				catAtivInativacao.setId(rs.getInt("id_catAtivInativacao"));
				catAtivInativacao.setDtCadastro(rs.getDate("catAtivInativacao_dt"));
				catAtivInativacao.setNome(rs.getString("catativinativacao"));
				catAtivInativacao.setTipo(rs.getString("catTipo"));
				
				Livro livro = new Livro();
				livro.setId(rs.getInt("id"));
				livro.setDtCadastro(rs.getDate("dtCadastro"));
				livro.setTitulo(rs.getString("titulo"));
				livro.setAno(rs.getInt("ano"));
				livro.setEdicao(rs.getInt("edicao"));
				livro.setNumPaginas(rs.getInt("numpaginas"));
				livro.setCodBarras(rs.getString("codbarras"));
				livro.setAtivo(rs.getBoolean("ativo"));
				livro.setSinopse(rs.getString("sinopse"));
				livro.setJustificativa(rs.getString("justificativa"));
				livro.setCatAtivInativacao(catAtivInativacao);
				livro.setAutor(autor);
				livro.setEditora(editora);
				livro.setGrupoPrec(grupo);
				livro.setDimensoes(dimensoes);
				livro.setCategorias(buscarCategoriasLivro(livro.getId()));
				livro.setSubcategorias(buscarSubcategoriasLivro(livro.getId()));
				livros.add(livro);
			}
			rs.close();
			return livros;
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
	
	public List<Categoria> buscarCategoriasLivro(int idLivro) {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c.id, c.dtCadastro, c.nome, c.tipo ");
		sql.append("FROM categoria c ");
		sql.append("join livro_categoria lc on c.id = lc.ID_CATEGORIA ");
		sql.append("where lc.id_livro = ?");
		
		try {
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, idLivro);
			ResultSet rs = pst.executeQuery();
			List<Categoria> categorias = new ArrayList<Categoria>()	;
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
		}
		return null;
	}
	public List<Subcategoria> buscarSubcategoriasLivro(int idLivro) {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT s.id as id, s.dtCadastro as dtCadastro, s.nome as nome ");
		sql.append("FROM subcategoria s ");
		sql.append("JOIN livro_subcategoria ls ON s.id = ls.ID_SUBCATEGORIA ");
		sql.append("WHERE ls.id_livro = ?");
		
		try {
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, idLivro);
			ResultSet rs = pst.executeQuery();
			List<Subcategoria> subcategorias = new ArrayList<Subcategoria>()	;
			while(rs.next() ) {
				Subcategoria subcat = new Subcategoria();
				subcat.setId(rs.getInt("id"));
				subcat.setDtCadastro(rs.getDate("dtCadastro"));
				subcat.setNome(rs.getString("nome"));
				subcategorias.add(subcat);
			}
			rs.close();
			return subcategorias;
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
