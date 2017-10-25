package dominio.endereco;

public enum EnumEndereco {
	 RESIDENCIAL(1),
	 ENTREGA(2),
	 COBRANCA(3);
	private final int value;
	
	private EnumEndereco(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}
}
