package transaction.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import transaction.comm.Message;
import transaction.comm.MessageTypes;
import java.io.*;

// Transaction Server Proxy class
//Transaction client hits proxy, then the proxy server manages workers.
//represents transaction, which spins threads.
public class TServerProxy implements MessageTypes 
{
  String host = null;
  int port;

  // Four variables below were all private previously
  Socket dbConnection = null;
  ObjectOutputStream writeToNet = null;
  ObjectInputStream readFromNet = null;
  Integer transID = 0;

  public TServerProxy(String host, int port)
  {
    this.host = host;
    this.port = port;
  }

  public int openTransaction()
  {
    // create socket connection
    try
    {
        // connect to the server loop
        dbConnection = new Socket(host, port);
        System.out.println("[TServerProxy.open] Proxy connected!");
        // assign the object input and output
        writeToNet = new ObjectOutputStream( dbConnection.getOutputStream() );
        readFromNet = new ObjectInputStream( dbConnection.getInputStream() );
        transID++;
        System.out.println( "TServerProxy streams are set up." );

    }
    catch (IOException e)
    {
      System.out.println("[TServerProxy.open] Proxy error occurred!");
      e.printStackTrace();
    }
    
    return transID;

  }
  

  // close the transaction that was established
  public void closeTransaction()
  {
	try 
	{
		dbConnection.close();
		writeToNet.close();
		readFromNet.close();
	}
    catch (IOException e)
	{
    	System.out.println("[TServerProxy.open] Proxy error occurred when trying to close");
	}
    System.out.println("[TServerProxy.close] the transaction has ended");

  }
  
  // read the account number balance
  public int read(int accountNumber)
  {
    Message readMessage = new Message(READ_REQUEST, accountNumber);
    Integer balance = null;

    try
    {
    
      // 	
      System.out.println( "Attempting to send read request...");
      writeToNet.writeObject(readMessage);
      System.out.println( "Read request received..." );
   
      // 
      System.out.println( "Reading from read request now..." );
      balance = (Integer) readFromNet.readObject();
      System.out.println( "Balance is: " + balance );
      
           
    }
    catch (Exception ex)
    {
      System.out.println("[TServerProxy.read] Error occured while trying to read " );
      ex.printStackTrace();
    }

    return balance;

  }

  public int write(int accountNumber, int amount)
  {
    Message writeMessage = new Message( WRITE_REQUEST, amount );
    Integer balance = amount;

    try
    {
      writeToNet.writeObject(writeMessage);
    }
    catch (Exception ex)
    {
      System.out.println( "[TServerProxy.write] Error occured." );
      ex.printStackTrace();
    }

    return balance;
  }

}
