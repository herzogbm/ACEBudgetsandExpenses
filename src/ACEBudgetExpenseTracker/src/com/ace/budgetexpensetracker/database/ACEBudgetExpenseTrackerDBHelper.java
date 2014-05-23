/**
 * 
 */
package com.ace.budgetexpensetracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * @author Brandon
 *
 */
public class ACEBudgetExpenseTrackerDBHelper extends SQLiteOpenHelper {
	public static abstract class Budget implements BaseColumns {
	    public static final String TABLE_NAME = "'budget'";
	    public static final String COLUMN_NAME_BUDGET_ID = "budgetid";
	    public static final String COLUMN_NAME_TITLE = "title";
	    public static final String COLUMN_NAME_DESCRIPTION = "description";
	    public static final String COLUMN_NAME_START_DATE = "startdate";
	    public static final String COLUMN_NAME_FREQUENCY = "frequency";
	    public static final String COLUMN_NAME_END_DATE = "enddate";
	    public Budget() {}
	}
	
	public static abstract class Category implements BaseColumns {
	    public static final String TABLE_NAME = "'category'";
	    public static final String COLUMN_NAME_TITLE = "title";
	    public static final String COLUMN_NAME_DESCRIPTION = "description";
	    public static final String COLUMN_NAME_AMOUNT = "amount";
	    public Category() {}
	}
	
	public static abstract class BudgetEntry implements BaseColumns {
	    public static final String TABLE_NAME = "'budgetentry'";
	    public static final String COLUMN_NAME_ENTRY_ID = "entryid";
	    public static final String COLUMN_NAME_TITLE = "title";
	    public static final String COLUMN_NAME_DESCRIPTION = "description";
	    public static final String COLUMN_NAME_BUDGET_ID = "budgetid";
	    public static final String COLUMN_NAME_CATEGORY_ID = "categoryid";
	    public static final String COLUMN_NAME_FREQUENCY = "frequency";
	    public static final String COLUMN_NAME_AMOUNT = "amount";
	    public BudgetEntry() {}
	}
	
	public static abstract class Account implements BaseColumns {
	    public static final String TABLE_NAME = "'account'";
	    public static final String COLUMN_NAME_TITLE = "title";
	    public static final String COLUMN_NAME_DESCRIPTION = "description";
	    public static final String COLUMN_NAME_ACCOUNT_TYPE = "accounttype";
	    public static final String COLUMN_NAME_BALANCE = "balance";
	    public static final String COLUMN_NAME_INTEREST_RATE = "interestrate";
	    public static final String COLUMN_NAME_provider = "provider";
	    public static final String TYPE_CHECKING = "Checking";
	    public static final String TYPE_SAVING = "Saving";
	    public static final String TYPE_CREDIT_CARD = "Credit Card";
	    public static final String TYPE_LOAN = "Loan";
	    public Account() {}
	}
	
	public static abstract class Transaction implements BaseColumns {
	    public static final String TABLE_NAME = "'transaction'";
	    public static final String COLUMN_NAME_DESCRIPTION = "description";
	    public static final String COLUMN_NAME_ACCOUNT_ID = "accountid";
	    public static final String COLUMN_NAME_IS_DEBIT = "isdebit";
	    public static final String COLUMN_NAME_AMOUNT = "amount";
	    public static final String COLUMN_NAME_BALANCE = "balance";
	    public static final String COLUMN_NAME_BUDGET_ITEM_ID = "budgetitemid";
	 // TODO public static final String COLUMN_NAME_IS_RECURRING = "isrecurring";
	 // TODO public static final String COLUMN_NAME_FREQUENCY = "frequency";
	    public static final String COLUMN_NAME_DATE = "date";
	    // TODO public static final String COLUMN_NAME_RECEIPT_IMAGE = "receiptimage";
	    public Transaction() {}
	}
	
	public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "ACEBudgetExpenseTracker.db";
    
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    
    public ACEBudgetExpenseTrackerDBHelper(Context context) {
    	super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	createTables(db);
    	addData(db);
    }
    
