package dominio.livro;

import dominio.EntidadeDominio;

public class Dimensoes extends EntidadeDominio {
	
	private Double altura;
	private Double largura;
	private Double peso;
	private Double profundidade;
	public Double getAltura() {
		return altura;
	}
	public void setAltura(Double altura) {
		this.altura = altura;
	}
	public Double getLargura() {
		return largura;
	}
	public void setLargura(Double largura) {
		this.largura = largura;
	}
	public Double getPeso() {
		return peso;
	}
	public void setPeso(Double peso) {
		this.peso = peso;
	}
	public Double getProfundidade() {
		return profundidade;
	}
	public void setProfundidade(Double profundidade) {
		this.profundidade = profundidade;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((altura == null) ? 0 : altura.hashCode());
		result = prime * result + ((largura == null) ? 0 : largura.hashCode());
		result = prime * result + ((peso == null) ? 0 : peso.hashCode());
		result = prime * result + ((profundidade == null) ? 0 : profundidade.hashCode());
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
		Dimensoes other = (Dimensoes) obj;
		if (altura == null) {
			if (other.altura != null)
				return false;
		} else if (!altura.equals(other.altura))
			return false;
		if (largura == null) {
			if (other.largura != null)
				return false;
		} else if (!largura.equals(other.largura))
			return false;
		if (peso == null) {
			if (other.peso != null)
				return false;
		} else if (!peso.equals(other.peso))
			return false;
		if (profundidade == null) {
			if (other.profundidade != null)
				return false;
		} else if (!profundidade.equals(other.profundidade))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Dimensoes [altura=" + altura + ", largura=" + largura + ", peso=" + peso + ", profundidade="
				+ profundidade + "]";
	}
	
}
