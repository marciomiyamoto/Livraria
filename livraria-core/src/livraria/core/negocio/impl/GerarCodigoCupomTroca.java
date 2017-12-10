package livraria.core.negocio.impl;

import java.security.SecureRandom;
import java.util.Random;

import dominio.EntidadeDominio;
import dominio.venda.CupomTroca;
import livraria.core.IStrategy;

public class GerarCodigoCupomTroca implements IStrategy {
	@Override
	public String processar(EntidadeDominio entidade) {
		if(entidade instanceof CupomTroca) {
			CupomTroca cupom = (CupomTroca)entidade;
			cupom.setCodigo(gerarCodigo());
		}
		return null;
	}

	public String gerarCodigo() {
		char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
		StringBuilder sb = new StringBuilder();
		Random random = new SecureRandom();
		for (int i = 0; i < 8; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		String output = sb.toString();
		System.out.println(output);
		return output ;
	}
}
