public class MainServer {
    public static void main(String [] args) throws Exception{

        AndroidRemoteServer androidRemoteServer=new AndroidRemoteServer();

        while (true){
            androidRemoteServer.Connect();
        }
    }
}
