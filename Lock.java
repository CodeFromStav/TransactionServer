import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import transaction.server.TransactionServer;
import transaction.server.lock.Lock;
import transaction.server.transaction.Transaction;

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
        this.lockRequestors = new HashMap();
        this.account = account;
        this.currentLockType = EMPTY_LOCK;
    }

    // Function for acquiring a lock
    public synchronized void acquire( Transaction transaction, int newLockType )
    {
        transaction.log("[Lock.acquire]   | try + " + getLockTypeString(newLockType) + " on account #" + account.getAccountNum());
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
        if( lockHolders.isEmpty() ) //no TID's hold lock
        {
            lockHolders.add( transaction );
            currentLockType = newLockType;
            transaction.addLock(this);

            transaction.log("[Lock.acquire]        |<----woke up, setting" + getLockTypeString(newLockType) + " on account #" + account.getAccountNum());
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
        else if(!(lockHolders.size() == 1) && currentLockType == READ_LOCK && newLockType == WRITE_LOCK)
        {
          // when the two above checks fail, this transaction is a lock holders
          // here we check if the lock needs to be promoted, which is onlu possible if this transaction is the only holder
          transaction.log("[Lock.acquire]           | promote" + getLockTypeString(currentLockType) + " to " + getLockTypeString(newLockType) + " on acount" + account.getAccountNum());
          currentLockType = newLockType;
        }
        else
        {
          // do not do anything
          transaction.log("[Lock.acquire]        |<----Error, setting" + getLockTypeString(newLockType) + " on account #" + account.getAccountNum());
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
            transaction.log( "Current lock: " + getLockTypeString( currentLockType ) + " on account #" + account.getAccountNum() + " has no conflict." );
            return false;
        }
        else if( lockHolders.size() == 1 && lockHolders.contains( transaction ) )
        {
            if( currentLockType == READ_LOCK && newLockType == READ_LOCK )
            {
                // they can share it, so no conflict
                transaction.log( "Current lock: " + getLockTypeString( currentLockType ) + " and new lock: " + getLockTypeString( newLockType ) + " are both read locks, so no conflict." );
                return false;
            }
            else if( currentLockType == READ_LOCK && newLockType == WRITE_LOCK )
            {
                // new lock type is different from current lock type, so we have a conflict
                transaction.log( "Current lock: " + getLockTypeString( currentLockType ) + " is a read lock, while new lock: " + getLockTypeString( newLockType ) + " is a write lock, so we have a conflict." );
                return true;
            }
            else if( currentLockType == WRITE_LOCK )
            {
                // we will have a conflict
                transaction.log( "Current lock: " + getLockTypeString( currentLockType ) + " and new lock: " + getLockTypeString( newLockType ) + " are conflicting." );
                return true;

            }
        }
        else // more than one lock in lockHolders and it contains the transaction
        {
            if( currentLockType == READ_LOCK && newLockType == READ_LOCK )
            {
                // they can share it, so no conflict
                transaction.log( "Current lock: " + getLockTypeString( currentLockType ) + " and new lock: " + getLockTypeString( newLockType ) + " are both read locks, so no conflict." );
                return false;
            }
            else if( currentLockType == READ_LOCK && newLockType == WRITE_LOCK )
            {
                // new lock type is different from current lock type, so we have a conflict
                transaction.log( "Current lock: " + getLockTypeString( currentLockType ) + " is a read lock, while new lock: " + getLockTypeString( newLockType ) + " is a write lock, so we have a conflict." );
                return true;
            }
            else if( currentLockType == WRITE_LOCK )
            {
                // we will have a conflict
                transaction.log( "Current lock: " + getLockTypeString( currentLockType ) + " and new lock: " + getLockTypeString( newLockType ) + " are conflicting." );
                return true;

            }
        }
        return false;
    }

    // Returns type of lock
    public synchronized int getLockType()
    {
        return currentLockType;
    }

    private void addLockRequestor( Transaction requestor, int newLockType )
    {
      lockRequestors.add(requestor, newLockType);
    }

    public String getLockTypeString( int lockType )
    {
      return Integer.toString(lockType);
    }

    private void removeLockRequestor( Transaction requestor )
    {
      lockRequestors.remove(requestor);

    }
}
