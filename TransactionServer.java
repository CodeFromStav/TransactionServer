

/*abstract
Your transaction server will manage X number
of data objects that can be thought of as banking
accounts; each account is holding a certain amount of money.
The number of accounts is configurable.

-On the other hand, your transactional system,
 including the server proxy object on the client
  side, is absolutely unaware of the assumption of
  such a predefined sequence of activities, i.e. it
  can handle any activities in any order whatsoever,
  without any limitation or underlying assumption.

  // Create AccountManager object
    AccountManager accountManager = new AccountManager();

  // Create LockManager object
    LockManager lockManager = new LockManager();

  // Create TransactionManager object
    TransactionManager transactionManager = new TransactionManager();


//Server loop always running in this class
run()
{
while(true)
{
try
{
transactionManager.runTransaction(serverSocket.accept());
}
catch (IOException e)
{
//print Error accepting client
//print StackTrace
}
}
*/

import java.net.*;
import java.io.*;
import java.util.*;

// server loop that we implemented in other projects
public class TransactionServer {
    public static void main(String[] args) throws IOException
	  {

      // empty server socket
        ServerSocket server = null;
        try {
            // Starts the server up
            server = new ServerSocket(user1.getPort());
            // System.out.println(server); // this is for testing
            server.setReuseAddress(true);
            System.out.println("Server listening...");

            while (true) {
                final Socket client = server.accept();
                System.out.println("New client connected " + client.getInetAddress().getHostAddress());
                final EchoThread clientSock = new EchoThread(client);


                // Background thread will handle each client separately
                new Thread(clientSock).start();
            }
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }



    // EchoThread handles talking to the client
    private static class EchoThread implements Runnable
    {
      private final Socket clientSocket;

      public EchoThread(final Socket socket)
      {
        this.clientSocket = socket;
      }

      @Override
      public void run() {
        PrintWriter toClient = null;
        BufferedReader fromClient = null;
        try
        {
          toClient = new PrintWriter(clientSocket.getOutputStream(), true);
          fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          String line;
            // Display what is being sent by client to the server
            while ((line = fromClient.readLine()) != null)
            {
              System.out.printf("Sent from the client: %s\n", line);
              String lineCheck = line.replaceAll("[^\\p{IsAlphabetic}]", "");
              toClient.println(lineCheck);
              if("quit".equalsIgnoreCase(lineCheck)){
                  break;
              }
            }
          }
          catch (final IOException e)
          {
            e.printStackTrace();
          }
          finally
          {
            try
            {
              if (toClient != null)
              {
                toClient.close();
              }
                if (fromClient != null)
                {
                  fromClient.close();
                  // Client has disconnected from the server
                  System.out.println("Client disconnecting...");
                  clientSocket.close();
                }
                catch (final IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
