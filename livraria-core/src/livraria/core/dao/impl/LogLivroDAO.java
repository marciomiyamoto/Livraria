package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Autor;
import dominio.livro.LogLivro;

public class LogLivroDAO extends AbstractJdbcDAO{

	protected LogLivroDAO(String tabela, String idTabela) {
		super("LogLivro", "id");
	}
	
	public LogLivroDAO(Connection conn) {
		super(conn, "LogLivro", "id");
	}
	
	public LogLivroDAO() {
		super("LogLivro", "id");
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
		return null;
	}

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM livro_log");
		
		try {
			pst = connection.prepareStatement(sql.toString());			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> logs = new ArrayList<EntidadeDominio>();
			while(rs.next() ) {
				LogLivro log = new LogLivro();
				StringBuilder valoresAntigos = new StringBuilder();
				StringBuilder valoresNovos = new StringBuilder();
				
				log.setDtAcao(rs.getDate("data"));
				log.setHora(rs.getString("hora"));
				log.setAcao(rs.getString("acao"));
				log.setIdLivro(rs.getString("id_livro"));
				log.setUsuario(rs.getString("usuario"));
				valoresAntigos.append("Título = ");
				valoresAntigos.append(rs.getString("old_titulo"));
				valoresAntigos.append(", Ano = ");
				valoresAntigos.append(rs.getString("old_ano"));
				valoresAntigos.append(", Edição = ");
				valoresAntigos.append(rs.getString("old_edicao"));
				valoresAntigos.append(", Num.Pags = ");
				valoresAntigos.append(rs.getString("old_numpaginas"));
				valoresAntigos.append(", Cód. barras = ");
				valoresAntigos.append(rs.getString("old_codbarras"));
				valoresAntigos.append(", Ativo = ");
				valoresAntigos.append(rs.getString("old_status"));
				valoresAntigos.append(", Sinopse = ");
				valoresAntigos.append(rs.getString("old_sinopse"));
				valoresAntigos.append(", Editora = ");
				valoresAntigos.append(rs.getString("old_editora"));
				valoresAntigos.append(", Autor = ");
				valoresAntigos.append(rs.getString("old_autor"));
				valoresAntigos.append(", Categorias = ");
				valoresAntigos.append(rs.getString("old_categorias"));
				valoresAntigos.append(", Subcategorias = ");
				valoresAntigos.append(rs.getString("old_subcategorias"));
				valoresAntigos.append(", Dimensoes = ");
				valoresAntigos.append(rs.getString("old_altura"));
				valoresAntigos.append(", Grupo Prec. = ");
				valoresAntigos.append(rs.getString("old_grupoprec"));
				valoresAntigos.append(", Justificativa = ");
				valoresAntigos.append(rs.getString("old_justificativa"));
				valoresAntigos.append(", Cat. Ativ. In. = ");
				valoresAntigos.append(rs.getString("old_catativinativacao"));
				valoresNovos.append("Título = ");
				valoresNovos.append(rs.getString("new_titulo"));
				valoresNovos.append(", Ano = ");
				valoresNovos.append(rs.getString("new_ano"));
				valoresNovos.append(", Edição = ");
				valoresNovos.append(rs.getString("new_edicao"));
				valoresNovos.append(", Num.Pags = ");
				valoresNovos.append(rs.getString("new_numpaginas"));
				valoresNovos.append(", Cód. barras = ");
				valoresNovos.append(rs.getString("new_codbarras"));
				valoresNovos.append(", Ativo = ");
				valoresNovos.append(rs.getString("new_status"));
				valoresNovos.append(", Sinopse = ");
				valoresNovos.append(rs.getString("new_sinopse"));
				valoresNovos.append(", Editora = ");
				valoresNovos.append(rs.getString("new_editora"));
				valoresNovos.append(", Autor = ");
				valoresNovos.append(rs.getString("new_autor"));
				valoresNovos.append(", Categorias = ");
				valoresNovos.append(rs.getString("new_categorias"));
				valoresNovos.append(", Subcategorias = ");
				valoresNovos.append(rs.getString("new_subcategorias"));
				valoresNovos.append(", Dimensoes = ");
				valoresNovos.append(rs.getString("new_altura"));
				valoresNovos.append(", Grupo Prec. = ");
				valoresNovos.append(rs.getString("new_grupoprec"));
				valoresNovos.append(", Justificativa = ");
				valoresNovos.append(rs.getString("new_justificativa"));
				valoresNovos.append(", Cat. Ativ. In. = ");
				valoresNovos.append(rs.getString("new_catativinativacao"));
				
				if(log.getAcao().equals("INSERT")) {
					valoresAntigos.setLength(0);;
				} else if(log.getAcao().equals("DELETE")) {
					valoresNovos.setLength(0);;
				}
				log.setValoresAntigos(valoresAntigos.toString());
				log.setValoresNovos(valoresNovos.toString());
				logs.add(log);
			}
			rs.close();
			return logs;
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
