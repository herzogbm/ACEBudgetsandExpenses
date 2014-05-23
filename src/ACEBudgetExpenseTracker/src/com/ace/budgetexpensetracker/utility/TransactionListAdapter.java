package com.ace.budgetexpensetracker.utility;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Transaction;
import com.ace.budgetexpensetracker.views.TransactionLinearLayout;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class TransactionListAdapter extends CursorAdapter{
    // CursorAdapter will handle all the moveToFirst(), getCount() logic for you :)
	OnClickListener onclick;

    public TransactionListAdapter(Context context, Cursor c, OnClickListener onclick) {
        super(context, c, CursorAdapter.NO_SELECTION);
        this.onclick = onclick;
    }

    public void bindView(View view, Context context, Cursor cursor) {
    	boolean isDebit = cursor.getInt(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_IS_DEBIT))>0;
    	float amount = cursor.getFloat(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_AMOUNT));
    	TransactionLinearLayout layout = (TransactionLinearLayout) view;
    	layout.setTransactionID(cursor.getLong((cursor.getColumnIndexOrThrow(Transaction._ID))));
    	layout.setCompanyName(cursor.getString(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_DESCRIPTION)));
    	layout.setDate(cursor.getString(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_DATE)));
    	//layout.setDate(6, 15, 2013);
    	layout.setIsDebit(isDebit);
    	layout.setTransactionAmount(amount);
    	layout.setTransactionBalance(cursor.getDouble(cursor.getColumnIndexOrThrow(Transaction.COLUMN_NAME_BALANCE)));
    	layout.setOnClickListener(onclick);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate your view here.
    	TransactionLinearLayout view = new TransactionLinearLayout(context);
        return view;
    }

	
}
