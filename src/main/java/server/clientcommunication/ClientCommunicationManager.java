package server.clientcommunication;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.*;

public class ClientCommunicationManager {

	public static void startServer() {

		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress("127.0.0.1",8000), 0);
			server.createContext("/test", new MyHandler());
			server.setExecutor(null); // creates a default executor
			server.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Http server started?");
	}

	static class MyHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange t) throws IOException {
			String response = "This is the response";
			t.sendResponseHeaders(200, response.length());
			OutputStream os = t.getResponseBody();
			os.write(response.getBytes());
			os.close();
		}
	}

}
