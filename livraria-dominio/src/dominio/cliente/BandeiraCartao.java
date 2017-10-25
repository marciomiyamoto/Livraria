package dominio.cliente;

import dominio.EntidadeDominio;

public class BandeiraCartao extends EntidadeDominio {

	private Integer bin;
	private String nome;
	
	public Integer getBin() {
		return bin;
	}
	public void setBin(Integer bin) {
		this.bin = bin;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bin == null) ? 0 : bin.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BandeiraCartao other = (BandeiraCartao) obj;
		if (bin == null) {
			if (other.bin != null)
				return false;
		} else if (!bin.equals(other.bin))
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
		return "BandeiraCartao [bin=" + bin + ", nome=" + nome + "]";
	}

}