    private void addData(SQLiteDatabase db) {
    	// Debug sample accounts
		long tempId = insertAccount(db, "Chase Checking", "Main checking account", Account.TYPE_CHECKING, 2012.88, 0, "Chase");
		insertAccount(db, "Business Checking", "Business checking account", Account.TYPE_CHECKING, 0, 0, "Chase");
		insertAccount(db, "Chase Savings", "Main Savings account", Account.TYPE_SAVING, 0, 0, "Chase");
		insertAccount(db, "Chase Amazon", "Amazon reqards credit card", Account.TYPE_CREDIT_CARD, 799.28, 0, "Chase");
		insertAccount(db, "Student Loans", "Student Loans", Account.TYPE_LOAN, 17099.28, 0, "Nelnet");
		
		long tempBudgetID = insertBudget(db, "Catch All", 400);
		
    	// Debug sample transactions
		insertTransaction(db, "Paycheck", tempId, tempBudgetID, false, 3801.51, 0, "2011-12-01");
		insertTransaction(db, "Chase CC", tempId, tempBudgetID, true, 799.28, 0, "2012-01-01");
		insertTransaction(db, "Rent", tempId, tempBudgetID, true, 690, 0, "2012-01-11");
		insertTransaction(db, "Chase Quick Pay Receive", tempId, tempBudgetID, false, 250, 0, "2012-11-01");
		insertTransaction(db, "Chase Quick Pay Receive", tempId, tempBudgetID, false, 250, 0, "2012-11-01");
		insertTransaction(db, "Insurance", tempId, tempBudgetID, true, 435, 0, "2012-11-11");
		insertTransaction(db, "Discover CC", tempId, tempBudgetID, true, 71.79, 0, "2013-08-01");
		insertTransaction(db, "Citi CC", tempId, tempBudgetID, true, 100.55, 0, "2013-08-01");
		insertTransaction(db, "Student Loans", tempId, tempBudgetID, true, 103.36, 0, "2013-08-01");
		insertTransaction(db, "Upromise CC", tempId, tempBudgetID, true, 367.89, 0, "2013-08-01");
		insertTransaction(db, "Student Loan", tempId, tempBudgetID, true, 47.73, 0, "2013-08-01");
		insertTransaction(db, "ATM Withdrawal", tempId, tempBudgetID, true, 40, 0, "2013-08-01");
		insertTransaction(db, "Cash Deposit", tempId, tempBudgetID, false, 520, 0, "2013-08-01");
		insertTransaction(db, "Chase Quick Pay Correction", tempId, tempBudgetID, true, 125, 0, "2013-08-01");
		
		syncTransactionBalances(db, tempId);
	}

