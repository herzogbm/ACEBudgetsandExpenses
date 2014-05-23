/**
 * 
 */
package com.ace.budgetexpensetracker.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @author Brandon
 *
 */
public class IDTextView extends TextView {
	long mID;

	/**
	 * @param context
	 */
	public IDTextView(Context context) {
		super(context);
		mID = -1;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public IDTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mID = -1;
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public IDTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mID = -1;
	}
	
	public long getID()
	{
		return mID;
	}
	
	public void setID(long id)
	{
		mID = id;
	}
	
	@Override
	public String toString() {
		return (String) getText();
		//return super.toString();
	}
}
