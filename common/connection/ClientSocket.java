package connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;

import communication.Action;
import communication.Command;
import communication.Message;

public class ClientSocket implements Runnable {

  // The client socket
  private static Socket clientSocket = null;
  // The output stream
  private static ObjectOutputStream os = null;
  // The input stream
  private static ObjectInputStream is = null;

  private static BufferedReader inputLine = null;
  private static boolean closed = false;
  
  public static void main(String[] args) throws ClassNotFoundException {

    // The default port.
    int portNumber = 2222;
    // The default host.
    String host = "91.67.3.71";
//    String host = "localhost";

    /*
     * Open a socket on a given host and port. Open input and output streams.
     */
    try {
      clientSocket = new Socket(host, portNumber);
      inputLine = new BufferedReader(new InputStreamReader(System.in));

      os = new ObjectOutputStream(clientSocket.getOutputStream());
      is = new ObjectInputStream(clientSocket.getInputStream());
    } catch (UnknownHostException e) {
      System.err.println("Don't know about host " + host);
    } catch (IOException e) {
      System.err.println("Couldn't get I/O for the connection to the host "
          + host);
    }

    /*
     * If everything has been initialized then we want to write some data to the
     * socket we have opened a connection to on the port portNumber.
     */
    if (clientSocket != null && os != null && is != null) {
      try {

        /* Create a thread to read from the server. */
//        new Thread(new ClientSocket()).start();
//        while (!closed) {
//          os.println(inputLine.readLine().trim());
//        }
    	System.out.println("What is your name?");
    	
    	String name = inputLine.readLine();
    	
    	System.out.println("Which class do you want to play?");
    	
    	int heroChoice = Integer.parseInt(inputLine.readLine());
    	Command command = new Command();
    	command.setCommand(0);
    	command.setParam3(name);
    	command.setParam1(heroChoice);
    	
    	os.writeObject(command);
    	
    	Command sendCommand = new Command();
    	int param1,param2,commandID;
    	String param3;
    	
    	Message message;
    	message = (Message)is.readObject();
    	Iterator<Action> iter = message.getIter();
        
        while (iter.hasNext()){
        	Action action = iter.next();
        	System.out.println(action.getField());
        	System.out.println(action.getValue());
        	System.out.println(action.getValue2());
        }
    	
    	while (!closed){
    		
    		message = (Message)is.readObject();
            
            iter = message.getIter();
            
            while (iter.hasNext()){
            	Action action = iter.next();
            	System.out.println(action.getField());
            	System.out.println(action.getValue());
            	System.out.println(action.getValue2());
            }
            
            //clear Command
            sendCommand.clear();
            
            System.out.println("Which command?");
            sendCommand.setCommand(Integer.parseInt(inputLine.readLine()));
            
            System.out.println("Which param1?");
            param1 = Integer.parseInt(inputLine.readLine());
            if(param1 >= 0)
            sendCommand.setParam1(param1);
            
            System.out.println("Which param2?");
            param2 = Integer.parseInt(inputLine.readLine());
            if(param2 >= 0)
            sendCommand.setParam2(param2);
            
            System.out.println("Which param3?");
            param3 = inputLine.readLine();
            if(param3 != null)
            	sendCommand.setParam3(param3);
            
            os.writeObject(sendCommand);

    	}  
        
        
         /*
         * Close the output stream, close the input stream, close the socket.
         */
        os.close();
        is.close();
        clientSocket.close();
      } catch (IOException e) {
        System.err.println("IOException:  " + e);
      }
    }
  }

  /*
   * Create a thread to read from the server. (non-Javadoc)
   * 
   * @see java.lang.Runnable#run()
   */
  public void run() {
    /*
     * Keep on reading from the socket till we receive "Bye" from the
     * server. Once we received that then we want to break.
     */
    String responseLine;
    try {
      while ((responseLine = is.readLine()) != null) {
        System.out.println(responseLine);
        if (responseLine.indexOf("*** Bye") != -1)
          break;
      }
      closed = true;
    } catch (IOException e) {
      System.err.println("IOException:  " + e);
    }
  }
}
