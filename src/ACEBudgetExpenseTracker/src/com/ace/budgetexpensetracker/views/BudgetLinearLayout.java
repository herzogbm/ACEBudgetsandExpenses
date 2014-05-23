/**
 * 
 */
package com.ace.budgetexpensetracker.views;

import com.ace.budgetexpensetracker.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Brandon
 *
 */
public class BudgetLinearLayout extends LinearLayout{
	// Store all db items in this class for use
	long mId;
	String mTitle, mDescription;
	float mAmount;
	boolean drawEditIcon;
	ImageView imageview;
	LinearLayout secondrow;
	
	TextView tv_title, tv_description, tv_amount;

	/**
	 * @param context
	 */
	public BudgetLinearLayout(Context context) {
		super(context);
		mId =  0;
		mTitle = "";
		mAmount = 0;
		drawEditIcon = false;
		CreateViews(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public BudgetLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TransactionLinearLayout,
				0,0);
		try{
			mId = a.getInteger(R.styleable.AccountLinearLayout_account_uid, 0);
			mTitle = a.getString(R.styleable.AccountLinearLayout_title);
			mAmount = a.getFloat(R.styleable.AccountLinearLayout_account_balance, 0);
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
	public BudgetLinearLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		TypedArray a = context.getTheme().obtainStyledAttributes(
				attrs,
				R.styleable.TransactionLinearLayout,
				0,0);
		try{
			mId = a.getInteger(R.styleable.AccountLinearLayout_account_uid, 0);
			mTitle = a.getString(R.styleable.AccountLinearLayout_title);
			mAmount = a.getFloat(R.styleable.AccountLinearLayout_account_balance, 0);
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
        
        LinearLayout subrow = new LinearLayout(context);
        TextView textview_label = new TextView(context);
        textview_label.setText("Monthly: $");
        subrow.addView(textview_label);
        tv_amount = new TextView(context);
        tv_amount.setText(String.format("%.2f", mAmount));
        subrow.addView(tv_amount);
        firstrow.addView(subrow);
        
        // Second row
  		secondrow = new LinearLayout(context);
  		secondrow.setOrientation(LinearLayout.HORIZONTAL);
      	
  		TextView tv_description_label = new TextView(context);
  		tv_description_label.setText("Description:");
      	secondrow.addView(tv_description_label);
      	
      	// Third row
 		LinearLayout thirdrow = new LinearLayout(context);
 		thirdrow.setOrientation(LinearLayout.HORIZONTAL);
          
        tv_description = new TextView(context);
        tv_description.setText(mDescription);
        thirdrow.addView(tv_description);
        
        this.removeAllViews();
        this.addView(firstrow);
        this.addView(secondrow);
        this.addView(thirdrow);
        
        imageview = new ImageView(context);
  		imageview.setImageResource(R.drawable.edit_minus);
	}

	public void setBudgetID(long id)
	{
		mId = id;
		//tv_uid.setText("(" + mId + ")");
	}
	
	public long getBudgetID()
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
	
	public void setAmount(float amount)
	{
		mAmount = amount;
		tv_amount.setText( String.format("%.2f", mAmount) );
	}
	
	public float getAmount()
	{
		return mAmount;
	}
	
	public void setBudgetDescription(String desc)
	{
		mDescription = desc;
	}
	
	public String getBudgetDescription()
	{
		return mDescription;
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
