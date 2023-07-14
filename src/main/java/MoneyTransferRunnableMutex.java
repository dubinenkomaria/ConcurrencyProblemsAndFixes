import java.util.concurrent.locks.ReentrantLock;

public class MoneyTransferRunnableMutex  implements Runnable{
  private BankAccount fromAccount;
  private BankAccount toAccount;
  private int amount;
  private ReentrantLock lock;

  public MoneyTransferRunnableMutex(BankAccount fromAccount, BankAccount toAccount, int amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
    this.lock = new ReentrantLock();
  }

  public void run() {
    String threadName = Thread.currentThread().getName();

    lock.lock();
    try {
      fromAccount.withdraw(amount);
      System.out.println(threadName + " withdrew " + amount + " from " + fromAccount.getName() + " (" + fromAccount.getBalance() + "$)");


      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      toAccount.deposit(amount);
      System.out.println(threadName + " deposited " + amount + " to " + toAccount.getName() + " (" + toAccount.getBalance() + "$)");
    } finally {
      lock.unlock();
    }
  }
}
