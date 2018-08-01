import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

import static java.lang.System.out;

public class AndroidRemoteServer {

    public static InetAddress localAddr;

    public void Connect() {

        ServerSocket server = null;
        Socket client = null;
        BufferedReader in = null;
        String line;
        boolean isConnected=true;
        Robot robot = null;
        int C;
        final int SERVER_PORT = 4444;

        boolean leftpressed=false;
        boolean rightpressed=false;
        try{
            robot = new Robot();
            server = new ServerSocket(SERVER_PORT);
            //IPAddress();
            out.println("Waiting for Client");
            client = server.accept();
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

        }catch (IOException e) {
            out.println("Error in opening Socket");
            System.exit(-1);
        }catch (AWTException e) {
            out.println("Error in creating robot instance");
            System.exit(-1);
        }

        while(isConnected){
            try{
                line = in.readLine();
                out.println(line);

                if(line.equalsIgnoreCase("next")){
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                }

                else if(line.equalsIgnoreCase("previous")){
                    robot.keyPress(KeyEvent.VK_LEFT);
                    robot.keyRelease(KeyEvent.VK_LEFT);
                }
                else if(line.equalsIgnoreCase("play")){
                    robot.keyPress(KeyEvent.VK_SPACE);
                    robot.keyRelease(KeyEvent.VK_SPACE);
                }
                else if (line.equalsIgnoreCase("shut")){
                    Shutdown();
                }
                else if (line.equalsIgnoreCase("res")){
                    Restart();
                }
                else if(line.equalsIgnoreCase("escape")) {
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    robot.keyRelease(KeyEvent.VK_ESCAPE);
                }
                else if(line.contains(",")){
                    float movex=Float.parseFloat(line.split(",")[0]);
                    float movey=Float.parseFloat(line.split(",")[1]);
                    Point point = MouseInfo.getPointerInfo().getLocation();
                    float nowx=point.x;
                    float nowy=point.y;
                    robot.mouseMove((int)(nowx+movex),(int)(nowy+movey));
                }
                else if(line.contains("left_click")){
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                }
                else if(line.contains("right_click")) {
                    robot.mousePress(InputEvent.BUTTON2_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                }
                else if (line.contains("keyboard")){
                        int I=in.read();
                        robot.keyPress(KeyEvent.getExtendedKeyCodeForChar(I));
                        robot.keyRelease(KeyEvent.getExtendedKeyCodeForChar(I));
                }
                else if (line.equalsIgnoreCase("terminate")){
                    System.exit(1);
                }
                else if(line.equalsIgnoreCase("exit")){
                    isConnected=false;
                    server.close();
                    client.close();
                }
            } catch (IOException e) {
                out.println("Read failed");
                System.exit(-1);
            }
        }
        try {
            server.close();
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void IPAddress() throws SocketException {

        String sHost = "";
        try {
            localAddr = InetAddress.getLocalHost();
            if (localAddr.isLoopbackAddress()) {
                localAddr = LinuxInetAddress.getLocalHost();
            }
            sHost = localAddr.getHostAddress();
            System.out.println(sHost);
        } catch (UnknownHostException ex) {
            sHost = "Error finding local IP.";
        }
    }

    public static void Shutdown() throws RuntimeException,IOException{

        System.out.println("In Shutdown");
        String shutdownCommand;
        String operatingSystem=System.getProperty("os.name");

        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)){
            shutdownCommand="shutdown -h now";
        }
        else if ("Windows".equals(operatingSystem)){
            shutdownCommand="shutdown -s -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }
        Runtime.getRuntime().exec(shutdownCommand);
    }

    public static void Restart() throws IOException,RuntimeException {
        String shutdownCommand;
        String operatingSystem=System.getProperty("os.name");

        if ("Linux".equals(operatingSystem) || "Mac OS X".equals(operatingSystem)){
            shutdownCommand="shutdown -r now";
        }
        else if ("Windows".equals(operatingSystem)){
            shutdownCommand="shutdown -r -t 0";
        }
        else {
            throw new RuntimeException("Unsupported operating system.");
        }
        Runtime.getRuntime().exec(shutdownCommand);
    }
}