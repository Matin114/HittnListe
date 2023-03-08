package httpTest.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

public class HttpManager {

	public static void main(String[] args) {

		jsonRequest();
	}
	
	public static void mobobRequest() {
		URL url;
		try {

			url = new URL("http://localhost:8000/test");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			Map<String, String> parameters = new HashMap<>();
			parameters.put("param1", "val");


			if (con.getResponseCode() == 200) {
				System.out.println("Http connection established");
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer content = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();
			
			System.out.println(content.toString());

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void jsonRequest() {
		URI uri;
		try {
			HttpClient client = HttpClient.newHttpClient();
			uri = new URI("http://localhost:8080/test/");
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.append("testKey", "helloHTTP");
			jsonObject.append("anotherKey", "Yeet");
			
			HttpRequest request = HttpRequest.newBuilder(uri).
					POST(BodyPublishers.ofString(jsonObject.toString()))
					.header("Content-type", "application/json").
					build();
			
			HttpResponse<Void> response = client.send(request, BodyHandlers.discarding());
			System.out.println(response.toString());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
