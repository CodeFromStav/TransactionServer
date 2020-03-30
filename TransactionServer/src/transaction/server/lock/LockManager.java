package transaction.server.lock;
// LockManager (pseudocode is in the book lock.java, lockManager.java)
import java.util.HashMap;
import transaction.server.account.Account;
import java.util.Iterator;
import transaction.server.transaction.Transaction;
import transaction.server.lock.LockTypes;


public class LockManager implements LockTypes
{


  private static HashMap<Account, Lock> theLocks;
  private static boolean applyLocking;

  // LockManager constructor
  public LockManager(boolean applyLocking)
  {
    theLocks = new HashMap<>();
    this.applyLocking = applyLocking;
  }

  public void lock(Account account, Transaction transaction, int lockType)
  {
    if(!applyLocking)
    {
<<<<<<< HEAD
      return;
    }

=======
      final int WRITE_LOCK = 105;
      final int READ_LOCK = 106;
      final int EMPTY_LOCK = 107;
    }
    
>>>>>>> d046ee526050c78f4874e47a5d1752ede3cb4c25
    Lock lock;
    synchronized(this)
    {
      lock = theLocks.get(account);

      if(lock == null)
      {
        lock = new Lock(account);
        theLocks.put(account, lock);

        transaction.log("[LockManager.setLock]       | lock created, account #" + account.getAccountNum());
      }
    }
    lock.acquire(transaction, lockType);
  }

  public synchronized void unLock( Transaction transaction )
  {
      //return if no locking
      if(!applyLocking) return;

      Iterator<Lock> lockIterator = transaction.getLocks().listIterator();
      Lock currentLock;
      while(lockIterator.hasNext())
      {
          currentLock = lockIterator.next();
          transaction.log("[LockManager.unLock]      | release " + Lock.getLockTypeString(currentLock.getLockType()) + ", account #" + currentLock.getAccount());
          currentLock.release(transaction);
          //-------------------------------

              //if( lock is empty and lock request is empty */)
              //remove the lock, not needed anymore.
      }
  }
}


//Transaction itself should have a list of its own locks above!!!
