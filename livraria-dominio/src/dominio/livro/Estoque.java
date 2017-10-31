package dominio.livro;

import java.util.List;

import dominio.EntidadeDominio;

public class Estoque extends EntidadeDominio{
	
	private Livro livro;
	private List<Registro> registros;
	private Integer qtdeTotal;
	
	public void calcularQtdeEstoque() {
		if(!registros.isEmpty()) {
			int qtdeEntrada = 0;
			int qtdeSaida = 0;
			for(Registro r : registros) {
				if(r.getTipoRegistro().equals(EnumTipoRegistroEstoque.ENTRADA.getValue())) {
					qtdeEntrada += r.getQtde();
				}
				else if(r.getTipoRegistro().equals(EnumTipoRegistroEstoque.SAIDA.getValue())) {
					qtdeSaida += r.getQtde();
				}
			}
			qtdeTotal = qtdeEntrada - qtdeSaida;
		}
	}
	
	public Double calculaValorVenda() {
		// O VALOR DE VENDA SERÁ CALCULADO SOMANDO-SE TODOS OS VALORES DE COMPRA DE ENTRADA
		// DIVIDIDOS PELA QTDE TOTAL DE ENTRADAS, RESULTANDO NO VALOR MÉDIO DE COMPRA
		// E DEPOIS MULTIPLICANDO PELA MARGEM DE LUCRO 
		double somatorioValoresCompra = 0.0;
		int qtdeTotalEntrada = 0;
		double valorMedioCompra = 0.0;
		
		if(registros != null && registros.size() != 0 && livro.getGrupoPrec() != null && livro.getGrupoPrec().getMargemDeLucro() != null) {
			for(Registro r : registros) {
				if(r.getTipoRegistro().equals(EnumTipoRegistroEstoque.ENTRADA.getValue())) {
					somatorioValoresCompra += r.getValorCompra();
					qtdeTotalEntrada += r.getQtde();
				}
			}
			valorMedioCompra = somatorioValoresCompra / qtdeTotalEntrada;
			return valorMedioCompra + (valorMedioCompra * (livro.getGrupoPrec().getMargemDeLucro() / 100));
		}
		return 0.0;
	}
	
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

	public Integer getQtdeTotal() {
		return qtdeTotal;
	}

	public void setQtdeTotal(Integer qtdeTotal) {
		this.qtdeTotal = qtdeTotal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((livro == null) ? 0 : livro.hashCode());
		result = prime * result + ((qtdeTotal == null) ? 0 : qtdeTotal.hashCode());
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
		if (qtdeTotal == null) {
			if (other.qtdeTotal != null)
				return false;
		} else if (!qtdeTotal.equals(other.qtdeTotal))
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
		return "Estoque [livro=" + livro + ", registros=" + registros + ", qtdeTotal=" + qtdeTotal + "]";
	}
	
}
