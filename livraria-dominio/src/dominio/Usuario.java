package dominio;

public class Usuario extends EntidadeDominio {

	private String usuario;
	private String senha;
	private String senhaRepetida;
	private String senhaAntiga;
	
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public String getSenhaRepetida() {
		return senhaRepetida;
	}
	public void setSenhaRepetida(String senhaRepetida) {
		this.senhaRepetida = senhaRepetida;
	}
	public String getSenhaAntiga() {
		return senhaAntiga;
	}
	public void setSenhaAntiga(String senhaAntiga) {
		this.senhaAntiga = senhaAntiga;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((senha == null) ? 0 : senha.hashCode());
		result = prime * result + ((senhaAntiga == null) ? 0 : senhaAntiga.hashCode());
		result = prime * result + ((senhaRepetida == null) ? 0 : senhaRepetida.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
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
		Usuario other = (Usuario) obj;
		if (senha == null) {
			if (other.senha != null)
				return false;
		} else if (!senha.equals(other.senha))
			return false;
		if (senhaAntiga == null) {
			if (other.senhaAntiga != null)
				return false;
		} else if (!senhaAntiga.equals(other.senhaAntiga))
			return false;
		if (senhaRepetida == null) {
			if (other.senhaRepetida != null)
				return false;
		} else if (!senhaRepetida.equals(other.senhaRepetida))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Usuario [usuario=" + usuario + ", senha=" + senha + ", senhaRepetida=" + senhaRepetida
				+ ", senhaAntiga=" + senhaAntiga + "]";
	}
	
}
