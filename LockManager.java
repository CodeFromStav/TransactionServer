
// LockManager (pseudocode is in the book lock.java, lockManager.java)
import java.util.HashMap;
import transaction.server.account.Account;
import java.util.Iterator;
import transaction.server.transaction.Transaction;


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
      return;
    }

    Lock lock;
    synchronized(this)
    {
      lock = theLocks.get(account);

      if(lock == null)
      {
        lock = new Lock(account);
        theLocks.put(account, lock);

        transaction.log("[LockManager.setLock]       | lock created, account #" + account.getNumber());
      }
    }
    lock.acquire(transaction, locktype);
  }

  public synchronized void unLock( TransID trans)
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
