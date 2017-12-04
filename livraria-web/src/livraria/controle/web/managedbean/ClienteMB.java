package livraria.controle.web.managedbean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import dominio.EnumTelefone;
import dominio.Genero;
import dominio.Telefone;
import dominio.TipoTelefone;
import dominio.cliente.BandeiraCartao;
import dominio.cliente.Cliente;
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
public class ClienteMB {

	private Genero genero;
	private Genero generoSelecionado;
	private List<Genero> generos;
	private TipoTelefone tipoTelefone;
	private List<TipoTelefone> tiposTelefone;
	private BandeiraCartao bandeira;
	private BandeiraCartao bandeiraSelecionada;
	private List<BandeiraCartao> bandeiras;
	private Pais pais;
	private Pais paisSelecionado;
	private List<Pais> paises;
	private Estado estado;
	private Estado estadoSelecionado;
	private List<Estado> estados;
	private Cidade cidade;
	private Cidade cidadeSelecionada;
	private List<Cidade> cidades;
	private Cliente cliente;
	private Cliente clienteSelecionado;
	private List<Cliente> clientes;
	private Endereco endereco;
	private Endereco endCobranca;
	private Endereco endEntrega;
	private Endereco endPreferencial;
	private Endereco enderecoSelecionado;
	private List<Endereco> enderecos;
	private Boolean flgEndEntrega;
	private Boolean flgEndCobranca;
	private Boolean flgEndPreferencial;
	private Telefone telefone;
	private Telefone telefoneSelecionado;
	private List<Telefone> telefones;
	private TipoEndereco tipoEndereco;
	private TipoEndereco tipoEnderecoSelecionado;
	private List<TipoEndereco> tiposEndereco;
	private String maskDtNascimento;
	private Telefone telFixo;
	private Telefone telCelular;
	private static Map<String, ICommand> commands;
	private ICommand command;

