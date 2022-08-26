import java.io.*;
import java.util.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;

    public Client(Socket socket,String Username){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = Username;
        }catch(IOException e){
            closeEverthing(socket,bufferedReader,bufferedWriter);
        }
    }

    public void sendMessage(){
        try{
            bufferedWriter.write(clientUserName);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            
            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String message = scanner.nextLine();
                bufferedWriter.write(clientUserName+":"+message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            closeEverthing(socket,bufferedReader,bufferedWriter);
        }
    }

    public void listenerMessage(){

       new Thread(new Runnable() {
       
        @Override
       public void run(){
            String msgFromGroup;
            while(socket.isConnected()){
                try{
                msgFromGroup = bufferedReader.readLine();
                System.out.println(msgFromGroup);
                }catch(IOException e){
                    closeEverthing(socket,bufferedReader,bufferedWriter);
                }
            }
       }
    }).start();;
    }

    public void closeEverthing(Socket socket,BufferedReader bufferedReader,BufferedWriter bufferedWriter){
        try{
            if(socket != null)
                socket.close();

            if(bufferedReader != null)
                bufferedReader.close();
            
            if(bufferedWriter != null)
                bufferedWriter.close();
        
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws UnknownHostException, IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost",1234);
        Client client = new Client(socket,username);
        client.listenerMessage();
        client.sendMessage();
    }
}
