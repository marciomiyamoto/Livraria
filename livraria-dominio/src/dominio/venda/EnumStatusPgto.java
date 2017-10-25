package dominio.venda;

public enum EnumStatusPgto {
	
	PENDENTE(1),
	APROVADO(2),
	REPROVADO(3);
	
	private final int value;
	
	private EnumStatusPgto(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}


}
