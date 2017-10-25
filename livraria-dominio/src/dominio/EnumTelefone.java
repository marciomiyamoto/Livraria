package dominio;

public enum EnumTelefone {
	
	FIXO(1),
	 CELULAR(2);
	
	private final int value;
	
	private EnumTelefone(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}

}
