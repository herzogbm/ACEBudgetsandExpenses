package com.ace.budgetexpensetracker;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper;
import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Account;
import com.ace.budgetexpensetracker.utility.AccountListAdapter;
import com.ace.budgetexpensetracker.views.AccountLinearLayout;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

//import com.google.ads.*;

public class AccountListActivity extends Activity implements OnClickListener, OnLongClickListener, ActionBar.OnNavigationListener{
	//private AdVi
	ListView mListview;
	ACEBudgetExpenseTrackerDBHelper mDbHelper;
	// This is the Adapter being used to display the list's data
    AccountListAdapter mAdapter;
    int mSelectedFilter;
    boolean editButtonClicked;
    
    /**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Draw view
			setContentView(R.layout.account_list);
		
		// Assign action listener
			mSelectedFilter = 0;
		
		// Create database helper
			mDbHelper = new ACEBudgetExpenseTrackerDBHelper(getBaseContext());
			
		// Set up the action bar to show a dropdown list.
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setDisplayHomeAsUpEnabled(true);
			//actionBar.set

		// Set up the dropdown list navigation in the action bar.
			actionBar.setListNavigationCallbacks(
			// Specify a SpinnerAdapter to populate the dropdown list.
					new ArrayAdapter<String>(getActionBarThemedContextCompat(),
							android.R.layout.simple_list_item_1,
							android.R.id.text1, getResources().getStringArray(R.array.items_account_type)), this);
		
        // For the cursor adapter, specify which columns go into which views
        //String[] fromColumns = {Account.COLUMN_NAME_TITLE};//{Account._ID, Account.COLUMN_NAME_TITLE, Account.COLUMN_NAME_BALANCE};
        //int[] toViews = {R.id.listView_accounts}; 
        Cursor cursor = mDbHelper.getTable(Account.TABLE_NAME);
        
        // Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new AccountListAdapter(getBaseContext(), cursor, this, this);
        
        // Get text view
        ListView listview = (ListView)findViewById(R.id.listView_accounts);
        listview.setAdapter(mAdapter);
        
        editButtonClicked = false;
        
//        mDbHelper.insertAccount("Chase Checking", "My main checking account!", "Checking", 1425.15, 0.5, "Chase");
//        mDbHelper.insertAccount("Chase Savings", "My main savings account!", "Savings", 825.15, 1.5, "Chase");
//        mDbHelper.insertAccount("Amazon Credit Card", "Amazon rewards credit card.", "CreditCard", 200, 18.5, "Chase");
//        mDbHelper.insertAccount("Credit Union Savings", "My extra savings account!", "Savings", 425.15, 3.5, "First Community FCU");
//        mDbHelper.insertAccount("UPromise Credit Card", "Credit card", "CreditCard", 25.15, 1.5, "Chase");
//        mDbHelper.insertAccount("Chase Checking", "My main checking account!", "Checking", 425.15, 1.5, "Chase");
	}
	
	/**
	 * Backward-compatible version of {@link ActionBar#getThemedContext()} that
	 * simply returns the {@link android.app.Activity} if
	 * <code>getThemedContext</code> is unavailable.
	 */
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private Context getActionBarThemedContextCompat() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return getActionBar().getThemedContext();
		} else {
			return this;
		}
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		// Restore the previously serialized current dropdown position.
		if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
			getActionBar().setSelectedNavigationItem(
					savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// Serialize the current dropdown position.
		outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
				.getSelectedNavigationIndex());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list_menu, menu);
		return true;
	}
	
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		mSelectedFilter = position;
		if(position == 0)
			mAdapter.changeCursor(mDbHelper.getTable(Account.TABLE_NAME));
		else
			mAdapter.changeCursor(mDbHelper.getAccountByType(getResources().getStringArray(R.array.items_account_type)[position]));
		//mAdapter.changeCursor(mDbHelper.getTransactionsByAccount(account_id));
		// When the given dropdown item is selected, show its contents in the
		// container view.
//		Fragment fragment = new DummySectionFragment();
//		Bundle args = new Bundle();
//		args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
//		fragment.setArguments(args);
//		getSupportFragmentManager().beginTransaction()
//				.replace(R.id.container, fragment).commit();
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Cursor cursor = mDbHelper.getTable(Account.TABLE_NAME);
		mAdapter.swapCursor(cursor);
	}

	public void toggleEditMode(){
		editButtonClicked = !editButtonClicked;
		// Turn on/off edit button
		mAdapter.setDrawEditButton(editButtonClicked);
		mAdapter.changeCursor(mDbHelper.getTable(Account.TABLE_NAME));
	}
	
	@Override
	public void onClick(View v) {
		if(editButtonClicked)
			((AccountLinearLayout)v).openAccount(this); // Edit account
		else
			((AccountLinearLayout)v).openAccountTransactions(this); // View transactions
	}

	@Override
	public boolean onLongClick(View v) {
		((AccountLinearLayout)v).openAccount(this);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Toggle edit click mode
		case R.id.action_edit:
			toggleEditMode();
			return true;
		// Start add account activity
		case R.id.action_add:
			Intent intent = new Intent(getBaseContext(), EditAccountActivity.class);
			startActivity(intent);
			return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
