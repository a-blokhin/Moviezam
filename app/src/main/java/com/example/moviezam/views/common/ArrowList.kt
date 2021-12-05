package com.example.moviezam.views.common

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ArrowList {
    companion object {
        fun setArrows(
            rv: RecyclerView,
            leftArrow: FloatingActionButton,
            rightArrow: FloatingActionButton
        ) {
            if (rv.adapter!!.itemCount == 1) {
                leftArrow.visibility = View.GONE
                rightArrow.visibility = View.GONE
            }
            rightArrow.setOnClickListener {
                val next =
                    (rv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition() + 1
                if (next <= rv.adapter!!.itemCount) {
                    rv.smoothScrollToPosition(next)
                }
            }

            leftArrow.setOnClickListener {
                val prev =
                    (rv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition() - 1
                if (prev >= 0) {
                    rv.smoothScrollToPosition(prev)
                }
            }
        }

        fun getRVScrollListener(
            leftArrow: FloatingActionButton,
            rightArrow: FloatingActionButton
        ): RecyclerView.OnScrollListener {
            val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
                object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val lastNum: Int =
                            (recyclerView.layoutManager as LinearLayoutManager).itemCount
                        val firstItem: Int =
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        val lastItem =
                            (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                        if ((rightArrow.visibility == View.GONE) && (lastItem < lastNum)) {
                            rightArrow.visibility = View.VISIBLE
                        }
                        if ((leftArrow.visibility == View.GONE) && (firstItem > 0)) {
                            leftArrow.visibility = View.VISIBLE
                        }

                        if (lastItem == lastNum - 1) {
                            rightArrow.visibility = View.GONE
                        } else if (firstItem == 0) {
                            leftArrow.visibility = View.GONE
                        }
                    }
                }
            return recyclerViewOnScrollListener
        }
    }
}