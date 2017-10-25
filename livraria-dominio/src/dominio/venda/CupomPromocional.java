package dominio.venda;

import dominio.EntidadeDominio;

public class CupomPromocional extends Cupom {

	private Double porcentagemDesconto;

	public Double getPorcentagemDesconto() {
		return porcentagemDesconto;
	}

	public void setPorcentagemDesconto(Double porcentagemDesconto) {
		this.porcentagemDesconto = porcentagemDesconto;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((porcentagemDesconto == null) ? 0 : porcentagemDesconto.hashCode());
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
		CupomPromocional other = (CupomPromocional) obj;
		if (porcentagemDesconto == null) {
			if (other.porcentagemDesconto != null)
				return false;
		} else if (!porcentagemDesconto.equals(other.porcentagemDesconto))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CupomPromocional [porcentagemDesconto=" + porcentagemDesconto + "]";
	}
	
}
