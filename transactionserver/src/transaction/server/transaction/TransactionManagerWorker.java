package transaction.server.transaction;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import transaction.comm.Message;


// TransactionManagerWorker handles openTransaction requests, write requests, read requests, and closeTransaction requests
public class TransactionManagerWorker extends TransactionManager implements Runnable
{
    // Variable initialization
    Socket client = null;
    ObjectInputStream readFromNet = null;
    ObjectOutputStream readToNet = null;
    Message message = null;
    Transaction transaction = null;
    int accountNumber = 0;
    int balance = 0;
    boolean keepgoing = true;

    // Constructor
    public TransactionManagerWorker( Socket client )
    {
        this.client = client;
        try
        {
            readFromNet = new ObjectInputStream( client.getInputStream() );
            readToNet = new ObjectOutputStream( client.getOutputStream() );
        }
        catch ( IOException e )
        {
            System.out.println( "Failed to open object streams." );
            e.printStackTrace();
            System.exit( 1 );
        }
    }

    @Override
    public void run()
    {
        while( keepgoing )
        {
            try
            {
                message = (Message) readFromNet.readObject();
            }
            catch ( IOException | ClassNotFoundException e )
            {
                System.out.println( "Failed to read message from object stream." );
                System.exit( 1 );
            }
            // Switch statement to check and process type of message
            switch( message.getType() )
            {
                // Openning a transaction
                case OPEN_TRANSACTION:
                    synchronized( transactions )
                    {
                        transaction = new Transaction( transactionCounter++ );

                    }
                    try
                    {
                        readToNet.writeObject( transaction.getID() );
                    }
                    catch ( IOException e )
                    {
                        System.out.println( "Error opening transaction" );
                    }
                    transaction.log( "OPEN_TRANSACTION #" + transaction.getID() );
                    break;
                // Closing a transaction
                case CLOSE_TRANSACTION:
                    TransactionServer.lockManager.unlock(transaction);
                    transactions.remove(transaction);

                    try
                    {
                      readFromNet.close();
                      readToNet.close();
                      client.close();
                      keepgoing = false;
                    }
                    catch (IOException e)
                    {
                      System.out.println("[TransactionManagerWorker.run] CLOSE_TRANSACTION - error when closing connection to client");
                    }

                    transaction.log("[TransactionManagerWorker.run] CLOSE_TRANSACTION #" + transaction.getID());

                    // final printout of all the transaction's logs
                    if (TransactionServer.transactionView)
                    {
                      System.out.println(transaction.getlog());
                    }

                    break;

                    // Read a transaction
                case READ_REQUEST:
                    accountNumber = (Integer)message.getContent();
                    transaction.log("[TransactionManagerWorker.run] READ_REQUEST >>>>>>>>>> account #" + accountNumber);
                    balance = TransactionServer.accountManager.read(accountNumber, transaction);

                    try
                    {
                      readToNet.writeObject((Integer) balance);
                    }
                    catch (IOException e)
                    {
                      System.out.println("[TransactionManagerWorker.run] READ_REQUEST - Error when writing to object stream");
                    }

                    transaction.log("[TransactionManagerWorker.run] READ_REQUEST <<<<<<<<<<< account #" + accountNumber + ", new balance $" + balance);

                    break;

                case WRITE_REQUEST:

                    Object[] content = (Object[]) message.getContent();
                    accountNumber = ((Integer) content[0]);
                    balance = ((Integer) content[1]);
                    transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST >>>>>>>>>> account #" + accountNumber + ", new balance $" + balance);
                    balance = TransactionServer.accountManager.write(accountNumber, transaction, balance);

                    try
                    {
                        readToNet.writeObject((Integer) balance);
                    }
                    catch (IOException e)
                    {
                      System.out.println("[TransactionManagerWorker.run] WRITE_REQUEST - Error when writing to object stream");
                    }

                    transaction.log("[TransactionManagerWorker.run] WRITE_REQUEST <<<<<<<<<<< account #" + accountNumber + ", new balance $" + balance);

                    break;

                default:
                    System.out.println("[TransactionManagerWorker.run] Warning: Message type not implemented");

            }
        }
    }
}
