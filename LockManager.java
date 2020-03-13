/*abstract

LockManager (pseudocode is in the book lock.java, lockManager.java)

public class LockManager{


private Hashtable theLocks;

// LockManager constructor
public LockManager()
{

}

public void setLock( Object object, TransID trans, LockType lockType)
{
    Lock foundLock;
    synchronized (this)
    {
        //find the lock associated with object
        //if there isnt one, create it and add to hashtable
    }
}
/* (this is all terribly inefficient)

public synchronized void unLock( TransID trans)
{
    Enumeration e = theLocks.element();
    while(e.hasMoreElements())
    {
        Lock aLock = (Lock) (e.nextElement());
        if( *trans is a holder of this lock ) //check if lock is being held by this transaction 
        {
            aLock.release(trans) 
        }
    }
}
*/

//Transaction itself should have a list of its own locks above!!!