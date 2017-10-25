package dominio.venda;

import dominio.EntidadeDominio;
import dominio.livro.Livro;

public class Frete extends EntidadeDominio {
	
	private Livro livro;
	private Integer qtde;
	private String cepOrigem;
	private String cepDestino;
	private EnumTipoEnvio tipoEnvio;
	
	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	public Integer getQtde() {
		return qtde;
	}
	public void setQtde(Integer qtde) {
		this.qtde = qtde;
	}
	public String getCepOrigem() {
		return cepOrigem;
	}
	public void setCepOrigem(String cepOrigem) {
		this.cepOrigem = cepOrigem;
	}
	public String getCepDestino() {
		return cepDestino;
	}
	public void setCepDestino(String cepDestino) {
		this.cepDestino = cepDestino;
	}
	public EnumTipoEnvio getTipoEnvio() {
		return tipoEnvio;
	}
	public void setTipoEnvio(EnumTipoEnvio tipoEnvio) {
		this.tipoEnvio = tipoEnvio;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cepDestino == null) ? 0 : cepDestino.hashCode());
		result = prime * result + ((cepOrigem == null) ? 0 : cepOrigem.hashCode());
		result = prime * result + ((livro == null) ? 0 : livro.hashCode());
		result = prime * result + ((qtde == null) ? 0 : qtde.hashCode());
		result = prime * result + ((tipoEnvio == null) ? 0 : tipoEnvio.hashCode());
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
		Frete other = (Frete) obj;
		if (cepDestino == null) {
			if (other.cepDestino != null)
				return false;
		} else if (!cepDestino.equals(other.cepDestino))
			return false;
		if (cepOrigem == null) {
			if (other.cepOrigem != null)
				return false;
		} else if (!cepOrigem.equals(other.cepOrigem))
			return false;
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
		if (tipoEnvio != other.tipoEnvio)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Frete [livro=" + livro + ", qtde=" + qtde + ", cepOrigem=" + cepOrigem + ", cepDestino=" + cepDestino
				+ ", tipoEnvio=" + tipoEnvio + "]";
	}
	
}
