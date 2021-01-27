package com.cmg.vaccine.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.cmg.vaccine.R

class RecyclerviewItemDecoration(
    val context: Context
): RecyclerView.ItemDecoration() {
    var mDivider:Drawable?=null

    init {
        mDivider = context.resources.getDrawable(R.drawable.recyclerview_item_decoration, null)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left:Int = parent.paddingLeft
        val right:Int = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child: View = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top: Int = child.bottom + params.bottomMargin
            val bottom: Int = top + mDivider?.intrinsicHeight!!
            mDivider?.setBounds(left, top, right, bottom)
            mDivider?.draw(c)
        }
    }
}