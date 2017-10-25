package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dominio.EntidadeDominio;
import livraria.core.IDAO;
import livraria.core.util.impl.ConexaoOracle;

public abstract class AbstractJdbcDAO implements IDAO {

	protected Connection connection;
	protected String tabela;
	protected String idTabela;
	protected boolean ctrlTransacao = true;
	
	public AbstractJdbcDAO(Connection connection, String tabela, String idTabela) {
		this.connection = connection;
		this.tabela = tabela;
		this.idTabela = idTabela;
	}
	
	protected AbstractJdbcDAO(String tabela, String idTabela) {
		this.tabela = tabela;
		this.idTabela = idTabela;	}

	@Override
	public void excluir(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM ");
		sql.append(tabela);
		sql.append(" WHERE ");
		sql.append(idTabela);
		sql.append("=");
		sql.append("?");
		try {
			connection.setAutoCommit(false);
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, entidade.getId());
			
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
			try {
				pst.close();
				if(ctrlTransacao)
					connection.close();
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
	}

	protected void abrirConexao() {
		try {
			if(connection == null || connection.isClosed() ) {
				connection = ConexaoOracle.getConnection();
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
