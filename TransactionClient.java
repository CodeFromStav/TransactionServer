
// Transaction Client class
public class TransactionClient extends Thread
{
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
}
