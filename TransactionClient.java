
// Transaction Client class
public class TransactionClient extends Thread
{
  public static int numberTransactions;
  public static int numberAccounts;
  public static int initialBalance;

  public static String host;
  public static int port;

  public static StringBuffer log;

  // this is the contructor to set up the server??? seems like it reads from a file with a class that does the parsing
  public TransactionClient(String clientPropertiesFile, String serverPropertiesFile)
  {
    try
    {
      Properties serverProperties = new PropertyHandler(serverPropertiesFile);
      host = serverProperties.getProperty("HOST");
      port = Integer.parseInt(serverProperties.getProperty("PORT"));
      numberAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS"));
      initialBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE"));

      // establishing client properties
      Properties clientProperties = new PropertyHandler(clientPropertiesFile);
      numberTransactions = Integer.parseInt(clientProperties.getProperty("NUMBER_TRANSACTIONS"));

    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    log = new StringBuffer("");
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
          TransactionServerProxy transaction = new TransactionServerProxy( host, port );
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
