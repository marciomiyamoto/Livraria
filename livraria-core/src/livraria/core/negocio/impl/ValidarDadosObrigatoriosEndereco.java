package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.endereco.Endereco;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosEndereco implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Endereco) {
			Endereco endereco = (Endereco)entidade;
			// VERIFICA SE TODOS OS CAMPOS OBRIGATÓRIOS ESTÃO DIFERENTES DE NULL
			if(endereco.getTipo() != null && endereco.getBairro() != null && endereco.getCep() != null &&
					endereco.getCidade().getId() != null && endereco.getCidade().getEstado().getId() != null && 
					endereco.getCidade().getEstado().getPais().getId() != null &&
					endereco.getLogradouro() != null && endereco.getNumero() != null && endereco.getTipoLogradouro() != null && 
					endereco.getTipoResidencia() != null && endereco.getTitulo() != null) {
				// VERIFICA SE TODOS OS CAMPOS OBRIGATÓRIOS ESTÃO PREENCHIDOS
				if(endereco.getTipo().getId() != 0 && endereco.getBairro() != "" && endereco.getCep() != "" &&
						endereco.getCidade().getId() != 0 && endereco.getCidade().getEstado().getId() != 0 && 
						endereco.getCidade().getEstado().getPais().getId() != 0 &&
						endereco.getLogradouro() != "" && endereco.getNumero() != "" && endereco.getTipoLogradouro() != "" && 
						endereco.getTipoResidencia() != "" && endereco.getTitulo() != "") {
					return null;
				}
			}
			return "Todo cadastro de endereços associados a clientes deve ser composto dos seguintes dados: Tipo de residência (Casa, Apartamento, etc),"
					+ " Tipo Logradouro, Logradouro, Número, Bairro, CEP, Cidade, Estado e País.";
		}
		return null;
	}

}
