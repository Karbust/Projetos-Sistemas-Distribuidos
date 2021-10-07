import java.net.*;
import java.io.*;

public class UDPServer {
    public static void main(String[] args){

        try (DatagramSocket aSocket = new DatagramSocket(6789)) {
            byte[] buffer = new byte[1000];
            int currentMessageId = 0;

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                String mensagem = new String(request.getData());

                String messageId = mensagem.substring(0, mensagem.indexOf(","));
                String remainder = mensagem.substring(mensagem.indexOf(",")+1);

                int messageIdInt = Integer.parseInt(messageId);

                byte[] response;

                if(messageIdInt != currentMessageId + 1) {
                    response = ("waitingfor," + (currentMessageId + 1)).getBytes();
                } else {
                    response = remainder.getBytes();
                    currentMessageId++;

                    //System.out.println("Message " + messageId + ": " + remainder);
                }

                DatagramPacket reply = new DatagramPacket(response,
                        response.length, request.getAddress(), request.getPort());

                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
