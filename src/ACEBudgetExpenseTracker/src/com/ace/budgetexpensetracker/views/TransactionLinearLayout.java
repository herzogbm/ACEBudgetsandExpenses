/**
 * 
 */
package com.ace.budgetexpensetracker.views;

import com.ace.budgetexpensetracker.EditTransactionActivity;
import com.ace.budgetexpensetracker.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Brandon
 *
 */
public class TransactionLinearLayout extends LinearLayout {
	long mId;
	String mCompany;
	String mDate;
	boolean mIsDebit;
	double mAmount;
	double mBalance;
	
	boolean mBalanceSet;
	
	TextView tv_company, tv_date, tv_debit_credit, tv_amount, tv_balance_amount;

	/**
	 * @param context
	 */
	public TransactionLinearLayout(Context context) {
		super(context);
		mId =  0;
		mCompany = "";
		mDate = "";
		mIsDebit = true;
		mAmount = 0;
		mBalance = 0;
		CreateViews(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TransactionLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TransactionLinearLayout,
				0,0);
		try{
			mId = a.getInteger(R.styleable.TransactionLinearLayout_transaction_uid, 0);
			mCompany = a.getString(R.styleable.TransactionLinearLayout_company_name);
			mDate = a.getString(R.styleable.TransactionLinearLayout_date);
			mIsDebit = a.getBoolean(R.styleable.TransactionLinearLayout_isDebit, true);
			mAmount = a.getFloat(R.styleable.TransactionLinearLayout_amount, 0);
			mBalance = a.getFloat(R.styleable.TransactionLinearLayout_balance, 0);
		} finally {
		    a.recycle();
		}
		
		CreateViews(context);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TransactionLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TransactionLinearLayout,
				0,0);
		try{
			mId = a.getInteger(R.styleable.TransactionLinearLayout_transaction_uid, 0);
			mCompany = a.getString(R.styleable.TransactionLinearLayout_company_name);
			mDate = a.getString(R.styleable.TransactionLinearLayout_date);
			mIsDebit = a.getBoolean(R.styleable.TransactionLinearLayout_isDebit, true);
			mAmount = a.getFloat(R.styleable.TransactionLinearLayout_amount, 0);
			mBalance = a.getFloat(R.styleable.TransactionLinearLayout_balance, 0);
		} finally {
		    a.recycle();
		}
		
		CreateViews(context);
	}
	
	public void CreateViews(Context context)
	{
		mBalanceSet = false;
		this.setOrientation(LinearLayout.VERTICAL);
		// First row
		LinearLayout firstrow = new LinearLayout(context);
		firstrow.setOrientation(LinearLayout.HORIZONTAL);
    	
    	tv_company = new TextView(context);
    	tv_company.setText(mCompany);
    	tv_company.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        firstrow.addView(tv_company);

        // Second row
 		LinearLayout secondrow = new LinearLayout(context);
 		secondrow.setOrientation(LinearLayout.HORIZONTAL);
     	
     	TextView tv_date_label = new TextView(context);
     	tv_date_label.setText("Date");
     	tv_date_label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        secondrow.addView(tv_date_label);
         
        tv_date = new TextView(context);
        tv_date.setText(mDate);
        tv_date.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        secondrow.addView(tv_date);
        
        // Third row
  		LinearLayout thirdrow = new LinearLayout(context);
  		thirdrow.setOrientation(LinearLayout.HORIZONTAL);
      	
      	tv_debit_credit = new TextView(context);
      	if(mIsDebit)
      		tv_debit_credit.setText("Debit");
      	else
      		tv_debit_credit.setText("Credit");
      	tv_debit_credit.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
      	thirdrow.addView(tv_debit_credit);
          
        tv_amount = new TextView(context);
        tv_amount.setText("$" + String.format("%.2f", mAmount));
        tv_amount.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        thirdrow.addView(tv_amount);
        
        // Fourth row
  		LinearLayout fourthrow = new LinearLayout(context);
  		fourthrow.setOrientation(LinearLayout.HORIZONTAL);
      	
      	TextView tv_balance_label = new TextView(context);
      	tv_balance_label.setText("Balance");
      	tv_balance_label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
      	fourthrow.addView(tv_balance_label);
          
        tv_balance_amount = new TextView(context);
        tv_balance_amount.setText("$" + String.format("%.2f", mBalance));
        tv_balance_amount.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        fourthrow.addView(tv_balance_amount);
        
        //LinearLayout horizontalLine = new LinearLayout(context);
        //horizontalLine.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_dark));
        //TextView temp = new TextView(context);
        //temp.setHeight(2);
        //horizontalLine.addView(temp);
        
        this.removeAllViews();
        this.addView(firstrow);
        this.addView(secondrow);
        this.addView(thirdrow);
        this.addView(fourthrow);
        //this.addView(horizontalLine);
        this.addView(new LinearLayout(context));
	}

	public void setTransactionID(long id)
	{
		mId = id;
		//tv_uid.setText("(" + mId + ")");
	}
	
	public long getID()
	{
		return mId;
	}
	
	public void setCompanyName(String name)
	{
		mCompany = name;
		tv_company.setText(name);
	}
	
	public String getCompanyName()
	{
		return mCompany;
	}
	
	public void setDate(int month, int day, int year)
	{
		mDate = String.format("%02d", month) + "/" + String.format("%02d", day) + "/" + String.format("%04d", year);
		tv_date.setText(mDate);
	}
	
	public void setDate(String date)
	{
		mDate = date;
		tv_date.setText(mDate);
	}
	
	public String getDate()
	{
		return mDate;
	}
	
	public void setIsDebit(boolean isDebit)
	{
		mIsDebit = isDebit;
		tv_debit_credit.setText(mIsDebit ? "Debit" : "Credit");
	}
	
	public boolean isDebit()
	{
		return mIsDebit;
	}

	public void setTransactionAmount(double amount)
	{
		mAmount = amount;
		tv_amount.setText( "$" + String.format("%.2f", mAmount) );
	}
	
	public double getTransactionAmount()
	{
		return mAmount;
	}
	
	public void setTransactionBalance(double balance)
	{
		mBalanceSet = true;
		mBalance = balance;
		tv_balance_amount.setText( "$" + String.format("%.2f", mBalance) );
	}
	
	public double getTransactionBalance()
	{
		return mBalance;
	}
	
	public boolean getTransactionBalanceSet()
	{
		return mBalanceSet;
	}
	
	public void openTransaction(Activity v) 
	{
		if(mId >= 0)
		{
			Intent intent = new Intent(this.getContext(), EditTransactionActivity.class);
			intent.putExtra("id", mId);
			v.startActivity(intent);
		}
	}
}
