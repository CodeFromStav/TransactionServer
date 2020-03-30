<<<<<<< HEAD:TransactionServer/src/transaction/server/Main.java
package transaction.server;



public class Main {
	
public static void main(String[] args)
{
  try
  {   // create a new thread for new client; pass in client socket and counter number
      new TransactionServer("../config/ServerProperties.properties").start();
  }

  catch (Exception ex)
  {
      System.out.println("An Exception");
  }
}
=======

public class main
{


    public static void main(String[] args)
    {
        try
        {   // create a new thread for new client; pass in client socket and counter number
            TransactionServer ts = new TransactionServer("../config/ServerProperties.properties").start();
        }
        catch (Exception ex)
        {
            System.out.println("An Exception");
        }
    }
>>>>>>> d046ee526050c78f4874e47a5d1752ede3cb4c25:TransactionServer/src/transaction/main.java
}