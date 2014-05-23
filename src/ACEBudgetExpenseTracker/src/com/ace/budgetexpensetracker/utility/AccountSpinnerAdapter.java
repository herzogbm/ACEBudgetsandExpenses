package com.ace.budgetexpensetracker.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.ace.budgetexpensetracker.views.IDTextView;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/**
 * A ListAdapter that manages a ListView backed by an array of arbitrary
 * objects.  By default this class expects that the provided resource id references
 * a single TextView.  If you want to use a more complex layout, use the constructors that
 * also takes a field id.  That field id should reference a TextView in the larger layout
 * resource.
 *
 * However the TextView is referenced, it will be filled with the toString() of each object in
 * the array. You can add lists or arrays of custom objects. Override the toString() method
 * of your objects to determine what text will be displayed for the item in the list.
 *
 * To use something other than TextViews for the array display, for instance, ImageViews,
 * or to have some of data besides toString() results fill the views,
 * override {@link #getView(int, View, ViewGroup)} to return the type of view you want.
 */
public class AccountSpinnerAdapter extends ArrayAdapter<IDTextView>{

	 /**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     */
	public AccountSpinnerAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId, 0, new ArrayList<IDTextView>());
	}
	
	/**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
	public AccountSpinnerAdapter(Context context, int resource, int textViewResourceId) {
		super(context, resource, textViewResourceId, new ArrayList<IDTextView>());
	}
	
	/**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public AccountSpinnerAdapter(Context context, int textViewResourceId, IDTextView[] objects) {
    	super(context, textViewResourceId, 0, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects The objects to represent in the ListView.
     */
    public AccountSpinnerAdapter(Context context, int resource, int textViewResourceId, IDTextView[] objects) {
    	super(context, resource, textViewResourceId, Arrays.asList(objects));
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param objects The objects to represent in the ListView.
     */
    public AccountSpinnerAdapter(Context context, int textViewResourceId, List<IDTextView> objects) {
    	super(context, textViewResourceId, 0, objects);
    }

    /**
     * Constructor
     *
     * @param context The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param objects The objects to represent in the ListView.
     */
    public AccountSpinnerAdapter(Context context, int resource, int textViewResourceId, List<IDTextView> objects) {
        super(context, resource, textViewResourceId, objects);
    }
    
    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The string representation of item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(String itemtext) {
    	IDTextView view = new IDTextView(getContext());
    	view.setText(itemtext);
    	return getPosition(view);
    }
    
    @Override
    public long getItemId(int position) {
    	return getItem(position).getID();
    }
}
