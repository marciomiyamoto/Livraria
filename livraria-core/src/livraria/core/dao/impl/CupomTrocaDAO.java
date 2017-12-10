package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.livro.Registro;
import dominio.venda.CupomPromocional;
import dominio.venda.CupomTroca;
import dominio.venda.Pedido;

public class CupomTrocaDAO extends AbstractJdbcDAO {

	protected CupomTrocaDAO(String tabela, String idTabela) {
		super("CupomTroca", "id");
	}
	
	public CupomTrocaDAO(Connection conn) {
		super(conn, "CupomTroca", "id");
	}
	
	public CupomTrocaDAO() {
		super("CupomTroca", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		CupomTroca cupom = (CupomTroca)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO CupomTroca ");
		sql.append("(ativo, codigo, valor, id_cliente) ");
		sql.append("VALUES(?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setBoolean(1, cupom.getAtivo());
			pst.setString(2, cupom.getCodigo());
			pst.setDouble(3, cupom.getValor());
			pst.setInt(4, cupom.getIdCliente());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				cupom.setId(generatedKeys.getInt(1));
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
		abrirConexao();
		PreparedStatement pst = null;
		CupomTroca cupom = (CupomTroca)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE CupomTroca SET ");
		sql.append("ativo = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setBoolean(1, cupom.getAtivo());
			pst.setInt(2, cupom.getId());
			
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
		abrirConexao();
		PreparedStatement pst = null;
		CupomTroca cupom = (CupomTroca) entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM CupomTroca ");
		sql.append("WHERE 1=1 ");
		
		if(cupom.getId() != null && cupom.getId() != 0) {
			sql.append("AND id = ?");
		}
		if(cupom.getCodigo() != null && !cupom.getCodigo().equals("")) {
			sql.append("AND codigo = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(cupom.getId() != null && cupom.getId() != 0) {
				pst.setInt(i, cupom.getId());
				i++;
			}
			if(cupom.getCodigo() != null && !cupom.getCodigo().equals("")) {
				pst.setString(i, cupom.getCodigo());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> cupons = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				CupomTroca cupomTemp = new CupomTroca();
				cupomTemp.setId(rs.getInt("id"));
				cupomTemp.setDtCadastro(rs.getDate("dtcadastro"));
				cupomTemp.setAtivo(rs.getBoolean("ativo"));
				cupomTemp.setCodigo(rs.getString("codigo"));
				cupomTemp.setValor(rs.getDouble("valor"));
				cupomTemp.setIdCliente(rs.getInt("id_cliente"));
				cupons.add(cupomTemp);
			}
			rs.close();
			return cupons;
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
