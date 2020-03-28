import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import transaction.comm.Message;
import transaction.comm.MessageTypes;
import transaction.server.TransactionServer;

/*
TransactionManager.java
-has transaction manager workers
-open up one connection per transaction, stays open until a close transaction is received.
-Read/Write requests handled by TransactionWorker
*/

public class TransactionManager implements MessageTypes
{

  private static int transactionCounter = 0;
  private static final ArrayList<Transaction> transactions = new ArrayList<>();

  // I dont know what this is supposed to be a list of
  // ArrayList<Transaction> transactions = new ArrayList<Transaction>;

  // Constructor
  public TransactionManager()
  {

  }

  // getter of the list of transactions
  public ArrayList<Transaction> getTransactions()
  {
    return transactions;
  }


  public void runTransaction( Socket client )
  {
    (new TransactionManagerWorker(client)).start();
    // accept incoming connections
    // call run() to start up worker threads
  }


  // Run function for creating worker threads
  public void run()
  {
      TransactionManagerWorker worker = new TransactionManagerWorker();
      Thread transactionWorker = new Thread(worker);
      transactionWorker.start();
  }
}
