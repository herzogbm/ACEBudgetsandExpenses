package com.ace.budgetexpensetracker.utility;

import com.ace.budgetexpensetracker.database.ACEBudgetExpenseTrackerDBHelper.Account;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomAdapter extends CursorAdapter{
    // CursorAdapter will handle all the moveToFirst(), getCount() logic for you :)
	OnClickListener onclick;

    public CustomAdapter(Context context, Cursor c, OnClickListener onclick) {
        super(context, c, CursorAdapter.NO_SELECTION);
        this.onclick = onclick;
    }

    public void bindView(View view, Context context, Cursor cursor) {
    	LinearLayout layout = (LinearLayout) view;
    	layout.removeAllViews();
    	layout.setOrientation(LinearLayout.VERTICAL);
    	
    	LinearLayout toplayout = new LinearLayout(context);
    	toplayout.setOrientation(LinearLayout.HORIZONTAL);
    	
    	TextView textView1 = new TextView(context);
        textView1.setText(cursor.getString(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_TITLE)));
        textView1.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        textView1.setTextColor(Color.WHITE);
        toplayout.addView(textView1);
        
        TextView textView2 = new TextView(context);
        textView2.setText("(" + cursor.getString(cursor.getColumnIndexOrThrow(Account._ID) )+ ")");
        textView2.setTextColor(Color.WHITE);
        //textView2.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        toplayout.addView(textView2);
        
        LinearLayout middle = new LinearLayout(context);
        middle.setOrientation(LinearLayout.HORIZONTAL);
        TextView textView3 = new TextView(context);
        textView3.setText("");
        textView3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        middle.addView(textView3);
        textView3 = new TextView(context);
        textView3.setText("Balance");
        textView3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        middle.addView(textView3);
        textView3 = new TextView(context);
        textView3.setText("$" + cursor.getDouble(cursor.getColumnIndexOrThrow(Account.COLUMN_NAME_BALANCE)));
        textView3.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        middle.addView(textView3);
        //textView3.setTextColor(Color.WHITE);
        
        
        //toplayout.setBackgroundColor(Color.RED);
        layout.addView(toplayout);
        layout.addView(middle);
        layout.setOnClickListener(onclick);

    	//String text = "";
//    	for(int i = 0; i < cursor.getColumnCount(); i++)
//    	{
//    		textView = new TextView(context);
//            textView.setText(cursor.getString(i));
//            textView.setPadding(10, 2, 10, 2);
//            layout.addView(textView);
//    	}
        // Get all the values
        // Use it however you need to
       // TextView textView = (TextView) view;
       // textView.setText(text);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
 /*   	int[][] states = new int[][]{
    			new int [] {android.R.attr.state_pressed},
    			new int [] {android.R.attr.state_focused},
    			new int [] {},
    	};
    	int[] colors = new int [] {
    		      Color.BLACK,//Color.rgb (255, 128, 192),
    		      Color.rgb (100, 200, 192),
    		      Color.RED
    		   };*/
        // Inflate your view here.
    	LinearLayout view = new LinearLayout(context);
        //TextView textview = new TextView(context);
        //view.setTextSize(20);
//        ColorStateList list = new ColorStateList(states, colors);
        //view.setTextColor(context.getResources().getColor(R.color.red));
//        view.setTextColor(list);
        return view;
    }

	
}
