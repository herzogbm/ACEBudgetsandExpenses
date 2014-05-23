package com.ace.budgetexpensetracker;

import java.util.Calendar;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper;
import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Transaction;
import com.ace.budgetexpensetracker.utility.TransactionListAdapter;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BudgetTransactionList extends Activity implements
		ActionBar.OnNavigationListener, OnClickListener {
	ListView mListview;
	TransactionListAdapter mAdapter;
	ACEBudgetExpenseTrackerDBHelper mDbHelper;
	Calendar mStartDate;
	long budget_id;
	double mBudgetAmount;
	double mFilterBalance;
	enum filter_mode {e_Week, e_BiWeek, e_Month, e_Year};
	filter_mode mSelectedFilter;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget_transaction_list);

		// Set up the action bar to show title and home button.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		//actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		// TODO action bar buttons for previous next
		// Set up the dropdown list navigation in the action bar.
		actionBar.setListNavigationCallbacks(
		// Specify a SpinnerAdapter to populate the dropdown list.
				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
						android.R.layout.simple_list_item_1,
						android.R.id.text1, getResources().getStringArray(R.array.budget_filter_periods)), this);
		
		//actionBar.setCustomView(R.menu.list_menu);
		
		// Create database helper
			mDbHelper = new ACEBudgetExpenseTrackerDBHelper(getBaseContext());
			
		mListview = (ListView)findViewById(R.id.listview_budget_transactions);
		
		// Get extras
			// Determine if called from intent with parameters
				Intent intent = getIntent();
				budget_id = intent.getLongExtra("budget_id", -1);
				
		mBudgetAmount = mDbHelper.getBudgetAmount(budget_id);
		mFilterBalance = mBudgetAmount;
				
		mStartDate = Calendar.getInstance();
				
		// Get data
//			Cursor cursor;
//			if(budget_id != -1)
//			{
//				cursor = mDbHelper.getTransactionsByBudget(budget_id,String.format("%04d", mStartYear) + "-" + String.format("%02d", mStartMonth) + "-01", String.format("%04d", mStartYear) + "-" + String.format("%02d", mStartMonth) + "-31");
//				for(int i = 0; i < mAccountNameList.length; i++)
//				{
//					if(mAccountNameList[i].equals(mDbHelper.getAccountName(account_id)))
//						actionBar.setSelectedNavigationItem(i);
//				}
//			}
//			// TODO else if(budget_id != -1)
			//{
			//	cursor = mDbHelper.getTable(Transaction.TABLE_NAME);
				//TODO cursor = mDbHelper.getTransactionsByBudget(budget_id);
			//}
//			else
//			{
//				cursor = mDbHelper.getTable(Transaction.TABLE_NAME);
//			}
			
			// Create list adapter
			mAdapter = new TransactionListAdapter(getBaseContext(), null, this);
			
			// Assign adapter to listview
			mListview.setAdapter(mAdapter);
	}
	
	void updateContent()
	{
		Cursor cursor;
		String startDate, endDate;
		if(mSelectedFilter == filter_mode.e_Year)
		{
			startDate = String.format("%04d", mStartDate.get(Calendar.YEAR)) + "-01-01";
			endDate = String.format("%04d", mStartDate.get(Calendar.YEAR)+1) + "-01-01";
			mFilterBalance = 12*mBudgetAmount;
		}
		else if(mSelectedFilter == filter_mode.e_Week)
		{
			startDate = String.format("%04d", mStartDate.get(Calendar.YEAR)) + "-" + String.format("%02d", mStartDate.get(Calendar.MONTH)+1) + "-" + String.format("%02d", mStartDate.get(Calendar.DAY_OF_MONTH));
			mStartDate.add(Calendar.DAY_OF_MONTH, 6);
			endDate = String.format("%04d", mStartDate.get(Calendar.YEAR)) + "-" + String.format("%02d", mStartDate.get(Calendar.MONTH)+1) + "-" + String.format("%02d", mStartDate.get(Calendar.DAY_OF_MONTH));
			mStartDate.add(Calendar.DAY_OF_MONTH, -6);
			mFilterBalance = (12.0*mBudgetAmount)/52.0;
		}
		else
		{
			startDate = String.format("%04d", mStartDate.get(Calendar.YEAR)) + "-" + String.format("%02d", mStartDate.get(Calendar.MONTH)+1) + "-01";
			endDate = String.format("%04d", mStartDate.get(Calendar.YEAR)) + "-" + String.format("%02d", mStartDate.get(Calendar.MONTH)+1) + "-31";
			mFilterBalance = mBudgetAmount;
		}
		cursor = mDbHelper.getTransactionsByBudget(budget_id,startDate, endDate);
		cursor.moveToFirst();
		while(!cursor.isAfterLast())
		{
			double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_AMOUNT));
			boolean isDebit = cursor.getInt(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_IS_DEBIT))==1;
			if(isDebit)
				mFilterBalance -= amount;
			else
				mFilterBalance += amount;
			cursor.moveToNext();
		}
		// Update balance
		mAdapter.changeCursor(cursor);
		((TextView)findViewById(R.id.label_budget_balance)).setText(String.format("%.2f", mFilterBalance));
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
		getMenuInflater().inflate(R.menu.budget_transaction_list, menu);
		return true;
	}

	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// When the given dropdown item is selected, show its contents in the
		// container view.
		switch(position)
		{
		case 0:
			mSelectedFilter = filter_mode.e_Month;
			//mStartDate.set(Calendar.DAY_OF_MONTH, 1);
			break;
		case 1:
			mSelectedFilter = filter_mode.e_Year;
			break;
		case 2:
			//mSelectedFilter = filter_mode.e_Week;
			//break;
		case 3:
			mSelectedFilter = filter_mode.e_Week;
			switch(mStartDate.get(Calendar.DAY_OF_WEEK))
			{
			case Calendar.SUNDAY:
				break;
			case Calendar.SATURDAY:
				mStartDate.add(Calendar.DAY_OF_MONTH, -6);
				break;
			case Calendar.FRIDAY:
				mStartDate.add(Calendar.DAY_OF_MONTH, -5);
				break;
			case Calendar.THURSDAY:
				mStartDate.add(Calendar.DAY_OF_MONTH, -4);
				break;
			case Calendar.WEDNESDAY:
				mStartDate.add(Calendar.DAY_OF_MONTH, -3);
				break;
			case Calendar.TUESDAY:
				mStartDate.add(Calendar.DAY_OF_MONTH, -2);
				break;
			case Calendar.MONDAY:
				mStartDate.add(Calendar.DAY_OF_MONTH, -1);
				break;
			}
			break;
		default:
			mSelectedFilter = filter_mode.e_Month;
			break;
		}
		updateContent();
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_prev:
			switch(mSelectedFilter)
			{
			case e_Week:
				mStartDate.add(Calendar.DAY_OF_MONTH, -7);
				break;
			case e_Year:
				mStartDate.add(Calendar.YEAR, -1);
				break;
			case e_Month:
				mStartDate.add(Calendar.MONTH, -1);
				break;
			default:
				break;
			}
			updateContent();
			return true;
		case R.id.action_next:
			switch(mSelectedFilter)
			{
			case e_Week:
				mStartDate.add(Calendar.DAY_OF_MONTH, 7);
				break;
			case e_Year:
				mStartDate.add(Calendar.YEAR, 1);
				break;
			case e_Month:
				mStartDate.add(Calendar.MONTH, 1);
				break;
			default:
				break;
			}
			updateContent();
			return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
