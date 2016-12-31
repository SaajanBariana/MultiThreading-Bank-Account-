package MultiThreadedBankAccount;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


public class Bank {
	
	String fileName;
	int number;
	BlockingQueue block;
	CountDownLatch countdown;
	CountDownLatch startThreads;
	BankAccount[] accounts;
	
	public static void main(String args[])
	{
		@SuppressWarnings({"unchecked", "unsafe"})
		String testfile = "";
		int number = 0;
		
		if (args.length == 2)
		{
			testfile = args[0];	
			number = Integer.parseInt(args[1]);
			Bank t = new Bank(testfile, number);
			t.start();
		}
		else
		{
			System.out.println("There was an error with the input. Please try to run the program again");
		}
		
	}

	public Bank(String file, int numberOfThreads) 
	{
		fileName = file;	
		number = numberOfThreads;
		block = new ArrayBlockingQueue(number);
		startThreads = new CountDownLatch(0);
		countdown = new CountDownLatch(number);
		accounts = new BankAccount[20];
		for (int i = 0; i < 20; i ++)
		{
			accounts[i] = new BankAccount(i);
		}
	}
	
	public void start()
	{
		//creates new WorkerThreads
		for (int i = 0; i < number; i ++)
		{
			Thread worker = new Thread(new Worker(1));
			worker.start();
		}
		
		startThreads.countDown();
		
		Thread mainThread = new Thread(new Runnable(){
			public void run()
			{
				
				//System.out.println("Reading file");
				try
				{
					Scanner in = new Scanner(new File(fileName));
					int[] info = new int [3];
					while (in.hasNextLine())
					{
						String line = in.nextLine();
						int current = 0;
						int position = 0;
						for(int i = 0; i < line.length(); i++)
						{
							if (line.charAt(i) == ' ')
							{
								info[position] = Integer.parseInt(line.substring(current, i).trim());
								position++;
								current = i;
							}
						}	
						block.put(new Transaction(info[0], info[1], info[2]));
					}
					for (int i = 0; i < number; i++)
					{
						block.put(new Transaction(-1, 0, 0));
					}
			}
			catch(Exception e)
			{
				System.out.println("There is something wrong with file " + e);
			}
			}
		});
		mainThread.run();
		
		
		try 
		{
			countdown.await();
		} 
		catch (Exception e) 
		{
			System.out.println("Countdown problem");
		}
		for(int i = 0; i < accounts.length; i ++)
		{
			System.out.println(accounts[i]);
		}
	}
	
	
	/**-----------------------------------------------------------------------------------*/
	public class Worker implements Runnable
	{
		int num;
		public Worker(int n)
		{
			num = n;
		}
		public void run()
		{
			try 
			{
				startThreads.await();
			} 
			catch (InterruptedException e) 
			{
				System.out.println("Something wrong with threads starting");
			}
			
			boolean done = false;
			while(!done)
			{
				if(block.isEmpty())
				{
					//do nothing
				}
				else
				{
					//check if the transaction is null
					try{
						
						Transaction t = (Transaction) block.take();
						if(t.accountFrom == -1)
						{
							try
							{
								countdown.countDown();
								done = true;
							}
							catch (Exception e)
							{
								System.out.println("There is something wrong with ending the thread");
							}
						}
						//perform some transactions from the accounts
						else
						{
							int from = t.accountFrom;
							int to = t.accountTo;
							int money = t.getAmount();
							accounts[from].changeBalance(-1 * money);
							accounts[from].addTransaction();
							accounts[to].changeBalance(money);
							accounts[to].addTransaction();
						}
					}
					catch(Exception e)
					{
						System.out.println("There was a problem with grabbing a Transaction");
					}
				}
				
			}
		}
	}

}
