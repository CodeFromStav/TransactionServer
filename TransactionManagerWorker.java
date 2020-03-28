import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;


// TransactionManagerWorker handles openTransaction requests, write requests, read requests, and closeTransaction requests
public class TransactionManagerWorker extends Thread implements MessageTypes
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
                    synchronized( transaction )
                    {
                        transaction = new Transaction( transactionCounter++ );

                    }
                    try
                    {
                        writeToNet.writeObject( transaction.getID() );
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
                      writeToNet.close();
                      client.close();
                      keepgoing = false;
                    }
                    catch (IOException e)
                    {
                      
                    }

            }
        }
    }
}
