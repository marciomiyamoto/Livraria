package livraria.controle.web.util;

import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dominio.venda.CupomTroca;
import dominio.venda.Pedido;

public class JavaMailApp {

	public void enviarEmail(Pedido pedido, CupomTroca cupom) {
		Properties props = new Properties();
		/** Parâmetros de conexão com servidor Gmail */
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("marcio.les2017@gmail.com", "Teste123");
			}
		});
		/** Ativa Debug para sessão */
		session.setDebug(true);
		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("marcio.les2017@gmail.com")); // Remetente

			Address[] toUser = InternetAddress // Destinatário(s)
					.parse(pedido.getCliente().getEmail());
			message.setRecipients(Message.RecipientType.TO, toUser);
			message.setSubject("Cupom de desconto Ref pedido " + pedido.getId());// Assunto
			message.setText("Cliente: " + pedido.getCliente().getNome() +
							"\nCodigo do cupom: " + cupom.getCodigo() +
							"\nValor: " + cupom.getValor());
			/** Método para enviar a mensagem criada */
			Transport.send(message);
			System.out.println("Feito!!!");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
}
