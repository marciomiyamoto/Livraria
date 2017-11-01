package dominio.venda;

import dominio.EntidadeDominio;

public class Pagamento extends EntidadeDominio {

	private double valor;
	private Integer status;
	private FormaPgto formaPgto;
	
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public FormaPgto getFormaPgto() {
		return formaPgto;
	}
	public void setFormaPgto(FormaPgto formaPgto) {
		this.formaPgto = formaPgto;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((formaPgto == null) ? 0 : formaPgto.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		long temp;
		temp = Double.doubleToLongBits(valor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Pagamento other = (Pagamento) obj;
		if (formaPgto == null) {
			if (other.formaPgto != null)
				return false;
		} else if (!formaPgto.equals(other.formaPgto))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (Double.doubleToLongBits(valor) != Double.doubleToLongBits(other.valor))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Pagamento [valor=" + valor + ", status=" + status + ", formaPgto=" + formaPgto + "]";
	}
	
}
