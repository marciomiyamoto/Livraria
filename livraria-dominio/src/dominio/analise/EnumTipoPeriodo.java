package dominio.analise;

public enum EnumTipoPeriodo {

	DIA(1),
	MES(2),
	ANO(3);
	
	private final int value;
	
	private EnumTipoPeriodo(int value) {
		this.value = value;
	}
	
	public int getValue() { 
		return value; 
	}
}
