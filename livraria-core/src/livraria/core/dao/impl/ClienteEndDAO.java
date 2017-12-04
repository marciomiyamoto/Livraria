package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.cliente.ClienteEnd;
import dominio.livro.Autor;

public class ClienteEndDAO extends AbstractJdbcDAO {

	protected ClienteEndDAO(String tabela, String idTabela) {
		super("ClienteEnd", "id");
	}
	
	public ClienteEndDAO(Connection conn) {
		super(conn, "ClienteEnd", "id");
	}
	
	public ClienteEndDAO() {
		super("ClienteEnd", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		ClienteEnd cliEnd = (ClienteEnd)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Cliente_End ");
		sql.append("(id_cliente, ");
		sql.append("id_endereco) ");
		sql.append("VALUES(?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, cliEnd.getIdCliente());
			pst.setInt(2, cliEnd.getIdEndereco());
			
			pst.executeUpdate();
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
	public void excluir(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		ClienteEnd cliEnd = (ClienteEnd)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM Cliente_end ");
		sql.append("WHERE id_cliente = ? ");
		
		if(cliEnd.getIdEndereco() != null && cliEnd.getIdEndereco() != 0) {
			sql.append("AND id_endereco = ?");
		}
		
		try {
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, cliEnd.getIdCliente());
			
			if(cliEnd.getIdEndereco() != null && cliEnd.getIdEndereco() != 0) {
				pst.setInt(2, cliEnd.getIdEndereco());
			}
			
			pst.executeUpdate();
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
	public EntidadeDominio visualizar(EntidadeDominio entidade) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EntidadeDominio> consultar(EntidadeDominio entidade) throws SQLException {
		
		return null;
	}
}
