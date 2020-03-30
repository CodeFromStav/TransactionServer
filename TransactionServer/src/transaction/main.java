public static void main(String[] args)
{
  try
  {   // create a new thread for new client; pass in client socket and counter number
      TransactionServer ts = new TransactionServer("../../config/ServerProperties.properties").start();
  }

  catch (Exception ex)
  {
      System.out.println("An Exception");
  }
}