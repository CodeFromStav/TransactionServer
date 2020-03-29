/*
number
balance
This allows, e.g. for reading an account balance and,
depending on the value read, writing a different balance back.
--Have array of accounts, where arrayIndex is the account number

-these two operations can be viewed as a withdrawal or a deposit,
depending on whether the original balance was larger or smaller
than the resulting one.

-Only write whole number values for money

an arbitrarily chosen dollar amount is withdrawn
from an arbitrarily chosen account and deposited
onto another, arbitrarily chosen account.

--always stay strictly at the low level of elementary
read/write operations, instead of  implementing higher
level constructs like withdrawal and deposit.
*/

public class Account
{

  private int initBalance;
  private final int accountNumber;
  private int account;

  public Account( int initBalance, int accountNumber )
  {
    this.initBalance = initBalance;
    this.accountNumber = accountNumber;
  }

  private int getAccountBalance()
  {
    return initBalance;
  }

  private int getAccountNum()
  {
    return accountNumber;
  }

  public void setBalance( int newBalance )
  {
    this.initBalance = newBalance;
  }

  public void setAccountNum( int newAccountNum )
  {
    this.account= newAccountNum;
  }
}
