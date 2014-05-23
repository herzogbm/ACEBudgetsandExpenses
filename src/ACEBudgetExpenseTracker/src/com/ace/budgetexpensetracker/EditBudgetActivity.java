package com.ace.budgetexpensetracker;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class EditBudgetActivity extends Activity {
	ACEBudgetExpenseTrackerDBHelper mDbHelper;
	boolean isUpdateMode;
	long id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Draw View
			setContentView(R.layout.activity_edit_budget);
			
		// Set up the action bar.
			final ActionBar actionBar = getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);
			
		// Create database helper
			mDbHelper = new ACEBudgetExpenseTrackerDBHelper(getBaseContext());
			
		// Determine if called from intent with parameters
			Intent intent = getIntent();
			id = intent.getLongExtra("id", -1);
			if(id != -1)
			{
				isUpdateMode = true;
			}
			else
				isUpdateMode = false;
			
	}
	
	public void syncRecord(){
		// Get element values from form
			
			if(isUpdateMode)
			{
				//long id = Long.valueOf(((EditText)(findViewById(R.id.input_account_ID))).getText().toString());
				mDbHelper.updateBudget(id, ((EditText)(findViewById(R.id.input_budget_title))).getText().toString(), Double.valueOf(((EditText)(findViewById(R.id.input_budget_amount))).getText().toString()));
			}
			else
			{
				// Insert new account into database
				id = mDbHelper.insertBudget(((EditText)(findViewById(R.id.input_budget_title))).getText().toString(), Double.valueOf(((EditText)(findViewById(R.id.input_budget_amount))).getText().toString()));
				
				// Set the form to the new RowId
				//((TextView)findViewById(R.id.input_account_ID)).setText(newRowId + "");
				//isUpdateMode = true;
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_menu, menu);
		return true;
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
