import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClient {
    public static void main (String args[]) {
        Socket s = null;
        try {
            int serverPort = 7896; //porto do servidor
            s = new Socket("localhost", serverPort);
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            out.writeUTF("mensagem em UTF"); //Envia os dados para o servidor
            String data = in.readUTF(); // Bloqueia à espera da resposta do servidor
            System.out.println("Received: "+ data);
        } catch (UnknownHostException e) {
            System.out.println("Sock:"+e.getMessage());
        } catch (IOException e) {
            System.out.println("IO:" + e.getMessage());
        } finally {
            if(s!=null) {
                try {
                    s.close();
                } catch (IOException e) {
                    System.out.println("close:"+e.getMessage());
                }
            }
        }
    }
}