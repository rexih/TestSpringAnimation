package cn.rexih.android.test

import android.animation.AnimatorSet
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var springForce: SpringForce? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_trigger.setOnClickListener {
            //            springForce?.dampingRatio = SpringForce.DAMPING_RATIO_HIGH_BOUNCY
            testSpring()
        }
        btn_trigger2.setOnClickListener {
            springForce?.dampingRatio = SpringForce.DAMPING_RATIO_NO_BOUNCY
        }
        springForce = SpringForce(1f)
//            .setDampingRatio(SpringForce.DAMPING_RATIO_NO_BOUNCY)
//            .setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY)
//            .setDampingRatio(1f)
            .setDampingRatio(SpringForce.DAMPING_RATIO_MEDIUM_BOUNCY)
//            .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
//            .setStiffness(SpringForce.STIFFNESS_HIGH)
            .setStiffness(SpringForce.STIFFNESS_MEDIUM)
//            .setStiffness(SpringForce.STIFFNESS_LOW)
//            .setStiffness(SpringForce.STIFFNESS_VERY_LOW)

        dv_drag.setOnClickListener {
            dv_drag.removeCallbacks(resetCntRunnable)
            if (rv_ring.isExpand) {
                rv_ring.keepExpand()
            }else if (mCnt < 2) {
                rv_ring.doClickAnim()
                mCnt++
            } else {
                rv_ring.expandClickAnim()
                mCnt = 0
            }
            dv_drag.postDelayed(resetCntRunnable, 2000)
        }
        dv_drag.dragListener = rv_ring
    }

    private val resetCntRunnable = Runnable {
        mCnt = 0
    }


    private var mCnt = 0
    private fun testSpring() {

//        val springForce = SpringForce(1f)
//            .setDampingRatio(SpringForce.DAMPING_RATIO_HIGH_BOUNCY)
//            .setStiffness(SpringForce.STIFFNESS_VERY_LOW)
        val anim = SpringAnimation(rv_ring, SpringAnimation.SCALE_X).setSpring(springForce)
        val anim2 = SpringAnimation(rv_ring, SpringAnimation.SCALE_Y).setSpring(springForce)

        anim2.addUpdateListener { animation, value, velocity ->
            Log.w("hwr", "v:$value")
        }
        anim.setStartValue(1.05f)
        anim2.setStartValue(1.05f)
        anim.start()
        anim2.start()
        anim.isRunning

        AnimatorSet().apply {
            //playTogether(anim,anim2)
        }
    }
}
