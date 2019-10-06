package aChattingRoom;


import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
 
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
 
/**
 * �����
 * @author ������
 *
 */
public class MyServer implements Runnable {
	
	//Server�˼����Ķ˿ں�
	public static final int PORT = 9999;
	//ӳ��� ���ÿ��socket��ַ(IP:Port)�����Ӧ��PrintWriter
	//ΪȺ����Ϣ��׼��
	Map<String, PrintWriter> map = new HashMap<>();
	//���������socket��ַ(IP:Port)������clientListView
	ObservableList<String> clients;
	ListView<String> clientListView;
	
	TextField ipText;
	TextField portText;
	TextArea sendMsgArea;
	TextField statusText;
	Button sendButton;
	TextArea receivedMsgArea;
 
	public MyServer() {
		
	}
	
	public MyServer(TextField ipText, TextField portText, TextArea sendMsgArea, TextField statusText, 
			Button sendButton, TextArea receivedMsgArea, ObservableList<String> clients, ListView<String> clientListView) {
		super();
		this.ipText = ipText;
		this.portText = portText;
		this.sendMsgArea = sendMsgArea;
		this.statusText = statusText;
		this.sendButton = sendButton;
		this.receivedMsgArea = receivedMsgArea;
		this.clients = clients;
		this.clientListView = clientListView;
	}
	
	/**
	 * ����UI�����IP��Port
	 */
	public void updateIpAndPort() {
		//�����ڷ�UI�̸߳���UI����
		Platform.runLater(()->{
			ipText.setText("127.0.0.1");
			portText.setText(String.valueOf(PORT));
		});
	}
	
	@Override
	public void run() {
		updateIpAndPort();
		ServerSocket server;
		Socket socket;
		try {
			server = new ServerSocket(PORT);
			while(true) {
				socket = server.accept();
				//һ���ͻ��˽��������һ��handler�߳�ȥ����
				new Thread(new Handler(map, socket, sendMsgArea, statusText, sendButton, receivedMsgArea, clients, clientListView)).start();
			}
		} catch (IOException e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
		}
	}
 
}
