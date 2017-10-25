package livraria.controle.web.managedbean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

import dominio.EnumTelefone;
import dominio.Genero;
import dominio.Telefone;
import dominio.Usuario;
import dominio.cliente.BandeiraCartao;
import dominio.cliente.Cartao;
import dominio.cliente.Cliente;
import dominio.cliente.ClienteEnd;
import dominio.endereco.Cidade;
import dominio.endereco.Endereco;
import dominio.endereco.EnumEndereco;
import dominio.endereco.Estado;
import dominio.endereco.Pais;
import dominio.endereco.TipoEndereco;
import livraria.controle.web.command.ICommand;
import livraria.controle.web.command.impl.AlterarCommand;
import livraria.controle.web.command.impl.ConsultarCommand;
import livraria.controle.web.command.impl.ExcluirCommand;
import livraria.controle.web.command.impl.SalvarCommand;
import livraria.controle.web.command.impl.VisualizarCommand;
import livraria.core.aplicacao.Resultado;

@ManagedBean
@ViewScoped
public class PainelClienteMB {
	
	private Cliente cliente;
	private String numTelFixo;
	private String numTelCelular;
	private Genero genero;
	private List<Genero> generos;
	private Cartao cartao;
	private Cartao cartaoSelecionado;
	private Cartao cartaoSalvar;
	private List<Cartao> cartoes;
	private BandeiraCartao bandeira;
	private BandeiraCartao bandeiraSalvar;
	private List<BandeiraCartao> bandeiras;
	private String maskDtVencimento;
	private String maskDtVencimentoSalvar;
	private String maskDtNascimento;
	private Endereco endereco;
	private Endereco endCobrSalvar;
	private Endereco endPrefSalvar;
	private Endereco endSelecionado;
	private Pais pais;
	private Pais paisSelecionado;
	private Pais paisSalvar;
	private Pais paisCobSalvar;
	private Pais paisPrefSalvar;
	private List<Pais> paises;
	private List<Pais> paisesSalvar;
	private List<Pais> paisesCobSalvar;
	private List<Pais> paisesPrefSalvar;
	private Estado estado;
	private Estado estadoSelecionado;
	private Estado estadoSalvar;
	private Estado estadoCobSalvar;
	private Estado estadoPrefSalvar;
	private List<Estado> estados;
	private List<Estado> estadosSalvar;
	private List<Estado> estadosCobSalvar;
	private List<Estado> estadosPrefSalvar;
	private Cidade cidade;
	private Cidade cidadeSelecionada;
	private Cidade cidadeSalvar;
	private Cidade cidadeCobSalvar;
	private Cidade cidadePrefSalvar;
	private List<Cidade> cidades;
	private List<Cidade> cidadesSalvar;
	private List<Cidade> cidadesCobSalvar;
	private List<Cidade> cidadesPrefSalvar;
	private String senhaAntiga;
	private String senhaNova;
	private String senhaRepetida;
	private Boolean cartaoPreferencial;
	private Boolean cartaoPrefEdit;
	private Boolean endPreferencial;
	private Boolean endPrefEdit;
	
	private static Map<String, ICommand> commands;
	private ICommand command;
	
