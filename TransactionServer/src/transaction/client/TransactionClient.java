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
    System.out.println("THIS IS THE NUMBER OF TRANSACTIONS");
    System.out.println(numberTransactions);
    
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
          
          System.out.println("WE MADE IT ABOVE THE WRITE IN THE CLIENT");
          transaction.write( accountFrom, balance - amount );
          System.out.println("WE MADE IT BELOW THE WRITE IN THE CLIENT");
          
          
          balance = transaction.read(accountTo);
          transaction.write( accountTo, balance + amount );

          transaction.closeTransaction();

          System.out.println("transaction #" + transID + " finished");
        }
      }.start();
      
    }
  }
}
