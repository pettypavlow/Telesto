package sugar.free.telesto.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

public class TwoColumnLayout extends ViewGroup {

    public TwoColumnLayout(Context context) {
        super(context);
    }

    public TwoColumnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TwoColumnLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMeasureSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSpecPixels = View.MeasureSpec.getSize(widthMeasureSpec);
        int innerMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
        int columnWidth = 0;
        switch (widthMeasureSpecMode) {
            case MeasureSpec.EXACTLY:
                columnWidth = (widthMeasureSpecPixels - getPaddingLeft() - getPaddingRight() - innerMargin) / 2;
                break;
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                for (int i = 0; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child.getVisibility() == GONE) continue;
                    child.measure(View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    if (columnWidth < child.getMeasuredWidth())
                        columnWidth = child.getMeasuredWidth();
                }
                if (widthMeasureSpecMode == MeasureSpec.AT_MOST && columnWidth * 2 + getPaddingLeft() + getPaddingRight() + innerMargin > widthMeasureSpecPixels)
                    columnWidth = (widthMeasureSpecPixels - getPaddingLeft() - getPaddingRight() - innerMargin) / 2;
                break;
        }

        int leftColumnHeight = getPaddingTop() + getPaddingBottom();
        int rightColumnHeight = getPaddingTop() + getPaddingBottom();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            child.measure(View.MeasureSpec.makeMeasureSpec(columnWidth, MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            if (leftColumnHeight <= rightColumnHeight) {
                leftColumnHeight += (child.getMeasuredHeight());
                if (leftColumnHeight > getPaddingTop() + getPaddingBottom()) leftColumnHeight += innerMargin;
            } else {
                rightColumnHeight += (child.getMeasuredHeight());
                if (rightColumnHeight > getPaddingTop() + getPaddingBottom()) rightColumnHeight += innerMargin;
            }
        }
        int height = Math.max(leftColumnHeight, rightColumnHeight);
        int width = columnWidth * 2 + getPaddingLeft() + getPaddingRight() + innerMargin;
        setMeasuredDimension(Math.max(width, getSuggestedMinimumWidth()), Math.max(height, getSuggestedMinimumHeight()));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int innerMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, getResources().getDisplayMetrics()));
        int columnWidth = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight() - innerMargin) / 2;
        int leftStartX = left + getPaddingLeft();
        int leftEndX = leftStartX + columnWidth;
        int rightEndX = right - getPaddingRight();
        int rightStartX = rightEndX - columnWidth;

        int leftColumnY = getPaddingTop();
        int rightColumnY = getPaddingTop();

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE) continue;
            if (leftColumnY <= rightColumnY) {
                child.layout(leftStartX, leftColumnY, leftEndX, leftColumnY += child.getMeasuredHeight());
                leftColumnY += innerMargin;
            } else {
                child.layout(rightStartX, rightColumnY, rightEndX, rightColumnY += child.getMeasuredHeight());
                rightColumnY += innerMargin;
            }
        }
    }
}
