package MultiThreadedBankAccount;

public final class Transaction {
	
	public final int accountFrom;
	public final int accountTo;
	public final int amount;

	public Transaction(int accountf, int accountt, int num) {
		accountFrom = accountf;
		accountTo = accountt;
		amount = num;
	}
	
	public final int getAmount()
	{
		return this.amount;
	}
	public final int getAccountTo()
	{
		return this.accountTo;
	}
	public final int getAccountFrom()
	{
		return this.accountFrom;
	}
}
