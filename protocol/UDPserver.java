package protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPserver {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        DatagramSocket serverSocket = new DatagramSocket(5678);
        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];
        while(true){
            DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
            System.out.println("udp is processing");
            serverSocket.receive(receivePacket);
            String text = new String(receivePacket.getData());
            InetAddress ip = receivePacket.getAddress();
            String address = ip.getHostAddress();
            int port = receivePacket.getPort();
            String response ="the message is "+text;
            System.out.println(response);
            System.out.println("from client "+address+" on port "+port);
            String capSentence = text.toUpperCase()+"\n";
            sendData = capSentence.getBytes();
            DatagramPacket sendPacket=  new DatagramPacket(sendData,sendData.length,ip,port);
            serverSocket.send(sendPacket);
        }
    }
}
