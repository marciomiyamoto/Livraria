package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.endereco.Endereco;
import dominio.livro.Autor;

public class EnderecoDAO extends AbstractJdbcDAO {

	protected EnderecoDAO(String tabela, String idTabela) {
		super("Endereco", "id");
	}
	
	public EnderecoDAO(Connection conn) {
		super(conn, "Endereco", "id");
	}
	
	public EnderecoDAO() {
		super("Endereco", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		if(connection == null) {
			abrirConexao();
		}
		PreparedStatement pst = null;
		Endereco endereco = (Endereco)entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("INSERT INTO Endereco ");
		sql.append("(logradouro, ");
		sql.append("numero, ");
		sql.append("bairro, ");
		sql.append("cep, ");
		sql.append("observacoes, ");
		sql.append("tipoResidencia, ");
		sql.append("tipoLogradouro, ");
		sql.append("titulo, ");
		sql.append("id_cidade, ");
		sql.append("id_estado, ");
		sql.append("id_pais, ");
		sql.append("id_tipoEndereco) ");
		sql.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, endereco.getLogradouro());
			pst.setString(2, endereco.getNumero());
			pst.setString(3, endereco.getBairro());
			pst.setString(4, endereco.getCep());
			pst.setString(5, endereco.getObservacoes());
			pst.setString(6, endereco.getTipoResidencia());
			pst.setString(7, endereco.getTipoLogradouro());
			pst.setString(8, endereco.getTitulo());
			pst.setInt(9, endereco.getCidade().getId());
			pst.setInt(10, endereco.getCidade().getEstado().getId());
			pst.setInt(11, endereco.getCidade().getEstado().getPais().getId());
			pst.setInt(12, endereco.getTipo().getId());
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				endereco.setId(generatedKeys.getInt(1));
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
		Endereco endereco = (Endereco) entidade;
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Endereco SET ");
		sql.append("titulo = ?, ");
		sql.append("tipoResidencia = ?, ");
		sql.append("tipoLogradouro = ?, ");
		sql.append("logradouro = ?, ");
		sql.append("numero = ?, ");
		sql.append("bairro = ?, ");
		sql.append("cep = ?, ");
		sql.append("observacoes = ?, ");
		sql.append("id_cidade = ?, ");
		sql.append("id_estado = ?, ");
		sql.append("id_pais = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, endereco.getTitulo());
			pst.setString(2, endereco.getTipoResidencia());
			pst.setString(3, endereco.getTipoLogradouro());
			pst.setString(4, endereco.getLogradouro());
			pst.setString(5, endereco.getNumero());
			pst.setString(6, endereco.getBairro());
			pst.setString(7, endereco.getCep());
			pst.setString(8, endereco.getObservacoes());
			pst.setInt(9, endereco.getCidade().getId());
			pst.setInt(10, endereco.getCidade().getEstado().getId());
			pst.setInt(11, endereco.getCidade().getEstado().getPais().getId());
			pst.setInt(12, endereco.getId());
			
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
		Endereco endereco = (Endereco) entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT * FROM Endereco ");
		sql.append("WHERE 1 =1 ");
		
		if(endereco.getId() != null && endereco.getId() != 0) {
			sql.append("AND id = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			if(endereco.getId() != null && endereco.getId() != 0) {
				pst.setInt(1, endereco.getId());
				i++;
			}
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> enderecos = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				Endereco end = new Endereco();
			}
			rs.close();
			return enderecos;
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
