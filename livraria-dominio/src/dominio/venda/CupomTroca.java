package dominio.venda;

import dominio.EntidadeDominio;

public class CupomTroca extends Cupom{
	
	private Double valor;
	private Integer idCliente;
	
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
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
		result = prime * result + ((idCliente == null) ? 0 : idCliente.hashCode());
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
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
		CupomTroca other = (CupomTroca) obj;
		if (idCliente == null) {
			if (other.idCliente != null)
				return false;
		} else if (!idCliente.equals(other.idCliente))
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CupomTroca [valor=" + valor + ", idCliente=" + idCliente + "]";
	}
	
}
