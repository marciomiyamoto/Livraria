package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dominio.EntidadeDominio;
import dominio.EnumTelefone;
import dominio.Genero;
import dominio.Telefone;
import dominio.TipoTelefone;
import dominio.Usuario;
import dominio.cliente.Cartao;
import dominio.cliente.Cliente;
import dominio.cliente.EnumTipoCartao;
import dominio.endereco.Cidade;
import dominio.endereco.Endereco;
import dominio.endereco.EnumEndereco;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.endereco.TipoEndereco;
import dominio.livro.Autor;

public class ClienteDAO extends AbstractJdbcDAO {
	
	protected ClienteDAO(String tabela, String idTabela) {
		super("Cliente", "id");
	}
	
	public ClienteDAO(Connection conn) {
		super(conn, "Cliente", "id");
	}
	
	public ClienteDAO() {
		super("Cliente", "id");
	}
	@Override
	public void salvar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		Cliente cliente = (Cliente)entidade;
		EnderecoDAO endDAO = new EnderecoDAO();
		Usuario usuario = new Usuario();
		UsuarioDAO userDAO = new UsuarioDAO();
		StringBuilder sql = new StringBuilder();
		
		// SALVANDO USUARIO
		usuario.setUsuario(cliente.getUsuario());
		usuario.setSenha(cliente.getSenha());
		userDAO.ctrlTransacao = false;
		userDAO.salvar(usuario);
		
		// SALVANDO ENDEREÇO RESIDENCIAL
		endDAO.ctrlTransacao = false;
		endDAO.salvar(cliente.getEnderecoResidencial());
		
		// SALVANDO ENDEREÇOS DE ENTREGA ASSOCIADOS COM CLIENTE
		if(cliente.getEndsEntrega() != null) {
			for(Endereco end : cliente.getEndsEntrega()) {
				endDAO.ctrlTransacao = false;
				endDAO.salvar(end);
			}
		}
		
		// SALVANDO ENDEREÇO DE COBRANÇA
		if(cliente.getEndCobranca() != null) {
			endDAO.ctrlTransacao = false;
			endDAO.salvar(cliente.getEndCobranca());
		}
		
		//SALVANDO LISTA DE TELEFONES
		TelefoneDAO telDAO = new TelefoneDAO();
		for(Telefone tel : cliente.getTelefones()) {
			telDAO.ctrlTransacao = false;
			telDAO.salvar(tel);
		}
		