	@PostConstruct
	public void init() {
		genero = new Genero();
		generoSelecionado = new Genero();
		generos = new ArrayList<Genero>();
		tipoTelefone = new TipoTelefone();
		tiposTelefone = new ArrayList<TipoTelefone>();
		bandeira = new BandeiraCartao();
		bandeiraSelecionada = new BandeiraCartao();
		bandeiras = new ArrayList<BandeiraCartao>();
		pais = new Pais();
		paisSelecionado = new Pais();
		paises = new ArrayList<Pais>();
		estado = new Estado();
		estadoSelecionado = new Estado();
		estados = new ArrayList<Estado>();
		cidade = new Cidade();
		cidadeSelecionada = new Cidade();
		cidades = new ArrayList<Cidade>();
		cliente = new Cliente();
		clienteSelecionado = new Cliente();
		clientes = new ArrayList<Cliente>();
		endereco = new Endereco();
		enderecoSelecionado = new Endereco();
		endCobranca = new Endereco();
		endEntrega = new Endereco();
		enderecos = new ArrayList<Endereco>();
		telefone = new Telefone();
		telefoneSelecionado = new Telefone();
		telefones = new ArrayList<Telefone>();
		tipoEndereco = new TipoEndereco();
		tipoEnderecoSelecionado = new TipoEndereco();
		tiposEndereco = new ArrayList<TipoEndereco>();
		maskDtNascimento = null;
		telFixo = new Telefone();
		telCelular = new Telefone();
		commands = new HashMap<String, ICommand>();
		commands.put("SALVAR", new SalvarCommand());
		commands.put("EXCLUIR", new ExcluirCommand());
		commands.put("CONSULTAR", new ConsultarCommand());
		commands.put("VISUALIZAR", new VisualizarCommand());
		commands.put("ALTERAR", new AlterarCommand());
		popularGeneros();
		popularTiposTelefone();
		popularBandeirasCartao();
		popularPaises();
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

	private void popularTiposTelefone() {
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(tipoTelefone);

		for (int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				tiposTelefone.add(i, (TipoTelefone) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void popularBandeirasCartao() {
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
	
	public void validaSenha() {
		String padrao = "(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,}";
		if(cliente.getSenha().matches(padrao)) {
			if(cliente.getSenha().equals(cliente.getSenhaRepetida())) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Senha válida!"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Senha inválida! A senha deve ser igual nos dois campos"));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", " Senha inválida! A senha "
							+ " deve ser composta de pelo menos 8 caracteres, ter letras maiúsculas e minúsculas"
							+ " além de conter um dos seguintes caracteres especiais: !@#$%^&+=."));
		}
	}

	public void cadastrarCliente() {
		cliente.setUsuario(cliente.getEmail());
		cliente.setGenero(generoSelecionado);
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
		if(maskDtNascimento != null && maskDtNascimento != "") {
			try {
				Date data = format.parse(maskDtNascimento);
				cliente.setDtNascimento(data);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		// COMPONDO LISTA DE TELEFONES
		cliente.setTelefones(null);
		telefones = new ArrayList<Telefone>();
		TipoTelefone tipoFixo = new TipoTelefone();
		TipoTelefone tipoCel = new TipoTelefone();
		tipoFixo.setId(EnumTelefone.FIXO.getValue());
		tipoCel.setId(EnumTelefone.CELULAR.getValue());
		telFixo.setTipo(tipoFixo);
		telefones.add(telFixo);
		telCelular.setTipo(tipoCel);
		telefones.add(telCelular);
		cliente.setTelefones(telefones);
		// COMPONDO LISTA DE ENDEREÇOS
		cliente.setEndCobranca(null);
		cliente.setEnderecoResidencial(null);
		cliente.setEndPreferencial(null);
		cliente.setEndsEntrega(null);
		endCobranca = new Endereco();
		endEntrega = new Endereco();
		Endereco endEntrega = new Endereco();
		List<Endereco> ends = new ArrayList<Endereco>();
		endPreferencial = new Endereco();
		endereco.setCidade(cidadeSelecionada);
		endereco.getCidade().setEstado(estadoSelecionado);
		endereco.getCidade().getEstado().setPais(paisSelecionado);
		TipoEndereco tipoResidencial = new TipoEndereco();
		tipoResidencial.setId(EnumEndereco.RESIDENCIAL.getValue());
		cliente.setEnderecoResidencial(endereco);
		cliente.getEnderecoResidencial().setTipo(tipoResidencial);
		TipoEndereco tipoCobranca = new TipoEndereco();
		tipoCobranca.setId(EnumEndereco.COBRANCA.getValue());
		endCobranca.setBairro(endereco.getBairro());
		endCobranca.setCep(endereco.getCep());
		endCobranca.setCidade(endereco.getCidade());
		endCobranca.setLogradouro(endereco.getLogradouro());
		endCobranca.setNumero(endereco.getNumero());
		endCobranca.setObservacoes(endereco.getObservacoes());
		endCobranca.setTipoLogradouro(endereco.getTipoLogradouro());
		endCobranca.setTipoResidencia(endereco.getTipoResidencia());
		endCobranca.setTitulo(endereco.getTitulo());
		endCobranca.setTipo(tipoCobranca);
		cliente.setEndCobranca(endCobranca);
		TipoEndereco tipoEntrega = new TipoEndereco();
		tipoEntrega.setId(EnumEndereco.ENTREGA.getValue());
		endEntrega.setBairro(endereco.getBairro());
		endEntrega.setCep(endereco.getCep());
		endEntrega.setCidade(endereco.getCidade());
		endEntrega.setLogradouro(endereco.getLogradouro());
		endEntrega.setNumero(endereco.getNumero());
		endEntrega.setObservacoes(endereco.getObservacoes());
		endEntrega.setTipoLogradouro(endereco.getTipoLogradouro());
		endEntrega.setTipoResidencia(endereco.getTipoResidencia());
		endEntrega.setTitulo(endereco.getTitulo());
		endEntrega.setTipo(tipoEntrega);
		ends.add(endEntrega);
		cliente.setEndsEntrega(ends);
		
		command = commands.get("SALVAR");
		Resultado rs = command.execute(cliente);
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Cliente cadastrado com sucesso!"));
		}
	}
	
	public void buscarClientes() {
		cliente.setGenero(generoSelecionado);
		if(maskDtNascimento != null && !maskDtNascimento.equals("")) {
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			try {
				Date data = format.parse(maskDtNascimento);
				cliente.setDtNascimento(data);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		cliente.setTelefones(null);
		telefones = new ArrayList<Telefone>();
		TipoTelefone tipoFixo = new TipoTelefone();
		TipoTelefone tipoCel = new TipoTelefone();
		tipoFixo.setId(EnumTelefone.FIXO.getValue());
		tipoCel.setId(EnumTelefone.CELULAR.getValue());
		if((telFixo.getNumero() != null && telFixo.getNumero() != 0) || (telFixo.getDdd() != null && telFixo.getDdd() != 0)) {
			telFixo.setTipo(tipoFixo);
			telefones.add(telFixo);
		}
		if((telCelular.getNumero() != null && telCelular.getNumero() != 0) || (telCelular.getDdd() != null && telCelular.getDdd() != 0)) {
			telCelular.setTipo(tipoCel);
			telefones.add(telCelular);
		}
		cliente.setTelefones(telefones);
		
		command = commands.get("CONSULTAR");
		Resultado rs = command.execute(cliente);
		// PREENCHENDO LISTA DE CLIENTES
		for(int i = 0; i < rs.getEntidades().size(); i++) {
			try {
				clientes.add(i, (Cliente) rs.getEntidades().get(i));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (rs.getMsg() != null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", rs.getMsg()));
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "", "Nenhum cliente encontrado"));
		}
		
	}
	
	public String visualizarCliente() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.getRequestMap().put("cliente", clienteSelecionado);
        return "painel-cliente.xhtml";
	}
	

	// GETTERS E SETTERS
	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public Genero getGeneroSelecionado() {
		return generoSelecionado;
	}

	public void setGeneroSelecionado(Genero generoSelecionado) {
		this.generoSelecionado = generoSelecionado;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public TipoTelefone getTipoTelefone() {
		return tipoTelefone;
	}

	public void setTipoTelefone(TipoTelefone tipoTelefone) {
		this.tipoTelefone = tipoTelefone;
	}

	public List<TipoTelefone> getTiposTelefone() {
		return tiposTelefone;
	}

	public void setTiposTelefone(List<TipoTelefone> tiposTelefone) {
		this.tiposTelefone = tiposTelefone;
	}

	public BandeiraCartao getBandeira() {
		return bandeira;
	}

	public void setBandeira(BandeiraCartao bandeira) {
		this.bandeira = bandeira;
	}

	public BandeiraCartao getBandeiraSelecionada() {
		return bandeiraSelecionada;
	}

	public void setBandeiraSelecionada(BandeiraCartao bandeiraSelecionada) {
		this.bandeiraSelecionada = bandeiraSelecionada;
	}

	public List<BandeiraCartao> getBandeiras() {
		return bandeiras;
	}

	public void setBandeiras(List<BandeiraCartao> bandeiras) {
		this.bandeiras = bandeiras;
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

	public static Map<String, ICommand> getCommands() {
		return commands;
	}

	public static void setCommands(Map<String, ICommand> commands) {
		ClienteMB.commands = commands;
	}

	public ICommand getCommand() {
		return command;
	}

	public void setCommand(ICommand command) {
		this.command = command;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Cliente getClienteSelecionado() {
		return clienteSelecionado;
	}

	public void setClienteSelecionado(Cliente clienteSelecionado) {
		this.clienteSelecionado = clienteSelecionado;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Endereco getEnderecoSelecionado() {
		return enderecoSelecionado;
	}

	public void setEnderecoSelecionado(Endereco enderecoSelecionado) {
		this.enderecoSelecionado = enderecoSelecionado;
	}

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}

	public Telefone getTelefone() {
		return telefone;
	}

	public void setTelefone(Telefone telefone) {
		this.telefone = telefone;
	}

	public Telefone getTelefoneSelecionado() {
		return telefoneSelecionado;
	}

	public void setTelefoneSelecionado(Telefone telefoneSelecionado) {
		this.telefoneSelecionado = telefoneSelecionado;
	}

	public List<Telefone> getTelefones() {
		return telefones;
	}

	public void setTelefones(List<Telefone> telefones) {
		this.telefones = telefones;
	}

	public TipoEndereco getTipoEndereco() {
		return tipoEndereco;
	}

	public void setTipoEndereco(TipoEndereco tipoEndereco) {
		this.tipoEndereco = tipoEndereco;
	}

	public TipoEndereco getTipoEnderecoSelecionado() {
		return tipoEnderecoSelecionado;
	}

	public void setTipoEnderecoSelecionado(TipoEndereco tipoEnderecoSelecionado) {
		this.tipoEnderecoSelecionado = tipoEnderecoSelecionado;
	}

	public List<TipoEndereco> getTiposEndereco() {
		return tiposEndereco;
	}

	public void setTiposEndereco(List<TipoEndereco> tiposEndereco) {
		this.tiposEndereco = tiposEndereco;
	}

	public String getMaskDtNascimento() {
		return maskDtNascimento;
	}

	public void setMaskDtNascimento(String maskDtNascimento) {
		this.maskDtNascimento = maskDtNascimento;
	}

	public Endereco getEndCobranca() {
		return endCobranca;
	}

	public void setEndCobranca(Endereco endCobranca) {
		this.endCobranca = endCobranca;
	}

	public Boolean getFlgEndCobranca() {
		return flgEndCobranca;
	}

	public void setFlgEndCobranca(Boolean flgEndCobranca) {
		this.flgEndCobranca = flgEndCobranca;
	}

	public Endereco getEndEntrega() {
		return endEntrega;
	}

	public void setEndEntrega(Endereco endEntrega) {
		this.endEntrega = endEntrega;
	}

	public Boolean getFlgEndEntrega() {
		return flgEndEntrega;
	}

	public void setFlgEndEntrega(Boolean flgEndEntrega) {
		this.flgEndEntrega = flgEndEntrega;
	}

	public Telefone getTelFixo() {
		return telFixo;
	}

	public void setTelFixo(Telefone telFixo) {
		this.telFixo = telFixo;
	}

	public Telefone getTelCelular() {
		return telCelular;
	}

	public void setTelCelular(Telefone telCelular) {
		this.telCelular = telCelular;
	}

	public Endereco getEndPreferencial() {
		return endPreferencial;
	}

	public void setEndPreferencial(Endereco endPreferencial) {
		this.endPreferencial = endPreferencial;
	}

	public Boolean getFlgEndPreferencial() {
		return flgEndPreferencial;
	}

	public void setFlgEndPreferencial(Boolean flgEndPreferencial) {
		this.flgEndPreferencial = flgEndPreferencial;
	}
	
}
