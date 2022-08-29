import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }
    
    public void startServer(){
        try{
            while(!serverSocket.isClosed()){//while server socket is open,waitting client connected
                Socket socket = serverSocket.accept();//meaning program will be halted here until a client connects
                System.out.println("A new client is connected!");
                
                //each object of this class will be responsible for communication with a client
                ClientHandler clientHandler = new ClientHandler(socket);//pass in socket

                //if we didn't spawn a new thread to handle the connection with each new client 
                //our application would only be able to handle one client at a time(cause need to multiple users connect)
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        }catch(IOException e){
            
        }
    }

    public void closeServerSocket(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException{
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServer();
    }
}
