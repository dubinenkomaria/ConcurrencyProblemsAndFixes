
public class MoneyTransferRunnableDeadLock implements Runnable{
  private BankAccount fromAccount;
  private BankAccount toAccount;
  private int amount;


  public MoneyTransferRunnableDeadLock(BankAccount fromAccount, BankAccount toAccount, int amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
  }

  public void run(){
    String threadName = Thread.currentThread().getName();

    synchronized (fromAccount) {

      System.out.println(threadName+" acquired lock " +fromAccount.getName());
      try {  //sleep to catch deadlock
        Thread.sleep(100L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      synchronized (toAccount) {
        System.out.println(threadName+" acquired lock " +toAccount.getName());
        if (fromAccount.getBalance() < amount) {
          throw new InsufficientFundsException();
        } else {
          fromAccount.withdraw(amount);
          toAccount.deposit(amount);
        }
      }
    }
  }
}
