package com.ace.budgetexpensetracker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper;
import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Account;
import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Transaction;
import com.ace.budgetexpensetracker.views.IDTextView;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditTransactionActivity extends Activity{
	ACEBudgetExpenseTrackerDBHelper mDbHelper;
	boolean isUpdateMode;
	Spinner account_spinner, budget_spinner;
	long account_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Draw view
			setContentView(R.layout.edit_transaction);
		
		// Set up the action bar to show a dropdown list.
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
		// Fill in
			account_id = 0;
			Calendar c = Calendar.getInstance();
			((EditText)findViewById(R.id.input_transaction_date)).setText(c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH)+1) + "-" + c.get(Calendar.DAY_OF_MONTH));
		
			isUpdateMode = false;
		
		// Create database helper
			mDbHelper = new ACEBudgetExpenseTrackerDBHelper(getBaseContext());
		
		// Get spinner items
			account_spinner = (Spinner)(findViewById(R.id.input_transaction_account_id));
			budget_spinner = (Spinner)(findViewById(R.id.input_transaction_budget));
		//SpinnerAdapter adapter = account_spinner.getAdapter();
		
		// TODO fill spinner form items
//		Cursor tcursor = mDbHelper.getAccountNames();
//		tcursor.moveToFirst();
//		String[] list = new String[tcursor.getCount()];
//		while(!tcursor.isAfterLast())
//		{
//			list[tcursor.getPosition()] = tcursor.getString(tcursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE));
//			tcursor.moveToNext();
//		}
//		ArrayAdapter<IDTextView> adapter = getAccountList();
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, mDbHelper.getAccountNames());
		account_spinner.setAdapter(adapter);
		
		ArrayAdapter<String> budget_adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_dropdown_item_1line, mDbHelper.getCategoryNames());
		budget_spinner.setAdapter(budget_adapter);
		
		// Determine if called from intent with parameters
		Intent intent = getIntent();
		long id = intent.getLongExtra("id", -1);
		account_id = intent.getLongExtra("account_id", -1);
		if(account_id != -1)
		{
			Cursor account_cursor = mDbHelper.getAccount(account_id);
			account_cursor.moveToFirst();
			if(account_cursor.getCount() > 0)
			{
				account_spinner.setSelection(adapter.getPosition(account_cursor.getString(account_cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE))));
			}
		}
		else
			account_id = 1;
		if(id != -1)
		{
			Cursor cursor = mDbHelper.getTransaction(id);
			cursor.moveToFirst();
			if(cursor.getCount() > 0)
			{
				// Set up view for update data, rather than create data
				// Get id editText and assign to the given id
				((EditText)findViewById(R.id.input_transaction_ID)).setText(String.valueOf(id));
				((EditText)findViewById(R.id.input_transaction_description)).setText(cursor.getString(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_DESCRIPTION)));
				((EditText)findViewById(R.id.input_transaction_amount)).setText(cursor.getString(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_AMOUNT)));
				((CheckBox)findViewById(R.id.input_is_debit)).setChecked(0 < cursor.getInt(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_IS_DEBIT)));
				((EditText)findViewById(R.id.input_transaction_date)).setText(cursor.getString(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_DATE)));
				Cursor account_cursor = mDbHelper.getAccount(cursor.getLong(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_ACCOUNT_ID)));
				account_cursor.moveToFirst();
				if(account_cursor.getCount() > 0)
				{
					account_spinner.setSelection(adapter.getPosition(account_cursor.getString(account_cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE))));
				}
				//String budget_name = adapter.getPosition(mDbHelper.getBudgetName(account_cursor.getLong(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_BUDGET_ITEM_ID))));
				//if(budget_name != "")
				{
					budget_spinner.setSelection(budget_adapter.getPosition(mDbHelper.getBudgetName(cursor.getLong(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_BUDGET_ITEM_ID)))));
				}
				isUpdateMode = true;
				//account_spinner.setEnabled(false);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_menu, menu);
		return true;
	}
	
	public ArrayAdapter<IDTextView> getAccountList()
	{
		// Create list
		ArrayAdapter<IDTextView> list = new ArrayAdapter<IDTextView>(getBaseContext(), android.R.layout.simple_spinner_dropdown_item);
		
		// Get Account List
		Cursor cursor = mDbHelper.getTable(Account.TABLE_NAME);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			IDTextView textview = new IDTextView(getBaseContext());
			textview.setText(cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE)));
			textview.setID(cursor.getLong(cursor.getColumnIndexOrThrow(Account._ID)));
			list.add(textview);
			cursor.moveToNext();
		}
		
		// Return the list
		return list;
	}

	public int findPosition(ArrayAdapter<IDTextView> adapter, String searchTerm)
	{
		for(int i = 0; i < adapter.getCount(); i++)
		{
			IDTextView textview = (IDTextView) adapter.getItem(i);
			if(textview.getText() == searchTerm)
			{
				return i;
			}
		}
		return 0;
	}
	
	public boolean syncRecord()
	{
		boolean dateValid = true;
		// Get element values from form
			String description = ((EditText)(findViewById(R.id.input_transaction_description))).getText().toString();
		
			// Get account selection
			String account_id_string = ((Spinner)(findViewById(R.id.input_transaction_account_id))).getSelectedItem().toString();
			account_id = mDbHelper.getAccountId(account_id_string);
			// TODO Translate selection to ID
			//long accountID = 1;
			// Get budget id
			String budget_id_string = ((Spinner)(findViewById(R.id.input_transaction_budget))).getSelectedItem().toString();
			// TODO Translate selection to ID
			long budgetItemID = mDbHelper.getBudgetId(budget_id_string);
			boolean isDebit = ((CheckBox)(findViewById(R.id.input_is_debit))).isChecked();
		
			// Get amount String and check string
			String amount_string = ((EditText)(findViewById(R.id.input_transaction_amount))).getText().toString();
			if(amount_string.equals("")) amount_string = "0";
			double amount = Double.valueOf(amount_string).doubleValue();
		
			String[] transaction_date = ((EditText)findViewById(R.id.input_transaction_date)).getText().toString().split("-");
			int month = 0, dayOfMonth = 0, year = 0;
			if(transaction_date.length != 3) dateValid = false;
			else{
				try{
					month = Integer.valueOf(transaction_date[1]);
					dayOfMonth = Integer.valueOf(transaction_date[2]);
					year = Integer.valueOf(transaction_date[0]);
					if(month <= 0 || month > 12) dateValid = false;
					else
					{
						Calendar mycal = new GregorianCalendar(year, month - 1, 1);
						if(dayOfMonth <= 0 || dayOfMonth > mycal.getActualMaximum(Calendar.DAY_OF_MONTH)) dateValid = false;
					}
				}catch(Exception e){
					dateValid = false;
				}
			}
			
		
			// Update database
			if(!dateValid)
			{
				((EditText)findViewById(R.id.input_transaction_date)).setTextColor(getResources().getColor(android.R.color.holo_red_dark));
				Toast.makeText(getBaseContext(), "Invalid date! YYYY-MM-DD", Toast.LENGTH_SHORT).show();
				return false;
			}
			else if(isUpdateMode)
			{
				long id = Long.valueOf(((EditText)(findViewById(R.id.input_transaction_ID))).getText().toString());
				mDbHelper.updateTransaction(id, description, account_id, budgetItemID, isDebit, amount, 0, String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth));
				//mDbHelper.updateTransaction(account_id, description, account_id, isDebit, amount, budgetItemID, false, 0, transaction_date);
				return true;
			}
			else
			{
				// Insert new account into database
				long newRowId = mDbHelper.insertTransaction(description, account_id, budgetItemID, isDebit, amount, 0, String.format("%04d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth));
				
				// Set the form to the new RowId
				((TextView)findViewById(R.id.input_transaction_ID)).setText(newRowId + "");
				return true;
			}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Sync data before returning up a level
		case R.id.action_commit:
			if(!syncRecord()) 
				return false;
		// Cancel edit and return to up a level
		case R.id.action_discard:
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
	        // Get account selection
	     			String account_id_string = ((Spinner)(findViewById(R.id.input_transaction_account_id))).getSelectedItem().toString();
	     			account_id = mDbHelper.getAccountId(account_id_string);
	        upIntent.putExtra("account_id", account_id);
	        if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
	            // This activity is NOT part of this app's task, so create a new task
	            // when navigating up, with a synthesized back stack.
	            TaskStackBuilder.create(this)
	                    // Add all of this activity's parents to the back stack
	                    .addNextIntentWithParentStack(upIntent)
	                    // Navigate up to the closest parent
	                    .startActivities();
	        } else {
	            // This activity is part of this app's task, so simply
	            // navigate up to the logical parent activity.
	            NavUtils.navigateUpTo(this, upIntent);
	        }
	        return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
