package dominio.venda;

import java.util.List;

import dominio.EntidadeDominio;
import dominio.cliente.Cliente;

public class Pedido extends EntidadeDominio {

	private Double valorTotal;
	private Cliente cliente;
	private CustoFrete custoFrete;
	private EnumStatusPedido statusPedido;
	private List<Pagamento> pagamentos;
	private List<ItemPedido> itens;
	
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
	public EnumStatusPedido getStatusPedido() {
		return statusPedido;
	}
	public void setStatusPedido(EnumStatusPedido statusPedido) {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((cliente == null) ? 0 : cliente.hashCode());
		result = prime * result + ((custoFrete == null) ? 0 : custoFrete.hashCode());
		result = prime * result + ((itens == null) ? 0 : itens.hashCode());
		result = prime * result + ((pagamentos == null) ? 0 : pagamentos.hashCode());
		result = prime * result + ((statusPedido == null) ? 0 : statusPedido.hashCode());
		result = prime * result + ((valorTotal == null) ? 0 : valorTotal.hashCode());
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
		if (custoFrete == null) {
			if (other.custoFrete != null)
				return false;
		} else if (!custoFrete.equals(other.custoFrete))
			return false;
		if (itens == null) {
			if (other.itens != null)
				return false;
		} else if (!itens.equals(other.itens))
			return false;
		if (pagamentos == null) {
			if (other.pagamentos != null)
				return false;
		} else if (!pagamentos.equals(other.pagamentos))
			return false;
		if (statusPedido != other.statusPedido)
			return false;
		if (valorTotal == null) {
			if (other.valorTotal != null)
				return false;
		} else if (!valorTotal.equals(other.valorTotal))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Pedido [valorTotal=" + valorTotal + ", cliente=" + cliente + ", custoFrete=" + custoFrete
				+ ", statusPedido=" + statusPedido + ", pagamentos=" + pagamentos + ", itens=" + itens + "]";
	}
	
}
