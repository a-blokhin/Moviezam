package com.example.moviezam.views.ui

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment()  {

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(id: Int, f: BaseFragment)
    }
}