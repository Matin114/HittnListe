package httpTest.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class HTTPServer {

	public static void main(String[] args) {
		
		new ActualServer().start();
		
	}
	
}

class ActualServer {
	public void start() {
		HttpServer server;
		try {
			server = HttpServer.create(new InetSocketAddress("localhost", 8000), 0);
			server.createContext("/test", new TestHandler());
			server.setExecutor(Executors.newCachedThreadPool());
			server.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Http server started?");
		
	}
}

class TestExecuter implements Executor {

	@Override
	public void execute(Runnable command) {
		System.out.println(command.getClass());
		command.run();
	}
	
}

class TestHandler implements HttpHandler {

	@Override
	public void handle(HttpExchange exchange) throws IOException {
		while (true) {
			System.out.println(Thread.currentThread().getId());
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}