		// SALVANDO CLIENTE
		sql.append("INSERT INTO Cliente ");
		sql.append("(nome, ");
		sql.append("cpf, ");
		sql.append("dtNascimento, ");
		sql.append("email,  ");
		sql.append("ativo, ");
		sql.append("id_genero, ");
		sql.append("id_usuario, ");
		sql.append("id_endPreferencial) ");
		sql.append("VALUES(?,?,?,?,?,?,?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString(), new String[] {"id"});
			pst.setString(1, cliente.getNome());
			pst.setLong(2, cliente.getCpf());
			pst.setDate(3, new java.sql.Date(cliente.getDtNascimento().getTime()));
			pst.setString(4, cliente.getEmail());
			pst.setBoolean(5, true);
			pst.setInt(6, cliente.getGenero().getId());
			pst.setInt(7, usuario.getId());
			if(cliente.getEndPreferencial() != null) {
				pst.setInt(8, cliente.getEndPreferencial().getId());
			} else {
				pst.setNull(8, java.sql.Types.INTEGER);
			}
			
			pst.executeUpdate();
			ResultSet generatedKeys = pst.getGeneratedKeys();
			if(null != generatedKeys && generatedKeys.next()) {
				cliente.setId(generatedKeys.getInt(1));
			}
			generatedKeys.close();
			connection.commit();
			// ASSOCIANDO CLIENTE COM ENDEREÇOS NA TABELA CLIENTE_END
			if(cliente.getEndsEntrega() != null) {
				if(cliente.getEndPreferencial() != null) {
					salvarClienteEndereco(cliente, cliente.getEndPreferencial());
				} else {
					for(Endereco end : cliente.getEndsEntrega()) {
						salvarClienteEndereco(cliente, end);
					}
				}
			}
			if(cliente.getEndCobranca() != null) {
				salvarClienteEndereco(cliente, cliente.getEndCobranca());
			}
			salvarClienteEndereco(cliente, cliente.getEnderecoResidencial());
			
			// ASSOCIANDO CLIENTE COM TELEFONES NA TABELA CLIENTE_TEL
			for(Telefone tel : cliente.getTelefones()) {
				salvarClienteTel(cliente, tel);
			}
			
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
	
	private void salvarClienteEndereco(Cliente cli, Endereco end) {
		
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();		
		sql.append("INSERT INTO Cliente_end ");
		sql.append("(id_cliente, ");
		sql.append("id_endereco) ");
		sql.append("VALUES(?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, cli.getId());
			pst.setInt(2, end.getId());
			
			pst.executeUpdate();
			pst.close();
			connection.commit();
		}catch(SQLException e) {
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
		} 
	}
	private void salvarClienteTel(Cliente cli, Telefone tel) {
		
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();		
		sql.append("INSERT INTO Cliente_tel ");
		sql.append("(id_cliente, ");
		sql.append("id_telefone) ");
		sql.append("VALUES(?,?)");
		
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, cli.getId());
			pst.setInt(2, tel.getId());
			
			pst.executeUpdate();
			pst.close();
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
		} 
	}

	@Override
	public void alterar(EntidadeDominio entidade) throws SQLException {
		abrirConexao();
		PreparedStatement pst = null;
		Cliente cliente = (Cliente)entidade;
		Usuario usuario = new Usuario();
		UsuarioDAO userDAO = new UsuarioDAO();
		
		// ALTERANDO DADOS DO USUARIO
		if( cliente.getIdUsuario() != null && cliente.getUsuario() != null && cliente.getSenha() != null) {
			if(cliente.getIdUsuario() != 0 && !cliente.getUsuario().equals("") && !cliente.getSenha().equals("")) {
				usuario.setId(cliente.getIdUsuario());
				usuario.setUsuario(cliente.getUsuario());
				usuario.setSenha(cliente.getSenha());
				userDAO.ctrlTransacao = false;
				userDAO.alterar(usuario);
			}
		}
		
		// ALTERANDO DADOS DO CLIENTE
		StringBuilder sql = new StringBuilder();
		
		sql.append("UPDATE Cliente SET ");
		sql.append("nome = ?, ");
		sql.append("cpf = ?, ");
		sql.append("dtNascimento = ?, ");
		sql.append("email = ?, ");
		sql.append("ativo = ?, ");
		sql.append("id_genero = ?, ");
		sql.append("id_endPreferencial = ?, ");
		sql.append("id_cartaoPreferencial = ? ");
		sql.append("WHERE id = ?");
		try {
			connection.setAutoCommit(false);
			
			pst = connection.prepareStatement(sql.toString());
			pst.setString(1, cliente.getNome());
			pst.setLong(2, cliente.getCpf());
			pst.setDate(3, new java.sql.Date(cliente.getDtNascimento().getTime()));
			pst.setString(4, cliente.getEmail());
			pst.setBoolean(5, cliente.getAtivo());
			pst.setInt(6, cliente.getGenero().getId());
			if(cliente.getEndPreferencial() != null && cliente.getEndPreferencial().getId() != null && cliente.getEndPreferencial().getId() != 0) {
				pst.setInt(7, cliente.getEndPreferencial().getId());
			} else {
				pst.setNull(7, java.sql.Types.INTEGER);
			}
			if(cliente.getCartaoPreferencial() != null && cliente.getCartaoPreferencial().getId() != null && cliente.getCartaoPreferencial().getId() != 0) {
				pst.setInt(8, cliente.getCartaoPreferencial().getId());
			} else {
				pst.setNull(8, java.sql.Types.INTEGER);
			}
			pst.setInt(9, cliente.getId());
			
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
		Cliente cli = (Cliente)entidade;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT c.id as id, c.dtCadastro as dtCadastro, c.nome as nome, c.cpf as cpf, ");
		sql.append("c.dtNascimento as dtNascimento, c.email as email, c.ativo as ativo, c.id_endPreferencial, c.id_cartaoPreferencial, ");
		sql.append("g.id as g_id, g.dtCadastro as g_dtCadastro, g.nome as genero, ");
		sql.append("u.id as id_usuario, u.dtCadastro as u_dtCadastro, u.usuario as usuario, u.senha as senha ");
		sql.append("FROM Cliente c ");
		sql.append("JOIN genero g on g.id = c.ID_GENERO ");
		sql.append("JOIN usuario u on u.id = c.ID_USUARIO ");
		sql.append("WHERE 1 = 1 ");
		
		if(cli.getId() != null && cli.getId() != 0) {
			sql.append("AND c.id = ? ");
		}
		if(cli.getNome() != null && !cli.getNome().equals("")) {
			sql.append("AND c.nome = ? ");
		}
		if(cli.getCpf() != null && cli.getCpf() != 0) {
			sql.append("AND cpf = ? ");
		}
		if(cli.getDtNascimento() != null) {
			sql.append("AND dtNascimento = ? ");
		}
		if(cli.getEmail() != null && !cli.getEmail().equals("")) {
			sql.append("AND email = ? ");
		}
		if(cli.getAtivo() != null) {
			sql.append("AND ativo = ? ");
		}
		if(cli.getGenero() != null && cli.getGenero().getId() != null && cli.getGenero().getId() != 0) {
			sql.append("AND g.id = ?");
		}
		
		try {
			pst = connection.prepareStatement(sql.toString());
			int i = 1;
			
			if(cli.getId() != null && cli.getId() != 0) {
				pst.setInt(i, cli.getId());
				i++;
			}
			if(cli.getNome() != null && !cli.getNome().equals("")) {
				pst.setString(i, cli.getNome());
				i++;
			}
			if(cli.getCpf() != null && cli.getCpf() != 0) {
				pst.setLong(i, cli.getCpf());
				i++;
			}
			if(cli.getDtNascimento() != null) {
				pst.setDate(i, new java.sql.Date(cli.getDtNascimento().getTime()));
				i++;
			}
			if(cli.getEmail() != null && !cli.getEmail().equals("")) {
				pst.setString(i, cli.getEmail());
				i++;
			}
			if(cli.getAtivo() != null) {
				pst.setBoolean(i, cli.getAtivo());
				i++;
			}
			if(cli.getGenero() != null && cli.getGenero().getId() != null && cli.getGenero().getId() != 0) {
				pst.setInt(i, cli.getGenero().getId());
				i++;
			}
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> clientes = new ArrayList<EntidadeDominio>()	;
			while(rs.next() ) {
				Usuario usuario = new Usuario();
				usuario.setId(rs.getInt("id_usuario"));
				usuario.setDtCadastro(rs.getDate("u_dtCadastro"));
				usuario.setUsuario(rs.getString("usuario"));
				usuario.setSenha(rs.getString("senha"));
				
				Genero genero = new Genero();
				genero.setId(rs.getInt("g_id"));
				genero.setDtCadastro(rs.getDate("g_dtCadastro"));
				genero.setNome(rs.getString("genero"));
				
				Cliente cliente = new Cliente();
				cliente.setId(rs.getInt("id"));
				cliente.setDtCadastro(rs.getDate("dtCadastro"));
				cliente.setNome(rs.getString("nome"));
				cliente.setCpf(rs.getLong("cpf"));
				cliente.setDtNascimento(rs.getDate("dtNascimento"));
				cliente.setEmail(rs.getString("email"));
				cliente.setAtivo(rs.getBoolean("ativo"));
				cliente.setGenero(genero);
				cliente.setIdUsuario(rs.getInt("id_usuario"));
				cliente.setUsuario(usuario.getUsuario());
				cliente.setSenha(usuario.getSenha());
				cliente.setTelefones(buscarTelefones(cliente.getId()));
				
				int idEndPreferencial = rs.getInt("id_endPreferencial");
				List<Endereco> endsEntrega = new ArrayList<Endereco>();
				for(Endereco end : buscarEnderecos(cliente.getId())) {
					if(end.getTipo().getId() == EnumEndereco.RESIDENCIAL.getValue()) {
						cliente.setEnderecoResidencial(end);
					} else if(end.getTipo().getId() == EnumEndereco.COBRANCA.getValue()) {
						cliente.setEndCobranca(end);
					} else if(end.getTipo().getId() == EnumEndereco.ENTREGA.getValue()) {
						if(idEndPreferencial == end.getId()) {
							cliente.setEndPreferencial(end);
						} else {
							endsEntrega.add(end);
						}
					}
				}
				cliente.setEndsEntrega(endsEntrega);
				
				CartaoDAO car = new CartaoDAO();
				Cartao cartao = new Cartao();
				cartao.setIdCliente(cliente.getId());
				cartao.setTipoCartao(EnumTipoCartao.CADASTRO_CLIENTE.getValue());
				List<Cartao> cartoes = (List<Cartao>)(List<?>)car.consultar(cartao);
				int idCartaoPref = rs.getInt("id_cartaoPreferencial");
				for(Cartao c : cartoes) {
					if(c.getId() == idCartaoPref) {
						cliente.setCartaoPreferencial(c);
					}
				}
				if(cliente.getCartaoPreferencial() != null) {
					cartoes.remove(cliente.getCartaoPreferencial());
				}
				cliente.setCartoes(cartoes);
				
				clientes.add(cliente);
			}
			rs.close();
			List<Cliente> cliList = new ArrayList<Cliente>();
			if(cli.getTelefones() != null && cli.getTelefones().size() != 0) {
				for(Cliente c : (List<Cliente>)(List<?>)clientes) {
					for(Telefone telCli : c.getTelefones()) {
						for(Telefone t : cli.getTelefones()) {
							if(t.getTipo().getId().equals(telCli.getTipo().getId())) {
								if((t.getDdd() != null && t.getDdd().equals(telCli.getDdd())) 
										||(t.getNumero() != null && t.getNumero().equals(telCli.getNumero()))) {
									cliList.add(c);
								}
							}
						}
					}
				}
				clientes = (List<EntidadeDominio>)(List<?>)cliList.stream().distinct().collect(Collectors.toList());
			}
			return clientes;
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
	
	public List<Telefone> buscarTelefones(int idCliente) {
		
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT t.id, t.dtCadastro, t.ddd, t.numero, ");
		sql.append("tt.id AS tt_id, tt.dtCadastro AS tt_dtCadastro, tt.nome AS tipo ");
		sql.append("FROM Telefone t ");
		sql.append("JOIN cliente_tel c ON t.id = c.ID_TELEFONE ");
		sql.append("JOIN TIPOTELEFONE tt ON tt.id = t.ID_TIPO ");
		sql.append("where c.ID_CLIENTE = ?");
		
		try {
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, idCliente);
			ResultSet rs = pst.executeQuery();
			List<Telefone> telefones = new ArrayList<Telefone>()	;
			while(rs.next() ) {
				TipoTelefone tipo = new TipoTelefone();	
				tipo.setId(rs.getInt("tt_id"));
				tipo.setDtCadastro(rs.getDate("tt_dtCadastro"));
				tipo.setNome(rs.getString("tipo"));
				
				Telefone tel = new Telefone();
				tel.setId(rs.getInt("id"));
				tel.setDtCadastro(rs.getDate("dtCadastro"));
				tel.setDdd(rs.getInt("ddd"));
				tel.setNumero(rs.getInt("numero"));
				tel.setTipo(tipo);
				telefones.add(tel);
			}
			pst.close();
			rs.close();
			return telefones;
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
		} 
		return null;
	}
	
	public List<Endereco> buscarEnderecos(int idCliente) {
		
		PreparedStatement pst = null;
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT e.id AS id, e.dtCadastro AS dtCadastro, e.logradouro AS logradouro, ");
		sql.append("e.numero AS numero, e.bairro AS bairro, e.cep AS cep, e.observacoes AS observacoes, ");
		sql.append("e.tiporesidencia AS tipoResidencia, e.tipologradouro AS tipoLogradouro, e.titulo AS titulo, ");
		sql.append("ci.id AS ci_id, ci.dtCadastro AS ci_dtCadastro, ci.nome AS cidade, ci.id_estado as ci_idEstado, ");
		sql.append("es.id AS es_id, es.dtCadastro AS es_dtCadastro, es.nome AS estado, es.id_pais as es_idPais, ");
		sql.append("pa.id AS pa_id, pa.dtCadastro AS pa_dtCadastro, pa.nome AS pais, ");
		sql.append("te.id AS te_id, te.dtCadastro AS te_dtCadastro, te.nome AS tipoResidencia ");
		sql.append("FROM  endereco e ");
		sql.append("JOIN cliente_end ce ON e.id = ce.id_endereco ");
		sql.append("JOIN cidade ci ON ci.id = e.id_cidade ");
		sql.append("JOIN estado es ON es.id = e.id_estado ");
		sql.append("JOIN pais pa ON pa.id = e.id_pais ");
		sql.append("JOIN tipoEndereco te ON te.id = id_tipoendereco ");
		sql.append("WHERE ce.id_cliente = ?");
		
		try {
			pst = connection.prepareStatement(sql.toString());
			pst.setInt(1, idCliente);
			ResultSet rs = pst.executeQuery();
			List<Endereco> enderecos = new ArrayList<Endereco>()	;
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
			pst.close();
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
		} 
		return null;
	}

}
