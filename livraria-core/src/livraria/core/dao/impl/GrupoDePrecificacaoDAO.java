package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.GrupoDePrecificacao;

public class GrupoDePrecificacaoDAO extends AbstractJdbcDAO {

	protected GrupoDePrecificacaoDAO(String tabela, String idTabela) {
		super("GrupoDePrecificacao", "id");
	}
	
	public GrupoDePrecificacaoDAO(Connection conn) {
		super(conn, "GrupoDePrecificacao", "id");
	}
	
	public GrupoDePrecificacaoDAO() {
		super("GrupoDePrecificacao", "id");
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
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM GrupoDePrecificacao");
		
		try {
			pst = connection.prepareStatement(sql.toString());			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> grupos = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				GrupoDePrecificacao grupo = new GrupoDePrecificacao();
				grupo.setId(rs.getInt("id"));
				grupo.setDtCadastro(rs.getDate("dtCadastro"));
				grupo.setMargemDeLucro(rs.getDouble("margemdelucro"));
				grupo.setNome(rs.getString("nome"));
				grupos.add(grupo);
			}
			rs.close();
			return grupos;
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