	public void createTables(SQLiteDatabase db) {
    	// Create Transaction Table
    	db.execSQL(
    			"CREATE TABLE " + Transaction.TABLE_NAME + " (" +
						Transaction._ID + " INTEGER PRIMARY KEY," +
						Transaction.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
						Transaction.COLUMN_NAME_ACCOUNT_ID + " INTEGER NOT NULL" + COMMA_SEP +
						Transaction.COLUMN_NAME_IS_DEBIT + " INTEGER NOT NULL" + COMMA_SEP +
						Transaction.COLUMN_NAME_AMOUNT + " REAL NOT NULL" + COMMA_SEP +
						Transaction.COLUMN_NAME_BALANCE + " REAL NOT NULL" + COMMA_SEP +
						Transaction.COLUMN_NAME_BUDGET_ITEM_ID + " INTEGER" + COMMA_SEP +
						// TODO Transaction.COLUMN_NAME_IS_RECURRING + " INTEGER NOT NULL" + COMMA_SEP +
						// TODO Transaction.COLUMN_NAME_FREQUENCY + " INTEGER NOT NULL" + COMMA_SEP +
						Transaction.COLUMN_NAME_DATE + " TEXT " + //COMMA_SEP +
						// TODO Transaction.COLUMN_NAME_RECEIPT_IMAGE + " BLOB " + 
				" )"
			);
    	
    	// Create Account Table
    	db.execSQL(
				"CREATE TABLE " + Account.TABLE_NAME + " (" +
    					Account._ID + " INTEGER PRIMARY KEY," +
    					Account.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
    					Account.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
    					Account.COLUMN_NAME_ACCOUNT_TYPE + TEXT_TYPE + COMMA_SEP +
						Account.COLUMN_NAME_BALANCE + " REAL NOT NULL" + COMMA_SEP +
						Account.COLUMN_NAME_INTEREST_RATE + " REAL NOT NULL" + COMMA_SEP +
						Account.COLUMN_NAME_provider + " TEXT " +  
				" )"
			);
    	
    	// Create BudgetEntry Table
    	db.execSQL(
    			"CREATE TABLE " + BudgetEntry.TABLE_NAME + " (" +
    					BudgetEntry._ID + " INTEGER PRIMARY KEY," +
    					BudgetEntry.COLUMN_NAME_ENTRY_ID  + " INTEGER NOT NULL" + COMMA_SEP +
    					BudgetEntry.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
    					BudgetEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
						BudgetEntry.COLUMN_NAME_CATEGORY_ID   + " INTEGER NOT NULL" + COMMA_SEP +
						BudgetEntry.COLUMN_NAME_FREQUENCY  + " INTEGER NOT NULL" + COMMA_SEP +
						BudgetEntry.COLUMN_NAME_AMOUNT  + " REAL" +  
				" )"
			);
    	
    	// Create Category Table
    	db.execSQL(
    			"CREATE TABLE " + Category.TABLE_NAME + " (" +
    					Category._ID + " INTEGER PRIMARY KEY," +
    					Category.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
    					Category.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
    					Category.COLUMN_NAME_AMOUNT + " REAL " +
				" )"
			);
    	
    	// Create Category Table
    	db.execSQL(
    			"CREATE TABLE " + Budget.TABLE_NAME + " (" +
    					Budget._ID + " INTEGER PRIMARY KEY," +
    					Budget.COLUMN_NAME_BUDGET_ID    + " INTEGER NOT NULL" + COMMA_SEP +
    					Budget.COLUMN_NAME_TITLE + TEXT_TYPE + COMMA_SEP +
    					Budget.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
    					Budget.COLUMN_NAME_START_DATE + TEXT_TYPE + COMMA_SEP +
    					Budget.COLUMN_NAME_FREQUENCY + " INTEGER" + COMMA_SEP +
    					Budget.COLUMN_NAME_END_DATE + TEXT_TYPE +
				" )"
			);
    }
    
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	// This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
    	// BUT first be sure to sync any entries added while offline!!
    	
    	// TODO sync new entries OR convert to new db scheme for future sync
    	
    	// delete tables
        db.execSQL("DROP TABLE IF EXISTS " + Transaction.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Budget.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + BudgetEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Category.TABLE_NAME);        
        db.execSQL("DROP TABLE IF EXISTS " + Account.TABLE_NAME);
        
