package livraria.controle.web.managedbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import dominio.livro.LogLivro;
import livraria.controle.web.command.ICommand;
import livraria.controle.web.command.impl.AlterarCommand;
import livraria.controle.web.command.impl.ConsultarCommand;
import livraria.controle.web.command.impl.ExcluirCommand;
import livraria.controle.web.command.impl.SalvarCommand;
import livraria.controle.web.command.impl.VisualizarCommand;
import livraria.core.aplicacao.Resultado;

@ManagedBean
@ViewScoped
public class LogLivroMB {
	
	private LogLivro log;
	private List<LogLivro> logs;
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		log = new LogLivro();
		logs = new ArrayList<LogLivro>();
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		populaLogLivros();
	}
	
	public void populaLogLivros() {
		logs.clear();
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(log);
		
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				logs.add(i, (LogLivro) rs.getEntidades().get(i));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public LogLivro getLog() {
		return log;
	}

	public void setLog(LogLivro log) {
		this.log = log;
	}

	public List<LogLivro> getLogs() {
		return logs;
	}

	public void setLogs(List<LogLivro> logs) {
		this.logs = logs;
	}

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		LogLivroMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}

}
