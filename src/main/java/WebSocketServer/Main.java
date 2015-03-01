package WebSocketServer;

public class Main {
	public static void main(String[] args) {
		JavaWebSocketServer.getInstance();// Init the server.
		
		for(int i = 0; i < 20; i++) {
		
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("emit message" + i);
			JavaWebSocketServer.getInstance().broadcastMessage("Hello ! : " + i);
		}
	}
}
