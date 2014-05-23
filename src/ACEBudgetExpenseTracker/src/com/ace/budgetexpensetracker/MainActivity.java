package com.ace.budgetexpensetracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		((Button)findViewById( R.id.button_accounts)).setOnClickListener(this);
		((Button)findViewById( R.id.button_budgets)).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.button_accounts:
			Intent account_intent = new Intent(v.getContext(), AccountListActivity.class);
			startActivity(account_intent);
			break;
		case R.id.button_budgets:
			Intent intent = new Intent(v.getContext(), BudgetListActivity.class);
			startActivity(intent);
			break;
		}
	}

}
