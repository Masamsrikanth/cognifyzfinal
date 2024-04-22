import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class BankAccount {
    private double balance;
    private final Lock lock = new ReentrantLock();

    public BankAccount(double initialBalance) {
        this.balance = initialBalance;
    }

    public void withdraw(double amount) {
        lock.lock();
        try {
            if (balance >= amount) {
                balance -= amount;
                System.out.println(Thread.currentThread().getName() + " withdrew " + amount + ". Remaining balance: " + balance);
            } else {
                System.out.println(Thread.currentThread().getName() + " tried to withdraw " + amount + " but insufficient funds.");
            }
        } finally {
            lock.unlock();
        }
    }
}

class WithdrawThread extends Thread {
    private final BankAccount account;
    private final double amount;

    public WithdrawThread(BankAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }

    @Override
    public void run() {
        account.withdraw(amount);
    }
}

public class ConcurrentBankDemo {
    public static void main(String[] args) {
        BankAccount account = new BankAccount(1000);

        Thread thread1 = new WithdrawThread(account, 300);
        Thread thread2 = new WithdrawThread(account, 500);

        thread1.start();
        thread2.start();
    }
}
