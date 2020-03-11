import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

// Lock class containing basic lock functionality
public class Lock
{    
    // Variable initialization
    private Object objects;
    private Vector <E> holders;
    private LockType lockType;

    // Function for acquiring a lock
    public synchronized void acquire( TransID trans, LockType aLockType )
    {
        while( /* another transaction holds the lock in conflicting mode */ )
        {
            try 
            {
               wait(); 
            } 
            catch (InterruptedException e) {
                //TODO: handle exception
            }
        }
        // if no transactions hold locks
        if( holders.isEmpty() )
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
    public synchronized void release( TransID trans )
    {
        holders.removeElement( trans );
        // set lockType to none
        notifyAll();
    }
}
