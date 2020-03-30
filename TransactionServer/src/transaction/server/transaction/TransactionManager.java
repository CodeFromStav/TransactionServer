package transaction.server.transaction;

import java.net.Socket;
import java.util.ArrayList;
import transaction.comm.MessageTypes;



/*
TransactionManager.java
-has transaction manager workers
-open up one connection per transaction, stays open until a close transaction is received.
-Read/Write requests handled by TransactionWorker
*/
public class TransactionManager implements MessageTypes
{

  // variables
  public static int transactionCounter = 0;
  public static final ArrayList<Transaction> transactions = new ArrayList<>();


  // Constructor
  public TransactionManager()
  {

  }

  // getter of the list of transactions
  public ArrayList<Transaction> getTransactions()
  {
    return transactions;
  }

  // function for running the threads
  public void runTransaction( Socket client )
  {
	  TransactionManagerWorker tmw = new TransactionManagerWorker(client);
	  Thread workerThread = new Thread(tmw);
	  workerThread.start();
    // accept incoming connections
    // call run() to start up worker threads
  }
}
