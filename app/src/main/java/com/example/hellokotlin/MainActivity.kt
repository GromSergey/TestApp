package com.example.hellokotlin

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {
    lateinit var root: RelativeLayout
    val blocks = ArrayList<Block>()
    var capturedBlock: Block? = null
    var lastX = 0
    var lastY = 0
    var downX = 0
    var downY = 0

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        root = findViewById(R.id.root)
        root.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> { for (block in blocks.reversed())
                    if (block.containsPoint(event.x.toInt(), event.y.toInt())) {
                        downX = event.x.toInt()
                        downY = event.y.toInt()
                        lastX = downX
                        lastY = downY
                        capturedBlock = block
                    }
                }
                MotionEvent.ACTION_MOVE -> { if (capturedBlock != null) {
                    capturedBlock!!.move(event.x.toInt() - lastX,
                                        event.y.toInt() - lastY)
                    lastX = event.x.toInt()
                    lastY = event.y.toInt()
                    }
                }
                MotionEvent.ACTION_UP -> {
                    if (event.x.toInt() == downX && event.y.toInt() == downY)
                        capturedBlock?.rotate()
                    capturedBlock = null
                }
            }
            true
        }

//        blocks.add(Block(root, 50, 0, 0, 50, 0, 50, 50, 100, 50, 100, 100))
//        blocks.add(Block(root, 50, 200, 200, 200, 250, 200, 300))

    }
}
