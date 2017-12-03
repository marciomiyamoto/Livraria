package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.cliente.Cliente;
import livraria.core.IStrategy;

public class ValidarSenhaCadastroCliente implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Cliente) {
			Cliente cliente = (Cliente)entidade;
			// EXPRESS�O REGULAR PARA VALIDAR A SENHA
			String padrao = "(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}";
			// CASO A SENHA PASSE PELA VALIDA��O DA EXPRESS�O REGULAR
			if(cliente.getSenha().matches(padrao)) {
				// CASO AS SENHAS SEJAM IGUAIS
				if(cliente.getSenha().equals(cliente.getSenhaRepetida())) {
					return null;
				} else {
					return "Senha inv�lida! A senha deve ser igual nos dois campos";
				}
			} else {
				return " Senha inv�lida! A senha deve ser composta de pelo menos 8 caracteres, ter letras mai�sculas "
						+ "e min�sculas, al�m de conter um dos seguintes caracteres especiais: !@#$%^&+=.";
			}
		}
		return null;
	}

}
