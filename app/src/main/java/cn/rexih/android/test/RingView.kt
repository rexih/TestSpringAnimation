package cn.rexih.android.test

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.dynamicanimation.animation.FloatPropertyCompat
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.SpringForce.*


/**
 *
 * //传递一个有意义的值，以确保动画不会消耗太多的CPU性能
stretchAnimation.setMinimumVisibleChange(DynamicAnimation.MIN_VISIBLE_CHANGE_SCALE);
androidx.dynamicanimation.animation.DynamicAnimation.MIN_VISIBLE_CHANGE_SCALE
......
 * @package cn.rexih.android.test
 * @file RingView
 * @date 2021-08-15
 * @author huangwr
 * @version %I%, %G%
 */
class RingView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr),IDragListener {

    var paint: Paint
    var innerPaint: Paint
    var mScaleProp: FloatPropertyCompat<RingView>
    lateinit var mRingAnim: SpringAnimation
    lateinit var mRingSxAnim: SpringAnimation
    lateinit var mRingSyAnim: SpringAnimation
    lateinit var springForce: SpringForce

    init {
        paint = Paint()
        innerPaint = Paint()
        paint.color = Color.RED
        innerPaint.color = Color.WHITE




        mScaleProp = object : FloatPropertyCompat<RingView>("scale") {
            override fun getValue(obj: RingView?): Float {
                return obj?.scaleX ?: 1f
            }

            override fun setValue(obj: RingView?, value: Float) {
                obj?.scaleX = value
                obj?.scaleY = value
            }
        }

        initAnim()
    }

    private fun initAnim() {
        mRingAnim =
            SpringAnimation(this, mScaleProp).addUpdateListener { animation, value, velocity ->
                Log.w("hwr", "v:$value")
            }

        springForce = SpringForce(1f)
    //            .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
    //            .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
    //            .setDampingRatio(1f)
            .setDampingRatio(DAMPING_RATIO_MEDIUM_BOUNCY)
    //            .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
    //            .setStiffness(SpringForce.STIFFNESS_HIGH)
            .setStiffness(STIFFNESS_MEDIUM)
        //            .setStiffness(SpringForce.STIFFNESS_LOW)
        //            .setStiffness(SpringForce.STIFFNESS_VERY_LOW)
        mRingAnim.spring = springForce


        mRingSxAnim = SpringAnimation(this, SpringAnimation.SCALE_X).setSpring(springForce)
        mRingSyAnim = SpringAnimation(this, SpringAnimation.SCALE_Y).setSpring(springForce)

    }


    var mCenter = 0f
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCenter = (w / 2).toFloat()

    }

    override fun setScaleX(scaleX: Float) {
        super.setScaleX(scaleX)
        Log.w("hwr", "sx:$scaleX")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawCircle(mCenter, mCenter, mCenter, paint)
        canvas?.drawCircle(mCenter, mCenter, mCenter / 2, innerPaint)
    }


    fun expandClickAnim() {
        resetClose()
        isExpand = true
        springForce.setDampingRatio(DAMPING_RATIO_EXPAND)
            .setStiffness(STIFFNESS_EXPAND)
            .setFinalPosition(4.3f)
        if (mRingSyAnim.isRunning) {
            mRingSxAnim.animateToFinalPosition(4.3f)
            mRingSyAnim.animateToFinalPosition(4.3f)
        }else {
            mRingSyAnim.start()
            mRingSxAnim.start()
        }
        postDelayed(resetCntRunnable,2000)
    }

    fun closeClickAnim() {
        isExpand = false
        springForce.setDampingRatio(DAMPING_RATIO_EXPAND)
            .setStiffness(STIFFNESS_EXPAND)
            .setFinalPosition(1f)
        if (mRingSyAnim.isRunning) {
            mRingSxAnim.animateToFinalPosition(1f)
            mRingSyAnim.animateToFinalPosition(1f)
        }else {
            mRingSyAnim.start()
            mRingSxAnim.start()
        }
    }

    fun resetClose() {
        removeCallbacks(resetCntRunnable)
    }

    private val resetCntRunnable = Runnable {
        closeClickAnim()
    }


    fun doClickAnim() {
        resetClose()
//        initAnim()
        springForce.setDampingRatio(DAMPING_RATIO_CLICK)
            .setStiffness(STIFFNESS_CLICK)
            .setFinalPosition(1f)
        if (mRingSyAnim.isRunning) {
            mRingSxAnim.animateToFinalPosition(1f)
            mRingSyAnim.animateToFinalPosition(1f)
        }else {
            mRingSyAnim.setStartValue(1.05f)
            mRingSxAnim.setStartValue(1.05f)
            mRingSyAnim.start()
            mRingSxAnim.start()
        }

    }

    fun keepExpand() {
        resetClose()
        postDelayed(resetCntRunnable,2000)
    }

    var isExpand = false

    override fun updateDrag(x: Int, y: Int) {
        if (!isExpand) {
            resetClose()
            isExpand = true
            springForce.setDampingRatio(DAMPING_RATIO_EXPAND)
                .setStiffness(STIFFNESS_EXPAND)
                .setFinalPosition(4.3f)
            if (mRingSyAnim.isRunning) {
                mRingSxAnim.animateToFinalPosition(4.3f)
                mRingSyAnim.animateToFinalPosition(4.3f)
            }else {
                mRingSyAnim.start()
                mRingSxAnim.start()
            }
        } else {

        }

    }

    override fun releaseDrag(x: Int, y: Int) {
        closeClickAnim()
    }

    override fun cancelDrag() {
    }


    companion object {
        const val DAMPING_RATIO_CLICK = DAMPING_RATIO_MEDIUM_BOUNCY
        const val DAMPING_RATIO_EXPAND = DAMPING_RATIO_NO_BOUNCY
        const val STIFFNESS_EXPAND = STIFFNESS_MEDIUM
        const val STIFFNESS_CLICK = STIFFNESS_MEDIUM
    }
}