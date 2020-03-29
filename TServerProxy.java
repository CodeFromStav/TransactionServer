import java.io.ObjectInputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import transaction.comm.Message;
import transaction.comm.MessageTypes;

// Transaction Server Proxy class
//Transaction client hits proxy, then the proxy server manages workers.
//represents transaction, which spins threads.
public class TServerProxy implements MessageTypes
{
  String host = null;
  int port;

  private Socket dbConnection = null;
  private ObjectOutputStream writeToNet = null;
  private ObjectInputStream readFromNet = null;
  private Integer transID = 0;

  TServerProxy(String host, int port)
  {
    this.host = host;
    this.port = port;
  }

  public int openTransaction()
  {

  }

  public void closeTransaction()
  {

  }

  public int read(int accountNumber)
  {
    Message readMessage = new Message(READ_REQUEST, accountNumber);
    Integer balance = null;

    try
    {
      writeToNet.writeObject(readMessage);
      balance = (Integer) readFromNet.readObject();
    }
    catch (Exception ex)
    {
      System.out.println("[TransactionServerProxy.read] Error occured");
      ex.printStackTrace();
    }

    return balance;

  }

  public int write(int accountNumber, int amount)
  {

  }

}
