package dominio.venda;

import java.sql.Time;

import dominio.EntidadeDominio;

public class ItemBloqueioPedido extends EntidadeDominio{
	
	private Time hora;
	private Integer qtde;
	private Integer idEstoque;
	private Integer idPedido;
	
	public Time getHora() {
		return hora;
	}
	public void setHora(Time hora) {
		this.hora = hora;
	}
	public Integer getQtde() {
		return qtde;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public Integer getIdEstoque() {
		return idEstoque;
	}
	public void setIdEstoque(Integer idEstoque) {
		this.idEstoque = idEstoque;
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
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		result = prime * result + ((idEstoque == null) ? 0 : idEstoque.hashCode());
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
		ItemBloqueioPedido other = (ItemBloqueioPedido) obj;
		if (hora == null) {
			if (other.hora != null)
				return false;
		} else if (!hora.equals(other.hora))
			return false;
		if (idEstoque == null) {
			if (other.idEstoque != null)
				return false;
		} else if (!idEstoque.equals(other.idEstoque))
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
		return "ItemBloqueioPedido [hora=" + hora + ", qtde=" + qtde + ", idEstoque=" + idEstoque + ", idPedido="
				+ idPedido + "]";
	}

}
