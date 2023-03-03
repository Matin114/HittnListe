package server;

import java.sql.Connection;
import java.sql.SQLException;

import server.clientcommunication.ClientCommunicationManager;

public class ServerStart {

	public static void main(String[] args) {
		
		try {
			Connection connection = DatabaseManager.getConnection();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		
		ClientCommunicationManager.startServer();
		
	}
	
}
