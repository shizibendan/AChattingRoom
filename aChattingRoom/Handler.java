package aChattingRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
 
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
 
/**
 * �����߳�
 * @author ������
 *
 */
public class Handler implements Runnable {
	
	Socket socket;
	TextArea sendMsgArea;
	TextField statusText;
	Button sendButton;
	TextArea receivedMsgArea;
	ObservableList<String> clients;
	ListView<String> clientListView;
	Map<String, PrintWriter> map;
	
	public Handler() {
		super();
	}
 
	public Handler(Map<String, PrintWriter> map, Socket socket, TextArea sendMsgArea, TextField statusText, Button sendButton,
			TextArea receivedMsgArea, ObservableList<String> clients, ListView<String> clientListView) {
		super();
		this.map = map;
		this.socket = socket;
		this.sendMsgArea = sendMsgArea;
		this.statusText = statusText;
		this.sendButton = sendButton;
		this.receivedMsgArea = receivedMsgArea;
		this.clients = clients;
		this.clientListView = clientListView;
	}
	
	/**
	 * ����ͻ��˺󣬸���UI����
	 * 1.����½���ͻ��˵ĵ�ַ��Ϣ
	 * 2.receivedMsgarea��ӡ�ɹ�������Ϣ
	 * 3.statusText���³ɹ����Ӹ���
	 */
	public void updateForConnect(String remoteSocketAddress) {
		Platform.runLater(()->{
			clients.add(remoteSocketAddress);
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			receivedMsgArea.appendText(String.valueOf(clients.size()) + " Connected from " + remoteSocketAddress + " " + sdf.format(new Date()) + "\n");
			statusText.setText(String.valueOf(clients.size()) + " Connect success.");
		});
	}
	
	/**
	 * �Ͽ��ͻ��˺󣬸���UI����
	 * 1.�Ƴ��Ͽ��ͻ��˵ĵ�ַ��Ϣ
	 * 2.receivedMsgarea��ӡ�Ͽ�������Ϣ
	 * 3.statusText���³ɹ����Ӹ���
	 * 4.�Ƴ�map�ж�Ӧ��remoteSocketAddress
	 */
	public void updateForDisConnect(String remoteSocketAddress) {
		Platform.runLater(()->{
			clients.remove(remoteSocketAddress);
			statusText.setText(String.valueOf(clients.size()) + " Connect success.");
			receivedMsgArea.appendText(remoteSocketAddress + " out of connected.." + "\n");
			map.remove(remoteSocketAddress);
		});
	}
	
	/**
	 * ������Ⱥ����Ϣ
	 * 1.ΪclientListView���ü����� 
	 *   1.1��ȡ��ѡ�����(IP:Port)
	 *   1.2��ӳ�����ȡ����ӦprintWriter����printWriters����
	 * 2.ΪsendButton����������¼�
	 *   2.1����printWriters����
	 *   2.2д������͵���Ϣ
	 */
	public void sendMessage() {
		Set<PrintWriter> printWriters = new HashSet<>();
		clientListView.getSelectionModel().selectedItemProperty().addListener(ov->{
			printWriters.clear();
			for(String key: clientListView.getSelectionModel().getSelectedItems()) {
				printWriters.add(map.get(key));
			}
		});
		sendButton.setOnAction(e->{
			for (PrintWriter printWriter : printWriters) {
				printWriter.write("127.0.0.1:9999" + "  " + sendMsgArea.getText() + "\r\n");
				printWriter.flush();
			}
		});
	}
	
	@Override
	public void run() {
		String remoteSocketAddress = socket.getRemoteSocketAddress().toString().substring(1);
		updateForConnect(remoteSocketAddress);
		try {
			InputStream in = socket.getInputStream();
			BufferedReader bReader = new BufferedReader(new InputStreamReader(in));
			OutputStream out = socket.getOutputStream();
			PrintWriter pWriter = new PrintWriter(out);
			map.put(remoteSocketAddress, pWriter);
			//����Ϣ
			sendMessage();
			//����Ϣ
			String message;
			while(true) {
				message = bReader.readLine();
				receivedMsgArea.appendText(message + "\n");
			}
		} catch (IOException e) {
			updateForDisConnect(remoteSocketAddress);
		}
	}
 
}

