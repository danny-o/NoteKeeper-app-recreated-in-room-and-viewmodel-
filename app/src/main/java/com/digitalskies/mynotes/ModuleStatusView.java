package com.digitalskies.mynotes;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;

import java.util.List;


/**
 * TODO: document your custom view class.
 */
public class ModuleStatusView extends View {
    private static final int EDIT_MODE_MODULE_COUNT = 7;
    private static final int INVALID_INDEX = -1;
    private static final int SHAPE_CIRCLE = 0;
    private static final float DEFAULT_OUTLINE_DP = 3f;
    private static final float SHAPE_SIZE_CONSTANT_DP = 72f;
    private static final float SPACING_CONSTANT_DP = 15f;
    private String mExampleString; // TODO: use a default from R.string...
    private int mExampleColor = Color.RED; // TODO: use a default from R.color...
    private float mExampleDimension = 0; // TODO: use a default from R.dimen...
    private Drawable mExampleDrawable;

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    private  boolean[] mModuleStatus;
    private float mOutLineWidth;
    private float shapeSize;
    private float spacing;
    private Rect[] mModuleRectangles;
    private Paint mPaintOutline;
    private Paint mPaintFill;
    private float radius;
    private int mMaxHorizontalModules;
    private int mShape;
    private ModuleStatusAccessibilityHelper accessibilityHelper;

    public ModuleStatusView(Context context) {
        super(context);
        init(null, 0);
    }

