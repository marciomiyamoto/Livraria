package dominio.venda;

public enum EnumTipoEnvio {
	
	SEDEX("04014"),
	SEDEX10("40215"),
	PAC("04510");
	
	private final String value;
	
	private EnumTipoEnvio(String value) {
		this.value = value;
	}
	
	public String getValue() { 
		return value; 
	}

}
