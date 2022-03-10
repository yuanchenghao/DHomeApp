package com.dejia.anju.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class YMStaggeredGridLayoutManager : StaggeredGridLayoutManager {
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {}
    constructor(spanCount: Int, orientation: Int) : super(spanCount, orientation) {}

    override fun onLayoutChildren(recycler: Recycler, state: RecyclerView.State) {
        try {
            //这里捕获之前的数组越界问题...
            super.onLayoutChildren(recycler, state)
        } catch (e: IndexOutOfBoundsException) {
            e.printStackTrace()
        }
    }
}