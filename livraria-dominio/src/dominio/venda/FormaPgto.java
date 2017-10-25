package dominio.venda;

import java.util.List;

import dominio.EntidadeDominio;
import dominio.cliente.Cartao;

public class FormaPgto extends EntidadeDominio {
	
	private Cartao cartao;
	private CupomTroca cupomTroca;
	public Cartao getCartao() {
		return cartao;
	}
	public void setCartao(Cartao cartao) {
		this.cartao = cartao;
	}
	public CupomTroca getCupomTroca() {
		return cupomTroca;
	}
	public void setCupomTroca(CupomTroca cupomTroca) {
		this.cupomTroca = cupomTroca;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cartao == null) ? 0 : cartao.hashCode());
		result = prime * result + ((cupomTroca == null) ? 0 : cupomTroca.hashCode());
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
		FormaPgto other = (FormaPgto) obj;
		if (cartao == null) {
			if (other.cartao != null)
				return false;
		} else if (!cartao.equals(other.cartao))
			return false;
		if (cupomTroca == null) {
			if (other.cupomTroca != null)
				return false;
		} else if (!cupomTroca.equals(other.cupomTroca))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "FormaPgto [cartao=" + cartao + ", cupomTroca=" + cupomTroca + "]";
	}
	
}
