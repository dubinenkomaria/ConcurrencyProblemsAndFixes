import org.junit.Test;

public class MoneyTransferTest {
/**
 * Deadlock may appear as if all the threads acquire their locks not in the same order
 * In this test deadlock can occur if two threads call transferMoney at the same time
 * one transferring from X to Y, and the other doing the opposite.
 * With unlucky timing, A will acquire the lock on accountMariia and wait for the
 * lock on accountUlises, while B is holding the lock on accountUlises and waiting for
 * the lock on accountMariia
 **/
  @Test
  public void testDeadlock() throws InterruptedException{
    BankAccount accountMariia = new BankAccount(1000, "Mariia's account");
    BankAccount accountUlises = new BankAccount(500, "Ulises's account");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableDeadLock(accountMariia, accountUlises, 200));
    Thread transferThread2 = new Thread(new MoneyTransferRunnableDeadLock(accountUlises, accountMariia, 300));

    transferThread1.start();
    transferThread2.start();
    transferThread1.join();
    transferThread2.join();

    // Print the final balances
    System.out.println("Final balance of Account 1: " + accountMariia.getBalance());
    System.out.println("Final balance of Account 2: " + accountUlises.getBalance());
  }

  /**
   * One way to induce an ordering on objects is to use System.identityHashCode,
   * which returns the value that would be returned by Object.hashCode.
   * It involves a few extra lines of code, but eliminates the possibility of deadlock.
   * **/
  @Test
  public void testDeadlockSolution() throws InterruptedException{
    BankAccount accountMariia = new BankAccount(1000, "Mariia's account");
    BankAccount accountUlises = new BankAccount(500, "Ulises's account");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableWithIdentityHashCode(accountMariia, accountUlises, 200));
    Thread transferThread2 = new Thread(new MoneyTransferRunnableWithIdentityHashCode(accountUlises, accountMariia, 300));

    transferThread1.start();
    transferThread2.start();
    transferThread1.join();
    transferThread2.join();

    System.out.println("Final balance of Account 1: " + accountMariia.getBalance());
    System.out.println("Final balance of Account 2: " + accountUlises.getBalance());
  }

  /** Race condition: A race condition occurs when the behavior of a program depends on the relative timing
   or interleaving of multiple threads. It can lead to inconsistent and incorrect results when threads access
   and modify shared data simultaneously. To avoid race conditions, there must be a way to prevent other
   threads from using a variable while we&rsquo;re in the middle of modifying it, so we
   can ensure that other threads can observe or modify the state only before we start
   or after we finish, but not in the middle.

   To ensure thread safety, check-then-act operations (like lazy initialization)
   and read-modify-write operations (like increment) must always be atomic **/
  @Test
  public void testRaceCondition() throws InterruptedException {
    BankAccount account1 = new BankAccount(1000, "Account 1");
    BankAccount account2 = new BankAccount(500, "Account 2");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableRaceCondition(account1, account2, 200));
    Thread transferThread2 = new Thread(new MoneyTransferRunnableRaceCondition(account1, account2, 300));

    transferThread1.start();
    transferThread2.start();

    transferThread1.join();
    transferThread2.join();

    System.out.println("Final balance of Account 1: " + account1.getBalance());
    System.out.println("Final balance of Account 2: " + account2.getBalance());

    /*From time to time there are incorrect results and not always same, try to run multiple times.
    What I had in result for example:
    Thread-0 withdrew 200.0 from Account 1 (500.0$)
    Thread-1 withdrew 300.0 from Account 1 (500.0$)
    Thread-1 deposited 300.0 to Account 2 (700.0$)
    Thread-0 deposited 200.0 to Account 2 (700.0$)
    Final balance of Account 1: 500.0
    Final balance of Account 2: 700.0

    Result is correct in "lucky" cases when thread1 makes both actions, then thread2 makes both actions
    In some cases (like 32bit systems and double variables atomicity is a problem in one adding operation)
    */
  }

  @Test
  public void testRaceConditionFixWithAtomic() throws InterruptedException {
    AtomicBankAccount account1 = new AtomicBankAccount(1000, "Account 1");
    AtomicBankAccount account2 = new AtomicBankAccount(500, "Account 2");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableRaceConditionAtomic(account1, account2, 200));
    Thread transferThread2 = new Thread(new MoneyTransferRunnableRaceConditionAtomic(account1, account2, 300));

    transferThread1.start();
    transferThread2.start();
    transferThread1.join();
    transferThread2.join();

    System.out.println("Final balance of Account 1: " + account1.getBalance());
    System.out.println("Final balance of Account 2: " + account2.getBalance());
  }

  @Test
  public void testRaceConditionFixWithMutex() throws InterruptedException {
    BankAccount account1 = new BankAccount(1000, "Account 1");
    BankAccount account2 = new BankAccount(500, "Account 2");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableMutex(account1, account2, 200));
    Thread transferThread2 = new Thread(new MoneyTransferRunnableMutex(account1, account2, 300));

    transferThread1.start();
    transferThread2.start();
    transferThread1.join();
    transferThread2.join();

    System.out.println("Final balance of Account 1: " + account1.getBalance());
    System.out.println("Final balance of Account 2: " + account2.getBalance());
  }



  /**Java provides a built-in locking mechanism for enforcing atomicity: the synchronized block.
   * A synchronized block has two parts: a reference to an object that will serve as the lock, and a
   * block of code to be guarded by that lock. A synchronized method is a shorthand
   * for a synchronized block that spans an entire method body.
   * Every Java object can implicitly act as a lock for purposes of synchronization;
   * these built-in locks are called intrinsic locks or monitor locks.
   * Intrinsic locks in Java act as mutexes (or mutual exclusion locks), which means
   * that at most one thread may own the lock.
   **/

  @Test
  public void testRaceConditionFixWithSynchronisation() throws InterruptedException {
    BankAccount account1 = new BankAccount(1000, "Account 1");
    BankAccount account2 = new BankAccount(500, "Account 2");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableRaceConditionLock(account1, account2, 200));
    Thread transferThread2 = new Thread(new MoneyTransferRunnableRaceConditionLock(account1, account2, 300));

    transferThread1.start();
    transferThread2.start();

    transferThread1.join();
    transferThread2.join();

    System.out.println("Final balance of Account 1: " + account1.getBalance());
    System.out.println("Final balance of Account 2: " + account2.getBalance());
  }


  //TODO: in the book "Java concurrency in practice" read part "The Java Memory Model in 500 words or less" it is great
  //https://kb.epam.com/pages/viewpage.action?spaceKey=GDOKB&title=Data+Engineering+CommunityLow+latency+course this list is also great
  /**
   * A data race occurs when a variable is read by more than one thread, and written
   * by at least one thread, but the reads and writes are not ordered by happens-before.
   * When two threads synchronize on different locks, we can’t say anything about the ordering
   * of actions between them—there is no happens-before relation between the actions
   * in the two threads.
   * **/
  @Test
  public void testDataRaceExample(){
//example is same as race condition. Sometimes they occur both, sometime separately
    //in this case happens-before violation affected so we can observe data race and race condition
    /*So data race - violating happens-before rule (
    * Race condition - violating several rules: Atomicity, Mutual Exclusion, Order of Operations, Visibility, Deadlocks
    * */
  }

  @Test
  public void testVisibilityProblem()throws InterruptedException {
    BankAccount account1 = new BankAccount(1000, "Account 1");
    BankAccount account2 = new BankAccount(500, "Account 2");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableVisibilityProblem(account1, account2, 200));
    Thread transferThread2 = new Thread(new PayRunnableVisibilityProblem( account2, 600));

    transferThread1.start();
    transferThread2.start();
    transferThread1.join();
    transferThread2.join();

    System.out.println("Final balance of Account 1: " + account1.getBalance());
    System.out.println("Final balance of Account 2: " + account2.getBalance());

    /*
    It can occur that after sending from account1 to account2 money that are sufficient to pay
    accont2 client still can't pay and observes old value
    Thread-0 withdrew 200 from Account 1 (800$)
    Thread-0 deposited 200 to Account 2 (700$)
    Thread-1 does nothing cause it's less then 600$ onAccount 2
    Final balance of Account 1: 800
    Final balance of Account 2: 700*/
  }

  @Test
  public void testFixVisibilityProblemWithVolatile()throws InterruptedException {
    BankAccount account1 = new BankAccount(1000, "Account 1");
    BankAccount account2 = new BankAccount(500, "Account 2");

    Thread transferThread1 = new Thread(new MoneyTransferRunnableVisibilityProblemVolatileFix(account1, account2, 200));
    Thread transferThread2 = new Thread(new PayRunnableVisibilityProblemVolatileFix( account2, 600));

    transferThread1.start();
    transferThread2.start();
    transferThread1.join();
    transferThread2.join();

    System.out.println("Final balance of Account 1: " + account1.getBalance());
    System.out.println("Final balance of Account 2: " + account2.getBalance());

    /*In this case thread2 will see volatile variable only after threas1 finishes working :( Not a good fix*/

    /*Locking can guarantee both visibility and atomicity; volatile variables can only guarantee visibility -  in book*/
  }

}
