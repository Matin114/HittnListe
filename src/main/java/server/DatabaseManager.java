package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

	private static Connection connection;
	
	public static Connection getConnection() throws ClassNotFoundException, SQLException {
		
		if(connection == null) {
			establishDatabaseConnection();
		}
		return connection;
	}
	
	private static void establishDatabaseConnection() throws SQLException, ClassNotFoundException {
	//Just some first trys to connect to a local database at this stage only example
		
//		Class.forName("oracle.jdbc.driver.OracleDriver");
//		connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","c##john","abcd1234");  
//
//		PreparedStatement statement = connection.prepareStatement("select COUNT(*) from biffluser");
//		ResultSet result = statement.executeQuery();
//		
//		while(result.next()) {
//			if(result.getString(1) != null) {
//				System.out.println("Database connected");
//			}
//		}
	}
	
	public static ResultSet doQuery(String statementString) throws SQLException {
		return connection.prepareStatement(statementString).executeQuery();
	}
	
}
