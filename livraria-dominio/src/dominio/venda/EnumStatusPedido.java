package dominio.venda;

public enum EnumStatusPedido {

	EM_PROCESSAMENTO(1),
	APROVADO(2),
	REPROVADO(3),
	EM_TRANSPORTE(4),
	ENTREGUE(5),
	CANCELADO(6),
	PEDIDO_EM_TROCA(7),
	ITEM_EM_TROCA(8),
	TROCADO(9);
	
	private final int value;
	
	private EnumStatusPedido(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}
}
