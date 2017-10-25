package dominio;

import java.util.Date;

public class EntidadeDominio implements IEntidade {
	
	private Integer id;
	private Date dtCadastro;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getDtCadastro() {
		return dtCadastro;
	}
	public void setDtCadastro(Date dtCadastro) {
		this.dtCadastro = dtCadastro;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dtCadastro == null) ? 0 : dtCadastro.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EntidadeDominio other = (EntidadeDominio) obj;
		if (dtCadastro == null) {
			if (other.dtCadastro != null)
				return false;
		} else if (!dtCadastro.equals(other.dtCadastro))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "EntidadeDominio [id=" + id + ", dtCadastro=" + dtCadastro + "]";
	}
	
}
