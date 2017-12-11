package dominio.analise;

import java.util.Date;
import java.util.List;
import java.util.Map;

import dominio.EntidadeDominio;

public class LivroPeriodoVendas extends EntidadeDominio {

	// MAPA COM INDICE DE LIVRO, CONTENDO MAPA DE DATA E QTDE VENDIDA
	private List<LinhaLivroPeriodoVendas> linhas;
	private Date dtInicial;
	private Date dtFinal;
	private Integer qtdeMin;
	private Integer qtdeMax;
	private Integer tipoPeriodo;

	public List<LinhaLivroPeriodoVendas> getLinhas() {
		return linhas;
	}

	public void setLinhas(List<LinhaLivroPeriodoVendas> linhas) {
		this.linhas = linhas;
	}

	public Date getDtInicial() {
		return dtInicial;
	}

	public void setDtInicial(Date dtInicial) {
		this.dtInicial = dtInicial;
	}

	public Date getDtFinal() {
		return dtFinal;
	}

	public void setDtFinal(Date dtFinal) {
		this.dtFinal = dtFinal;
	}

	public Integer getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(Integer tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public Integer getQtdeMin() {
		return qtdeMin;
	}

	public void setQtdeMin(Integer qtdeMin) {
		this.qtdeMin = qtdeMin;
	}

	public Integer getQtdeMax() {
		return qtdeMax;
	}

	public void setQtdeMax(Integer qtdeMax) {
		this.qtdeMax = qtdeMax;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((dtFinal == null) ? 0 : dtFinal.hashCode());
		result = prime * result + ((dtInicial == null) ? 0 : dtInicial.hashCode());
		result = prime * result + ((linhas == null) ? 0 : linhas.hashCode());
		result = prime * result + ((qtdeMax == null) ? 0 : qtdeMax.hashCode());
		result = prime * result + ((qtdeMin == null) ? 0 : qtdeMin.hashCode());
		result = prime * result + ((tipoPeriodo == null) ? 0 : tipoPeriodo.hashCode());
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
		LivroPeriodoVendas other = (LivroPeriodoVendas) obj;
		if (dtFinal == null) {
			if (other.dtFinal != null)
				return false;
		} else if (!dtFinal.equals(other.dtFinal))
			return false;
		if (dtInicial == null) {
			if (other.dtInicial != null)
				return false;
		} else if (!dtInicial.equals(other.dtInicial))
			return false;
		if (linhas == null) {
			if (other.linhas != null)
				return false;
		} else if (!linhas.equals(other.linhas))
			return false;
		if (qtdeMax == null) {
			if (other.qtdeMax != null)
				return false;
		} else if (!qtdeMax.equals(other.qtdeMax))
			return false;
		if (qtdeMin == null) {
			if (other.qtdeMin != null)
				return false;
		} else if (!qtdeMin.equals(other.qtdeMin))
			return false;
		if (tipoPeriodo == null) {
			if (other.tipoPeriodo != null)
				return false;
		} else if (!tipoPeriodo.equals(other.tipoPeriodo))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LivroPeriodoVendas [linhas=" + linhas + ", dtInicial=" + dtInicial + ", dtFinal=" + dtFinal
				+ ", qtdeMin=" + qtdeMin + ", qtdeMax=" + qtdeMax + ", tipoPeriodo=" + tipoPeriodo + "]";
	}

}
