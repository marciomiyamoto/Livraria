package livraria.core.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.analise.EnumTipoPeriodo;
import dominio.analise.LinhaLivroPeriodoVendas;
import dominio.analise.LivroPeriodoVendas;

public class GraficoLivroPeriodoVendaDAO extends AbstractJdbcDAO {

	protected GraficoLivroPeriodoVendaDAO(String tabela, String idTabela) {
		super("ItemPedido", "id");
	}
	
	public GraficoLivroPeriodoVendaDAO(Connection conn) {
		super(conn, "ItemPedido", "id");
	}
	
	public GraficoLivroPeriodoVendaDAO() {
		super("ItemPedido", "id");
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
		LivroPeriodoVendas livro = (LivroPeriodoVendas)entidade;
		StringBuilder sql = new StringBuilder();
		
		if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.DIA.getValue())) {
			sql.append("SELECT id_estoque, to_char(dtCadastro, 'DD/MM/YY') as data, ");
		} else if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.MES.getValue())){
			sql.append("SELECT id_estoque, to_char(dtCadastro, 'MM/YY') as data, ");
		} else {
			sql.append("SELECT id_estoque, to_char(dtCadastro, 'YY') as data, ");
		}		
		
		sql.append("sum(qtde) as soma ");
		sql.append("FROM ItemPedido ");
		sql.append("WHERE 1=1 ");
		
		if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.DIA.getValue())) {
			sql.append("AND dtCadastro BETWEEN to_date(?, 'DD/MM/YY') ");
		} else if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.MES.getValue())){
			sql.append("AND dtCadastro BETWEEN to_date(?, 'MM/YY') ");
		} else {
			sql.append("AND dtCadastro BETWEEN to_date(?, 'YY') ");
		}
		
		if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.DIA.getValue())) {
			sql.append("AND to_date(?, 'DD/MM/YY') ");
		} else if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.MES.getValue())){
			sql.append("AND to_date(?, 'MM/YY') ");
		} else {
			sql.append("AND to_date(?, 'YY') ");
		}
		
		if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.DIA.getValue())) {
			sql.append("GROUP BY id_estoque, to_char(dtCadastro, 'DD/MM/YY') ");
		} else if(livro.getTipoPeriodo().equals(EnumTipoPeriodo.MES.getValue())){
			sql.append("GROUP BY id_estoque, to_char(dtCadastro, 'MM/YY') ");
		} else {
			sql.append("GROUP BY id_estoque, to_char(dtCadastro, 'YY') ");
		}
		
		sql.append("ORDER BY data");
			
		try {
			pst = connection.prepareStatement(sql.toString());
			
			pst.setDate(1, new java.sql.Date(livro.getDtInicial().getTime()));
			pst.setDate(2, new java.sql.Date(livro.getDtFinal().getTime()));
			
			ResultSet rs = pst.executeQuery();
			List<EntidadeDominio> livros = new ArrayList<EntidadeDominio>()	;
			List<LinhaLivroPeriodoVendas> linhas = new ArrayList<LinhaLivroPeriodoVendas>();
			while(rs.next() ) {				
				LinhaLivroPeriodoVendas linha = new LinhaLivroPeriodoVendas();
				linha.setIdEstoque(rs.getInt("id_estoque"));
				linha.setData(rs.getString("data"));
				linha.setQtde(rs.getInt("soma"));
				linhas.add(linha);
			}
			livro.setLinhas(linhas);
			livros.add(livro);
			rs.close();
			return livros;
		} catch(SQLException ex) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