	@PostConstruct
	public void init() {
		
		cartao = new Cartao();
		cartaoSalvar = new Cartao();
		cartaoSelecionado = new Cartao();
		cartoes = new ArrayList<Cartao>();
		bandeira = new BandeiraCartao();
		bandeiraSalvar = new BandeiraCartao();
		bandeiras = new ArrayList<BandeiraCartao>();
		maskDtVencimento = null;
		maskDtVencimentoSalvar = null;
		endereco = new Endereco();
		endSelecionado = new Endereco();
		pais = new Pais();
		paisSelecionado = new Pais();
		paises = new ArrayList<Pais>();
		paisesSalvar = new ArrayList<Pais>();
		estado = new Estado();
		estadoSelecionado = new Estado();
		estados = new ArrayList<Estado>();
		estadosSalvar = new ArrayList<Estado>();
		cidade = new Cidade();
		cidadeSelecionada = new Cidade();
		cidades = new ArrayList<Cidade>();
		cidadesSalvar = new ArrayList<Cidade>();
		paisSalvar = new Pais();
		estadoSalvar = new Estado();
		cidadeSalvar = new Cidade();
		genero = new Genero();
		generos =  new ArrayList<Genero>();
		maskDtNascimento = null;
		senhaAntiga = null;
		senhaNova = null;
		senhaRepetida = null;
		endCobrSalvar = new Endereco();
		paisCobSalvar = new Pais();
		estadoCobSalvar = new Estado();
		cidadeCobSalvar = new Cidade();
		paisesCobSalvar = new ArrayList<Pais>();
		estadosCobSalvar = new ArrayList<Estado>();
		cidadesCobSalvar = new ArrayList<Cidade>();
		endPrefSalvar = new Endereco();
		paisPrefSalvar = new Pais();
		estadoPrefSalvar = new Estado();
		cidadePrefSalvar = new Cidade();
		paisesPrefSalvar = new ArrayList<Pais>();
		estadosPrefSalvar = new ArrayList<Estado>();
		cidadesPrefSalvar = new ArrayList<Cidade>();
		
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		
		ExternalContext ec =FacesContext.getCurrentInstance().getExternalContext();
	    this.cliente =  (Cliente)ec.getRequestMap().get("cliente");
	    
	    popularGeneros();
	    carregaTelefones();
	    popularBandeiras();
	    popularPaises();
	    popularPaisesSalvar();
	    popularPaisesCobSalvar();
	    popularPaisesPrefSalvar();
	}
	
