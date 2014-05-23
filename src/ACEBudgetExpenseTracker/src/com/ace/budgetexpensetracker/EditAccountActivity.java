package com.ace.budgetexpensetracker;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper;
import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Account;

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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class EditAccountActivity extends Activity{
	ACEBudgetExpenseTrackerDBHelper mDbHelper;
	boolean isUpdateMode;
	Spinner spinner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Draw view
			setContentView(R.layout.edit_account);
		
		// Set up the action bar to show a dropdown list.
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
		
		// Assign action listener
			isUpdateMode = false;
		
		// Create database helper
			mDbHelper = new ACEBudgetExpenseTrackerDBHelper(getBaseContext());
		
		// Determine if called from intent with parameters
		Intent intent = getIntent();
		long id = intent.getLongExtra("id", -1);
		String account_type = intent.getStringExtra("account_type");
		if(id != -1)
		{
			// Set up view for update data, rather than create data
			// Get id editText and assign to the given id
			((EditText)findViewById(R.id.input_account_ID)).setText(String.valueOf(id));
			
			// Query db for row information
			Cursor cursor = mDbHelper.getAccount(id);
			cursor.moveToFirst();
			// Set element values for form
			((EditText)(findViewById(R.id.input_account_name))).setText(
					cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE))
					);
			((EditText)(findViewById(R.id.input_account_description))).setText(
					cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_DESCRIPTION))
					);
			account_type = cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_ACCOUNT_TYPE));
			((EditText)(findViewById(R.id.input_account_balance))).setText(
					String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_BALANCE)))
					);
			// Disable edit of balance
			((EditText)(findViewById(R.id.input_account_balance))).setEnabled(false);
			((EditText)(findViewById(R.id.input_account_interest_rate))).setText(
					String.valueOf(cursor.getDouble(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_INTEREST_RATE)))
					);
			((EditText)(findViewById(R.id.input_account_providor))).setText(
					cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_provider))
					);
			// Flag edit mode
			isUpdateMode = true;
		}
		
		// Fill spinner with account types
		spinner = (Spinner)findViewById(R.id.input_account_type);
		String[] list = {Account.TYPE_CHECKING, Account.TYPE_SAVING, Account.TYPE_CREDIT_CARD, Account.TYPE_LOAN};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getBaseContext(), android.R.layout.simple_spinner_dropdown_item, list);
		spinner.setAdapter(adapter);
		spinner.setSelection(adapter.getPosition(account_type));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_menu, menu);
		return true;
	}
	
	public void syncRecord(){
		// Get element values from form
			String name = ((EditText)(findViewById(R.id.input_account_name))).getText().toString();
			String description = ((EditText)(findViewById(R.id.input_account_description))).getText().toString();
			String Type = ((Spinner)(findViewById(R.id.input_account_type))).getSelectedItem().toString();
			
			// Get Balance String and check string
			String balance_string = ((EditText)(findViewById(R.id.input_account_balance))).getText().toString();
			if(balance_string.equals("")) balance_string = "0";
			double balance = Double.valueOf(balance_string).doubleValue();
			
			// Get interest rate string and check string
			String interest_string = ((EditText)(findViewById(R.id.input_account_interest_rate))).getText().toString();
			if(interest_string.equals("")) interest_string = "0";
			double interestRate = Double.valueOf(interest_string).doubleValue();
			
			String providor = ((EditText)(findViewById(R.id.input_account_providor))).getText().toString();
			
			if(isUpdateMode)
			{
				long id = Long.valueOf(((EditText)(findViewById(R.id.input_account_ID))).getText().toString());
				mDbHelper.updateAccount(id, name, description, Type, balance, interestRate, providor);
			}
			else
			{
				// Insert new account into database
				long newRowId = mDbHelper.insertAccount(name, description, Type, balance, interestRate, providor);
				
				// Set the form to the new RowId
				((TextView)findViewById(R.id.input_account_ID)).setText(newRowId + "");
				isUpdateMode = true;
			}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Sync data before returning up a level
		case R.id.action_commit:
			syncRecord();
		// Cancel edit and return to up a level
		case R.id.action_discard:
	    // Respond to the action bar's Up/Home button
	    case android.R.id.home:
	        Intent upIntent = NavUtils.getParentActivityIntent(this);
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
