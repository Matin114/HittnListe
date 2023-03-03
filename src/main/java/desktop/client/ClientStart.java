package desktop.client;

import desktop.client.serverCommunication.HttpManager;

public class ClientStart {

	public static void main(String[] args) {
		
		HttpManager httpManager = HttpManager.initHttpManager();
		httpManager.setUpHttpCon();
		
	}

}
