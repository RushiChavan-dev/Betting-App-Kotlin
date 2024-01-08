package com.example.rushi.common

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.rushi.utils.AnalyticsHelper
import com.example.rushi.utils.LoadingHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
open class BaseFragment : Fragment() {

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    private val loadingHelper by lazy { LoadingHelper() }
    private var myContext: FragmentActivity? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    fun showLoading() {
        if (!loadingHelper.isAdded) {
            myContext?.let { loadingHelper.show(it.supportFragmentManager, "loading") }
        }
    }

    fun hideLoading() {
        if (loadingHelper.isAdded) {
            loadingHelper.dismissAllowingStateLoss()
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myContext = activity as FragmentActivity
    }
}