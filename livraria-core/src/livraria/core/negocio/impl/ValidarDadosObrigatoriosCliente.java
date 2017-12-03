package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.Telefone;
import dominio.cliente.Cliente;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosCliente implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Cliente) {
			Cliente cliente = (Cliente)entidade;
			// VERIFICA SE TODOS OS DADOS OBRIGAT�RIOS DE CLIENTE EST�O DIFERENTES DE NULL
			if(cliente.getCpf() != null && cliente.getDtNascimento() != null && cliente.getEmail() != null &&
					cliente.getEndCobranca() != null && cliente.getEndsEntrega() != null && cliente.getEnderecoResidencial() != null &&
					cliente.getGenero() != null &&cliente.getNome() != null && cliente.getTelefones() != null && cliente.getSenha() != null) {
				// VERIFICA SE TODOS OS DADOS OBRIGAT�RIOS DE CLIENTE EST�O PREENCHIDOS
				if(cliente.getCpf() != 0 && cliente.getDtNascimento() != null && cliente.getEmail() != "" &&
						cliente.getGenero().getNome() != "" &&cliente.getNome() != "" && cliente.getSenha() != "") {
					// VERIFICA TODOS OS DADOS OBRIGAT�RIOS DOS ENDERE�OS
					ValidarDadosObrigatoriosEndereco vEnd = new ValidarDadosObrigatoriosEndereco();
					if(vEnd.processar(cliente.getEndCobranca()) == null && vEnd.processar(cliente.getEnderecoResidencial()) == null && 
							vEnd.processar(cliente.getEndsEntrega().get(0)) == null) {
						// VERIFICA TODOS OS DADOS OBRIGATORIOS DOS TELEFONES
						ValidarDadosObrigatoriosTelefone vTel = new ValidarDadosObrigatoriosTelefone();
						for(Telefone tel : cliente.getTelefones()) {
							if(vTel.processar(tel) != null) {
								return "Todo telefone deve ser composto pelo tipo, DDD e n�mero";
							}
						}
						return null;
					} else {
						return "Todo cadastro de endere�os associados a clientes deve ser composto dos seguintes dados: Tipo de resid�ncia (Casa, Apartamento, etc),"
								+ " Tipo Logradouro, Logradouro, N�mero, Bairro, CEP, Cidade, Estado e Pa�s.";
					}
				}
			}
			return "Para todo cliente cadastrado � obrigat�rio o cadastro dos seguintes dados: G�nero, Nome, Data de Nascimento, CPF, Telefone (deve ser composto pelo tipo, "
					+ "DDD e n�mero), e-mail, senha, endere�o residencial.";
		}
		return null;
	}
}
