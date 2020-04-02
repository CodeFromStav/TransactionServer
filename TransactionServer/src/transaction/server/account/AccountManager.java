package transaction.server.account;
import java.util.ArrayList;
import transaction.server.transaction.*;
import transaction.server.TransactionServer;
import transaction.server.lock.LockTypes;

/*
initializes full set of counts needed
provides account number
giveAccountNumber()
getAccount()
read()
write()
-Takes read and write, tries to set locks. If no lock, within read and write you will be "blocked"
ACCOUNTS ONLY HOLD WHOLE NUMBER AMOUNTS
*/
public class AccountManager implements LockTypes
{
    int accountNum;
    int accountBal;
    private int transactionCode; //may not be necessary
    private final ArrayList<Account> accounts = new ArrayList<Account>();


    // Constructor (creates account blueprint)
    public AccountManager( int accountNumber, int balance)
    {
        this.accountNum = accountNumber;
        this.accountBal = balance;
        for (int i = 0; i < accountNumber; i++)
        {
            Account tempAccount = new Account(balance, i);
            accounts.add(tempAccount);
        }
        System.out.println("THIS IS THE NUMBER OF ACCOUNTS [ACCOUNTMANAGER]");
        System.out.println(accountNum);
        
    }

    public Account getAccount( int accountNumber )
    {
        return accounts.get( accountNumber );
    }

    // Returns accounts
    public ArrayList<Account> getAccounts()
    {
        return accounts;
    }

    // Read operation for accounts
    public int read( int accountNumber, Transaction transaction)
    {
        //get the acount
        Account account = getAccount(accountNumber);

        //set the read lock
        (TransactionServer.lockManager).lock(account, transaction, READ_LOCK);

        //the above call will wait (if not deadlock). Then continue.
        return (getAccount(accountNumber)).getAccountBalance();
    }

    // Write operation for accounts
    public int write( int accountNumber, Transaction transaction, int balance)
    {
        // Get account
        Account account = getAccount( accountNumber );

        // Set the write lock
        (TransactionServer.lockManager).lock( account, transaction, WRITE_LOCK );

        // Above lock may have to wait for deadlock, until it continues here
        account.setBalance( balance );
        return balance;
    }

    public int getCode() //may not be necessary
    {
        return transactionCode;
    }

    public int getBalance()
    {
        return accountBal;
    }
    
    public int getSize()
    {
        return accounts.size();
    }
    
}