	private void popularGeneros() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(genero);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				generos.add(i, (Genero) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void carregaTelefones() {
		for(Telefone tel : cliente.getTelefones()) {
			if(tel.getTipo().getId().equals(EnumTelefone.FIXO.getValue())) {
				numTelFixo =  "(" + tel.getDdd().toString() + ")" + tel.getNumero().toString();
			} else if(tel.getTipo().getId().equals(EnumTelefone.CELULAR.getValue())) {
				numTelCelular =  "(" + tel.getDdd().toString() + ")" + tel.getNumero().toString();
			}
		}
	}
	
	private void popularBandeiras() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(bandeira);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				bandeiras.add(i, (BandeiraCartao) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void popularPaises() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(pais);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				paises.add(i, (Pais) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularEstados() {
		estados.clear();
		cidades.clear();
		estado.setPais(paisSelecionado);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(estado);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				estados.add(i, (Estado) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularCidades() {
		cidades.clear();
		cidade.setEstado(estadoSelecionado);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cidade);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				cidades.add(i, (Cidade) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void popularPaisesSalvar() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(pais);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				paisesSalvar.add(i, (Pais) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularEstadosSalvar() {
		estadosSalvar.clear();
		cidadesSalvar.clear();
		estado.setPais(paisSalvar);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(estado);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				estadosSalvar.add(i, (Estado) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularCidadesSalvar() {
		cidadesSalvar.clear();
		cidade.setEstado(estadoSalvar);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cidade);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				cidadesSalvar.add(i, (Cidade) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void popularPaisesCobSalvar() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(pais);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				paisesCobSalvar.add(i, (Pais) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularEstadosCobSalvar() {
		estadosCobSalvar.clear();
		cidadesCobSalvar.clear();
		estado.setPais(paisCobSalvar);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(estado);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				estadosCobSalvar.add(i, (Estado) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularCidadesCobSalvar() {
		cidadesCobSalvar.clear();
		cidade.setEstado(estadoCobSalvar);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cidade);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				cidadesCobSalvar.add(i, (Cidade) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void popularPaisesPrefSalvar() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(pais);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				paisesPrefSalvar.add(i, (Pais) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularEstadosPrefSalvar() {
		estadosPrefSalvar.clear();
		cidadesPrefSalvar.clear();
		estado.setPais(paisPrefSalvar);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(estado);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				estadosPrefSalvar.add(i, (Estado) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void popularCidadesPrefSalvar() {
		cidadesPrefSalvar.clear();
		cidade.setEstado(estadoPrefSalvar);
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cidade);
		
		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				cidadesPrefSalvar.add(i, (Cidade) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void alterarCliente() {
		cliente.setUsuario(cliente.getEmail());
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date data = format.parse(maskDtNascimento);
			cliente.setDtNascimento(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(cliente);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cliente alterado com sucesso!"));
		}
	}
	
	public void alterarSenha() {
		if(senhaAntiga != null) {
			if(senhaAntiga.equals(cliente.getSenha())) {
				Usuario usuario = new Usuario();
				usuario.setId(cliente.getIdUsuario());
				usuario.setSenha(senhaNova);
				usuario.setUsuario(cliente.getUsuario());
				
				command = commands.get("ALTERAR");
				Resultado rs = command.execute(usuario);
				if (rs.getMsg() != null) {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Senha alterada com sucesso!"));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Senha antiga inválida!"));
			}
		}
	}
	
	public void ativarCliente() {
		if(!cliente.getAtivo()) {
			cliente.setAtivo(true);
			
			command = commands.get("ALTERAR");
			Resultado rs = command.execute(cliente);
			if (rs.getMsg() != null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
			} else {
				// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
				command = commands.get("CONSULTAR");
				rs = command.execute(cliente);
				cliente = (Cliente)rs.getEntidades().get(0);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cliente ativado com sucesso!"));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Erro! Cliente já está ativo!"));
		}
	}
	
	public void inativarCliente() {
		if(cliente.getAtivo()) {
			cliente.setAtivo(false);
			
			command = commands.get("ALTERAR");
			Resultado rs = command.execute(cliente);
			if (rs.getMsg() != null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
			} else {
				// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
				command = commands.get("CONSULTAR");
				rs = command.execute(cliente);
				cliente = (Cliente)rs.getEntidades().get(0);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cliente ativado com sucesso!"));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Erro! Cliente já está inativo!"));
		}
	}
	
	public void cadastrarCartao() {
		cartaoSalvar.setIdCliente(cliente.getId());
		cartaoSalvar.setBandeira(bandeiraSalvar);
		SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
		try {
			Date data = format.parse(maskDtVencimentoSalvar);
			cartaoSalvar.setDtVencimento(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		command = commands.get("SALVAR");
		Resultado rs = command.execute(cartaoSalvar);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			if(cartaoPreferencial) {
				cliente.setCartaoPreferencial((Cartao)rs.getEntidades().get(0));
				command = commands.get("ALTERAR");
				command.execute(cliente);
			}
			// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cartão cadastrado com sucesso!"));
		}
		RequestContext.getCurrentInstance().reset("painelCliente:pnCliente");
	}
	
	public void alterarCartao() {
		cartao.setIdCliente(cliente.getId());
		cartao.setBandeira(bandeira);
		SimpleDateFormat format = new SimpleDateFormat("MM/yyyy");
		try {
			Date data = format.parse(maskDtVencimento);
			cartao.setDtVencimento(data);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(cartao);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			if(cliente.getCartaoPreferencial() != null && cliente.getCartaoPreferencial().getId().equals(cartao.getId()) && !cartaoPrefEdit) {
				cliente.setCartaoPreferencial(null);
				command = commands.get("ALTERAR");
				command.execute(cliente);
			} else if((cliente.getCartaoPreferencial() == null || !cliente.getCartaoPreferencial().getId().equals(cartao.getId())) && cartaoPrefEdit) {
				cliente.setCartaoPreferencial(cartao);
				command = commands.get("ALTERAR");
				command.execute(cliente);
			}
			// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cartão alterado com sucesso!"));
		}
		cartaoPrefEdit = null;
	}
	
	public void excluirCartao(Cartao cartao) {
		RequestContext req = RequestContext.getCurrentInstance();
		if(cliente.getCartaoPreferencial().equals(cartao)) {
			cliente.setCartaoPreferencial(null);
			command = commands.get("ALTERAR");
			command.execute(cliente);
		}
		command = commands.get("EXCLUIR");
		Resultado rs = command.execute(cartao);
		if(rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cartão excluído com sucesso!"));
		}
		req.update("painelCliente:growl");
	}
	
	public void salvarEndereco() {
		TipoEndereco tipo = new TipoEndereco();
		tipo.setId(EnumEndereco.ENTREGA.getValue());
		endSelecionado.setTipo(tipo);
		endSelecionado.setCidade(cidadeSalvar);
		endSelecionado.getCidade().setEstado(estadoSalvar);
		endSelecionado.getCidade().getEstado().setPais(paisSalvar);
		
		command = commands.get("SALVAR");
		Resultado rs = command.execute(endSelecionado);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			if(endPreferencial) {
				cliente.setEndPreferencial((Endereco)rs.getEntidades().get(0));
				command = commands.get("ALTERAR");
				command.execute(cliente);
			}
			// INDEXANDO ENDEREÇO NA TABELA CLIENTE_END
			ClienteEnd cliEnd = new ClienteEnd();
			cliEnd.setIdCliente(cliente.getId());
			cliEnd.setIdEndereco(rs.getEntidades().get(0).getId());
			command = commands.get("SALVAR");
			rs = command.execute(cliEnd);
			// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Endereço salvo com sucesso!"));
		}
	}
	
	public void salvarEndPref() {
		TipoEndereco tipo = new TipoEndereco();
		tipo.setId(EnumEndereco.ENTREGA.getValue());
		endPrefSalvar.setTipo(tipo);
		endPrefSalvar.setCidade(cidadePrefSalvar);
		endPrefSalvar.getCidade().setEstado(estadoPrefSalvar);
		endPrefSalvar.getCidade().getEstado().setPais(paisPrefSalvar);
		
		command = commands.get("SALVAR");
		Resultado rs = command.execute(endPrefSalvar);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			// SALVANDO ID DE END PREFERENCIAL NA TABELA CLIENTE
			cliente.setEndPreferencial((Endereco)rs.getEntidades().get(0));
			command = commands.get("ALTERAR");
			command.execute(cliente);
			// INDEXANDO ENDEREÇO NA TABELA CLIENTE_END
			ClienteEnd cliEnd = new ClienteEnd();
			cliEnd.setIdCliente(cliente.getId());
			cliEnd.setIdEndereco(rs.getEntidades().get(0).getId());
			command = commands.get("SALVAR");
			rs = command.execute(cliEnd);
			// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Endereço salvo com sucesso!"));
		}
	}
	
	public void salvarEndCobranca() {
		TipoEndereco tipo = new TipoEndereco();
		tipo.setId(EnumEndereco.COBRANCA.getValue());
		endCobrSalvar.setTipo(tipo);
		endCobrSalvar.setCidade(cidadeCobSalvar);
		endCobrSalvar.getCidade().setEstado(estadoCobSalvar);
		endCobrSalvar.getCidade().getEstado().setPais(paisCobSalvar);
		
		command = commands.get("SALVAR");
		Resultado rs = command.execute(endCobrSalvar);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			// INDEXANDO ENDEREÇO NA TABELA CLIENTE_END
			ClienteEnd cliEnd = new ClienteEnd();
			cliEnd.setIdCliente(cliente.getId());
			cliEnd.setIdEndereco(rs.getEntidades().get(0).getId());
			rs = command.execute(cliEnd);
			// RECARREGANDO CLIENTE PARA VISUALIZAÇAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Endereço salvo com sucesso!"));
		}
		endCobrSalvar = new Endereco();
	}
	
	public void alterarEndereco() {
		endereco.setCidade(cidadeSelecionada);
		endereco.getCidade().setEstado(estadoSelecionado);
		endereco.getCidade().getEstado().setPais(paisSelecionado);
		
		command = commands.get("ALTERAR");
		Resultado rs = command.execute(endereco);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			if(cliente.getEndPreferencial() != null && endereco.getId().equals(cliente.getEndPreferencial().getId()) && !endPrefEdit) {
				cliente.setEndPreferencial(null);
				command = commands.get("ALTERAR");
				command.execute(cliente);
			} else if((cliente.getEndPreferencial() == null || !endereco.getId().equals(cliente.getEndPreferencial().getId())) && endPrefEdit) {
				cliente.setEndPreferencial(endereco);
				command = commands.get("ALTERAR");
				command.execute(cliente);
			}
			// CARREGANDO CLIENTE PARA VISUALIZACAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Endereço alterado com sucesso!"));
		}
	}
	
	public void excluirEndereco(Endereco end) {
		RequestContext req = RequestContext.getCurrentInstance();
		// DESVINCULANDO ENDEREÇO COM TABELA CLIENTE_END
		ClienteEnd cliEnd = new ClienteEnd();
		cliEnd.setIdCliente(cliente.getId());
		cliEnd.setIdEndereco(end.getId());
		command = commands.get("EXCLUIR");
		Resultado rs = command.execute(cliEnd);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			// REMOVENDO ID_ENDPREFERENCIAL DE CLIENTE, CASO SEJA ENDEREÇO PREFERENCIAL
			if(cliente.getEndPreferencial() != null && cliente.getEndPreferencial().getId().equals(end.getId())) {
				cliente.setEndPreferencial(null);
				command = commands.get("ALTERAR");
				command.execute(cliente);
			}
			// EXCLUINDO ENDEREÇO
			command = commands.get("EXCLUIR");
			rs = command.execute(end);
			// CARREGANDO CLIENTE PARA VISUALIZACAO
			command = commands.get("CONSULTAR");
			rs = command.execute(cliente);
			cliente = (Cliente)rs.getEntidades().get(0);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Endereço excluído com sucesso!"));
		}
		req.update("painelCliente:growl");
	}
	
	// GETTERS E SETTERS
	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public String getNumTelFixo() {
		return numTelFixo;
	}

	public void setNumTelFixo(String numTelFixo) {
		this.numTelFixo = numTelFixo;
	}

	public String getNumTelCelular() {
		return numTelCelular;
	}

	public void setNumTelCelular(String numTelCelular) {
		this.numTelCelular = numTelCelular;
	}

	public Cartao getCartao() {
		return cartao;
	}

	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}

	public Cartao getCartaoSelecionado() {
		return cartaoSelecionado;
	}

	public void setCartaoSelecionado(Cartao cartaoSelecionado) {
		this.cartaoSelecionado = cartaoSelecionado;
	}

	public List<Cartao> getCartoes() {
		return cartoes;
	}

	public void setCartoes(List<Cartao> cartoes) {
		this.cartoes = cartoes;
	}

	public BandeiraCartao getBandeira() {
		return bandeira;
	}

	public void setBandeira(BandeiraCartao bandeira) {
		this.bandeira = bandeira;
	}

	public List<BandeiraCartao> getBandeiras() {
		return bandeiras;
	}

	public void setBandeiras(List<BandeiraCartao> bandeiras) {
		this.bandeiras = bandeiras;
	}

	public String getMaskDtVencimento() {
		return maskDtVencimento;
	}

	public void setMaskDtVencimento(String maskDtVencimento) {
		this.maskDtVencimento = maskDtVencimento;
	}

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		PainelClienteMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Pais getPais() {
		return pais;
	}

	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public Pais getPaisSelecionado() {
		return paisSelecionado;
	}

	public void setPaisSelecionado(Pais paisSelecionado) {
		this.paisSelecionado = paisSelecionado;
	}

	public List<Pais> getPaises() {
		return paises;
	}

	public void setPaises(List<Pais> paises) {
		this.paises = paises;
	}

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	public Estado getEstadoSelecionado() {
		return estadoSelecionado;
	}

	public void setEstadoSelecionado(Estado estadoSelecionado) {
		this.estadoSelecionado = estadoSelecionado;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Cidade getCidadeSelecionada() {
		return cidadeSelecionada;
	}

	public void setCidadeSelecionada(Cidade cidadeSelecionada) {
		this.cidadeSelecionada = cidadeSelecionada;
	}

	public List<Cidade> getCidades() {
		return cidades;
	}

	public void setCidades(List<Cidade> cidades) {
		this.cidades = cidades;
	}

	public Endereco getEndSelecionado() {
		return endSelecionado;
	}

	public void setEndSelecionado(Endereco endSelecionado) {
		this.endSelecionado = endSelecionado;
	}

	public Pais getPaisSalvar() {
		return paisSalvar;
	}

	public void setPaisSalvar(Pais paisSalvar) {
		this.paisSalvar = paisSalvar;
	}

	public Estado getEstadoSalvar() {
		return estadoSalvar;
	}

	public void setEstadoSalvar(Estado estadoSalvar) {
		this.estadoSalvar = estadoSalvar;
	}

	public Cidade getCidadeSalvar() {
		return cidadeSalvar;
	}

	public void setCidadeSalvar(Cidade cidadeSalvar) {
		this.cidadeSalvar = cidadeSalvar;
	}

	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public String getMaskDtNascimento() {
		return maskDtNascimento;
	}

	public void setMaskDtNascimento(String maskDtNascimento) {
		this.maskDtNascimento = maskDtNascimento;
	}

	public String getSenhaAntiga() {
		return senhaAntiga;
	}

	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}

	public String getSenhaNova() {
		return senhaNova;
	}

	public void setSenhaNova(String senhaNova) {
		this.senhaNova = senhaNova;
	}

	public String getSenhaRepetida() {
		return senhaRepetida;
	}

	public void setSenhaRepetida(String senhaRepetida) {
		this.senhaRepetida = senhaRepetida;
	}

	public Cartao getCartaoSalvar() {
		return cartaoSalvar;
	}

	public void setCartaoSalvar(Cartao cartaoSalvar) {
		this.cartaoSalvar = cartaoSalvar;
	}

	public BandeiraCartao getBandeiraSalvar() {
		return bandeiraSalvar;
	}

	public void setBandeiraSalvar(BandeiraCartao bandeiraSalvar) {
		this.bandeiraSalvar = bandeiraSalvar;
	}

	public String getMaskDtVencimentoSalvar() {
		return maskDtVencimentoSalvar;
	}

	public void setMaskDtVencimentoSalvar(String maskDtVencimentoSalvar) {
		this.maskDtVencimentoSalvar = maskDtVencimentoSalvar;
	}

	public List<Pais> getPaisesSalvar() {
		return paisesSalvar;
	}

	public void setPaisesSalvar(List<Pais> paisesSalvar) {
		this.paisesSalvar = paisesSalvar;
	}

	public List<Estado> getEstadosSalvar() {
		return estadosSalvar;
	}

	public void setEstadosSalvar(List<Estado> estadosSalvar) {
		this.estadosSalvar = estadosSalvar;
	}

	public List<Cidade> getCidadesSalvar() {
		return cidadesSalvar;
	}

	public void setCidadesSalvar(List<Cidade> cidadesSalvar) {
		this.cidadesSalvar = cidadesSalvar;
	}

	public Boolean getCartaoPreferencial() {
		return cartaoPreferencial;
	}

	public void setCartaoPreferencial(Boolean cartaoPreferencial) {
		this.cartaoPreferencial = cartaoPreferencial;
	}

	public Boolean getEndPreferencial() {
		return endPreferencial;
	}

	public void setEndPreferencial(Boolean endPreferencial) {
		this.endPreferencial = endPreferencial;
	}

	public Boolean getCartaoPrefEdit() {
		return cartaoPrefEdit;
	}

	public void setCartaoPrefEdit(Boolean cartaoPrefEdit) {
		this.cartaoPrefEdit = cartaoPrefEdit;
	}

	public Boolean getEndPrefEdit() {
		return endPrefEdit;
	}

	public void setEndPrefEdit(Boolean endPrefEdit) {
		this.endPrefEdit = endPrefEdit;
	}

	public Endereco getEndCobrSalvar() {
		return endCobrSalvar;
	}

	public void setEndCobrSalvar(Endereco endCobrSalvar) {
		this.endCobrSalvar = endCobrSalvar;
	}

	public Pais getPaisCobSalvar() {
		return paisCobSalvar;
	}

	public void setPaisCobSalvar(Pais paisCobSalvar) {
		this.paisCobSalvar = paisCobSalvar;
	}

	public List<Pais> getPaisesCobSalvar() {
		return paisesCobSalvar;
	}

	public void setPaisesCobSalvar(List<Pais> paisesCobSalvar) {
		this.paisesCobSalvar = paisesCobSalvar;
	}

	public Estado getEstadoCobSalvar() {
		return estadoCobSalvar;
	}

	public void setEstadoCobSalvar(Estado estadoCobSalvar) {
		this.estadoCobSalvar = estadoCobSalvar;
	}

	public List<Estado> getEstadosCobSalvar() {
		return estadosCobSalvar;
	}

	public void setEstadosCobSalvar(List<Estado> estadosCobSalvar) {
		this.estadosCobSalvar = estadosCobSalvar;
	}

	public Cidade getCidadeCobSalvar() {
		return cidadeCobSalvar;
	}

	public void setCidadeCobSalvar(Cidade cidadeCobSalvar) {
		this.cidadeCobSalvar = cidadeCobSalvar;
	}

	public List<Cidade> getCidadesCobSalvar() {
		return cidadesCobSalvar;
	}

	public void setCidadesCobSalvar(List<Cidade> cidadesCobSalvar) {
		this.cidadesCobSalvar = cidadesCobSalvar;
	}

	public Endereco getEndPrefSalvar() {
		return endPrefSalvar;
	}

	public void setEndPrefSalvar(Endereco endPrefSalvar) {
		this.endPrefSalvar = endPrefSalvar;
	}

	public Pais getPaisPrefSalvar() {
		return paisPrefSalvar;
	}

	public void setPaisPrefSalvar(Pais paisPrefSalvar) {
		this.paisPrefSalvar = paisPrefSalvar;
	}

	public List<Pais> getPaisesPrefSalvar() {
		return paisesPrefSalvar;
	}

	public void setPaisesPrefSalvar(List<Pais> paisesPrefSalvar) {
		this.paisesPrefSalvar = paisesPrefSalvar;
	}

	public Estado getEstadoPrefSalvar() {
		return estadoPrefSalvar;
	}

	public void setEstadoPrefSalvar(Estado estadoPrefSalvar) {
		this.estadoPrefSalvar = estadoPrefSalvar;
	}

	public List<Estado> getEstadosPrefSalvar() {
		return estadosPrefSalvar;
	}

	public void setEstadosPrefSalvar(List<Estado> estadosPrefSalvar) {
		this.estadosPrefSalvar = estadosPrefSalvar;
	}

	public Cidade getCidadePrefSalvar() {
		return cidadePrefSalvar;
	}

	public void setCidadePrefSalvar(Cidade cidadePrefSalvar) {
		this.cidadePrefSalvar = cidadePrefSalvar;
	}

	public List<Cidade> getCidadesPrefSalvar() {
		return cidadesPrefSalvar;
	}

	public void setCidadesPrefSalvar(List<Cidade> cidadesPrefSalvar) {
		this.cidadesPrefSalvar = cidadesPrefSalvar;
	}
	
}
