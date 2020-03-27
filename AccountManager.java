
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
public class AccountManager implements TransactionTypes
{
    int accountNum;
    int accountBal;
    private int transactionCode; //may not be necessary


    // Constructor (creates account blueprint)
    public AccountManager( int accountNumber, int balance)
    {
        this.accountNum = accountNumber;
        this.accountBal = balance;        

    }

    // Read operation for accounts
    public int read( int accountNumber, Transaction transaction)
    {
        //get the acount
        Account account = getAccount(accountNumber);

        //set the read lock
        (TransactionServer.lockManager).lock(account, transaction, READ_LOCK);

        //the above call will wait (if not deadlock). Then continue.
       TODO://return (getAccount(accountNumber)).getBalance();

        system.out.println( "Account Number: " + accountNumber + "\n" );
        system.out.println( "Transaction: " + transaction );
        //return these values

    }

    // Write operation for accounts
    public int write( int accountNumber, Transaction transaction, int balance)
    {
        // int withdraw;
        // int deposit;

        Account account = getAccount(accountNumber);
        //Use Transaction Types
        switch( transaction ) //plug in transactionCode
        {
            case withdraw:
                balance -= withdraw; //sets new balance after withdrawal

                break;

            case deposit:
                balance += deposit; //sets new balance after deposit

                break;
        }    
        
        // if ()
        // withdraw += balance;
        //get account
        //lock 
        //set balance
        //return balance
    }


    public int getCode() //may not be necessary
    {
        return transactionCode;
    }

    public void getBalance()
    {

    }

    public int getAccount(int accountnumber)
    {

    }

    // public void giveAccount(int accountNumber)
    // {

    // }
}