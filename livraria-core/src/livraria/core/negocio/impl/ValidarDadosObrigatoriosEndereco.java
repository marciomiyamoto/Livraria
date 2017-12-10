package livraria.core.negocio.impl;

import dominio.EntidadeDominio;
import dominio.endereco.Endereco;
import livraria.core.IStrategy;

public class ValidarDadosObrigatoriosEndereco implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof Endereco) {
			Endereco endereco = (Endereco)entidade;
			// VERIFICA SE TODOS OS CAMPOS OBRIGAT�RIOS EST�O DIFERENTES DE NULL
			if(endereco.getTipo() != null && endereco.getBairro() != null && endereco.getCep() != null &&
					endereco.getCidade().getId() != null && endereco.getCidade().getEstado().getId() != null && 
					endereco.getCidade().getEstado().getPais().getId() != null &&
					endereco.getLogradouro() != null && endereco.getNumero() != null && endereco.getTipoLogradouro() != null && 
					endereco.getTipoResidencia() != null && endereco.getTitulo() != null) {
				// VERIFICA SE TODOS OS CAMPOS OBRIGAT�RIOS EST�O PREENCHIDOS
				if(endereco.getTipo().getId() != 0 && endereco.getBairro() != "" && endereco.getCep() != "" &&
						endereco.getCidade().getId() != 0 && endereco.getCidade().getEstado().getId() != 0 && 
						endereco.getCidade().getEstado().getPais().getId() != 0 &&
						endereco.getLogradouro() != "" && endereco.getNumero() != "" && endereco.getTipoLogradouro() != "" && 
						endereco.getTipoResidencia() != "" && endereco.getTitulo() != "") {
					return null;
				}
			}
			return "Todo cadastro de endere�os associados a clientes deve ser composto dos seguintes dados: Tipo de resid�ncia (Casa, Apartamento, etc),"
					+ " Tipo Logradouro, Logradouro, N�mero, Bairro, CEP, Cidade, Estado e Pa�s.";
		}
		return null;
	}

}
