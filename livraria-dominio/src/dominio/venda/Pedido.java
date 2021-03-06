package dominio.venda;

import java.util.List;

import dominio.EntidadeDominio;
import dominio.cliente.Cliente;
import dominio.endereco.Endereco;

public class Pedido extends EntidadeDominio {

	private Double valorTotal;
	private Double valorTotalComDescontos;
	private Cliente cliente;
	private CustoFrete custoFrete;
	private Integer statusPedido;
	private List<Pagamento> pagamentos;
	private List<ItemPedido> itens;
	private List<ItemBloqueioPedido> itensBloqueados;
	private Endereco endEntrega;
	private CupomPromocional cupomPromocional;
	
	public Double getValorTotal() {
		return valorTotal;
	}
	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public CustoFrete getCustoFrete() {
		return custoFrete;
	}
	public void setCustoFrete(CustoFrete custoFrete) {
		this.custoFrete = custoFrete;
	}
	public Integer getStatusPedido() {
		return statusPedido;
	}
	public void setStatusPedido(Integer statusPedido) {
		this.statusPedido = statusPedido;
	}
	public List<Pagamento> getPagamentos() {
		return pagamentos;
	}
	public void setPagamentos(List<Pagamento> pagamentos) {
		this.pagamentos = pagamentos;
	}
	public List<ItemPedido> getItens() {
		return itens;
	}
	public void setItens(List<ItemPedido> itens) {
		this.itens = itens;
	}
	public Endereco getEndEntrega() {
		return endEntrega;
	}
	public void setEndEntrega(Endereco endEntrega) {
		this.endEntrega = endEntrega;
	}
	public CupomPromocional getCupomPromocional() {
		return cupomPromocional;
	}
	public void setCupomPromocional(CupomPromocional cupomPromocional) {
		this.cupomPromocional = cupomPromocional;
	}
	public Double getValorTotalComDescontos() {
		return valorTotalComDescontos;
	}
	public void setValorTotalComDescontos(Double valorTotalComDescontos) {
		this.valorTotalComDescontos = valorTotalComDescontos;
	}
	public List<ItemBloqueioPedido> getItensBloqueados() {
		return itensBloqueados;
	}
	public void setItensBloqueados(List<ItemBloqueioPedido> itensBloqueados) {
		this.itensBloqueados = itensBloqueados;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((cupomPromocional == null) ? 0 : cupomPromocional.hashCode());
		result = prime * result + ((custoFrete == null) ? 0 : custoFrete.hashCode());
		result = prime * result + ((endEntrega == null) ? 0 : endEntrega.hashCode());
		result = prime * result + ((itens == null) ? 0 : itens.hashCode());
		result = prime * result + ((itensBloqueados == null) ? 0 : itensBloqueados.hashCode());
		result = prime * result + ((pagamentos == null) ? 0 : pagamentos.hashCode());
		result = prime * result + ((statusPedido == null) ? 0 : statusPedido.hashCode());
		result = prime * result + ((valorTotal == null) ? 0 : valorTotal.hashCode());
		result = prime * result + ((valorTotalComDescontos == null) ? 0 : valorTotalComDescontos.hashCode());
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
		Pedido other = (Pedido) obj;
		if (cliente == null) {
			if (other.cliente != null)
				return false;
		} else if (!cliente.equals(other.cliente))
			return false;
		if (cupomPromocional == null) {
			if (other.cupomPromocional != null)
				return false;
		} else if (!cupomPromocional.equals(other.cupomPromocional))
			return false;
		if (custoFrete == null) {
			if (other.custoFrete != null)
				return false;
		} else if (!custoFrete.equals(other.custoFrete))
			return false;
		if (endEntrega == null) {
			if (other.endEntrega != null)
				return false;
		} else if (!endEntrega.equals(other.endEntrega))
			return false;
		if (itens == null) {
			if (other.itens != null)
				return false;
		} else if (!itens.equals(other.itens))
			return false;
		if (itensBloqueados == null) {
			if (other.itensBloqueados != null)
				return false;
		} else if (!itensBloqueados.equals(other.itensBloqueados))
			return false;
		if (pagamentos == null) {
			if (other.pagamentos != null)
				return false;
		} else if (!pagamentos.equals(other.pagamentos))
			return false;
		if (statusPedido == null) {
			if (other.statusPedido != null)
				return false;
		} else if (!statusPedido.equals(other.statusPedido))
			return false;
		if (valorTotal == null) {
			if (other.valorTotal != null)
				return false;
		} else if (!valorTotal.equals(other.valorTotal))
			return false;
		if (valorTotalComDescontos == null) {
			if (other.valorTotalComDescontos != null)
				return false;
		} else if (!valorTotalComDescontos.equals(other.valorTotalComDescontos))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Pedido [valorTotal=" + valorTotal + ", valorTotalComDescontos=" + valorTotalComDescontos + ", cliente="
				+ cliente + ", custoFrete=" + custoFrete + ", statusPedido=" + statusPedido + ", pagamentos="
				+ pagamentos + ", itens=" + itens + ", itensBloqueados=" + itensBloqueados + ", endEntrega="
				+ endEntrega + ", cupomPromocional=" + cupomPromocional + "]";
	}
	
}
