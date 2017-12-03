package dominio.cliente;

public enum EnumTipoCartao {
	
	CADASTRO_CLIENTE(1),
	PEDIDO(2);
	
	private final int value;
	
	private EnumTipoCartao(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}
}
