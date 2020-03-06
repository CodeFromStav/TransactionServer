# TransactionServer

Project Description
Please read the following paragraphs carefully and contact me with any questions that you have.

Data objects
Your transaction server will manage X number of data objects that can be thought of as banking accounts; each account is holding a certain amount of money. The number of accounts is configurable.

When you start your transaction server, each account is initialized to some configurable amount, e.g. $10. Accounts can be "over-drafted", resulting in a negative balance. For simplicity, we will assume that accounts contain only full dollar amounts. This will allow you to represent account balances by simple integers.

Operations on data objects
Account balances can be read and can be written - those are the two fundamental operations that can be applied to accounts. This allows, e.g. for reading an account balance and, depending on the value read, writing a different balance back. The combined effect of these two operations can be viewed as a withdrawal or a deposit, depending on whether the original balance was larger or smaller than the resulting one.

Because accounts can only contain full dollar amounts, write operations can also only write full dollar amounts.

Transactions
While conceptually your transaction server can support any number of read/write operations on its data objects in any order, we want to introduce yet another simplification as follows. All transactions hitting your server will only ever do the same set of operations that each and every transaction is comprised of: an arbitrarily chosen dollar amount is withdrawn from an arbitrarily chosen account and deposited onto another, arbitrarily chosen account. I ask you to always stay strictly at the low level of elementary read/write operations, instead of  implementing higher level constructs like withdrawal and deposit.

As can be easily seen, this results in a "zero sum game", i.e. after any properly run number of such transactions has completed, the sum of all balances over all X accounts should be e.g. $10 times X.

From the above description it should be clear that all transactions look the same in what they do. However, a run of a certain transaction will be different from any other run of another transaction in that the dollar amounts to be transferred between accounts is chosen by chance, as are the source and destination accounts. Needless to say, transactions are uniquely identified by their transaction IDs. 

While you are allowed to make the assumptions about what all transactions uniformly look like, the only entity that is allowed to implement a "hard-coded" representation of activities that a transaction is comprised of is the client application itself. On the other hand, your transactional system, including the server proxy object on the client side, is absolutely unaware of the assumption of such a predefined sequence of activities, i.e. it can handle any activities in any order whatsoever, without any limitation or underlying assumption.

Although transactions contain a fixed set of operations, they need to be properly opened, as well as closed. But, again for reasons of simplicity, we assume that it is not possible to abort a transaction, neither from the server, nor from the client's side.

Locks
Unfortunately, the above description of how your (one type of) transactions are supposed to be run implies that race conditions exist that will render your data objects inconsistent and thus useless, if no precautions are taken. To guarantee the proper working of concurrently running transactions and eliminate the detrimental effects of race conditions, your transaction server is supposed to employ locking. In order to observe the effects of locking, or, the absence of such a precaution, your transaction server has to be implemented such that the presence of a locking scheme is configurable.

Clients
A client may run one or many transactions, it is up to you how to implement this. Overall, the number of transactions to be run on the server is configurable. Clients will accept the connectivity information of the server as a configurable property. Convince yourself that all the client is doing with this information is to pass it on to the server proxy object, the latter handling the low-level communication with the server and advertising the transactional API to the client.

Your client(s) need to connect to your transaction server over a TCP/IP network.

Minimal structural expectations
For a project of this complexity, your code needs to be well-structured. This concerns splitting your code into units with well-defined responsibilities. For our project, it is especially important to structure your code along the lines of the abstraction levels that the project's semantics suggest.

To bring you up to speed quickly and at the same time allow you to learn about good design, I suggest to implement the following parts:

On the client side:

Transaction Client - initializes a Transaction Server Proxy and then, using the proxy, runs any number of transactions as specified above.
Transaction Server Proxy - represents the server on the client side, implements and advertises the transactional API.
On the server side:

Transaction Server - initializes server by reading properties, according to which all managers are created/initialized and then runs the (multi-threaded) server loop.
Transaction Manager - as the name implies, has oversight of all transactions and spawns Transaction Manager Workers to handle incoming transactions.
Account Manager - initializes accounts and implements read/write operations, which, most importantly, involves locking, if locking is active.
Lock Manager - handles all locks and lock/unlock requests. Convince yourself that, in order to implement strict two-phase locking, this manager receives lock requests from the Account Manager, while it receives unlock requests from the Transaction Manager (Worker), when a transaction closes.