    public ModuleStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }



    public ModuleStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public boolean[] getModuleStatus() {
        return mModuleStatus;
    }

    public void setModuleStatus(boolean[] mModuleStatus) {
        this.mModuleStatus = mModuleStatus;
    }

    private void init(AttributeSet attrs, int defStyle) {

        setFocusable(true);
        accessibilityHelper = new ModuleStatusAccessibilityHelper(this);
        ViewCompat.setAccessibilityDelegate(this, accessibilityHelper);

        DisplayMetrics dm=getContext().getResources().getDisplayMetrics();
        float screenDensity=dm.density;
        int height=dm.heightPixels;
        int width=dm.widthPixels;
        float defaultOutlineWidthPixels=screenDensity* DEFAULT_OUTLINE_DP;
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.ModuleStatusView, defStyle, 0);

        int mOutLineColor=a.getColor(R.styleable.ModuleStatusView_outlineColor,Color.BLACK);

        mShape = a.getInt(R.styleable.ModuleStatusView_shape, SHAPE_CIRCLE);
        //the get dimension method returns dimensions in physical pixels based on screen density
        //if outline width is set to 4dp and screen density is 2, it'll return 8 physical pixels
        //screen density=number of physical pixels per device independent pixels
        mOutLineWidth = a.getDimension(R.styleable.ModuleStatusView_outlineWidth,defaultOutlineWidthPixels);

        a.recycle();

        if(isInEditMode()){
             setUpEditModeValues();
        }
        //declaring sizes in pixels
        shapeSize = screenDensity* SHAPE_SIZE_CONSTANT_DP;
        spacing = screenDensity* SPACING_CONSTANT_DP;
        //note that the rightmost and bottom parts of the shape are not considered part of the shape
        //The right and bottom outline are not considered part of the shape
        radius = (shapeSize-mOutLineWidth)/2;


        mPaintOutline = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintOutline.setColor(mOutLineColor);
        mPaintOutline.setStyle(Paint.Style.STROKE);
        mPaintOutline.setStrokeWidth(mOutLineWidth);

        int mFillColor=getContext().getResources().getColor(R.color.pluralsight_orange);
        mPaintFill = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintFill.setStyle(Paint.Style.FILL);
        mPaintFill.setColor(mFillColor);



    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, @Nullable Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        accessibilityHelper.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        return accessibilityHelper.dispatchKeyEvent(event)||super.dispatchKeyEvent(event);
    }

    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        return accessibilityHelper.dispatchHoverEvent(event)||super.dispatchHoverEvent(event);
    }

    private void setUpEditModeValues() {
        boolean[] exampleModuleValues=new  boolean[EDIT_MODE_MODULE_COUNT];

        int middle=EDIT_MODE_MODULE_COUNT/2;
        for(int i=0;i<middle;i++)
            exampleModuleValues[i]=true;

        setModuleStatus(exampleModuleValues);
    }

    private void setUpModuleRectangles(int width) {

        int availableWidth=width-getPaddingLeft()-getPaddingRight();
        int horizontalModulesThatCanFit=(int)(availableWidth/(spacing+shapeSize));
        int maxHorizontalModules=Math.min(horizontalModulesThatCanFit,mModuleStatus.length);

        mModuleRectangles = new Rect[mModuleStatus.length];

        for(int moduleIndex = 0; moduleIndex< mModuleRectangles.length; moduleIndex++){
            //The modulus operator gives the remainder of an integer divide
            int column=moduleIndex% maxHorizontalModules;
            int row=moduleIndex/ maxHorizontalModules;

            //left edge of rectangle
            int x=getPaddingLeft()+(int) (column*(shapeSize+spacing));
            //top edge
            int y=getPaddingTop()+(int)(row*(shapeSize+spacing));
            mModuleRectangles[moduleIndex]=new Rect(x,y,(int)(x+shapeSize),(int)(y+shapeSize));


        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //calculating the positions of the rectangles using the width that the view is going to use
        setUpModuleRectangles(w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int moduleIndex=0;moduleIndex<mModuleRectangles.length;moduleIndex++){

            if(mShape==SHAPE_CIRCLE) {


                float x = mModuleRectangles[moduleIndex].centerX();
                float y = mModuleRectangles[moduleIndex].centerY();


                if (mModuleStatus[moduleIndex])
                    canvas.drawCircle(x, y, radius, mPaintFill);

                canvas.drawCircle(x, y, radius, mPaintOutline);
            }
            else{
                drawSquare(canvas,moduleIndex);
            }
        }
    }

    private void drawSquare(Canvas canvas, int moduleIndex) {
        Rect moduleRectangle=mModuleRectangles[moduleIndex];

        if(mModuleStatus[moduleIndex])
            canvas.drawRect(moduleRectangle,mPaintFill);
        canvas.drawRect(moduleRectangle.left+(int)(mOutLineWidth/2),
                            moduleRectangle.top+(int)(mOutLineWidth/2),
                                moduleRectangle.right+(int)(mOutLineWidth/2),
                                moduleRectangle.bottom+(int)(mOutLineWidth/2),
                                    mPaintOutline);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                return  true;

            case MotionEvent.ACTION_UP:
                int moduleIndex=findItemAtPoint(event.getX(),event.getY());
                onModuleSelected(moduleIndex);
                return  true;
        }

        return super.onTouchEvent(event);


    }

    private void onModuleSelected(int moduleIndex) {
        if(moduleIndex==INVALID_INDEX)
            return;
        mModuleStatus[moduleIndex]=!mModuleStatus[moduleIndex];
        invalidate();
        //informs accessibility system that certain property of virtual view has changed, so that the accessibility state is updated
        //along with the visual state. e.g if the visual state is now unchecked the content description needs to change to unchecked too
        accessibilityHelper.invalidateVirtualView(moduleIndex);
        //sends a notification about the event to accessibility system
        accessibilityHelper.sendEventForVirtualView(moduleIndex, AccessibilityEvent.TYPE_VIEW_CLICKED);
    }

    private int findItemAtPoint(float x, float y) {
        int moduleIndex = INVALID_INDEX;
        for(int i=0;i<mModuleRectangles.length;i++){
            if(mModuleRectangles[i].contains((int)x,(int)y)){
                moduleIndex=i;
                break;
            }
        }
        return moduleIndex;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int desiredWidth=0;
        int desiredHeight=0;

        int specWidth=MeasureSpec.getSize(widthMeasureSpec);
        int availableWidth=specWidth-getPaddingLeft()-getPaddingRight();
        int mHorizontalModulesThatCanFit=(int)(availableWidth/(shapeSize+spacing));
        mMaxHorizontalModules = Math.min(mHorizontalModulesThatCanFit,mModuleStatus.length);

        desiredWidth=(int)((mMaxHorizontalModules *(shapeSize+spacing))-spacing);
        desiredWidth += getPaddingLeft() + getPaddingRight();

        int rows=((mModuleStatus.length-1)/ mMaxHorizontalModules)+1;
        desiredHeight=(int)((rows*(shapeSize+spacing))-spacing);
        desiredHeight+=getPaddingTop()+getPaddingBottom();

        //the width and height constraints are passed to resolveSizeandState() just as received by onMeasure method.
        //the two parameters contain both size and mode, the resolveSizeandState() method will use the size and mode to
        //reconcile desired size and constraint size
        //ToDo do nothing
        int width=resolveSizeAndState(desiredWidth,widthMeasureSpec,0);
        int height=resolveSizeAndState(desiredHeight,heightMeasureSpec,0);

        setMeasuredDimension(width,height);

    }
        private class ModuleStatusAccessibilityHelper extends ExploreByTouchHelper {
            /**
             * Constructs a new helper that can expose a virtual view hierarchy for the
             * specified host view.
             *
             * @param host view whose virtual view hierarchy is exposed by this helper
             */
            public ModuleStatusAccessibilityHelper(@NonNull View host) {
                super(host);
            }

            //returns the id of the virtual view that has been tapped by user
            //method called by accessibility system that passes the x and y coordinates
            @Override
            protected int getVirtualViewAt(float x, float y) {
                int moduleIndex=findItemAtPoint(x,y);
                return moduleIndex==INVALID_INDEX?ExploreByTouchHelper.INVALID_ID:moduleIndex;
            }
            @Override
            protected void getVisibleVirtualViews(List<Integer> virtualViewIds) {
                if(mModuleRectangles==null)
                    return;
                for(int moduleIndex=0;moduleIndex<mModuleRectangles.length;moduleIndex++)
                        virtualViewIds.add(moduleIndex);
            }
            //accessibility system calls method to get info about virtual views
            @Override
            protected void onPopulateNodeForVirtualView(int virtualViewId, @NonNull AccessibilityNodeInfoCompat node) {
                //in here, the contentDescription and bounds must be set
                node.setFocusable(true);
                //indicates to accessibility system where to draw highlight box
                node.setBoundsInParent(mModuleRectangles[virtualViewId]);
                //provides info to screenreader
                node.setContentDescription("module"+virtualViewId);

                //tells accessibility system that virtual views support the concept of being checked or unchecked
                node.setCheckable(true);
                //modules that are complete will be checked
                node.setChecked(mModuleStatus[virtualViewId]);

                node.addAction(AccessibilityNodeInfoCompat.ACTION_CLICK);


            }

            @Override
            protected boolean onPerformActionForVirtualView(int virtualViewId, int action, @Nullable Bundle arguments) {
              switch (action) {
                  case AccessibilityNodeInfoCompat.ACTION_CLICK:
                      onModuleSelected(virtualViewId);
                      return true;

              }

                return false;
            }
        }


}
