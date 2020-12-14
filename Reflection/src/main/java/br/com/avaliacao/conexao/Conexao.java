package br.com.avaliacao.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	public static Connection getConnection() {
		String url = "jdbc:postgresql://localhost:5432/projeto";
		String usuario = "postgres";
		String senha  = "681683";
		try {
			return DriverManager.getConnection(url,usuario,senha);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
