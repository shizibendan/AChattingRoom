package aChattingRoom;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
 
/**
 * �ͻ���UI
 * @author ������
 *
 */
public class ClientUI extends Application{
	final TextArea receivedMsgArea = new TextArea();
	final TextField ipText = new TextField();
	final TextField portText = new TextField();
	final TextArea sendMsgArea = new TextArea();
	final TextField statusText = new TextField();
	final Button sendButton = new Button(" Send ");
 
	@Override
	public void start(Stage primaryStage) throws Exception {
		//�ұ� Received Message
		GridPane rightPane = new GridPane();
		rightPane.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		rightPane.setHgap(5.5);
		rightPane.setVgap(5.5);
		rightPane.add(new Label("Received Message:"), 0, 0);
		receivedMsgArea.setWrapText(true);
		receivedMsgArea.setEditable(false);
		receivedMsgArea.setMaxWidth(350);
		receivedMsgArea.setPrefHeight(410);
		rightPane.add(receivedMsgArea, 0, 1);
		
		//��� IPAdress+Port
		GridPane leftPane1 = new GridPane();
		leftPane1.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		leftPane1.setHgap(5.5);
		leftPane1.setVgap(5.5);
		leftPane1.add(new Label("IPAdress:"), 0, 0);
		ipText.setEditable(false);
		leftPane1.add(ipText, 1, 0);
		leftPane1.add(new Label("Port:"), 0, 1);
		portText.setEditable(false);
		leftPane1.add(portText, 1, 1);
		
		//��� Send Message
		GridPane leftPane2 = new GridPane();
		leftPane2.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		leftPane2.setHgap(5.5);
		leftPane2.setVgap(5.5);
		leftPane2.add(new Label("Send Message:"), 0, 2);
		sendMsgArea.setPrefHeight(250);
		sendMsgArea.setMaxWidth(275);
		sendMsgArea.setWrapText(true);
		leftPane2.add(sendMsgArea, 0, 3);
		
		//��� Connect Status + button
		GridPane leftPane3 = new GridPane();
		leftPane3.setPadding(new Insets(11.5, 12.5, 13.5, 14.5));
		leftPane3.setHgap(5.5);
		leftPane3.setVgap(5.5);
		statusText.setEditable(false);
		leftPane3.add(statusText, 0, 0);
		leftPane3.add(sendButton, 1, 0);
		
		//���
		VBox vBox = new VBox();
		vBox.getChildren().addAll(leftPane1, leftPane2, leftPane3);
		HBox hBox = new HBox();
		hBox.getChildren().addAll(vBox, rightPane);
		
		Scene scene = new Scene(hBox);
		primaryStage.setTitle("client");
		primaryStage.setScene(scene);
		//�ر�UI�߳�ʱͬʱ�رո����߳�
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
		primaryStage.show();
		
		//����Client�߳�
		new Thread(new MyClient(receivedMsgArea, ipText, portText, sendMsgArea, statusText, sendButton)).start();
	}
	public static void main(String[] args) {
		launch(args);
	}
}

