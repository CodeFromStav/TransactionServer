
/*
initializes full set of counts needed
provides account number
giveAccountNumber()
getAccount()
read()
write()
-Takes read and write, tries to set locks. If no lock, within read and write you will be "blocked"
*/
<<<<<<< Updated upstream

public class AccountManager
=======
public class AccountManager implements MessageTypes
>>>>>>> Stashed changes
{
    // Constructor
    public AccountManager()
    {

<<<<<<< Updated upstream
    }

    // Read operation
    public int read( /* accountnumber, transaction, */)
    {  

    }

    // Write operation
    public int write( int accountnumber, Transaction transaction, float balance, MessageTypes messageCode)
    {
        //get account
        //lock 
        //set balance
        //return balance
    }
        

    public void getAccount()
    {

    }

    public void giveAccountNumber()
    {

=======
    // Constructor
    public AccountManager()
    {

    }

    // Read operation
    public int read( int accountnumber, Transaction transaction)
    {

    }

    // Write operation
    public int write( int accountnumber, Transaction transaction, float balance)
    {
        // int withdraw;
        // int deposit;

        

        
        
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

>>>>>>> Stashed changes
    }
}