package aChattingRoom;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
 
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
 
/**
 * �ͻ���
 * @author ������
 *
 */
public class MyClient implements Runnable {
	TextArea receivedMsgArea;
	TextField ipText;
	TextField portText;
	TextArea sendMsgArea;
	TextField statusText;
	Button sendButton;
 
	public MyClient() {
		super();
	}
 
	public MyClient(TextArea receivedMsgArea, TextField ipText, TextField portText, TextArea sendMsgArea,
			TextField statusText, Button sendButton) {
		super();
		this.receivedMsgArea = receivedMsgArea;
		this.ipText = ipText;
		this.portText = portText;
		this.sendMsgArea = sendMsgArea;
		this.statusText = statusText;
		this.sendButton = sendButton;
	}
	
	/**
	 * ����UI�����IP�Ͷ˿�
	 */
	public void updateIpAndPort(Socket socket) {
		Platform.runLater(()->{
			ipText.setText(socket.getLocalAddress().toString().substring(1));
			portText.setText(String.valueOf(socket.getLocalPort()));
		});
	}
 
	@Override
	public void run() {
		try {
			Socket socket = new Socket("127.0.0.1", 9999);
			updateIpAndPort(socket);
			statusText.setText("Success connected..");
			receivedMsgArea.appendText("Hello, I am server..." + "\n");
			InputStream in = socket.getInputStream();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
			OutputStream out = socket.getOutputStream();
			PrintWriter pWriter = new PrintWriter(out);
			//����Ϣ
			sendButton.setOnAction(e->{
				pWriter.write(socket.getLocalSocketAddress().toString().substring(1) + "  " + sendMsgArea.getText() + "\r\n");
				pWriter.flush();
			});
			//����Ϣ
			String message;
			while(true) {
				message = bReader.readLine();
				receivedMsgArea.appendText(message + "\n");
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			statusText.setText("Server is closed.");
			receivedMsgArea.appendText("Server is closed.");
			e.printStackTrace();
		}
	}
}
