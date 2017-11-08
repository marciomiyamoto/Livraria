package livraria.controle.web.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import dominio.livro.Livro;
import dominio.venda.CustoFrete;
import dominio.venda.EnumTipoEnvio;
import dominio.venda.Frete;

public class CorreiosWS {
	
//	private  = new Frete();
//	private Livro livro;
	
	public CustoFrete calcularFrete(Frete frete) {
		CustoFrete custoFrete = new CustoFrete();
		custoFrete.setFrete(frete);
		// Dados pesquisa		
		String nCdEmpresa = "";
		String sDsSenha = "";
		String nCdServico = frete.getTipoEnvio();
		String sCepOrigem = frete.getCepOrigem();
		String sCepDestino = frete.getCepDestino();
		String nVlPeso = frete.getLivro().getDimensoes().getPeso().toString();
		String nCdFormato = "1";
		String nVlComprimento = frete.getLivro().getDimensoes().getProfundidade().toString();
		String nVlAltura = frete.getLivro().getDimensoes().getAltura().toString();
		String nVlLargura = frete.getLivro().getDimensoes().getLargura().toString();
		String nVlDiametro = "0";
		String sCdMaoPropria = "s";
		String nVlValorDeclarado = "0";
		String sCdAvisoRecebimento = "N";
		String StrRetorno = "xml";
		//URL do webservice correio para calculo de frete
		String urlString = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx";
		// os parametros a serem enviados
		Properties parameters = new Properties();
		parameters.setProperty("nCdEmpresa", nCdEmpresa);
		parameters.setProperty("sDsSenha", sDsSenha);
		parameters.setProperty("nCdServico", nCdServico);
		parameters.setProperty("sCepOrigem", sCepOrigem);
		parameters.setProperty("sCepDestino", sCepDestino);
		parameters.setProperty("nVlPeso", nVlPeso);
		parameters.setProperty("nCdFormato", nCdFormato);
		parameters.setProperty("nVlComprimento", nVlComprimento);
		parameters.setProperty("nVlAltura", nVlAltura);
		parameters.setProperty("nVlLargura", nVlLargura);
		parameters.setProperty("nVlDiametro", nVlDiametro);
		parameters.setProperty("sCdMaoPropria", sCdMaoPropria);
		parameters.setProperty("nVlValorDeclarado", nVlValorDeclarado);
		parameters.setProperty("sCdAvisoRecebimento", sCdAvisoRecebimento);
		parameters.setProperty("StrRetorno", StrRetorno);
		// o iterador, para criar a URL
		Iterator i = parameters.keySet().iterator();
		// o contador
		int counter = 0;
		// enquanto ainda existir parametros
		while (i.hasNext()) {
			// pega o nome
			String name = (String) i.next();
			// pega o valor
			String value = parameters.getProperty(name);
			// adiciona com um conector (? ou &)
			// o primeiro é ?, depois são &
			urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;
		}
		try {
			// cria o objeto url
			URL url = new URL(urlString);
			// cria o objeto httpurlconnection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// seta o metodo
			connection.setRequestProperty("Request-Method", "GET");
			// seta a variavel para ler o resultado
			connection.setDoInput(true);
			connection.setDoOutput(false);
			// conecta com a url destino
			connection.connect();
			// abre a conexão pra input
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			// le ate o final
			StringBuffer newData = new StringBuffer();
			String s = "";
			while (null != ((s = br.readLine()))) {
				newData.append(s);
			}
			br.close();
			//Prepara o XML que está em string para executar leitura por nodes
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		    InputSource is = new InputSource();
		    is.setCharacterStream(new StringReader(newData.toString()));
		    Document doc = db.parse(is);
		    NodeList nodes = doc.getElementsByTagName("cServico");
		    //Faz a leitura dos nodes
		    Locale Local = new Locale("pt","BR");
		    DecimalFormat df = new DecimalFormat("#,##0.00", new DecimalFormatSymbols(Local));
		    for (int j = 0; j < nodes.getLength(); j++) {
		      Element element = (Element) nodes.item(j);
		      NodeList erro = element.getElementsByTagName("Erro");
		      Element line = (Element) erro.item(0);
		      String erroString = getCharacterDataFromElement(line);
		      custoFrete.setErro(erroString);
		      if(!custoFrete.getErro().equals("0")) {
		    	  break;
		      }
		      System.out.println("Erro: " + getCharacterDataFromElement(line));
		      NodeList valor = element.getElementsByTagName("Valor");
		      line = (Element) valor.item(0);
		      String valorString = getCharacterDataFromElement(line);
		      custoFrete.setValor((Double)df.parseObject(valorString));
		      System.out.println("Valor: " + getCharacterDataFromElement(line));
		      NodeList prazoEntrega = element.getElementsByTagName("PrazoEntrega");
		      line = (Element) prazoEntrega.item(0);
		      String prazoString = getCharacterDataFromElement(line);
		      custoFrete.setPrazoEntrega(Integer.parseInt(prazoString));
		      System.out.println("Prazo: " + getCharacterDataFromElement(line));
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return custoFrete;
	}
	
	public static String getCharacterDataFromElement(Element e) {
	    Node child = e.getFirstChild();
	    if (child instanceof org.w3c.dom.CharacterData) {
	      org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
	      return cd.getData();
	    }
	    return "";
	  }

}
