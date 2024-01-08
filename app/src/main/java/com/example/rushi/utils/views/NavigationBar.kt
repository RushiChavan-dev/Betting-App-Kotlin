package com.example.rushi.utils.views

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.rushi.R
import com.example.rushi.databinding.NavigationBarBinding
import com.example.rushi.utils.OddUtilHelper
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class NavigationBar : LinearLayout, View.OnClickListener {

    @Inject
    lateinit var oddUtilHelper: OddUtilHelper

    var binding: NavigationBarBinding =
        NavigationBarBinding.inflate(LayoutInflater.from(context), this, true)
    private var listener: NavigationBarListener? = null

    private var selectedPos: Int = 0

    init {
        listeners()
        setupObservers()
    }

    private fun setupObservers() {
        oddUtilHelper.selectedBetMatchOdds.observe(context as LifecycleOwner) {
            if (it != null ) {
                binding.basketTv.text = "${it.size}"
                Timber.d("Coupon child count : ${it.size}")
            }

        }

    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun listeners() {
        binding.basketContainer.setOnClickListener(this)
        binding.favoritesContainer.setOnClickListener(this)
        binding.fixturesContainer.setOnClickListener(this)
    }

    fun setListener(listener: NavigationBarListener) {
        this.listener = listener
    }

    @Synchronized
    fun selectItem(pos: Int) {
        if (selectedPos != pos) {
            selectedPos = pos
            makeSelected(pos)
        }
    }

    @Synchronized
    fun makeUnSelected() {
        binding.fixturesIv.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.nav_disable_tint
            ),
            PorterDuff.Mode.SRC_IN
        )
        binding.fixturesTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.nav_disable_tint
            ),
        )/*
        binding.basketIv.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.nav_disable_tint
            ),
            PorterDuff.Mode.SRC_IN
        )
        binding.basketTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.nav_disable_tint
            ),
        )*/
        binding.favoritesIv.setColorFilter(
            ContextCompat.getColor(
                context,
                R.color.nav_disable_tint
            ),
            PorterDuff.Mode.SRC_IN
        )
        binding.favoritesTv.setTextColor(
            ContextCompat.getColor(
                context,
                R.color.nav_disable_tint
            ),
        )
    }

    @Synchronized
    fun makeSelected(pos: Int) {
        makeUnSelected()
        val imageAnim = ValueAnimator()
        imageAnim.setIntValues(
            ContextCompat.getColor(context, R.color.nav_disable_tint),
            ContextCompat.getColor(context, R.color.nav_active_tint)
        )
        imageAnim.setEvaluator(ArgbEvaluator())
        imageAnim.addUpdateListener { valueAnimator: ValueAnimator ->
            when (pos) {
                0 -> {
                    binding.fixturesIv.setColorFilter(
                        (valueAnimator.animatedValue as Int),
                        PorterDuff.Mode.SRC_IN
                    )
                    binding.fixturesTv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.nav_active_tint
                        )
                    )
                }
                1 -> {

                }
                2 -> {
                    binding.favoritesIv.setColorFilter(
                        (valueAnimator.animatedValue as Int),
                        PorterDuff.Mode.SRC_IN
                    )
                    binding.favoritesTv.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.nav_active_tint
                        )
                    )
                }
            }
        }
        val textAnim = ValueAnimator()
        textAnim.setIntValues(
            ContextCompat.getColor(context, R.color.gray3),
            ContextCompat.getColor(context, R.color.gray3)
        )
        textAnim.setEvaluator(ArgbEvaluator())
        textAnim.addUpdateListener { valueAnimator: ValueAnimator ->
            when (pos) {
                0 -> binding.fixturesTv.setTextColor((valueAnimator.animatedValue as Int))
                2 -> binding.favoritesTv.setTextColor((valueAnimator.animatedValue as Int))
            }
        }
        textAnim.duration = 50
        textAnim.start()
        imageAnim.duration = 50
        imageAnim.start()
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        binding.fixturesIv.setColorFilter(
            ContextCompat.getColor(context, R.color.nav_active_tint),
            PorterDuff.Mode.SRC_IN
        )
        binding.fixturesTv.setTextColor(ContextCompat.getColor(context, R.color.nav_active_tint))
/*

        binding.basketIv.setColorFilter(
            ContextCompat.getColor(context, R.color.nav_disable_tint),
            PorterDuff.Mode.SRC_IN
        )
        binding.basketTv.setTextColor(ContextCompat.getColor(context, R.color.nav_disable_tint))
*/

        binding.favoritesIv.setColorFilter(
            ContextCompat.getColor(context, R.color.nav_disable_tint),
            PorterDuff.Mode.SRC_IN
        )
        binding.favoritesTv.setTextColor(ContextCompat.getColor(context, R.color.nav_disable_tint))
    }

    override fun onClick(v: View?) {
        synchronizedOnClicked(v!!)
    }

    @Synchronized
    private fun synchronizedOnClicked(view: View) {
        when (view.id) {
            R.id.fixtures_container -> selectItem(0)
            R.id.basket_container -> selectItem(1)
            R.id.favorites_container -> selectItem(2)
        }
        if (listener != null) listener!!.onNavigationClicked(selectedPos)
    }

    interface NavigationBarListener {
        fun onNavigationClicked(pos: Int)
    }


}
