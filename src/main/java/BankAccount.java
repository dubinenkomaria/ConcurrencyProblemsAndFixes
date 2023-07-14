public class BankAccount {
  private int balance;
  private String name;

  public BankAccount(int initialBalance, String name) {
    this.balance = initialBalance;
    this.name = name;
  }

  public int getBalance() {
    return balance;
  }

  public String getName(){
    return name;
  }

  public void deposit(int amount) {
    balance += amount;
  }

  public void withdraw(int amount) {
    balance -= amount;
  }
}
