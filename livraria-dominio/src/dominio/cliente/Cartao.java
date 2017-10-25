package dominio.cliente;

import java.util.Date;

import dominio.EntidadeDominio;

public class Cartao extends EntidadeDominio {

	private Long numero;
	private String nomeImpresso;
	private Integer codSeguranca;
	private Date dtVencimento;
	private BandeiraCartao bandeira;
	private Integer idCliente;
	
	public Long getNumero() {
		return numero;
	}
	public void setNumero(Long numero) {
		this.numero = numero;
	}
	public String getNomeImpresso() {
		return nomeImpresso;
	}
	public void setNomeImpresso(String nomeImpresso) {
		this.nomeImpresso = nomeImpresso;
	}
	public Integer getCodSeguranca() {
		return codSeguranca;
	}
	public void setCodSeguranca(Integer codSeguranca) {
		this.codSeguranca = codSeguranca;
	}
	public Date getDtVencimento() {
		return dtVencimento;
	}
	public void setDtVencimento(Date dtVencimento) {
		this.dtVencimento = dtVencimento;
	}
	public BandeiraCartao getBandeira() {
		return bandeira;
	}
	public void setBandeira(BandeiraCartao bandeira) {
		this.bandeira = bandeira;
	}
	public Integer getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((bandeira == null) ? 0 : bandeira.hashCode());
		result = prime * result + ((codSeguranca == null) ? 0 : codSeguranca.hashCode());
		result = prime * result + ((dtVencimento == null) ? 0 : dtVencimento.hashCode());
		result = prime * result + ((idCliente == null) ? 0 : idCliente.hashCode());
		result = prime * result + ((nomeImpresso == null) ? 0 : nomeImpresso.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cartao other = (Cartao) obj;
		if (bandeira == null) {
			if (other.bandeira != null)
				return false;
		} else if (!bandeira.equals(other.bandeira))
			return false;
		if (codSeguranca == null) {
			if (other.codSeguranca != null)
				return false;
		} else if (!codSeguranca.equals(other.codSeguranca))
			return false;
		if (dtVencimento == null) {
			if (other.dtVencimento != null)
				return false;
		} else if (!dtVencimento.equals(other.dtVencimento))
			return false;
		if (idCliente == null) {
			if (other.idCliente != null)
				return false;
		} else if (!idCliente.equals(other.idCliente))
			return false;
		if (nomeImpresso == null) {
			if (other.nomeImpresso != null)
				return false;
		} else if (!nomeImpresso.equals(other.nomeImpresso))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Cartao [numero=" + numero + ", nomeImpresso=" + nomeImpresso + ", codSeguranca=" + codSeguranca
				+ ", dtVencimento=" + dtVencimento + ", bandeira=" + bandeira + ", idCliente=" + idCliente + "]";
	}
	
}
