package dominio.livro;

import java.util.List;

import dominio.EntidadeDominio;

public class Estoque extends EntidadeDominio{
	
	private Livro livro;
	private List<Registro> registros;
	public Livro getLivro() {
		return livro;
	}
	public void setLivro(Livro livro) {
		this.livro = livro;
	}
	public List<Registro> getRegistros() {
		return registros;
	}
	public void setRegistros(List<Registro> registros) {
		this.registros = registros;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((livro == null) ? 0 : livro.hashCode());
		result = prime * result + ((registros == null) ? 0 : registros.hashCode());
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
		Estoque other = (Estoque) obj;
		if (livro == null) {
			if (other.livro != null)
				return false;
		} else if (!livro.equals(other.livro))
			return false;
		if (registros == null) {
			if (other.registros != null)
				return false;
		} else if (!registros.equals(other.registros))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Estoque [livro=" + livro + ", registros=" + registros + "]";
	}
	
}
