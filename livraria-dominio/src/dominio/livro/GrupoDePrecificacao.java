package dominio.livro;

import dominio.EntidadeDominio;

public class GrupoDePrecificacao extends EntidadeDominio {
	
	private String nome;
	private Double margemDeLucro;
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Double getMargemDeLucro() {
		return margemDeLucro;
	}
	public void setMargemDeLucro(Double margemDeLucro) {
		this.margemDeLucro = margemDeLucro;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((margemDeLucro == null) ? 0 : margemDeLucro.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		GrupoDePrecificacao other = (GrupoDePrecificacao) obj;
		if (margemDeLucro == null) {
			if (other.margemDeLucro != null)
				return false;
		} else if (!margemDeLucro.equals(other.margemDeLucro))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GrupoDePrecificacao [nome=" + nome + ", margemDeLucro=" + margemDeLucro + "]";
	}
	
}
