import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

// Lock class containing basic lock functionality
public class Lock implements LockTypes
{    
    // Variable initialization
    private Object objects; //object being protected by the lock -- might not need this anymore--
    private Vector <E> holders; //the TID's of current holders --might not need this anymore--
    private LockType lockType; //the currenty type --might not need this anymore--
    private final Account account;
    private int currentLockType;
    private final ArrayList<Transaction> lockHolders;
    private final HashMap<Transaction, Object[]> lockRequestors;

    public Lock(Account account)
    {
        this.lockHolders = new ArrayList();
        this.lockReq = new HashMap();
        this.account = account;
        this.currentLockType = EMPTY_LOCK;
    }

    // Function for acquiring a lock
    public synchronized void acquire( TransID trans, LockType aLockType )
    {
        while( isConflict(transaction, newLockType ) )/* another transaction holds the lock in conflicting mode */ 
        {
            try 
            {
                // transaction.log("[Lock.acquire])
                addLockRequest(transaction, newLockType);
                wait(); 
                removeLockRequest(transaction);
                //transaction.log("[Lock.acquire])
            } 
            catch (InterruptedException e) {
                //TODO: handle exception
                //Print stacktrace
            
            }
        }
        // if no transactions hold locks
        if( holders.isEmpty() ) //no TID's hold lock
        {
            holders.addElement( trans );
            lockType = aLockType;
        }

        else if( /* another transaction holds the lock, share it */ )
        {

            if( /* this transaction is not a holder */ )
            {
                holders.addElement( trans );
            }

        }

        else if( /* this transaction is a holder but needs a more exlusive look */)
        {
            lockType.promote();
        }
    }

    // Function for releasing a lock
    public synchronized void release( Transaction transaction )
    {
        lockHolders.remove( transaction );
        if( lockHolders.isEmpty() )
        {
            currentLockType = EMPTY_LOCK;
            if( lockRequestors.isEmpty() )
            {
                // lock is not being used so delete it
            }
        }
        notifyAll();
    }

    // Used to check is there are conflicting locks being set
    private boolean isConflict( Transaction transaction, int newLockType )
    {
        if( lockHolders.isEmpty() )
        {
            // no lock holders so no conflict
            transaction.log( /* stuff goes here */ );
            return false;
        }
        else if( lockHolders.size() == 1 && lockHolders.contains( transaction ) )
        {
            // not sure if below code is correct, just guessing based on screenshots
            lockHolders.add( transaction );
            currentLockType = newLockType;
            transaction.addLock( this );
            // log it
        }
        else if( !lockHolders.contains( transaction ) )
        {
            // another transaction holds the lock (this is a read lock), so just share it
            Iterator <Transaction> lockIterator = lockHolders.iterator();
            Transaction otherTransaction;
            StringBuilder logString = new StringBuilder( "Created lock " + getLockTypeString( currentLockType ) + " on account #" + account.getAccountNum() );
            while( lockIterator.hasNext() )
            {
                otherTransaction = lockIterator.next();
                logString.append( " " ).append( otherTransaction.getID() );
            }
            transaction.log( logString.toString() );
            // add the transaction
            lockHolders.add( transaction );
            transaction.addLock( this );
        }
        else if( lockHolders.size() == 1 && currentLockType == READ_LOCK && newLockType == WRITE_LOCK )
        {
            // check if the lock needs to be promoted
            transaction.log( "Promoting " + getLockTypeString( currentLockType ) + " to " + getLockTypeString( newLockType ) + " on account #" + account.getAccountNum() )
            currentLockType = newLockType;
        }
        else
        {
            // no need to do anything else
            transaction.log( "Ignore setting " + getLockTypeString( currentLockType ) + " to " getLockTypeString( newLockType ) + " on account #" + account.getAccountNum() );
        }
    }
}
