package dominio.livro;

public enum EnumTipoRegistroEstoque {
	
	ENTRADA(1),
	SAIDA(2); 
	
	private final int value;
	
	private EnumTipoRegistroEstoque(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}
}
