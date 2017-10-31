package dominio.livro;

import dominio.EntidadeDominio;

public class Registro extends EntidadeDominio {
	private Integer qtde;
	private Double valorCompra;
	private Double valorVenda;
	private Integer idEstoque;
	private Integer tipoRegistro;
	
	public Integer getQtde() {
		return qtde;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public Double getValorCompra() {
		return valorCompra;
	}
	public void setValorCompra(Double valorCompra) {
		this.valorCompra = valorCompra;
	}
	public Double getValorVenda() {
		return valorVenda;
	}
	public void setValorVenda(Double valorVenda) {
		this.valorVenda = valorVenda;
	}
	public Integer getIdEstoque() {
		return idEstoque;
	}
	public void setIdEstoque(Integer idEstoque) {
		this.idEstoque = idEstoque;
	}
	public Integer getTipoRegistro() {
		return tipoRegistro;
	}
	public void setTipoRegistro(Integer tipoRegistro) {
		this.tipoRegistro = tipoRegistro;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((idEstoque == null) ? 0 : idEstoque.hashCode());
		result = prime * result + ((qtde == null) ? 0 : qtde.hashCode());
		result = prime * result + ((tipoRegistro == null) ? 0 : tipoRegistro.hashCode());
		result = prime * result + ((valorCompra == null) ? 0 : valorCompra.hashCode());
		result = prime * result + ((valorVenda == null) ? 0 : valorVenda.hashCode());
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
		Registro other = (Registro) obj;
		if (idEstoque == null) {
			if (other.idEstoque != null)
				return false;
		} else if (!idEstoque.equals(other.idEstoque))
			return false;
		if (qtde == null) {
			if (other.qtde != null)
				return false;
		} else if (!qtde.equals(other.qtde))
			return false;
		if (tipoRegistro == null) {
			if (other.tipoRegistro != null)
				return false;
		} else if (!tipoRegistro.equals(other.tipoRegistro))
			return false;
		if (valorCompra == null) {
			if (other.valorCompra != null)
				return false;
		} else if (!valorCompra.equals(other.valorCompra))
			return false;
		if (valorVenda == null) {
			if (other.valorVenda != null)
				return false;
		} else if (!valorVenda.equals(other.valorVenda))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Registro [qtde=" + qtde + ", valorCompra=" + valorCompra + ", valorVenda=" + valorVenda + ", idEstoque="
				+ idEstoque + ", tipoRegistro=" + tipoRegistro + "]";
	}
	
}
