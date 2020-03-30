package transaction.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import transaction.server.account.AccountManager;
import transaction.server.transaction.TransactionManager;
import transaction.server.lock.LockManager;
import utils.PropertyHandler;


// abstract
// Your transaction server will manage X number
// of data objects that can be thought of as banking
// accounts; each account is holding a certain amount of money.
// The number of accounts is configurable.

// -On the other hand, your transactional system,
//  including the server proxy object on the client
//   side, is absolutely unaware of the assumption of
//   such a predefined sequence of activities, i.e. it
//   can handle any activities in any order whatsoever,
//   without any limitation or underlying assumption.

public class TransactionServer extends Thread
{
  
  public static boolean transactionView;
  public static AccountManager accountManager = null;
  public static TransactionManager transactionManager = null;
  public static LockManager lockManager = null;

  static ServerSocket serverSocket = null;

  public TransactionServer(String serverPropertiesFile)
  {
    // Properties stuff
    Properties serverProperties = null;

    // get Properties
    try
    {
      serverProperties = new PropertyHandler(serverPropertiesFile);
    }
    catch (Exception e)
    {
      System.out.println("[TransactionServer.TransactionServer] Didn't find properties file \"" + serverPropertiesFile + "\"");
      e.printStackTrace();
      System.exit(1);
    }

    // create transaction LockManager
    transactionView = Boolean.valueOf(serverProperties.getProperty("TRANSACTION_VIEW"));
    TransactionServer.transactionManager = new TransactionManager();
    System.out.println("[TransactionServer.TransactionServer] TransactionManager created");

    // create lock LockManager
    boolean applyLocking = Boolean.valueOf(serverProperties.getProperty("APPLY_LOCKING"));
    TransactionServer.lockManager = new LockManager(applyLocking);
    System.out.println("[TransactionServer.TransactionServer] LockManager created");

    // create account LockManager
    int numberAccounts = 0;
    numberAccounts = Integer.parseInt(serverProperties.getProperty("NUMBER_ACCOUNTS"));
    int initialBalance;
    initialBalance = Integer.parseInt(serverProperties.getProperty("INITIAL_BALANCE"));

    TransactionServer.accountManager = new AccountManager(numberAccounts, initialBalance);
    System.out.println("[TransactionServer.TransactionServer] AccountManager created ");

    // create server socket
    try
    {
      int port = Integer.parseInt(serverProperties.getProperty("PORT"));
      serverSocket = new ServerSocket(port);
      
      
      System.out.println("[TransactionServer.TransactionServer] ServerSocket created");
    }
    catch (IOException ex)
    {
      System.out.println("[TransactionServer.TransactionServer] could not create server socket");
      ex.printStackTrace();
      System.exit(1);
    }
  }

public void run()
{
  // run server loop
  while(true)
  {
    try
    {
      transactionManager.runTransaction(serverSocket.accept());
    }
    catch (IOException e)
    {
      System.out.println("[TransactionServer.run] Warning: Error accepting client");
      e.printStackTrace();
    }
  }
}

}
