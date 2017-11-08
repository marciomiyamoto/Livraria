package dominio.venda;

import dominio.EntidadeDominio;
import dominio.livro.Estoque;
import dominio.livro.Livro;

public class ItemPedido extends EntidadeDominio {
	
	private Integer qtde;
	private Estoque estoque;
	private Integer idPedido;
	
	public Integer getQtde() {
		return qtde;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public Estoque getEstoque() {
		return estoque;
	}
	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}
	public Integer getIdPedido() {
		return idPedido;
	}
	public void setIdPedido(Integer idPedido) {
		this.idPedido = idPedido;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((estoque == null) ? 0 : estoque.hashCode());
		result = prime * result + ((idPedido == null) ? 0 : idPedido.hashCode());
		result = prime * result + ((qtde == null) ? 0 : qtde.hashCode());
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
		ItemPedido other = (ItemPedido) obj;
		if (estoque == null) {
			if (other.estoque != null)
				return false;
		} else if (!estoque.equals(other.estoque))
			return false;
		if (idPedido == null) {
			if (other.idPedido != null)
				return false;
		} else if (!idPedido.equals(other.idPedido))
			return false;
		if (qtde == null) {
			if (other.qtde != null)
				return false;
		} else if (!qtde.equals(other.qtde))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "ItemPedido [qtde=" + qtde + ", estoque=" + estoque + ", idPedido=" + idPedido + "]";
	}

}
