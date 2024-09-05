package com.fsck.k9.view

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout

class OutsideClickRelativeLayout(context: Context, attrs: AttributeSet?) :
    RelativeLayout(context, attrs) {

    private var viewOutsideClickListenerMap = mutableMapOf<View, () -> Unit>()

    fun setOnOutsideClickListenerForView(view: View, listener: () -> Unit) {
        viewOutsideClickListenerMap[view] = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if(ev.action != MotionEvent.ACTION_DOWN)  return super.onInterceptTouchEvent(ev)
        viewOutsideClickListenerMap.forEach { (view, function) ->
            if (isMotionEventOutsideView(view, ev)) function.invoke()
        }
        return super.onInterceptTouchEvent(ev)
    }

    private fun isMotionEventOutsideView(view: View, motionEvent: MotionEvent): Boolean {
        val viewRectangle = Rect()
        view.getGlobalVisibleRect(viewRectangle)
        return !viewRectangle.contains(motionEvent.rawX.toInt(), motionEvent.rawY.toInt())
    }
}
