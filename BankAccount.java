package MultiThreadedBankAccount;

public class BankAccount {
	
	double balance;
	int transactions;
	int accountNumber;
	
	public BankAccount(int number) {
		balance = 1000;
		transactions = 0;
		accountNumber = number;
		
	}
	
	public synchronized void addTransaction()
	{
		transactions ++;
	}
	
	public synchronized void changeBalance(int num)
	{
		balance += num;
	}
	
	
	public String toString()
	{
		return "acct: " + accountNumber + " bal: " + balance + " trans: " + transactions;
	}
}
