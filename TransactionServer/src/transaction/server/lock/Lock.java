package transaction.server.lock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import transaction.server.transaction.Transaction;
import transaction.server.account.Account;

// Lock class containing basic lock functionality
public class Lock implements LockTypes
{
    // Variable initialization
    private final Account account;
    private int currentLockType;
    private final ArrayList<Transaction> lockHolders;
    private final HashMap<Transaction, Object[]> lockRequestors;
    
    // lockInfo second string of ids current lock holders
        // current lock, lock to be set, 

    public Lock(Account account)
    {
        this.lockHolders = new ArrayList<Transaction>();
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
                transaction.log("[Lock.acquire     | ---->wait to set " + getLockTypeString(newLockType) + " on account #" + account.getAccountNum());
                addLockRequestor(transaction, newLockType);
                wait();
                removeLockRequestor(transaction);
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
                // need to ask a question here
                    // 
            	currentLockType = EMPTY_LOCK;
            }
        }
        notifyAll();
    }
    
    
    private boolean isConflict( Transaction transaction, int newLockType )
    {
        if (lockHolders.isEmpty())
        {
            // there are no lock holders, so there is no conflict
            transaction.log("[Lock.isConflict      | current lock " + getLockTypeString(currentLockType) + " on account #" + account.getAccountNum() + ", no holders");
            return false;
        }
        else if (lockHolders.size() == 1 && lockHolders.contains(transaction))
        {
             // there is only one lock holder, and it's this transaction
            // so this is not a conflict
            // if we hold a read lock and want to set a write lock, the lock will be promoted
            
            // it would be useless to set a read lock on our own read lock or read or write lock on our own write lock
            // but in principle transactions are allowed to be doing that
            transaction.log("[Lock.isConflict            | current lock" + getLockTypeString(currentLockType) + " on account #" + account.getAccountNum() + " this transaction is the only holder" );
            return false;
        }
        else if (currentLockType == READ_LOCK && newLockType == READ_LOCK)
        {
            // then the two share the lock
            transaction.log("[Lock.isConflict]            | current lock" + getLockTypeString(currentLockType) + " on account #" + account.getAccountNum() + ", sharing the lock");
            return false;
        }
        else
        {
            // at this point, we have a conflict
            Iterator <Transaction> lockholdersIterator = lockHolders.iterator();
            Transaction otherTransaction;
            StringBuilder holders = new StringBuilder("");
            while (lockholdersIterator.hasNext()) 
            {
                otherTransaction = lockholdersIterator.next();
                holders.append(" ").append(otherTransaction.getID());
            }
            transaction.log("[Lock.isConflict]            | current lock " + getLockTypeString(currentLockType) + " held by transaction(s) " + holders);
            return true;
        }
    }
    
    // Returns type of lock
    public synchronized int getLockType()
    {
        return currentLockType;
    }

    private void addLockRequestor( Transaction requestor, int newLockType )
    {
      Object[] content = new Object[]{account.getAccountNum(), newLockType };
      lockRequestors.put(requestor, content); 
    }

    public static String getLockTypeString( int lockType )
    {
      return Integer.toString(lockType);
    }

    private void removeLockRequestor( Transaction requestor )
    {
      lockRequestors.remove(requestor);

    }
    
    public Account getAccount()
    {
    	return account;
    }
}
