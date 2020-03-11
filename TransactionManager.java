 /*
TransactionManager.java
-has transaction manager workers
-open up one connection per transaction, stays open until a close transaction is received.
-Read/Write requests handled by TransactionWorker
*/

public class TransactionManager
{
  // I dont know what this is supposed to be a list of
  ArrayList<Transaction> transactions = new ArrayList<Transaction>;

  // Constructor
  public TransactionManager()
  {

  }

  // getter of the list of transactions
  public ArrayList<Transaction> getTransactions()
  {
    return this.transactions;
  }



  // Run function for creating worker threads
  public void runTransaction()
  {
      TransactionManagerWorker worker = new TransactionManagerWorker();
      Thread transactionWorker = new Thread(worker);
      transactionWorker.start();
  }
}
