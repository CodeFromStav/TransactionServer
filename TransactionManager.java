/*

TransactionManager.java
-has transaction manager workers
-open up one connection per transaction, stays open until a close transaction is received. 
-Read/Write requests handled by TransactionWorker
*/

public class TransactionManager 
{
    // Constructor for TransactionManager
    public TransactionManager()
    {

    }

    // Run function for starting up TransactionManagerWorker threads
    public void run()
    {
        // Creates TransactionManagerWorker thread and then starts it up
        TransactionManagerWorker worker = new TransactionManagerWorker();
        worker.start();
    }
}