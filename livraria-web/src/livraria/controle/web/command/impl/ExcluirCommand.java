package livraria.controle.web.command.impl;

import dominio.EntidadeDominio;
import livraria.core.aplicacao.Resultado;

public class ExcluirCommand extends AbstractCommand {
	
	@Override
	public Resultado execute(EntidadeDominio entidade) {
		return fachada.excluir(entidade);
	}
}
