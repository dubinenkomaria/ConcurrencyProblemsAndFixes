public class MoneyTransferRunnableWithIdentityHashCode implements Runnable{
  private BankAccount fromAccount;
  private BankAccount toAccount;
  private int amount;
  private static final Object tieLock = new Object(); //in rare cases when hashcode is same
  public MoneyTransferRunnableWithIdentityHashCode(BankAccount fromAccount, BankAccount toAccount, int amount) {
    this.fromAccount = fromAccount;
    this.toAccount = toAccount;
    this.amount = amount;
  }

    public void transfer() throws InsufficientFundsException {
      if (fromAccount.getBalance() < amount)
        throw new InsufficientFundsException();
      else {
        fromAccount.withdraw(amount);
        toAccount.deposit(amount);
      }
    }

  public void run() {
    String threadName = Thread.currentThread().getName();
    int fromHash = System.identityHashCode(fromAccount);
    int toHash = System.identityHashCode(toAccount);

    if (fromHash < toHash) {
      synchronized (fromAccount) {
        System.out.println(threadName + " acquired lock " + fromAccount.getName());
        synchronized (toAccount) {
          System.out.println(threadName + " acquired lock " + toAccount.getName());
          transfer();
        }
      }
    } else if (fromHash > toHash) {
      synchronized (toAccount) {
        System.out.println(threadName + " acquired lock " + toAccount.getName());
        synchronized (fromAccount) {
          System.out.println(threadName + " acquired lock " + fromAccount.getName());
          transfer();
        }
      }
    }
    /*In the rare case that two objects have the same hash code, we must use an
    arbitrary means of ordering the lock acquisitions, and this reintroduces the possibility of deadlock.
    To prevent inconsistent lock ordering in this case, a third “tie breaking” lock is used.
    By acquiring the tie-breaking lock before acquiring either
    Account lock, we ensure that only one thread at a time performs the risky task of
    acquiring two locks in an arbitrary order, eliminating the possibility of deadlock*/
    else {
      synchronized (tieLock) {
        synchronized (fromAccount) {
          System.out.println(threadName + " acquired lock " + fromAccount.getName());
          synchronized (toAccount) {
            System.out.println(threadName + " acquired lock " + toAccount.getName());
            transfer();
          }
        }
      }
    }
  }
}

