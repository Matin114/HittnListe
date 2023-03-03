package desktop.client.serverCommunication;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpManager {

	private static HttpManager ref;

	public static HttpManager initHttpManager() {

		if (ref == null) {
			ref = new HttpManager();
		}
		return ref;
	}

	public void setUpHttpCon() {
		URL url;
		try {

			url = new URL("http://localhost:8000/test");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");

			Map<String, String> parameters = new HashMap<>();
			parameters.put("param1", "val");

			con.setDoOutput(true);
			DataOutputStream out = new DataOutputStream(con.getOutputStream());
			out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
			out.flush();
			out.close();

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

	public class ParameterStringBuilder {
		public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
			StringBuilder result = new StringBuilder();

			for (Map.Entry<String, String> entry : params.entrySet()) {
				result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
				result.append("=");
				result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
				result.append("&");
			}

			String resultString = result.toString();
			return resultString.length() > 0 ? resultString.substring(0, resultString.length() - 1) : resultString;
		}
	}

}
