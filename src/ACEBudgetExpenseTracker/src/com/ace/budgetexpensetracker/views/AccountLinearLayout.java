/**
 * 
 */
package com.ace.budgetexpensetracker.views;

import com.ace.budgetexpensetracker.EditAccountActivity;
import com.ace.budgetexpensetracker.R;
import com.ace.budgetexpensetracker.TransactionListActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Brandon
 *
 */
public class AccountLinearLayout extends LinearLayout{
	// Store all db items in this class for use
	long mId;
	String mTitle, mDescription, mAccounttype, mProvidor;
	float mBalance, mInterestRate;
	boolean drawEditIcon;
	ImageView imageview;
	LinearLayout secondrow;
	
	TextView tv_title, tv_balance_amount;

	/**
	 * @param context
	 */
	public AccountLinearLayout(Context context) {
		super(context);
		mId =  0;
		mTitle = "";
		mBalance = 0;
		drawEditIcon = false;
		CreateViews(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AccountLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TransactionLinearLayout,
				0,0);
		try{
			mId = a.getInteger(R.styleable.AccountLinearLayout_account_uid, 0);
			mTitle = a.getString(R.styleable.AccountLinearLayout_title);
			mBalance = a.getFloat(R.styleable.AccountLinearLayout_account_balance, 0);
			drawEditIcon = false;
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
	public AccountLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TransactionLinearLayout,
				0,0);
		try{
			mId = a.getInteger(R.styleable.AccountLinearLayout_account_uid, 0);
			mTitle = a.getString(R.styleable.AccountLinearLayout_title);
			mBalance = a.getFloat(R.styleable.AccountLinearLayout_account_balance, 0);
			drawEditIcon = false;
		} finally {
		    a.recycle();
		}
		
		CreateViews(context);
	}
	
	public void CreateViews(Context context)
	{
		this.setOrientation(LinearLayout.VERTICAL);
		// First row
		LinearLayout firstrow = new LinearLayout(context);
		firstrow.setOrientation(LinearLayout.HORIZONTAL);
    	
    	tv_title = new TextView(context);
    	tv_title.setText(mTitle);
    	tv_title.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        firstrow.addView(tv_title);
        
        //tv_uid = new TextView(context);
       // tv_uid.setText("(" + mId + ")");
       // firstrow.addView(tv_uid);
        
        // Second row
  		secondrow = new LinearLayout(context);
  		secondrow.setOrientation(LinearLayout.HORIZONTAL);
      	//if(drawEditIcon)
      	{
      		imageview = new ImageView(context);
      		imageview.setImageResource(R.drawable.edit_minus);
      		//secondrow.addView(imageview);
      	}
  		TextView tv_empty_label = new TextView(context);
  		tv_empty_label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
      	secondrow.addView(tv_empty_label);
      	
  		TextView tv_balance_label = new TextView(context);
      	tv_balance_label.setText("Balance");
      	tv_balance_label.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
      	secondrow.addView(tv_balance_label);
          
        tv_balance_amount = new TextView(context);
        tv_balance_amount.setText("$" + String.format("%.2f", mBalance));
        tv_balance_amount.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        secondrow.addView(tv_balance_amount);
        
        this.removeAllViews();
        this.addView(firstrow);
        this.addView(secondrow);
	}

	public void setAccountID(long id)
	{
		mId = id;
		//tv_uid.setText("(" + mId + ")");
	}
	
	public long getID()
	{
		return mId;
	}
	
	public void setTitle(String title)
	{
		mTitle = title;
		tv_title.setText(title);
	}
	
	public String getTitle()
	{
		return mTitle;
	}
	
	public void setAccountBalance(float balance)
	{
		mBalance = balance;
		tv_balance_amount.setText( "$" + String.format("%.2f", mBalance) );
	}
	
	public float getAccountBalance()
	{
		return mBalance;
	}

	public void openAccountTransactions(Activity v) 
	{
		if(mId >= 0)
		{
			Intent intent = new Intent(this.getContext(), TransactionListActivity.class);
			intent.putExtra("account_id", mId);
			v.startActivity(intent);
		}
	}
	
	public void openAccount(Activity v) 
	{
		if(mId >= 0)
		{
			Intent intent = new Intent(this.getContext(), EditAccountActivity.class);
			intent.putExtra("id", mId);
			//intent.putExtra("account_type", Account.TYPE_LOAN);
			v.startActivity(intent);
		}
	}
	
	public void setAccountDescription(String desc)
	{
		mDescription = desc;
	}
	
	public String getAccountDescription()
	{
		return mDescription;
	}
	
	public void setAccountType(String type)
	{
		mAccounttype = type;
	}
	
	public String getAccountType()
	{
		return mAccounttype;
	}
	
	public void setAccountProvidor(String name)
	{
		mProvidor = name;
	}
	
	public String getAccountProvidor()
	{
		return mProvidor;
	}
	
	public void setAccountInterestRate(float rate)
	{
		mInterestRate = rate;
	}
	
	public float getAccountInterestRate()
	{
		return mInterestRate;
	}
	
	public void setDrawEditIcon(boolean drawEditIcon){
		if(this.drawEditIcon != drawEditIcon)
		{
			this.drawEditIcon = drawEditIcon;
			if(drawEditIcon)
				secondrow.addView(imageview, 0);
			else
				secondrow.removeView(imageview);
		}
	}
	
	public boolean getDrawEditIcon(){
		return drawEditIcon;
	}
}
