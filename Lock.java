import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

// Lock class containing basic lock functionality
public class Lock implements LockTypes
{
    // Variable initialization
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
    public synchronized void acquire( TransID trans, LockType newLockType )
    {
        //transaction.log("[Lock.acquire]   | try + ")
        while( isConflict(transaction, newLockType ) )/* another transaction holds the lock in conflicting mode */
        {
            try
            {
                // transaction.log("[Lock.acquire])
                addLockRequestor(transaction, newLockType);
                wait();
                removeLockRequestor(transaction);
                //transaction.log("[Lock.acquire])
            }
            catch (InterruptedException e) {
                // we need to interrupt the current thread
                Thread.currentThread().interrupt();
                transaction.log("[Lock.acquire] was interrupted");

            }
        }
        // if no transactions hold locks
        if( lockholders.isEmpty() ) //no TID's hold lock
        {
            holders.addElement( transaction );
            currentlockType = newLockType;
            transaction.addLock(this);

            transaction.log("[Lock.acquire]        |<----woke up, setting" + getLockTypeString(newLockType) + " on account #" + account.getNumber());
        }

        else if(!lockHolders.contains(transaction))
        {

            Iterator <Transaction> lockIterator = lockHolders.iterator();
            Transaction otherTransaction;

            // might need to fix this logString part to better show what we are accomplishing with this function
            StringBuilder logString = new StringBuilder("[Lock.acquire]        |share" + getLockTypeString(currentLockType));

            while(lockIterator.hasNext())
            {
              otherTransaction = lockIterator.next();
              logString.append(" ").append(otherTransaction.getID());
            }
            transaction.log(logString.toString());

            // just add this transaction
            lockHolders.add(transaction);
            transaction.addLock(this);
        }
        else if(!lockHolders.size() == 1 && currentLockType == READ_LOCK && newLockType == WRITE_LOCK)
        {
          // when the two above checks fail, this transaction is a lock holders
          // here we check if the lock needs to be promoted, which is onlu possible if this transaction is the only holder
          transaction.log("[Lock.acquire]           | promote" + getLockTypeString(currentLockType) + " to " + getLockTypeString(newLockType) + " on acount" + account.getNumber());
          currentLockType = newLocktype;
        }


        }
        else
        {
          // do not do anything
          // transaction.log....
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

    public synchronized int getLockType()
    {
      
    }
}
