package dominio.cliente;

import java.util.List;

import dominio.Pessoa;
import dominio.Telefone;
import dominio.endereco.Endereco;

public class Cliente extends Pessoa {
	
	private Integer idUsuario;
	private String email;
	private Boolean ativo;
	private List<Endereco> endsEntrega;
	private Endereco endCobranca;
	private Endereco endPreferencial;
	private List<Cartao> cartoes;
	private Cartao cartaoPreferencial;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public List<Endereco> getEndsEntrega() {
		return endsEntrega;
	}
	public void setEndsEntrega(List<Endereco> endsEntrega) {
		this.endsEntrega = endsEntrega;
	}
	public Endereco getEndCobranca() {
		return endCobranca;
	}
	public void setEndCobranca(Endereco endCobranca) {
		this.endCobranca = endCobranca;
	}
	public List<Cartao> getCartoes() {
		return cartoes;
	}
	public void setCartoes(List<Cartao> cartoes) {
		this.cartoes = cartoes;
	}
	public Endereco getEndPreferencial() {
		return endPreferencial;
	}
	public void setEndPreferencial(Endereco endPreferencial) {
		this.endPreferencial = endPreferencial;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
	public Cartao getCartaoPreferencial() {
		return cartaoPreferencial;
	}
	public void setCartaoPreferencial(Cartao cartaoPreferencial) {
		this.cartaoPreferencial = cartaoPreferencial;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
		result = prime * result + ((cartaoPreferencial == null) ? 0 : cartaoPreferencial.hashCode());
		result = prime * result + ((cartoes == null) ? 0 : cartoes.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((endCobranca == null) ? 0 : endCobranca.hashCode());
		result = prime * result + ((endPreferencial == null) ? 0 : endPreferencial.hashCode());
		result = prime * result + ((endsEntrega == null) ? 0 : endsEntrega.hashCode());
		result = prime * result + ((idUsuario == null) ? 0 : idUsuario.hashCode());
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
		Cliente other = (Cliente) obj;
		if (ativo == null) {
			if (other.ativo != null)
				return false;
		} else if (!ativo.equals(other.ativo))
			return false;
		if (cartaoPreferencial == null) {
			if (other.cartaoPreferencial != null)
				return false;
		} else if (!cartaoPreferencial.equals(other.cartaoPreferencial))
			return false;
		if (cartoes == null) {
			if (other.cartoes != null)
				return false;
		} else if (!cartoes.equals(other.cartoes))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (endCobranca == null) {
			if (other.endCobranca != null)
				return false;
		} else if (!endCobranca.equals(other.endCobranca))
			return false;
		if (endPreferencial == null) {
			if (other.endPreferencial != null)
				return false;
		} else if (!endPreferencial.equals(other.endPreferencial))
			return false;
		if (endsEntrega == null) {
			if (other.endsEntrega != null)
				return false;
		} else if (!endsEntrega.equals(other.endsEntrega))
			return false;
		if (idUsuario == null) {
			if (other.idUsuario != null)
				return false;
		} else if (!idUsuario.equals(other.idUsuario))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Cliente [idUsuario=" + idUsuario + ", email=" + email + ", ativo=" + ativo + ", endsEntrega="
				+ endsEntrega + ", endCobranca=" + endCobranca + ", endPreferencial=" + endPreferencial + ", cartoes="
				+ cartoes + ", cartaoPreferencial=" + cartaoPreferencial + "]";
	}
	
}
