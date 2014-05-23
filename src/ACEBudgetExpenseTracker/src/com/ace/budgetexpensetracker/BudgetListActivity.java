package com.ace.budgetexpensetracker;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper;
import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Category;
import com.ace.budgetexpensetracker.utility.BudgetListAdapter;
import com.ace.budgetexpensetracker.views.BudgetLinearLayout;

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
import android.widget.ListView;

public class BudgetListActivity extends Activity implements OnClickListener
{
	ListView mListview;
	ACEBudgetExpenseTrackerDBHelper mDbHelper;
	// This is the Adapter being used to display the list's data
    BudgetListAdapter mAdapter;
    //int mSelectedFilter;
    boolean editButtonClicked;

	/**
	 * The serialization (saved instance state) Bundle key representing the
	 * current dropdown position.
	 */
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_budget_list);
		
		// Create database helper
		mDbHelper = new ACEBudgetExpenseTrackerDBHelper(getBaseContext());

		// Set up the action bar to show a dropdown list.
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

		// Set up the dropdown list navigation in the action bar.
//		actionBar.setListNavigationCallbacks(
//		// Specify a SpinnerAdapter to populate the dropdown list.
//				new ArrayAdapter<String>(getActionBarThemedContextCompat(),
//						android.R.layout.simple_list_item_1,
//						android.R.id.text1, new String[] {
//								getString(R.string.title_section1),
//								getString(R.string.title_section2),
//								getString(R.string.title_section3), }), this);
		
		Cursor cursor = mDbHelper.getTable(Category.TABLE_NAME);
		
		// Create an empty adapter we will use to display the loaded data.
        // We pass null for the cursor, then update it in onLoadFinished()
        mAdapter = new BudgetListAdapter(getBaseContext(), cursor, this);
        
        // Get text view
        ListView listview = (ListView)findViewById(R.id.listview_budgets);
        listview.setAdapter(mAdapter);
        
        editButtonClicked = false;
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
	
	public void toggleEditMode(){
		editButtonClicked = !editButtonClicked;
		// Turn on/off edit button
		mAdapter.setDrawEditButton(editButtonClicked);
		mAdapter.changeCursor(mDbHelper.getTable(Category.TABLE_NAME));
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
			Intent intent = new Intent(getBaseContext(), EditBudgetActivity.class);
			startActivity(intent);
			return true;
	    }
	    return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		long mId = ((BudgetLinearLayout)v).getBudgetID();
		if(editButtonClicked)
		{
			if(mId >= 0)
			{
				Intent intent = new Intent(v.getContext(), EditBudgetActivity.class);
				intent.putExtra("id", mId);
				startActivity(intent);
			}
		}
		else
		{
			if(mId >= 0)
			{
				Intent intent = new Intent(v.getContext(), BudgetTransactionList.class);
				intent.putExtra("budget_id", mId);
				startActivity(intent);
			}
		}
	}
}
