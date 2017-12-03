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
			// VERIFICA SE TODOS OS DADOS OBRIGATÓRIOS DE CLIENTE ESTÃO DIFERENTES DE NULL
			if(cliente.getCpf() != null && cliente.getDtNascimento() != null && cliente.getEmail() != null &&
					cliente.getEndCobranca() != null && cliente.getEndsEntrega() != null && cliente.getEnderecoResidencial() != null &&
					cliente.getGenero() != null &&cliente.getNome() != null && cliente.getTelefones() != null && cliente.getSenha() != null) {
				// VERIFICA SE TODOS OS DADOS OBRIGATÓRIOS DE CLIENTE ESTÃO PREENCHIDOS
				if(cliente.getCpf() != 0 && cliente.getDtNascimento() != null && cliente.getEmail() != "" &&
						cliente.getGenero().getNome() != "" &&cliente.getNome() != "" && cliente.getSenha() != "") {
					// VERIFICA TODOS OS DADOS OBRIGATÓRIOS DOS ENDEREÇOS
					ValidarDadosObrigatoriosEndereco vEnd = new ValidarDadosObrigatoriosEndereco();
					if(vEnd.processar(cliente.getEndCobranca()) == null && vEnd.processar(cliente.getEnderecoResidencial()) == null && 
							vEnd.processar(cliente.getEndsEntrega().get(0)) == null) {
						// VERIFICA TODOS OS DADOS OBRIGATORIOS DOS TELEFONES
						ValidarDadosObrigatoriosTelefone vTel = new ValidarDadosObrigatoriosTelefone();
						for(Telefone tel : cliente.getTelefones()) {
							if(vTel.processar(tel) != null) {
								return "Todo telefone deve ser composto pelo tipo, DDD e número";
							}
						}
						return null;
					} else {
						return "Todo cadastro de endereços associados a clientes deve ser composto dos seguintes dados: Tipo de residência (Casa, Apartamento, etc),"
								+ " Tipo Logradouro, Logradouro, Número, Bairro, CEP, Cidade, Estado e País.";
					}
				}
			}
			return "Para todo cliente cadastrado é obrigatório o cadastro dos seguintes dados: Gênero, Nome, Data de Nascimento, CPF, Telefone (deve ser composto pelo tipo, "
					+ "DDD e número), e-mail, senha, endereço residencial.";
		}
		return null;
	}
}
