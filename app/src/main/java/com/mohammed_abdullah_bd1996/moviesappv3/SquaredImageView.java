package com.mohammed_abdullah_bd1996.moviesappv3;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
//I can   specify custom transformations for more advanced effects.
/** An image view which always remains square with respect to its width. */
final public class SquaredImageView extends ImageView {
    public SquaredImageView(Context context) {
        super(context);
    }
    public SquaredImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}//end of the class