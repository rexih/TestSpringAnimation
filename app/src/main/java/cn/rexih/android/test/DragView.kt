package cn.rexih.android.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs


/**
 *
 * @package cn.rexih.android.test
 * @file RingView
 * @date 2021-08-15
 * @author huangwr
 * @version %I%, %G%
 */
class DragView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var paint: Paint

    var dragListener: IDragListener? = null

    init {
        paint = Paint()
        paint.color = Color.GRAY
    }

    var mCenter = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenter = (w / 2).toFloat()

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(mCenter, mCenter, mCenter, paint)
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                handleDown(event)
            }
            MotionEvent.ACTION_MOVE -> {
                handleMove(event)
            }
            MotionEvent.ACTION_UP -> {
                handleUp(event)
            }
            MotionEvent.ACTION_CANCEL -> {
                dragListener?.cancelDrag()
            }
        }


        return true
    }

    private fun handleUp(event: MotionEvent) {

        var dx = event.rawX - mDownX
        var dy = event.rawY - mDownY
        var dt = SystemClock.uptimeMillis() - mDownTime

        if (asClick(dx, dy, dt)) {
            performClick()
        } else {
            getLocationOnScreen(mLocCache)
            dragListener?.releaseDrag(mLocCache[0] + width / 2, mLocCache[1] + height / 2)
        }

        translationX = 0f
        translationY = 0f
    }

    private fun asClick(dx: Float, dy: Float, dt: Long): Boolean {
        return abs(dx) < 20 && abs(dy) < 20 && dt < 200
    }

    var mLocCache = intArrayOf(0, 0)
    private fun handleMove(event: MotionEvent) {

        var dx = event.rawX - mDownX
        var dy = event.rawY - mDownY
        var dt = SystemClock.uptimeMillis() - mDownTime

        translationX = dx
        translationY = dy

        if (!asClick(dx, dy, dt)) {
            getLocationOnScreen(mLocCache)
            dragListener?.updateDrag(mLocCache[0] + width / 2, mLocCache[1] + height / 2)
        }


    }

    private var mDownX = 0f
    private var mDownY = 0f
    var mDownTime = 0L
    private fun handleDown(event: MotionEvent) {


        mDownX = event.rawX
        mDownY = event.rawY
        mDownTime = SystemClock.uptimeMillis()
    }

}

interface IDragListener {

    fun updateDrag(x: Int, y: Int)
    fun releaseDrag(x: Int, y: Int)
    fun cancelDrag()
}