package transaction.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

// Transaction Client class
public class TransactionClient extends Thread
{
  // Variable initialization
  Properties serverProperties = new Properties();
  InputStream input = null;
  String host;
  int port;
  int numberAccounts;
  int initialBalance;
  int numberTransactions;

  // this is the constructor to set up the server??? seems like it reads from a file with a class that does the parsing
  public TransactionClient(String propertiesFile)
  {
    try
    {
      // input = new FileInputStream( "ServerProperties.properties" );
      // serverProperties.load( input );
      Properties serverProperties = new PropertyHandler(propertiesFile);

      host = serverProperties.getProperty("HOST");
      port = Integer.parseInt(serverProperties.getProperty("PORT"));
      numberAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS"));
      initialBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE"));

      
      Properties serverProperties = new PropertyHandler(propertiesFile);
      // establishing client properties
      numberTransactions = Integer.parseInt(serverProperties.getProperty("NUMBER_TRANSACTIONS"));

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      if( input != null )
      {
        try
        {
          input.close();
        }
        catch ( IOException e )
        {
          e.printStackTrace();
        }
      }
    }

    StringBuffer log = new StringBuffer("");
  }


  @Override
  public void run()
  {
    // Spawns off proxy threads
    // TServerProxy worker = new TServerProxy();
    // Thread proxyWorker = new Thread( worker );
    // proxyWorker.start();
    int index;
    for ( index = 0; index < numberTransactions; index++ )
    {
      new Thread()
      {
        @Override
        public void run()
        {
          TServerProxy transaction = new TServerProxy( host, port );
          int transID = transaction.openTransaction();
          System.out.println( "transaction #" + transID + " started" );

          int accountFrom = (int) Math.floor(Math.random() * numberAccounts );
          int accountTo = (int) Math.floor(Math.random() * numberAccounts );
          int amount = (int) Math.ceil(Math.random() * initialBalance );
          int balance;
          System.out.println( "\ttransaction #" + transID + ", $" + amount + " " + accountFrom + "->" + accountTo );

          balance = transaction.read(accountFrom);
          transaction.write( accountFrom, balance - amount );

          balance = transaction.read(accountTo);
          transaction.write( accountTo, balance + amount );

          transaction.closeTransaction();

          System.out.println("transaction #" + transID + " finished");
        }
      }.start();
    }
  }

  public static void main(String[] args)
  {
    (new TransactionClient("../../config/TransactionClient.properties", "../../config/TransactionServer.properties")).start();
  }
}
