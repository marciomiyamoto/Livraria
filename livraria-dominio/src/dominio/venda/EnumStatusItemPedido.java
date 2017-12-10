package dominio.venda;

public enum EnumStatusItemPedido {

	EM_PROCESSAMENTO(1),
	APROVADO(2),
	REPROVADO(3),
	EM_TRANSPORTE(4),
	ENTREGUE(5),
	CANCELADO(6),
	EM_TROCA(7),
	TROCADO(8);
	
	private final int value;
	
	private EnumStatusItemPedido(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}
}
