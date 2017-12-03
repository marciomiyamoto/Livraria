package livraria.core.util.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoOracle {

	public static final String url = "jdbc:oracle:thin:@localhost:1521:xe";
	public static final String usuario = "marcio_livraria";
	public static final String senha = "1234";
	
	public static Connection getConnection() throws ClassNotFoundException, 
	SQLException {
		
//		Connection conn = null;
//		try {
			Connection conn = DriverManager.getConnection(url, usuario, senha);
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
		return conn;
	}
}
