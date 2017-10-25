package dominio.livro;

import java.util.Date;

import dominio.EntidadeDominio;

public class LogLivro extends EntidadeDominio {
	
	private Date dtAcao;
	private String hora;
	private String acao;
	private String usuario;
	private String idLivro;
	private String valoresAntigos;
	private String valoresNovos;
	
	public Date getDtAcao() {
		return dtAcao;
	}
	public void setDtAcao(Date dtAcao) {
		this.dtAcao = dtAcao;
	}
	public String getAcao() {
		return acao;
	}
	public void setAcao(String acao) {
		this.acao = acao;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getIdLivro() {
		return idLivro;
	}
	public void setIdLivro(String idLivro) {
		this.idLivro = idLivro;
	}
	public String getValoresAntigos() {
		return valoresAntigos;
	}
	public void setValoresAntigos(String valoresAntigos) {
		this.valoresAntigos = valoresAntigos;
	}
	public String getValoresNovos() {
		return valoresNovos;
	}
	public void setValoresNovos(String valoresNovos) {
		this.valoresNovos = valoresNovos;
	}
	public String getHora() {
		return hora;
	}
	public void setHora(String hora) {
		this.hora = hora;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((acao == null) ? 0 : acao.hashCode());
		result = prime * result + ((dtAcao == null) ? 0 : dtAcao.hashCode());
		result = prime * result + ((hora == null) ? 0 : hora.hashCode());
		result = prime * result + ((idLivro == null) ? 0 : idLivro.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		result = prime * result + ((valoresAntigos == null) ? 0 : valoresAntigos.hashCode());
		result = prime * result + ((valoresNovos == null) ? 0 : valoresNovos.hashCode());
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
		LogLivro other = (LogLivro) obj;
		if (acao == null) {
			if (other.acao != null)
				return false;
		} else if (!acao.equals(other.acao))
			return false;
		if (dtAcao == null) {
			if (other.dtAcao != null)
				return false;
		} else if (!dtAcao.equals(other.dtAcao))
			return false;
		if (hora == null) {
			if (other.hora != null)
				return false;
		} else if (!hora.equals(other.hora))
			return false;
		if (idLivro == null) {
			if (other.idLivro != null)
				return false;
		} else if (!idLivro.equals(other.idLivro))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		if (valoresAntigos == null) {
			if (other.valoresAntigos != null)
				return false;
		} else if (!valoresAntigos.equals(other.valoresAntigos))
			return false;
		if (valoresNovos == null) {
			if (other.valoresNovos != null)
				return false;
		} else if (!valoresNovos.equals(other.valoresNovos))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "LogLivro [dtAcao=" + dtAcao + ", hora=" + hora + ", acao=" + acao + ", usuario=" + usuario
				+ ", idLivro=" + idLivro + ", valoresAntigos=" + valoresAntigos + ", valoresNovos=" + valoresNovos
				+ "]";
	}
	
}
