package com.ace.budgetexpensetracker.utility;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Category;
import com.ace.budgetexpensetracker.views.BudgetLinearLayout;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class BudgetListAdapter extends CursorAdapter{
    // CursorAdapter will handle all the moveToFirst(), getCount() logic for you :)
	OnClickListener onclick;
	boolean mDrawEditButton;

    public BudgetListAdapter(Context context, Cursor c, OnClickListener onclick) {
        super(context, c, CursorAdapter.NO_SELECTION);
        this.onclick = onclick;
        mDrawEditButton = false;
    }

    public void bindView(View view, Context context, Cursor cursor) {
    	BudgetLinearLayout layout = (BudgetLinearLayout) view;
    	layout.setBudgetID(cursor.getLong((cursor.getColumnIndexOrThrow(Category._ID))));
    	layout.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_TITLE)));
    	layout.setAmount(cursor.getFloat(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_AMOUNT)));
    	//layout.setBudgetDescription(cursor.getString(cursor.getColumnIndexOrThrow(Category.COLUMN_NAME_DESCRIPTION)));
    	layout.setOnClickListener(onclick);
    	layout.setDrawEditIcon(mDrawEditButton);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate your view here.
    	BudgetLinearLayout view = new BudgetLinearLayout(context);
        return view;
    }
    
    public void setDrawEditButton(boolean drawEditButton){
    	mDrawEditButton = drawEditButton;
    }

	
}
