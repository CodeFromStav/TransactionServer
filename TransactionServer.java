

/*abstract
Your transaction server will manage X number
of data objects that can be thought of as banking
accounts; each account is holding a certain amount of money.
The number of accounts is configurable.

-On the other hand, your transactional system,
 including the server proxy object on the client
  side, is absolutely unaware of the assumption of 
  such a predefined sequence of activities, i.e. it 
  can handle any activities in any order whatsoever, 
  without any limitation or underlying assumption.

  // Create AccountManager object
    AccountManager accountManager = new AccountManager();

  // Create LockManager object
    LockManager lockManager = new LockManager();

  // Create TransactionManager object
    TransactionManager transactionManager = new TransactionManager();


//Server loop always running in this class
run()
{
while(true)
{
try
{
transactionManager.runTransaction(serverSocket.accept());
}
catch (IOException e)
{
//print Error accepting client
//print StackTrace
}
}
*/