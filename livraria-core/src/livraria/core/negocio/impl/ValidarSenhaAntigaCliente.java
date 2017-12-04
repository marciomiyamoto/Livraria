package livraria.core.negocio.impl;

import java.sql.SQLException;

import dominio.EntidadeDominio;
import dominio.cliente.Cliente;
import livraria.core.IStrategy;
import livraria.core.dao.impl.ClienteDAO;

public class ValidarSenhaAntigaCliente implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Cliente) {
			Cliente cliente = (Cliente)entidade;
			if(cliente.getSenhaAntiga() != null && cliente.getSenhaAntiga() != "") {
				// RECUPERA DADOS DO CLIENTE NO BANCO PARA COMPARAÇÃO
				ClienteDAO cliDao = new ClienteDAO();
				Cliente cliTemp = new Cliente();
				cliTemp.setId(cliente.getId());
				try {
					cliTemp = (Cliente)cliDao.consultar(cliTemp).get(0);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// VERIFICA SE A SENHA ANTIGA FOI DIGITADA CORRETAMENTE
				if(cliente.getSenhaAntiga().equals(cliTemp.getSenha())) {
					return null;
				}
			}
			return "Senha antiga incorreta";
		}
		return null;
	}

}
