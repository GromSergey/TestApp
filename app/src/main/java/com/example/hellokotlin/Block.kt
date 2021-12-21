package com.example.hellokotlin

import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import android.widget.RelativeLayout

class Block(parent: RelativeLayout, private val size: Int, vararg cellsPos: Int) {
    val cells = ArrayList<ImageView>()
    var isAnimated = true // разрешено для поворота
    var cx: Int // координаты центра
    var cy: Int // координаты центра
    init {
        var top = Int.MAX_VALUE   // крайние точки всего блока
        var right = Int.MIN_VALUE // ...
        var bot = Int.MIN_VALUE   // ...
        var left = Int.MAX_VALUE  // крайние точки всего блока
        for (i in (0..cellsPos.size-2 step 2)) {
            val img = ImageView(parent.context)
            cells.add(img)
            img.setImageResource(R.drawable.gold)

            val params = RelativeLayout.LayoutParams(size, size)
            params.leftMargin = cellsPos[i]
            params.topMargin = cellsPos[i + 1]
            //img.layoutParams = params // второй вариант

            parent.addView(img, params) // parent.addView(img) // второй вариант

            if (cellsPos[i + 1] < top) top = cellsPos[i + 1]
            if (cellsPos[i] + size > right) right = cellsPos[i] + size
            if (cellsPos[i + 1] + size > bot) bot = cellsPos[i + 1] + size
            if (cellsPos[i] < left) left = cellsPos[i]
        }
        cx = if (cellsPos.isEmpty()) 0 else left + (right - left) / 2
        cy = if (cellsPos.isEmpty()) 0 else top + (bot - top) / 2
    }

    fun containsPoint(x: Int, y: Int): Boolean {
        for (cell in cells)
            if (x in (cell.left..cell.right) && y in (cell.top..cell.bottom))
                return true
        return false
    }

    fun move(deltaX: Int, deltaY: Int) {
        cx += deltaX
        cy += deltaY
        for (cell in cells) {
            val params = cell.layoutParams as RelativeLayout.LayoutParams
            params.leftMargin += deltaX
            params.topMargin += deltaY
            cell.layoutParams = params
        }
    }

    fun rotate() {
        if (!isAnimated) return
        isAnimated = false
        for (cell in cells) {
            val anim = RotateAnimation(0f, 90f,
                (cx - cell.left).toFloat(), (cy - cell.top).toFloat())
            anim.duration = 400 // длительность
            anim.isFillEnabled = true // хз зачем но надо
            anim.fillBefore = true // хз зачем но надо

            anim.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) { }
                override fun onAnimationEnd(p0: Animation?) {
                    // всё это выполняетя после окончания анимации
                    val params = cell.layoutParams as RelativeLayout.LayoutParams
                    val temp = cx + cy - params.topMargin - size
                    params.topMargin = cy - cx + params.leftMargin
                    params.leftMargin = temp
                    cell.layoutParams = params
                    cell.rotation = (cell.rotation + 90) % 360
                    isAnimated = true
                }
                override fun onAnimationRepeat(p0: Animation?) { }

            })

            cell.startAnimation(anim)
        }
    }
}