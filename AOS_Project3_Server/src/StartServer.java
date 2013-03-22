import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;


public class StartServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocketChannel ssChannel;
		
		try {
			ssChannel = ServerSocketChannel.open();
			ssChannel.socket().bind(new InetSocketAddress("localhost", 5000));
			
			while(true)
			{
				SocketChannel channel = ssChannel.accept();
			}
			
		} catch (Exception e) {
			System.out.println("Exception in main!!!");
			e.printStackTrace();
		}
		
		
	}

}
