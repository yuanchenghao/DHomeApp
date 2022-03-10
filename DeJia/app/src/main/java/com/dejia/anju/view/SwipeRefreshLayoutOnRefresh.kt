package com.dejia.anju.view

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class SwipeRefreshLayoutOnRefresh(private val mPullLoadMoreRecyclerView: PullLoadMoreRecyclerView) : SwipeRefreshLayout.OnRefreshListener {
    override fun onRefresh() {
        if (!mPullLoadMoreRecyclerView.isRefresh) {
            mPullLoadMoreRecyclerView.setIsRefresh(true)
            mPullLoadMoreRecyclerView.refresh()
        }
    }
}