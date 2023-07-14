public class PayRunnableVisibilityProblemVolatileFix implements Runnable {
  private  volatile BankAccount fromAccount;
  private int amount;

  public PayRunnableVisibilityProblemVolatileFix(BankAccount fromAccount, int amount) {
    this.fromAccount = fromAccount;
    this.amount = amount;
  }

  public void run() {
    String threadName = Thread.currentThread().getName();
    if (fromAccount.getBalance() < amount) {
      System.out.println(threadName + " does nothing cause it's less then "+amount+"$ on" + fromAccount.getName());

    } else {
      fromAccount.withdraw(amount);
      System.out.println(threadName + " withdrew " + amount + " from " + fromAccount.getName() + " (" + fromAccount.getBalance() + "$)");

    }
  }
}
