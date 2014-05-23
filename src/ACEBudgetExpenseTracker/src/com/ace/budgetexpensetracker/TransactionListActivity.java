package com.ace.budgetexpensetracker;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper;
import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Transaction;
import com.ace.budgetexpensetracker.utility.TransactionListAdapter;
import com.ace.budgetexpensetracker.views.TransactionLinearLayout;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TransactionListActivity extends Activity implements OnClickListener, ActionBar.OnNavigationListener{
	
	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	
	ListView mListview;
	TransactionListAdapter mAdapter;
	ACEBudgetExpenseTrackerDBHelper mDbHelper;
	String[] mAccountNameList;
	long account_id, budget_id;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Draw layout
			setContentView(R.layout.transaction_list);
		
		// Create database helper
			mDbHelper = new ACEBudgetExpenseTrackerDBHelper(getBaseContext());
			
		// Get account names
			mAccountNameList = mDbHelper.getAccountNames();
		
		// Set up the action bar to show a dropdown list.
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayShowTitleEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
			actionBar.setDisplayHomeAsUpEnabled(true);

		// Set up the dropdown list navigation in the action bar.
			actionBar.setListNavigationCallbacks(
			// Specify a SpinnerAdapter to populate the dropdown list.
					new ArrayAdapter<String>(getActionBarThemedContextCompat(),
							android.R.layout.simple_list_item_1,
							android.R.id.text1, mAccountNameList), this);
		// Set up action bar icons
			//actionBar.
		
		mListview = (ListView)findViewById(R.id.listView_transactions);
		
		
		// Get extras
		// Determine if called from intent with parameters
			Intent intent = getIntent();
			account_id = intent.getLongExtra("account_id", -1);
			// TODO budget_id = intent.getLongExtra("budget_id", -1);
		
		// Get data
		Cursor cursor;
		if(account_id != -1)
		{
			cursor = mDbHelper.getTransactionsByAccount(account_id);
			for(int i = 0; i < mAccountNameList.length; i++)
			{
				if(mAccountNameList[i].equals(mDbHelper.getAccountName(account_id)))
					actionBar.setSelectedNavigationItem(i);
			}
		}
		// TODO else if(budget_id != -1)
		//{
		//	cursor = mDbHelper.getTable(Transaction.TABLE_NAME);
			//TODO cursor = mDbHelper.getTransactionsByBudget(budget_id);
		//}
		else
		{
			cursor = mDbHelper.getTable(Transaction.TABLE_NAME);
		}
		
		// Create list adapter
		mAdapter = new TransactionListAdapter(getBaseContext(), cursor, this);
		
		// Assign adapter to listview
		mListview.setAdapter(mAdapter);
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
		account_id = mDbHelper.getAccountId(mAccountNameList[position]);
		mAdapter.changeCursor(mDbHelper.getTransactionsByAccount(account_id));
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

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
//	public static class DummySectionFragment extends Fragment {
//		/**
//		 * The fragment argument representing the section number for this
//		 * fragment.
//		 */
//		public static final String ARG_SECTION_NUMBER = "section_number";
//
//		public DummySectionFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
//					container, false);
//			TextView dummyTextView = (TextView) rootView
//					.findViewById(R.id.section_label);
//			dummyTextView.setText(Integer.toString(getArguments().getInt(
//					ARG_SECTION_NUMBER)));
//			return rootView;
//		}
//	}
	
	@Override
	protected void onResume() {
		super.onResume();
		// Get data
		Cursor cursor;
		if(account_id != -1)
		{
			cursor = mDbHelper.getTransactionsByAccount(account_id);
		}
		else if(budget_id != -1)
		{
			cursor = mDbHelper.getTable(Transaction.TABLE_NAME);
			//TODO cursor = mDbHelper.getTransactionsByBudget(budget_id);
		}
		else
		{
			cursor = mDbHelper.getTable(Transaction.TABLE_NAME);
		}
		mAdapter.changeCursor(cursor);
	}
	
	@Override
	public void onClick(View v) {
		((TransactionLinearLayout)v).openTransaction(this);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Toggle edit click mode
		case R.id.action_edit:
			//toggleEditMode();
			return true;
		// Start add account activity
		case R.id.action_add:
			// Add transaction to account
			Intent intent = new Intent(getBaseContext(), EditTransactionActivity.class);
			intent.putExtra("account_id", account_id);
			startActivity(intent);
			return true;
	    }
	    return super.onOptionsItemSelected(item);
	}
}
