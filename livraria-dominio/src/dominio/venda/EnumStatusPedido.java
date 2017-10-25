package dominio.venda;

public enum EnumStatusPedido {

	PENDENTE(1),
	PGTO_RECEBIDO(2),
	EM_PROCESSAMENTO(3),
	ENVIADO(4),
	ENTREGUE(5),
	CANCELADO(6);
	
	private final int value;
	
	private EnumStatusPedido(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}
}
