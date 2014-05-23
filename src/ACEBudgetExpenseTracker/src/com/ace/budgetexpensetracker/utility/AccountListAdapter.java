package com.ace.budgetexpensetracker.utility;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Account;
import com.ace.budgetexpensetracker.views.AccountLinearLayout;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class AccountListAdapter extends CursorAdapter{
    // CursorAdapter will handle all the moveToFirst(), getCount() logic for you :)
	OnClickListener onclick;
	OnLongClickListener onLongClick;
	boolean mDrawEditButton;

    public AccountListAdapter(Context context, Cursor c, OnClickListener onclick, OnLongClickListener onLongClick) {
        super(context, c, CursorAdapter.NO_SELECTION);
        this.onclick = onclick;
        this.onLongClick = onLongClick;
        mDrawEditButton = false;
    }

    public void bindView(View view, Context context, Cursor cursor) {
    	AccountLinearLayout layout = (AccountLinearLayout) view;
    	layout.setAccountID(cursor.getLong((cursor.getColumnIndexOrThrow(Account._ID))));
    	layout.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE)));
    	layout.setAccountBalance(cursor.getFloat(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_BALANCE)));
    	layout.setOnClickListener(onclick);
    	layout.setOnLongClickListener(onLongClick);
    	layout.setDrawEditIcon(mDrawEditButton);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate your view here.
    	AccountLinearLayout view = new AccountLinearLayout(context);
        return view;
    }
    
    public void setDrawEditButton(boolean drawEditButton){
    	mDrawEditButton = drawEditButton;
    }

	
}
