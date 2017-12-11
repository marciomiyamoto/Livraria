package livraria.controle.web.managedbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

import dominio.analise.LinhaLivroPeriodoVendas;
import dominio.analise.LivroPeriodoVendas;
import dominio.livro.Estoque;
import livraria.controle.web.command.ICommand;
import livraria.controle.web.command.impl.AlterarCommand;
import livraria.controle.web.command.impl.ConsultarCommand;
import livraria.controle.web.command.impl.ExcluirCommand;
import livraria.controle.web.command.impl.SalvarCommand;
import livraria.controle.web.command.impl.VisualizarCommand;
import livraria.core.aplicacao.Resultado;

@ManagedBean
public class GraficoMB {

	private LineChartModel linhaModelo;
	private List<Estoque> estoques;
	private Estoque estoqueA;
	private Estoque estoqueB;
	private Estoque estoqueC;
	private LivroPeriodoVendas livroPerVend;
	private Boolean flgGerar;
	
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		linhaModelo = new LineChartModel();
		estoques = new ArrayList<Estoque>();
		estoqueA = new Estoque();
		estoqueB = new Estoque();
		estoqueC = new Estoque();
		livroPerVend = new LivroPeriodoVendas();
		flgGerar = false;
		
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		carregarEstoques();
	}
	
	private void carregarEstoques() {
		Estoque estoque = new Estoque();
		
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(estoque);
		
		if(rs.getMsg() == null) {
			for (int i = 0; i < rs.getEntidades().size(); i++) {
				try {
					estoques.add(i, (Estoque) rs.getEntidades().get(i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		}
	}
	
	public void gerarGrafico() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(livroPerVend);
		
		if(rs.getMsg() == null) {
			livroPerVend = (LivroPeriodoVendas)rs.getEntidades().get(0);
			
			for(Estoque e : estoques) {
				if(e.getId().equals(estoqueA.getId())) {
					estoqueA = e;
				} else if(e.getId().equals(estoqueB.getId())) {
					estoqueB = e;
				} else if(e.getId().equals(estoqueC.getId())) {
					estoqueC = e;
				}
			}
						
			ChartSeries livroA = new ChartSeries();
			ChartSeries livroB = new ChartSeries();
			ChartSeries livroC = new ChartSeries();
			
			livroA.setLabel("Livro A");
			livroB.setLabel("Livro B");
			livroC.setLabel("Livro C");
			
			for(LinhaLivroPeriodoVendas livro : livroPerVend.getLinhas()) {
				if(livro.getIdEstoque().equals(estoqueA.getId())) {
					livroA.set(livro.getData(), livro.getQtde());
				} else if(livro.getIdEstoque().equals(estoqueB.getId())) {
					livroB.set(livro.getData(), livro.getQtde());
				} else if(livro.getIdEstoque().equals(estoqueC.getId())) {
					livroC.set(livro.getData(), livro.getQtde());
				}
			}
			
			linhaModelo.addSeries(livroA);
			linhaModelo.addSeries(livroB);
			linhaModelo.addSeries(livroC);
			linhaModelo.setTitle("Vendas em determinado período");
			linhaModelo.setLegendPosition("e");
			linhaModelo.setShowPointLabels(true);
			linhaModelo.getAxes().put(AxisType.X, new CategoryAxis("Dia"));
			Axis yAxis = linhaModelo.getAxis(AxisType.Y);
			yAxis.setLabel("Qtde vendas");
			yAxis.setMin(livroPerVend.getQtdeMin());
			yAxis.setMax(livroPerVend.getQtdeMax());
			flgGerar = true;
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		}
	}

	// GETTERS E SETTERS
	public LineChartModel getLinhaModelo() {
		return linhaModelo;
	}

	public void setLinhaModelo(LineChartModel linhaModelo) {
		this.linhaModelo = linhaModelo;
	}

	public List<Estoque> getEstoques() {
		return estoques;
	}

	public void setEstoques(List<Estoque> estoques) {
		this.estoques = estoques;
	}

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		GraficoMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}

	public LivroPeriodoVendas getLivroPerVend() {
		return livroPerVend;
	}

	public void setLivroPerVend(LivroPeriodoVendas livroPerVend) {
		this.livroPerVend = livroPerVend;
	}

	public Boolean getFlgGerar() {
		return flgGerar;
	}

	public void setFlgGerar(Boolean flgGerar) {
		this.flgGerar = flgGerar;
	}

	public Estoque getEstoqueA() {
		return estoqueA;
	}

	public void setEstoqueA(Estoque estoqueA) {
		this.estoqueA = estoqueA;
	}

	public Estoque getEstoqueB() {
		return estoqueB;
	}

	public void setEstoqueB(Estoque estoqueB) {
		this.estoqueB = estoqueB;
	}

	public Estoque getEstoqueC() {
		return estoqueC;
	}

	public void setEstoqueC(Estoque estoqueC) {
		this.estoqueC = estoqueC;
	}
	
}
