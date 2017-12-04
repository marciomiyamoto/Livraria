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
import dominio.venda.CupomPromocional;

public class CupomPromocionalDAO extends AbstractJdbcDAO {
	
	protected CupomPromocionalDAO(String tabela, String idTabela) {
		super("CupomPromocional", "id");
	}
	
	public CupomPromocionalDAO(Connection conn) {
		super(conn, "CupomPromocional", "id");
	}
	
	public CupomPromocionalDAO() {
		super("CupomPromocional", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
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
		CupomPromocional cupom = (CupomPromocional) entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM CupomPromocional ");
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
				CupomPromocional cupomTemp = new CupomPromocional();
				cupomTemp.setId(rs.getInt("id"));
				cupomTemp.setDtCadastro(rs.getDate("dtcadastro"));
				cupomTemp.setAtivo(rs.getBoolean("ativo"));
				cupomTemp.setCodigo(rs.getString("codigo"));
				cupomTemp.setPorcentagemDesconto(rs.getDouble("porcentagemDesc"));
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
			try {
				if(pst != null) {
					pst.close();
				}
				if(ctrlTransacao) {
					connection.close();
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
