package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.cliente.BandeiraCartao;
import dominio.cliente.Cartao;
import dominio.livro.Autor;

public class CartaoDAO extends AbstractJdbcDAO {

	protected CartaoDAO(String tabela, String idTabela) {
		super("Cartao", "id");
	}
	
	public CartaoDAO(Connection conn) {
		super(conn, "Cartao", "id");
	}
	
	public CartaoDAO() {
		super("Cartao", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Cartao cartao = (Cartao)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Cartao ");
		sql.append("(numero, ");
		sql.append("nomeImpresso, ");
		sql.append("codSeguranca, ");
		sql.append("dtVencimento,  ");
		sql.append("id_bandeira, ");
		sql.append("id_cliente) ");
		sql.append("VALUES(?,?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setLong(1, cartao.getNumero());
			pst.setString(2, cartao.getNomeImpresso());
			pst.setInt(3, cartao.getCodSeguranca());
			pst.setDate(4, new java.sql.Date(cartao.getDtVencimento().getTime()));
			pst.setInt(5, cartao.getBandeira().getId());
			pst.setInt(6, cartao.getIdCliente());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				cartao.setId(generatedKeys.getInt(1));
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
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Cartao cartao = (Cartao) entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Cartao SET ");
		sql.append("numero = ?, ");
		sql.append("nomeImpresso = ?, ");
		sql.append("codSeguranca = ?, ");
		sql.append("dtVencimento = ?, ");
		sql.append("id_bandeira = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setLong(1, cartao.getNumero());
			pst.setString(2, cartao.getNomeImpresso());
			pst.setInt(3, cartao.getCodSeguranca());
			pst.setDate(4, new java.sql.Date(cartao.getDtVencimento().getTime()));
			pst.setInt(5, cartao.getBandeira().getId());
			pst.setInt(6, cartao.getId());
			
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
		Cartao cartao = (Cartao)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c.id, c.dtCadastro, c.numero, c.nomeimpresso, c.codseguranca, c.dtVencimento, c.id_cliente as id_cliente, ");
		sql.append("b.id AS b_id, b.dtCadastro AS b_dtCadastro, b.bin AS bin, b.nome AS bandeira ");
		sql.append("FROM cartao c ");
		sql.append("JOIN BANDEIRACARTAO b ON b.id = c.ID_BANDEIRA ");
		sql.append("WHERE 1=1 ");
		
		if(cartao.getId() != null && cartao.getId() != 0) {
			sql.append("AND c.id = ? ");
		}
		if(cartao.getIdCliente() != null && cartao.getIdCliente() != 0) {
			sql.append("AND c.id_cliente = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			
			if(cartao.getId() != null && cartao.getId() != 0) {
				pst.setInt(i, cartao.getId());
			}
			if(cartao.getIdCliente() != null && cartao.getIdCliente() != 0) {
				pst.setInt(i, cartao.getIdCliente());
			}
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> cartoes = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				cartao = new Cartao();
				BandeiraCartao bandeira = new BandeiraCartao();
				
				bandeira.setId(rs.getInt("b_id"));
				bandeira.setDtCadastro(rs.getDate("b_dtCadastro"));
				bandeira.setBin(rs.getInt("bin"));
				bandeira.setNome(rs.getString("bandeira"));
				
				cartao.setId(rs.getInt("id"));
				cartao.setDtCadastro(rs.getDate("dtCadastro"));
				cartao.setNumero(rs.getLong("numero"));
				cartao.setNomeImpresso(rs.getString("nomeImpresso"));
				cartao.setCodSeguranca(rs.getInt("codSeguranca"));
				cartao.setDtVencimento(rs.getDate("dtVencimento"));
				cartao.setBandeira(bandeira);
				cartao.setIdCliente(rs.getInt("id_cliente"));
				
				cartoes.add(cartao);
			}
			rs.close();
			return cartoes;
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
