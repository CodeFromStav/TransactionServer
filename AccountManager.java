
/*
initializes full set of counts needed
provides account number
giveAccountNumber()
getAccount()
read()
write()
-Takes read and write, tries to set locks. If no lock, within read and write you will be "blocked"
*/

public class AccountManager
{
  // list of the accounts in the server
  ArrayList<Account> accounts = new ArrayList<Account>;
  // Constructor
  public AccountManager()
  {

  }

  // Read operation
    // might not need this?
  public int read( /* accountnumber, transaction, */)
  {

  }

  // Write operation
  public int write( int accountNumber, Transaction transaction, float balance)
  {
    // this should add and subtract a balance, not set.
    Account account = getAccount(accountNumber);
    TransactionServer.LockManager.Lock( accountnumber, transaction, WRITE_LOCK );
    account.setBalance(balance);
    return(account.getAccountBalance());
  }


  // method that loops through list and returns the account if found
  public Account getAccount( int accountNumber )
  {
    for( int i = 0; i < accounts.size(); i++ )
    {
      if( accounts.get(i).getAccountNum() == accountNumber )
      {
        return accounts.get(i);
      }
    }

    System.out.println("Account does not exist");
    return null;
  }

  public void giveAccountNumber()
  {

  }
}