        // create database
        onCreate(db);
    }
    
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    	onUpgrade(db, oldVersion, newVersion);
    }
    
    public long insertAccount(SQLiteDatabase db, String name, String description, String type, double balance, double interest_rate, String provider)
    {
    	// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Account.COLUMN_NAME_TITLE, name);
		values.put(Account.COLUMN_NAME_DESCRIPTION, description);
		values.put(Account.COLUMN_NAME_ACCOUNT_TYPE, type);
		values.put(Account.COLUMN_NAME_BALANCE, balance);
		values.put(Account.COLUMN_NAME_INTEREST_RATE, interest_rate);
		values.put(Account.COLUMN_NAME_provider, provider);
		
		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(Account.TABLE_NAME, "null", values);
		
		return newRowId;
    }
    
    public long insertAccount(String name, String description, String type, double balance, double interest_rate, String provider)
    {
    	return insertAccount(getWritableDatabase(), name, description, type, balance, interest_rate, provider);
    }
    
    public void updateAccount(long id, String name, String description, String type, double balance, double interest_rate, String provider)
    {
    	updateAccount(getWritableDatabase(), id, name, description, type, balance, interest_rate, provider);
    }
    
    public void updateAccount(SQLiteDatabase db, long id, String name, String description, String type, double balance, double interest_rate, String provider)
    {
    	// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Account.COLUMN_NAME_TITLE, name);
		values.put(Account.COLUMN_NAME_DESCRIPTION, description);
		values.put(Account.COLUMN_NAME_ACCOUNT_TYPE, type);
		values.put(Account.COLUMN_NAME_BALANCE, balance);
		values.put(Account.COLUMN_NAME_INTEREST_RATE, interest_rate);
		values.put(Account.COLUMN_NAME_provider, provider);
		
		// Specify selection criteria
		String selection = Account._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		// Update account
		db.update(Account.TABLE_NAME, values, selection, selectionArgs);
    }
    
    public void syncTransactionBalances(long accound_id)
    {
    	syncTransactionBalances(getWritableDatabase(), accound_id);
    }
    
    public void syncTransactionBalances(SQLiteDatabase db, long accound_id)
    {
    	double balance = getAccountBalance(db, accound_id);
    	Cursor cursor = getTransactionsByAccount(db, accound_id);
    	cursor.moveToFirst();
    	while(!cursor.isAfterLast())
    	{
    		// Create a new map of values, where column names are the keys
    		ContentValues values = new ContentValues();
    		values.put(Transaction.COLUMN_NAME_BALANCE, balance);
    		
    		// Specify selection criteria
    		String selection = Transaction._ID + " LIKE ?";
    		String[] selectionArgs = { cursor.getString(cursor.getColumnIndexOrThrow(Transaction._ID)) };
    		
    		if(cursor.getInt(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_IS_DEBIT))==0)
    		{
    			balance -= cursor.getDouble(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_AMOUNT));
    		}else{
    			balance += cursor.getDouble(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_AMOUNT));
    		}
    		
    		// Update transaction
    		db.update(Transaction.TABLE_NAME, values, selection, selectionArgs);
    		cursor.moveToNext();
    	}
    }
    
    public Cursor getAccount(long id)
    {
    	return getAccount(getWritableDatabase(), id);
    }

    public Cursor getAccount(SQLiteDatabase db, long id)
    {    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Account.COLUMN_NAME_TITLE,
    			Account.COLUMN_NAME_DESCRIPTION,
    			Account.COLUMN_NAME_ACCOUNT_TYPE,
    			Account.COLUMN_NAME_BALANCE,
    			Account.COLUMN_NAME_INTEREST_RATE,
    			Account.COLUMN_NAME_provider
    	    };
    	
    	// Specify selection criteria
		String selection = Account._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		Cursor c = db.query(Account.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		return c;
    }
    
    public String getAccountName(long id)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Account.COLUMN_NAME_TITLE,
    			Account.COLUMN_NAME_DESCRIPTION,
    			Account.COLUMN_NAME_ACCOUNT_TYPE,
    			Account.COLUMN_NAME_BALANCE,
    			Account.COLUMN_NAME_INTEREST_RATE,
    			Account.COLUMN_NAME_provider
    	    };
    	
    	// Specify selection criteria
		String selection = Account._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		Cursor c = db.query(Account.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		c.moveToFirst();
		if(c.getCount() > 0)
			return c.getString(c.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE));
		else		
			return "";
    }
    
    public String getBudgetName(long id)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Category.COLUMN_NAME_TITLE
    	    };
    	
    	// Specify selection criteria
		String selection = Category._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		Cursor c = db.query(Category.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		c.moveToFirst();
		if(c.getCount() > 0)
			return c.getString(c.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE));
		else		
			return "";
    }
    
    public Cursor getAccountByType(String type)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Account._ID,
    			Account.COLUMN_NAME_TITLE,
    			Account.COLUMN_NAME_DESCRIPTION,
    			Account.COLUMN_NAME_ACCOUNT_TYPE,
    			Account.COLUMN_NAME_BALANCE,
    			Account.COLUMN_NAME_INTEREST_RATE,
    			Account.COLUMN_NAME_provider
    	    };
    	
    	// Specify selection criteria
		String selection = Account.COLUMN_NAME_ACCOUNT_TYPE + " LIKE ?";
		String[] selectionArgs = { type };
		
		Cursor c = db.query(Account.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		
		return c;
    }
    public double getAccountBalance(long id)
    {
    	return getAccountBalance(getWritableDatabase(), id);
    }
    
    public double getAccountBalance(SQLiteDatabase db, long id)
    {    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Account.COLUMN_NAME_BALANCE
    	    };
    	
    	// Specify selection criteria
		String selection = Account._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		Cursor c = db.query(Account.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		c.moveToFirst();
		if(c.getCount() > 0)
			return c.getDouble(0);
		return 0;
    }
    
    public long insertTransaction(String description, long accountID, long budgetID, boolean isDebit, double amount, double balance, String date)
    {
    	return insertTransaction(getWritableDatabase(), description, accountID, budgetID, isDebit, amount, balance, date);
    }
    
    public long insertTransaction(SQLiteDatabase db, String description, long accountID, long budgetID, boolean isDebit, double amount, double balance, String date)
    {    	
    	// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Transaction.COLUMN_NAME_DESCRIPTION, description);
		values.put(Transaction.COLUMN_NAME_ACCOUNT_ID, accountID);
		values.put(Transaction.COLUMN_NAME_IS_DEBIT, isDebit);
		values.put(Transaction.COLUMN_NAME_AMOUNT, amount);
		values.put(Transaction.COLUMN_NAME_BALANCE, balance);
		values.put(Transaction.COLUMN_NAME_BUDGET_ITEM_ID, budgetID);
		// TODO values.put(Transaction.COLUMN_NAME_IS_RECURRING, isRecurring);
		// TODO values.put(Transaction.COLUMN_NAME_FREQUENCY, frequency);
		values.put(Transaction.COLUMN_NAME_DATE, date);
		// TODO values.put(Transaction.COLUMN_NAME_RECEIPT_IMAGE, provider);
		
		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(Transaction.TABLE_NAME, "null", values);
		
		if(isDebit)
			AccountAddToBalance(db, accountID, -amount);
		else
			AccountAddToBalance(db, accountID, amount);
		
		syncTransactionBalances(db, accountID);

		return newRowId;
    }
    
    public void updateTransaction(long id, String description, long accountID, long budgetID, boolean isDebit, double amount, double balance, String date)
    {
    	double changeInBalance = 0;
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Get old values
    	Cursor cursor = getTransaction(id);
    	double oldAmount = amount;
    	boolean oldIsDebit = isDebit;
    	long oldAccountID = accountID;
    	cursor.moveToFirst();
    	if(cursor.getCount() > 0)
    	{
    		oldAmount = cursor.getDouble(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_AMOUNT));
    		oldIsDebit = (cursor.getInt(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_IS_DEBIT)) > 0);
    		oldAccountID = cursor.getLong(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_ACCOUNT_ID));
    	}
    	
    	// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Transaction.COLUMN_NAME_DESCRIPTION, description);
		values.put(Transaction.COLUMN_NAME_ACCOUNT_ID, accountID);
		values.put(Transaction.COLUMN_NAME_IS_DEBIT, isDebit);
		values.put(Transaction.COLUMN_NAME_AMOUNT, amount);
		values.put(Transaction.COLUMN_NAME_BALANCE, balance);
		values.put(Transaction.COLUMN_NAME_BUDGET_ITEM_ID, budgetID);
		// TODO values.put(Transaction.COLUMN_NAME_IS_RECURRING, isRecurring);
		// TODO values.put(Transaction.COLUMN_NAME_FREQUENCY, frequency);
		values.put(Transaction.COLUMN_NAME_DATE, date);
		// TODO values.put(Transaction.COLUMN_NAME_RECEIPT_IMAGE, provider);
		
		// Specify selection criteria
		String selection = Transaction._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		// Update transaction
		db.update(Transaction.TABLE_NAME, values, selection, selectionArgs);
		
		if(accountID != oldAccountID)
		{
			// Update old account
			if(oldIsDebit)
				AccountAddToBalance(oldAccountID, oldAmount);
			else
				AccountAddToBalance(oldAccountID, -oldAmount);
			
			// Update new account
			if(isDebit)
				AccountAddToBalance(accountID, -amount);
			else
				AccountAddToBalance(accountID, amount);
		}
		
		else
		{
			if(isDebit != oldIsDebit)
			{
				if(isDebit)
				{
					changeInBalance -= (2 * oldAmount);
				}
				else
				{
					changeInBalance += (2 * oldAmount);
				}
			}
			
			if(amount != oldAmount)
			{
				if(isDebit)
					changeInBalance -= (amount - oldAmount);
				else
					changeInBalance += (amount - oldAmount);
			}
			
			if(changeInBalance != 0)
				AccountAddToBalance(accountID, changeInBalance);
		}
		
		syncTransactionBalances(db, accountID);
    }
    
    public Cursor getTransaction(long id)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Transaction.COLUMN_NAME_DESCRIPTION,
    			Transaction.COLUMN_NAME_ACCOUNT_ID, 
    			Transaction.COLUMN_NAME_IS_DEBIT,
    			Transaction.COLUMN_NAME_AMOUNT,
    			Transaction.COLUMN_NAME_BALANCE,
    			Transaction.COLUMN_NAME_BUDGET_ITEM_ID,
    			// TODO Transaction.COLUMN_NAME_IS_RECURRING, 
    			// TODO Transaction.COLUMN_NAME_FREQUENCY,
    			Transaction.COLUMN_NAME_DATE
    	    };
    	
    	// Specify selection criteria
		String selection = Transaction._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		Cursor c = db.query(Transaction.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		return c;
    }
    
    public Cursor getTransactionsByAccount(long id)
    {
    	return getTransactionsByAccount(getWritableDatabase(),id);
    }
    
    public Cursor getTransactionsByAccount(SQLiteDatabase db, long id)
    {
    	// Specify selection criteria
		String selection = Transaction.COLUMN_NAME_ACCOUNT_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		//TODO order by date!! (no date exists yet)
		String orderBy = Transaction.COLUMN_NAME_DATE + " DESC, " + Transaction._ID + " DESC";
		
		Cursor c = db.query(Transaction.TABLE_NAME, null, selection, selectionArgs, null, null, orderBy);
		return c;
    }
    
    public Cursor getTransactionsByAccount(String name)
    {
    	long id = getAccountId(name);
    	if( id>0)
    		return getTransactionsByAccount(getAccountId(name));
    	else
    		return null; // TODO proper error check
    }
    
    public Cursor getTransactionsByBudget(long id)
    {
    	return getTransactionsByBudget(getWritableDatabase(),id);
    }
    
    public Cursor getTransactionsByBudget(SQLiteDatabase db, long id)
    {
    	// Specify selection criteria
		String selection = Transaction.COLUMN_NAME_BUDGET_ITEM_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		//TODO order by date!! (no date exists yet)
		String orderBy = Transaction.COLUMN_NAME_DATE + " DESC, " + Transaction._ID + " DESC";
		
		Cursor c = db.query(Transaction.TABLE_NAME, null, selection, selectionArgs, null, null, orderBy);
		return c;
    }
    
    public Cursor getTransactionsByBudget(long id, String startDate, String endDate)
    { 
    	SQLiteDatabase db = getWritableDatabase();
    	// Specify selection criteria
		String selection = Transaction.COLUMN_NAME_BUDGET_ITEM_ID + " LIKE ? AND " + Transaction.COLUMN_NAME_DATE + " BETWEEN ? AND ?";
		String[] selectionArgs = { String.valueOf(id), startDate, endDate };
		
		String orderBy = Transaction.COLUMN_NAME_DATE + " DESC, " + Transaction._ID + " DESC";
		
		Cursor c = db.query(Transaction.TABLE_NAME, null, selection, selectionArgs, null, null, orderBy);
		return c;
    }
    
    public long insertBudget(String title, double amount)
    {
    	return insertBudget(getWritableDatabase(), title, amount);
    }
    
    public long insertBudget(SQLiteDatabase db, String title, double amount)
    {
    	// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(Category.COLUMN_NAME_TITLE, title);
		//values.put(Category.COLUMN_NAME_FREQUENCY, "MONTHLY");
		values.put(Category.COLUMN_NAME_AMOUNT, amount);
		
		// Insert the new row, returning the primary key value of the new row
		long newRowId;
		newRowId = db.insert(Category.TABLE_NAME, "null", values);
		
		return newRowId;
    }
    
    public void updateBudget(long id, String title, double amount)
    {
    	updateBudget(getWritableDatabase(), id, title, amount);
    }
    
    public void updateBudget(SQLiteDatabase db, long id, String title, double amount)
    {
    	// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(Category.COLUMN_NAME_TITLE, title);
			//values.put(BudgetEntry.COLUMN_NAME_FREQUENCY, "MONTHLY");
			values.put(Category.COLUMN_NAME_AMOUNT, amount);
    			
		// Specify selection criteria
			String selection = Category._ID + " LIKE ?";
			String[] selectionArgs = { String.valueOf(id) };
			
		// Update transaction
			db.update(Category.TABLE_NAME, values, selection, selectionArgs);
    }
    
    public double getBudgetAmount(long id)
    {
    	SQLiteDatabase db = getWritableDatabase();
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Category.COLUMN_NAME_AMOUNT
    	    };
    	
    	// Specify selection criteria
		String selection = Category._ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		Cursor c = db.query(Category.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		c.moveToFirst();
		if(c.getCount() > 0)
			return c.getDouble(0);
		return 0;
    }
 // TODO 
    /*
    public Cursor getTransactionsByBudget(long id)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Transaction.COLUMN_NAME_DESCRIPTION,
    			Transaction.COLUMN_NAME_ACCOUNT_ID, 
    			Transaction.COLUMN_NAME_IS_DEBIT,
    			Transaction.COLUMN_NAME_AMOUNT,
    			Transaction.COLUMN_NAME_BALANCE,
    			// TODO Transaction.COLUMN_NAME_BUDGET_ITEM_ID,
    			// TODO Transaction.COLUMN_NAME_IS_RECURRING, 
    			// TODO Transaction.COLUMN_NAME_FREQUENCY,
    			Transaction.COLUMN_NAME_DATE
    	    };
    	
    	// Specify selection criteria
		String selection = Transaction.COLUMN_NAME_BUDGET_ITEM_ID + " LIKE ?";
		String[] selectionArgs = { String.valueOf(id) };
		
		Cursor c = db.query(Transaction.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
		return c;
    }
    */
    public Cursor getTable(String tableName)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	Cursor c = db.query(tableName, null, null, null, null, null, null);		
		return c;
    }
    
    //public Cursor getAccountNames()
    public String[] getAccountNames()
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Account._ID,
    			Account.COLUMN_NAME_TITLE, 
    	    };
    	Cursor c = db.query(Account.TABLE_NAME, projection, null, null, null, null, null);	
    	
    	c.moveToFirst();
		String[] list = new String[c.getCount()];
		while(!c.isAfterLast())
		{
			list[c.getPosition()] = c.getString(c.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE));
			c.moveToNext();
		}
		return list;
		//return c;
    }
    
    public String[] getCategoryNames()
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Category._ID,
    			Category.COLUMN_NAME_TITLE, 
    	    };
    	Cursor c = db.query(Category.TABLE_NAME, projection, null, null, null, null, null);	
    	
    	c.moveToFirst();
		String[] list = new String[c.getCount()];
		while(!c.isAfterLast())
		{
			list[c.getPosition()] = c.getString(c.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE));
			c.moveToNext();
		}
		return list;
		//return c;
    }
    
    public Cursor getUniqueItem(String tableName, long id)
    {
    	if(tableName == Account.TABLE_NAME)
    	{
    		return getAccount(id);
    	}
    	else if(tableName == Transaction.TABLE_NAME)
    	{
    		return getTransaction(id);
    	}
    	else
    	{
    		return null;
    	}
    }
    
    public void AccountAddToBalance(long id, double amount)
    {
    	AccountAddToBalance(getWritableDatabase(), id, amount);
    }
    
    public void AccountAddToBalance(SQLiteDatabase db, long id, double amount)
    {
    	Cursor cursor = getAccount(db, id);
    	cursor.moveToFirst();
    	updateAccount(
    			db,
    			id, 
    			cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE)), 
    			cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_DESCRIPTION)),
    			cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_ACCOUNT_TYPE)),
    			cursor.getDouble(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_BALANCE)) + amount, 
    			cursor.getDouble(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_INTEREST_RATE)),
    			cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_provider))
    			);
    }
    
    public long getAccountId(String name)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Account._ID, 
    	    };
    	String selection = Account.COLUMN_NAME_TITLE + " LIKE ?";
    	String[] selectionArgs = { name };
    	Cursor c = db.query(Account.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    	c.moveToFirst();
    	if(c.getCount() > 0)
    	{
    		return c.getLong(0);
    	}
    	return 1;
    }
    
    public long getBudgetId(String name)
    {
    	// Gets the data repository in write mode
    	SQLiteDatabase db = getWritableDatabase();
    	
    	// Define a projection that specifies which columns from the database
    	// you will actually use after this query.
    	String[] projection = {
    			Category._ID, 
    	    };
    	String selection = Category.COLUMN_NAME_TITLE + " LIKE ?";
    	String[] selectionArgs = { name };
    	Cursor c = db.query(Category.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
    	c.moveToFirst();
    	if(c.getCount() > 0)
    	{
    		return c.getLong(0);
    	}
    	return 1;
    }
}
