package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.endereco.Cidade;
import dominio.endereco.Endereco;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.endereco.TipoEndereco;
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
		abrirConexao();
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
		Endereco endereco = (Endereco) entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e.id AS id, e.dtCadastro AS dtCadastro, e.logradouro AS logradouro, ");
		sql.append("e.numero AS numero, e.bairro AS bairro, e.cep AS cep, e.observacoes AS observacoes, ");
		sql.append("e.tiporesidencia AS tipoResidencia, e.tipologradouro AS tipoLogradouro, e.titulo AS titulo, ");
		sql.append("ci.id AS ci_id, ci.dtCadastro AS ci_dtCadastro, ci.nome AS cidade, ci.id_estado as ci_idEstado, ");
		sql.append("es.id AS es_id, es.dtCadastro AS es_dtCadastro, es.nome AS estado, es.id_pais as es_idPais, ");
		sql.append("pa.id AS pa_id, pa.dtCadastro AS pa_dtCadastro, pa.nome AS pais, ");
		sql.append("te.id AS te_id, te.dtCadastro AS te_dtCadastro, te.nome AS tipoResidencia ");
		sql.append("FROM  endereco e ");
		sql.append("JOIN cidade ci ON ci.id = e.id_cidade ");
		sql.append("JOIN estado es ON es.id = e.id_estado ");
		sql.append("JOIN pais pa ON pa.id = e.id_pais ");
		sql.append("JOIN tipoEndereco te ON te.id = id_tipoendereco ");
		sql.append("WHERE 1 =1 ");
		
		if(endereco.getId() != null && endereco.getId() != 0) {
			sql.append("AND e.id = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			if(endereco.getId() != null && endereco.getId() != 0) {
				pst.setInt(i, endereco.getId());
				i++;
			}
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> enderecos = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				TipoEndereco tipo = new TipoEndereco();
				tipo.setId(rs.getInt("te_id"));
				tipo.setDtCadastro(rs.getDate("te_dtCadastro"));
				tipo.setNome(rs.getString("tipoResidencia"));
				
				Pais pais = new Pais();
				pais.setId(rs.getInt("pa_id"));
				pais.setDtCadastro(rs.getDate("pa_dtCadastro"));
				pais.setNome(rs.getString("pais"));
				
				Estado estado = new Estado();
				estado.setId(rs.getInt("es_id"));
				estado.setDtCadastro(rs.getDate("es_dtCadastro"));
				estado.setNome(rs.getString("estado"));
				estado.setPais(pais);
				
				Cidade cidade = new Cidade();
				cidade.setId(rs.getInt("ci_id"));
				cidade.setDtCadastro(rs.getDate("ci_dtCadastro"));
				cidade.setNome(rs.getString("cidade"));
				cidade.setEstado(estado);
				
				Endereco end = new Endereco();
				end.setId(rs.getInt("id"));
				end.setDtCadastro(rs.getDate("dtCadastro"));
				end.setBairro(rs.getString("bairro"));
				end.setCep(rs.getString("cep"));
				end.setLogradouro(rs.getString("logradouro"));
				end.setNumero(rs.getString("numero"));
				end.setObservacoes(rs.getString("observacoes"));
				end.setTipoLogradouro(rs.getString("tipoLogradouro"));
				end.setTipoResidencia(rs.getString("tipoResidencia"));
				end.setTitulo(rs.getString("titulo"));
				end.setTipo(tipo);
				end.setCidade(cidade);
				
				enderecos.add(end);
			}
			rs.close();
			return enderecos;
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
