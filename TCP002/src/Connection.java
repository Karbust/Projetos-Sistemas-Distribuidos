import java.io.*;
import java.net.Socket;

public class Connection extends Thread {
    ObjectInputStream in;
    DataOutputStream out;
    Socket clientSocket;

    public Connection (Socket aClientSocket) {
        try {
            clientSocket = aClientSocket;
            in = new ObjectInputStream(clientSocket.getInputStream());
            out = new DataOutputStream(clientSocket.getOutputStream());

            this.start();
        } catch(IOException e) {
            System.out.println("Connection:"+e.getMessage());
        }
    }

    public void run(){
        try {
            Person person = (Person) in.readObject();
            out.writeUTF(person.getPlace().getLocality());
        } catch(EOFException e) {
            System.out.println("EOF:" + e.getMessage());
        } catch(IOException e) {
            System.out.println("IO:" + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException:" + e.getMessage());
        } finally{
            try {
                clientSocket.close();
            }catch (IOException e){
                /*close failed*/
            }
        }
    }
}