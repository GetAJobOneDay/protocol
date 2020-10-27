package protocol;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.HashMap;

public class client {
    public static void main(String[] args) throws IOException {
        BufferedReader x= new BufferedReader(new InputStreamReader(System.in));
        HashMap<String,String> status =  new HashMap<>();
        do{
            System.out.println("-x for message -t for type udp or tcp -s for hostname -p for port");
            String read = x.readLine();
            read = read.replaceAll("(?m)^[ \t]*\r?\n", "");
            String[] con = read.trim().split("-");
            for(String s : con) {
                if(s.equals("")){
                    continue;
                }else {
                    String[] message = s.trim().split(" ");
                    status.put(message[0], message[1]);
                }
            }

        }while (status.size()<4);
        String protocol = status.get("t");
        int port = Integer.parseInt(status.get("p"));
        String host = status.get("s");
        String message = status.get("x");
        if(protocol.equalsIgnoreCase("udp")){
            udp(port,host,message);
        }else if(protocol.equalsIgnoreCase("tcp")){
            tcp(port,host,message);
        }else{
            System.out.println("your protocol is not correct");
        }
    }
    public static void udp(int port,String host,String message) throws IOException {
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress ip = InetAddress.getByName(host);
        sendData =message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,ip,port);
        clientSocket.send(sendPacket);
        DatagramPacket receivePacket = new DatagramPacket(receiveData,receiveData.length);
        clientSocket.setSoTimeout(3000);
        try{
            clientSocket.receive(receivePacket);
            String fromServer = new String(receivePacket.getData());
            System.out.println("from UDPserver:"+fromServer.trim());
            clientSocket.close();
        }catch (SocketTimeoutException e){
            System.out.println(e.getMessage()+" more than 3 seconds");

        }
    }
    private static void tcp(int port,String host,String message) throws IOException {
        String modifiedSentence;
        Socket clientSocket = new Socket(host, port);
        DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("enter your message");
        outToServer.writeBytes(message + "\n");
        clientSocket.setSoTimeout(3000);
        try {
            modifiedSentence = inFromServer.readLine();
            System.out.println("FROM SERVER: "+modifiedSentence);
            clientSocket.close();
        }catch (SocketTimeoutException |ConnectException e){
            System.out.println(e.getMessage());
        }
    }
}

