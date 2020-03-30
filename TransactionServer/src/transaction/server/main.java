//import java.lang.Runnable
public static void main(String[] args)
{
  try
  {
      // create a new thread for new client; pass in client socket and counter number
      TransactionServer ts = new TransactionServer();
      Thread newThread = new Thread(ts);
      // start newly created thread
      newThread.start();
  }

  } 
  catch (Exception ex)
  {
      System.out.println("An Exception");
  }
}