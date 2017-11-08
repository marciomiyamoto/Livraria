package dominio.venda;

import dominio.EntidadeDominio;

public class CustoFrete extends EntidadeDominio{

	private Double valor;
	private Integer prazoEntrega;
	private String erro;
	private Frete frete;
	
	public Double getValor() {
		return valor;
	}
	public void setValor(Double valor) {
		this.valor = valor;
	}
	public Integer getPrazoEntrega() {
		return prazoEntrega;
	}
	public void setPrazoEntrega(Integer prazoEntrega) {
		this.prazoEntrega = prazoEntrega;
	}
	public String getErro() {
		return erro;
	}
	public void setErro(String erro) {
		this.erro = erro;
	}
	public Frete getFrete() {
		return frete;
	}
	public void setFrete(Frete frete) {
		this.frete = frete;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((erro == null) ? 0 : erro.hashCode());
		result = prime * result + ((frete == null) ? 0 : frete.hashCode());
		result = prime * result + ((prazoEntrega == null) ? 0 : prazoEntrega.hashCode());
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
		CustoFrete other = (CustoFrete) obj;
		if (erro == null) {
			if (other.erro != null)
				return false;
		} else if (!erro.equals(other.erro))
			return false;
		if (frete == null) {
			if (other.frete != null)
				return false;
		} else if (!frete.equals(other.frete))
			return false;
		if (prazoEntrega == null) {
			if (other.prazoEntrega != null)
				return false;
		} else if (!prazoEntrega.equals(other.prazoEntrega))
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
		return "CustoFrete [valor=" + valor + ", prazoEntrega=" + prazoEntrega + ", erro=" + erro + ", frete=" + frete
				+ "]";
	}
	
}
