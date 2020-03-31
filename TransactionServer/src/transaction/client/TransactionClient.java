package transaction.client;

import java.util.Properties;
import utils.PropertyHandler;


// Transaction Client class
public class TransactionClient extends Thread
{
  // Variable initialization
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
      Properties serverProperties = new PropertyHandler(propertiesFile);
      host = serverProperties.getProperty("HOST");
      port = Integer.parseInt(serverProperties.getProperty("PORT"));
      numberAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS"));
      initialBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE"));

      numberTransactions = Integer.parseInt(serverProperties.getProperty("NUMBER_TRANSACTIONS"));
      System.out.println( "Client properties loaded..." );

    } 
    catch (Exception ex)
    {
      ex.printStackTrace();
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
      int index_1 = 1;
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
          System.out.println( "\ttransaction #" + index_1 + ", $" + amount + " from account " + accountFrom + "->" + accountTo );
          
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

//  public static void main(String[] args)
//  {
//	  (new TransactionClient("../config/ServerProperties.properties")).start();
//	  
//  }
}
