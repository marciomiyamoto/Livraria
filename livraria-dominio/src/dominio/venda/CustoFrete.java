package dominio.venda;

public class CustoFrete {

	public double valor;
	public Integer prazoEntrega;
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Integer getPrazoEntrega() {
		return prazoEntrega;
	}
	public void setPrazoEntrega(Integer prazoEntrega) {
		this.prazoEntrega = prazoEntrega;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((prazoEntrega == null) ? 0 : prazoEntrega.hashCode());
		long temp;
		temp = Double.doubleToLongBits(valor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		CustoFrete other = (CustoFrete) obj;
		if (prazoEntrega == null) {
			if (other.prazoEntrega != null)
				return false;
		} else if (!prazoEntrega.equals(other.prazoEntrega))
			return false;
		if (Double.doubleToLongBits(valor) != Double.doubleToLongBits(other.valor))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "CustoFrete [valor=" + valor + ", prazoEntrega=" + prazoEntrega + "]";
	}
	
}
