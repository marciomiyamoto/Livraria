package dominio.venda;

public enum EnumTipoEnvio {
	
	SEDEX(40010),
	SEDEX10(40215),
	PAC(41106);
	
	private final int value;
	
	private EnumTipoEnvio(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}

}
