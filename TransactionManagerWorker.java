/*
public class TransactionManagerWorker extends thread
{
Socket client = null
.
.

}
*/
// TransactionManagerWorker handles openTransaction requests, write requests, read requests, and closeTransaction requests
public class TransactionManagerWorker extends Thread
{
    // switch( /* switch on type of incoming request*/ )
    // {
    //     case OPEN_TRANSACTION:
    //         // take incoming port of transaction
    //         // open connection
    //         break;
    //     case READ:
    //         // send back information to client without writing anything new
    //         break;
    //     case WRITE:
    //         // write new information as requested in transaction
    //         break;
    //     case CLOSE_TRANSACTION:
    //         // close connection
    //         break;
    // }
}