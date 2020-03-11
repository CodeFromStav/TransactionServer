import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

// Lock class containing basic lock functionality
public class Lock implements LockTypes
{    
    // Variable initialization
    private Object objects; //object being protected by the lock
    private Vector <E> holders; //the TID's of current holders
    private LockType lockType; //the currenty type

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
    public synchronized void release( TransID trans )
    {
        holders.removeElement( trans ); //remove this holder
        // set lockType to none
        notifyAll();
    }
}
