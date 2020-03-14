
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
public class AccountManager implements MessageTypes
{
    int accountNum;
    int accountBal;

    // Constructor (creates accounts)
    public AccountManager( int accountNumber, int balance)
    {
        this.accountNum = accountNumber;
        this.accountBal = balance;        

    }

    // Read operation for accounts
    public int read( int accountNumber, Transaction transaction)
    {
        system.out.println( "Account Number: " + accountNumber + "\n" );
        system.out.println( "Transaction: " + transaction );
        //return these values

    }

    // Write operation for accounts
    public int write( int accountNumber, Transaction transaction, int balance)
    {
        // int withdraw;
        // int deposit;
        //Use Transaction Types
        switch( transaction )
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



    public void getAccount( int accountnumber)
    {

    }

    public void giveAccountNumber()
    {

    }
}