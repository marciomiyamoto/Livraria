package livraria.controle.web.command;

import dominio.EntidadeDominio;
import livraria.core.aplicacao.Resultado;

public interface ICommand {
	
	public Resultado execute(EntidadeDominio entidade);

}
