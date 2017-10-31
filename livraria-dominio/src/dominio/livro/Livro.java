package dominio.livro;

import java.util.List;

import dominio.EntidadeDominio;

public class Livro extends EntidadeDominio {

	private String titulo;
	private Integer ano;
	private Integer edicao;
	private Integer numPaginas;
	private String codBarras;
	private Boolean ativo;
	private String sinopse;
	private String justificativa;
	private Autor autor;
	private Categoria catAtivInativacao;
	private List<Categoria> categorias;
	private List<Subcategoria> subcategorias;
	private Editora editora;
	private Dimensoes dimensoes;
	private GrupoDePrecificacao grupoPrec;
	private Double precoVenda;
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public Integer getAno() {
		return ano;
	}
	public void setAno(Integer ano) {
		this.ano = ano;
	}
	public Integer getEdicao() {
		return edicao;
	}
	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}
	public Integer getNumPaginas() {
		return numPaginas;
	}
	public void setNumPaginas(Integer numPaginas) {
		this.numPaginas = numPaginas;
	}
	public String getCodBarras() {
		return codBarras;
	}
	public void setCodBarras(String codBarras) {
		this.codBarras = codBarras;
	}
	public Boolean getAtivo() {
		return ativo;
	}
	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	public String getSinopse() {
		return sinopse;
	}
	public void setSinopse(String sinopse) {
		this.sinopse = sinopse;
	}
	public Autor getAutor() {
		return autor;
	}
	public void setAutor(Autor autor) {
		this.autor = autor;
	}
	public List<Categoria> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	public List<Subcategoria> getSubcategorias() {
		return subcategorias;
	}
	public void setSubcategorias(List<Subcategoria> subcategorias) {
		this.subcategorias = subcategorias;
	}
	public Editora getEditora() {
		return editora;
	}
	public void setEditora(Editora editora) {
		this.editora = editora;
	}
	public Dimensoes getDimensoes() {
		return dimensoes;
	}
	public void setDimensoes(Dimensoes dimensoes) {
		this.dimensoes = dimensoes;
	}
	public GrupoDePrecificacao getGrupoPrec() {
		return grupoPrec;
	}
	public void setGrupoPrec(GrupoDePrecificacao grupoPrec) {
		this.grupoPrec = grupoPrec;
	}
	public String getJustificativa() {
		return justificativa;
	}
	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}
	public Categoria getCatAtivInativacao() {
		return catAtivInativacao;
	}
	public void setCatAtivInativacao(Categoria catAtivInativacao) {
		this.catAtivInativacao = catAtivInativacao;
	}
	public Double getPrecoVenda() {
		return precoVenda;
	}
	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((ano == null) ? 0 : ano.hashCode());
		result = prime * result + ((ativo == null) ? 0 : ativo.hashCode());
		result = prime * result + ((autor == null) ? 0 : autor.hashCode());
		result = prime * result + ((catAtivInativacao == null) ? 0 : catAtivInativacao.hashCode());
		result = prime * result + ((categorias == null) ? 0 : categorias.hashCode());
		result = prime * result + ((codBarras == null) ? 0 : codBarras.hashCode());
		result = prime * result + ((dimensoes == null) ? 0 : dimensoes.hashCode());
		result = prime * result + ((edicao == null) ? 0 : edicao.hashCode());
		result = prime * result + ((editora == null) ? 0 : editora.hashCode());
		result = prime * result + ((grupoPrec == null) ? 0 : grupoPrec.hashCode());
		result = prime * result + ((justificativa == null) ? 0 : justificativa.hashCode());
		result = prime * result + ((numPaginas == null) ? 0 : numPaginas.hashCode());
		result = prime * result + ((precoVenda == null) ? 0 : precoVenda.hashCode());
		result = prime * result + ((sinopse == null) ? 0 : sinopse.hashCode());
		result = prime * result + ((subcategorias == null) ? 0 : subcategorias.hashCode());
		result = prime * result + ((titulo == null) ? 0 : titulo.hashCode());
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
		Livro other = (Livro) obj;
		if (ano == null) {
			if (other.ano != null)
				return false;
		} else if (!ano.equals(other.ano))
			return false;
		if (ativo == null) {
			if (other.ativo != null)
				return false;
		} else if (!ativo.equals(other.ativo))
			return false;
		if (autor == null) {
			if (other.autor != null)
				return false;
		} else if (!autor.equals(other.autor))
			return false;
		if (catAtivInativacao == null) {
			if (other.catAtivInativacao != null)
				return false;
		} else if (!catAtivInativacao.equals(other.catAtivInativacao))
			return false;
		if (categorias == null) {
			if (other.categorias != null)
				return false;
		} else if (!categorias.equals(other.categorias))
			return false;
		if (codBarras == null) {
			if (other.codBarras != null)
				return false;
		} else if (!codBarras.equals(other.codBarras))
			return false;
		if (dimensoes == null) {
			if (other.dimensoes != null)
				return false;
		} else if (!dimensoes.equals(other.dimensoes))
			return false;
		if (edicao == null) {
			if (other.edicao != null)
				return false;
		} else if (!edicao.equals(other.edicao))
			return false;
		if (editora == null) {
			if (other.editora != null)
				return false;
		} else if (!editora.equals(other.editora))
			return false;
		if (grupoPrec == null) {
			if (other.grupoPrec != null)
				return false;
		} else if (!grupoPrec.equals(other.grupoPrec))
			return false;
		if (justificativa == null) {
			if (other.justificativa != null)
				return false;
		} else if (!justificativa.equals(other.justificativa))
			return false;
		if (numPaginas == null) {
			if (other.numPaginas != null)
				return false;
		} else if (!numPaginas.equals(other.numPaginas))
			return false;
		if (precoVenda == null) {
			if (other.precoVenda != null)
				return false;
		} else if (!precoVenda.equals(other.precoVenda))
			return false;
		if (sinopse == null) {
			if (other.sinopse != null)
				return false;
		} else if (!sinopse.equals(other.sinopse))
			return false;
		if (subcategorias == null) {
			if (other.subcategorias != null)
				return false;
		} else if (!subcategorias.equals(other.subcategorias))
			return false;
		if (titulo == null) {
			if (other.titulo != null)
				return false;
		} else if (!titulo.equals(other.titulo))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Livro [titulo=" + titulo + ", ano=" + ano + ", edicao=" + edicao + ", numPaginas=" + numPaginas
				+ ", codBarras=" + codBarras + ", ativo=" + ativo + ", sinopse=" + sinopse + ", justificativa="
				+ justificativa + ", autor=" + autor + ", catAtivInativacao=" + catAtivInativacao + ", categorias="
				+ categorias + ", subcategorias=" + subcategorias + ", editora=" + editora + ", dimensoes=" + dimensoes
				+ ", grupoPrec=" + grupoPrec + ", precoVenda=" + precoVenda + "]";
	}
	
}
