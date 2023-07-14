public class MoneyTransferRunnableRaceConditionLock implements Runnable{
  private BankAccount fromAccount;
  private BankAccount toAccount;
  private int amount;
  //TODO:
  private static final Object lock = new Object();

  public MoneyTransferRunnableRaceConditionLock(BankAccount fromAccount, BankAccount toAccount, int amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
  }

  public void run() { //don't implement synchronised to method because it will not solve race condition
    String threadName = Thread.currentThread().getName();

    synchronized (lock) {
      fromAccount.withdraw(amount);
      System.out.println(threadName + " withdrew " + amount + " from " + fromAccount.getName() + " (" + fromAccount.getBalance() + "$)");
      // Simulate some processing time
      try {
        Thread.sleep(200);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      toAccount.deposit(amount);
      System.out.println(threadName + " deposited " + amount + " to " + toAccount.getName() + " (" + toAccount.getBalance() + "$)");
    }
  }
}
