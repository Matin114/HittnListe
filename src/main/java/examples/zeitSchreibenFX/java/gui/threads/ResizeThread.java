package examples.zeitSchreibenFX.java.gui.threads;

import javafx.scene.Node;

public class ResizeThread extends Thread implements Runnable {

	private Node node;
	private int time;

	public ResizeThread(Node node, int time) {
		this.node = node;
		this.time = time;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		node.autosize();
	}

}
