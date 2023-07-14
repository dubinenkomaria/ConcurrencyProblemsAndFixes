import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicBankAccount  {
  private AtomicInteger balance;
  private String name;

  public AtomicBankAccount(int initialBalance, String name) {
    this.balance = new AtomicInteger(initialBalance);
    this.name = name;
  }

  public int getBalance() {
    return balance.get();
  }

  public void deposit(int amount) {
    balance.getAndAdd(amount);
  }

  public void withdraw(int amount) {
    balance.getAndAdd(-amount);
  }

  public String getName() {
    return name;
  }
}
