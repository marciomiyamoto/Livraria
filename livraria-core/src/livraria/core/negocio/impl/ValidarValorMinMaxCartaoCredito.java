package livraria.core.negocio.impl;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import dominio.EntidadeDominio;
import dominio.venda.Pagamento;
import dominio.venda.Pedido;
import livraria.core.IStrategy;

public class ValidarValorMinMaxCartaoCredito implements IStrategy {

	@Override
	public String processar(EntidadeDominio entidade) {
		
		if(entidade instanceof Pedido) {
			Pedido pedido = (Pedido)entidade;
			Boolean flgCupom = false;
			// VERIFICA SE EXISTE PELO MENOS UMA FORMA DE PAGAMENTO
			if(pedido.getPagamentos() != null && !pedido.getPagamentos().isEmpty()) {
				List<Pagamento> pagamentos = new ArrayList<Pagamento>();
				// PERCORRE A LISTA DE PAGAMENTOS E ADICIONA TODOS OS PAGAMENTOS FEITOS COM CARTAO EM UM ARRAY;
				// TAMBÉM VERIFICA SE NAO EXISTE PAGAMENTO EM CUPOM
				for(Pagamento p : pedido.getPagamentos()) {
					if(p.getFormaPgto().getCartao() != null) {
						pagamentos.add(p);
					} else if (p.getFormaPgto().getCupomTroca() != null) {
						flgCupom = true;
					}
				}
				// CASO NA EXISTA NENHUM CUPOM COMO FORMA DE PAGAMENTO, VERIFICA O VALOR MINIMO DE 10,00 PARA O CASO DE MAIS DE UM CARTAO
				if(!flgCupom && pagamentos.size() > 1) {
					for(Pagamento p : pagamentos) {
						if(p.getValor() < 10.0) {
							return "Valor mínimo para pagamento com mais de um cartão é de R$10,00";
						}
					}
				}
				// CASO NÃO EXISTA NENHUM CUPOM COMO FORMA DE PAGAMENTO, VERIFICA SE O VALOR PAGO COM O(S) CARTÃO(ÕES) ESTÁ CORRETO
				double valorPgtos = 0;
				if(!flgCupom) {
					for(Pagamento p : pagamentos) {
						valorPgtos += p.getValor();
					}
					DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
					symbols.setDecimalSeparator('.');
					DecimalFormat df = new DecimalFormat("#.00", symbols); 
					valorPgtos = Double.parseDouble(df.format(valorPgtos));
					if(valorPgtos != pedido.getValorTotalComDescontos()) {
						return "Pagamento com valor inválido";
					}
				}
			} else {
				return "Erro! Selecione uma forma de pagamento";
			}
		}
		return null;
	}

}
