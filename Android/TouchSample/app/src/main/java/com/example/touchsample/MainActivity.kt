package com.example.touchsample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import com.example.touchsample.databinding.ActivityMainBinding
import kotlin.math.sqrt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    enum class TOUCHTYPE {
        NONE, TOUCH, DRAG, PINCH
    }

    private var touchMode: TOUCHTYPE = TOUCHTYPE.NONE

    private var dragStartX = 0.0f
    private var dragStartY = 0.0f
    private var pinchStartDistance: Double = 0.0
    private var touchTypeString: String = ""
    private var touchPoint1String: String = ""
    private var touchPoint2String: String = ""
    private var touchLengthString: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    /*
    Function ran when TouchEvent is occurred
    Displays information according to number of fingers touched
     */
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        // Pinch when more than two fingers are detected
        when (event!!.action and MotionEvent.ACTION_MASK) {
            // Additional finger detected
            MotionEvent.ACTION_POINTER_DOWN -> {
                if (event.pointerCount >= 2) {
                    pinchStartDistance = getPinchDistance(event)
                    if (pinchStartDistance > 50f) {
                        touchMode = TOUCHTYPE.PINCH
                        touchTypeString = "PINCH"
                        touchPoint1String =
                            "x: " + event.getX(0) + ", y: " + event.getY(0)
                        touchPoint2String =
                            "x: " + event.getX(1) + ", y: " + event.getY(1)
                        touchLengthString = "length: " + getPinchDistance(event)
                    }
                }
            }
            // Movement detected
            MotionEvent.ACTION_MOVE -> {
                if (touchMode == TOUCHTYPE.PINCH && pinchStartDistance > 0) {
                    touchTypeString = "PINCH"
                    touchPoint1String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                    touchPoint2String = "x: " + event.getX(1) + ", y: " + event.getY(1)
                    touchLengthString = "length: " + getPinchDistance(event)
                }
            }
            // Additional finger removed
            MotionEvent.ACTION_POINTER_UP -> {
                if (touchMode == TOUCHTYPE.PINCH) {
                    touchTypeString = "PINCH"
                    touchPoint1String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                    touchPoint2String = "x: " + event.getX(1) + ", y: " + event.getY(1)
                    touchLengthString = "length: " + getPinchDistance(event)
                    touchMode = TOUCHTYPE.NONE
                }
            }
        }

        // Touch and Drag when one finger is detected
        when (event.action and MotionEvent.ACTION_MASK) {
            // Finger detected
            MotionEvent.ACTION_DOWN -> {
                if (touchMode == TOUCHTYPE.NONE && event.pointerCount == 1) {
                    touchMode = TOUCHTYPE.TOUCH
                    dragStartX = event.getX(0)
                    dragStartY = event.getY(0)
                    touchTypeString = "TOUCH"
                    touchPoint1String = "x: $dragStartX, y: $dragStartY"
                    touchPoint2String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                    touchLengthString = "length: " + getDragDistance(event)
                }
            }
            // Movement detected
            MotionEvent.ACTION_MOVE -> {
                if (touchMode == TOUCHTYPE.DRAG || touchMode == TOUCHTYPE.TOUCH) {
                    touchMode = TOUCHTYPE.DRAG
                    touchTypeString = "DRAG"
                    touchPoint1String = "x: $dragStartX, y: $dragStartY"
                    touchPoint2String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                    touchLengthString = "length: " + getDragDistance(event)
                }
            }
            // Finger removed
            MotionEvent.ACTION_UP -> {
                if (touchMode == TOUCHTYPE.TOUCH) {
                    touchTypeString = "TOUCH"
                } else if (touchMode == TOUCHTYPE.DRAG) {
                    touchTypeString = "DRAG"
                }
                touchPoint1String = "x: $dragStartX, y: $dragStartY"
                touchPoint2String = "x: " + event.getX(0) + ", y: " + event.getY(0)
                touchLengthString = "length: " + getDragDistance(event)
                touchMode = TOUCHTYPE.NONE
            }

        }
        with(binding) {
            textViewTouchType.text = touchTypeString
            textViewTouchPoint1.text = touchPoint1String
            textViewTouchPoint2.text = touchPoint2String
            textViewTouchLength.text = touchLengthString
        }
        return super.onTouchEvent(event)
    }

    private fun displayPinch(event: MotionEvent) {

    }

    // Calculate distance between dragged points
    private fun getDragDistance(event: MotionEvent?): Double {
        val dragLengthX: Double = (event!!.getX(0) - dragStartX).toDouble()
        val dragLengthY: Double = (event!!.getY(0) - dragStartY).toDouble()
        return sqrt(dragLengthX * dragLengthX + dragLengthY * dragLengthY)
    }

    // Calculate distance between fingers
    private fun getPinchDistance(event: MotionEvent?): Double {
        val x: Double = (event!!.getX(0) - event.getX(1)).toDouble()
        val y: Double = (event!!.getY(0) - event.getY(1)).toDouble()
        return sqrt(x * x + y * y)
    }
}