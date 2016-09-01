package com.hammwerk.wizardpager

import android.content.Context
import android.os.Bundle
import android.support.v4.app.ListFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

class MultiFixedChoicePageFragment : ListFragment() {
	private val wizardFragmentHelper by lazy { WizardFragmentHelper<MultiFixedChoicePage>(arguments) }

	private val page: MultiFixedChoicePage
		get() = wizardFragmentHelper.page

	override fun onAttach(context: Context) {
		super.onAttach(context)
		wizardFragmentHelper.onAttach(context)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_multi_fixed_choice_page, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(view.findViewById(android.R.id.title) as TextView).text = page.title
		val listView = (view.findViewById(android.R.id.list) as ListView).apply {
			adapter = ArrayAdapter(activity,
					android.R.layout.simple_list_item_multiple_choice,
					android.R.id.text1,
					page.choices)
		}
		page.selectedChoices?.forEach { listView.setItemChecked(it, true) }
	}

	override fun onListItemClick(l: ListView, v: View?, position: Int, id: Long) {
		page.completed = l.checkedItemCount > 0
		page.selectedChoices = l.checkedPositions
	}

	private val ListView.checkedPositions: List<Int>?
		get() = checkedItemPositions?.let {
			(0 until checkedItemPositions.size())
					.filter { checkedItemPositions.valueAt(it) }
					.map { checkedItemPositions.keyAt(it) }
		}
}
