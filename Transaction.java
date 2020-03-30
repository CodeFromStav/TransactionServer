package transaction.server.transaction;

import java.util.ArrayList;
import transaction.server.TransactionServer;
import transaction.server.lock.Lock;

// Transaction class for creating transactions
public class Transaction
{
    // Variable initialization
    int transID;
    ArrayList<Lock> locks = null;
    StringBuffer log = new StringBuffer( "" );

    // Constructor
    public Transaction( int transID )
    {
        this.transID = transID;
        this.locks = new ArrayList();
    }

    // Getter methods
    public int getID()
    {
        return transID;
    }

    public ArrayList<Lock> getLocks()
    {
        return locks;
    }

    public StringBuffer getLog()
    {
        return log;
    }

    // Adding lock
    public void addLock( Lock lock )
    {
        locks.add( lock );
    }

    // Logging function
    public void log( String logString )
    {
        log.append( "\n" ).append( logString );
        if( !TransactionServer.transactionView )
        {
            System.out.println( "Transaction #" + this.getID() + (( this.getID() < 10 ) ? "" : "" ) + logString );
        }
    }
}
