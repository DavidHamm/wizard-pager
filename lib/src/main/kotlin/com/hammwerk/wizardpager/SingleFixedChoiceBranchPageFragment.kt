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

class SingleFixedChoiceBranchPageFragment : ListFragment() {
	private val wizardFragmentHelper by lazy { WizardFragmentHelper<SingleFixedChoiceBranchPage>(arguments) }

	private val page: SingleFixedChoiceBranchPage
		get() = wizardFragmentHelper.page

	override fun onAttach(context: Context?) {
		super.onAttach(context)
		wizardFragmentHelper.onAttach(context!!)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_single_fixed_choice_branch_page, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		(view.findViewById(android.R.id.title) as TextView).text = page.title
		val listView = (view.findViewById(android.R.id.list) as ListView).apply {
			adapter = ArrayAdapter<String>(activity,
					android.R.layout.simple_list_item_single_choice,
					android.R.id.text1,
					page.choices)
		}
		page.selectedBranchIndex?.let { listView.setItemChecked(it, true) }
	}

	override fun onListItemClick(l: ListView, v: View?, position: Int, id: Long) {
		page.selectBranch(position)
	}
}
