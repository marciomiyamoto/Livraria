package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.livro.Registro;

public class RegistroDAO extends AbstractJdbcDAO {
	
	protected RegistroDAO(String tabela, String idTabela) {
		super("Registro", "id");
	}
	
	public RegistroDAO(Connection conn) {
		super(conn, "Registro", "id");
	}
	
	public RegistroDAO() {
		super("Registro", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null || connection.isClosed()) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Registro registro = (Registro)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Registro ");
		sql.append("(qtde, valorCompra, valorVenda, tipoRegistro, id_estoque) ");
		sql.append("VALUES(?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setInt(1, registro.getQtde());
			if(registro.getValorCompra() != null && registro.getValorCompra() != 0) {
				pst.setDouble(2, registro.getValorCompra());
			} else {
				pst.setNull(2, Types.DOUBLE);
			}
			if(registro.getValorVenda() != null && registro.getValorVenda() != 0) {
				pst.setDouble(3, registro.getValorVenda());
			} else {
				pst.setNull(3, Types.DOUBLE);
			}
			pst.setInt(4, registro.getTipoRegistro());
			pst.setInt(5, registro.getIdEstoque());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				registro.setId(generatedKeys.getInt(1));
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
//		
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
		Registro registro = (Registro)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Registro ");
		sql.append("WHERE 1=1 ");
		
		if(registro.getIdEstoque() != null && registro.getIdEstoque() != 0) {
			sql.append("AND id_estoque = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());		
			int i = 1;
			
			if(registro.getIdEstoque() != null && registro.getIdEstoque() != 0) {
				pst.setInt(i, registro.getIdEstoque());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> registros = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				registro = new Registro();
				registro.setId(rs.getInt("id"));
				registro.setDtCadastro(rs.getDate("dtcadastro"));
				registro.setIdEstoque(rs.getInt("id_estoque"));
				registro.setQtde(rs.getInt("qtde"));
				registro.setValorCompra(rs.getDouble("valorCompra"));
				registro.setValorVenda(rs.getDouble("valorVenda"));
				registro.setTipoRegistro(rs.getInt("tipoRegistro"));
				registros.add(registro);
			}
			rs.close();
			return registros;
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
