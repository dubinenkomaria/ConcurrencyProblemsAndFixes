public class MoneyTransferRunnableVisibilityProblem implements Runnable{
  private  BankAccount fromAccount;
  private  BankAccount toAccount;
  private int amount;

  public MoneyTransferRunnableVisibilityProblem(BankAccount fromAccount, BankAccount toAccount, int amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
  }

  public void run() {
    String threadName = Thread.currentThread().getName();

    fromAccount.withdraw(amount);
    System.out.println(threadName + " withdrew " + amount + " from " + fromAccount.getName() + " (" + fromAccount.getBalance() + "$)");
    toAccount.deposit(amount);
    System.out.println(threadName + " deposited " + amount + " to " + toAccount.getName() + " (" + toAccount.getBalance() + "$)");
    try {
      Thread.sleep(200);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
