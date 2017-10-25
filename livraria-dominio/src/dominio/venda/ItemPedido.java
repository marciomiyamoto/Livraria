package dominio.venda;

import dominio.EntidadeDominio;
import dominio.livro.Livro;

public class ItemPedido extends EntidadeDominio {
	
	private Integer qtde;
	private Livro livro;
	public Integer getQtde() {
		return qtde;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((livro == null) ? 0 : livro.hashCode());
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
		if (livro == null) {
			if (other.livro != null)
				return false;
		} else if (!livro.equals(other.livro))
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
		return "ItemPedido [qtde=" + qtde + ", livro=" + livro + "]";
	}

}
