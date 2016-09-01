package com.hammwerk.wizardpager.core

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import com.hammwerk.wizardpager.WizardFragmentHelper
import java.util.*

open class WizardPagerAdapter(val context: Context,
							  fm: FragmentManager?) : FragmentStatePagerAdapter(fm) {
	var wizardTree: WizardTree = WizardTree()
		set(value) {
			field = value.apply {
				onTreeChanged = {
					lastChangedPageIndex = it
					notifyDataSetChanged()
				}
			}
		}

	private var lastChangedPageIndex: Int = 0

	private val itemPositions = HashMap<Fragment, Int>()

	override fun getItemPosition(`object`: Any?): Int {
		val item = itemPositions[`object`]
		return when {
			item != null && item < lastChangedPageIndex -> PagerAdapter.POSITION_UNCHANGED
			else -> PagerAdapter.POSITION_NONE
		}
	}

	override fun getItem(position: Int): Fragment {
		val args = Bundle().apply {
			putInt(WizardFragmentHelper.KEY_POSITION, position)
		}
		return Fragment.instantiate(context, wizardTree.getPage<Page>(position).fragmentName, args)
	}

	override fun instantiateItem(container: ViewGroup?, position: Int): Fragment {
		val instantiateItem = super.instantiateItem(container, position) as Fragment
		itemPositions.put(instantiateItem, position)
		return instantiateItem
	}

	override fun getCount(): Int {
		return wizardTree.numberOfAccessablePages
	}

	fun isPageValid(position: Int): Boolean {
		return wizardTree.getPage<Page>(position).valid
	}
}
