import java.net.*;
import java.io.*;
import java.util.*;

public class UDPServer {
    public static class Message {
        private int messageId;
        private String messageText;

        public Message(int messageId, String messageText) {
            this.messageId = messageId;
            this.messageText = messageText;
        }

        public int getMessageId() {
            return messageId;
        }

        public String getMessageText() {
            return messageText;
        }

        @Override
        public String toString() {
            return "(" + messageId + ", " + messageText + ")";
        }
    }

    private static final LinkedList<Message> mensagensEntregues = new LinkedList<>();
    private static final HashMap<Integer, Message> mensagensTemp = new HashMap<>();
    private static int lastMessageInOrderId = 0;

    private static byte[] processDeliveredMessages(int nCurrentMessage,
                                    String currentMessage) {

        if(nCurrentMessage != lastMessageInOrderId + 1) {
            mensagensTemp.put(nCurrentMessage, new Message(nCurrentMessage, currentMessage));
            return ("waitingfor," + (lastMessageInOrderId + 1)).getBytes();
        } else {
            mensagensEntregues.add(new Message(nCurrentMessage, currentMessage));

            byte[] response = currentMessage.getBytes();

            if(mensagensTemp.containsKey(nCurrentMessage + 1)){
                Iterator<Map.Entry<Integer, Message>> it = mensagensTemp.entrySet().iterator();
                int nextMessageInOrderId = nCurrentMessage + 1;

                while (it.hasNext()) {
                    Map.Entry<Integer, Message> pair = it.next();
                    if(pair.getKey() == nextMessageInOrderId) {
                        nextMessageInOrderId++;
                        it.remove();
                        mensagensEntregues.add(pair.getValue());
                    }
                }

                lastMessageInOrderId = mensagensEntregues.getLast().getMessageId();

                if(!mensagensTemp.isEmpty())
                    response = ("waitingfor," + (lastMessageInOrderId + 1)).getBytes();
            }

            lastMessageInOrderId = mensagensEntregues.getLast().getMessageId();
            return response;
        }
    }

    public static void main(String[] args){

        try (DatagramSocket aSocket = new DatagramSocket(6789)) {
            byte[] buffer = new byte[1000];

            while (true) {
                DatagramPacket request = new DatagramPacket(buffer, buffer.length);
                aSocket.receive(request);

                String mensagem = new String(request.getData());

                String messageId = mensagem.substring(0, mensagem.indexOf(","));
                String messageText = mensagem.substring(mensagem.indexOf(",")+1);

                int messageIdInt = Integer.parseInt(messageId);

                byte[] clientResponse = processDeliveredMessages(messageIdInt, messageText);

                DatagramPacket reply = new DatagramPacket(clientResponse,
                        clientResponse.length, request.getAddress(), request.getPort());

                aSocket.send(reply);
            }
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        }
    }
}
