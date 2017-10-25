package livraria.controle.web.command.impl;

import dominio.EntidadeDominio;
import livraria.core.aplicacao.Resultado;

public class AlterarCommand extends AbstractCommand {
	
	@Override
	public Resultado execute(EntidadeDominio entidade) {
		return fachada.alterar(entidade);
	}

}
