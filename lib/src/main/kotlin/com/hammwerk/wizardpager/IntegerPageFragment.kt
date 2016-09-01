package com.hammwerk.wizardpager

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView

class IntegerPageFragment : Fragment() {
	private val wizardFragmentHelper by lazy { WizardFragmentHelper<IntegerPage>(arguments) }

	private val page: IntegerPage
		get() = wizardFragmentHelper.page

	override fun onAttach(context: Context) {
		super.onAttach(context)
		wizardFragmentHelper.onAttach(context)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		return inflater.inflate(R.layout.fragment_integer_page, container, false)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		(view.findViewById(android.R.id.title) as TextView).text = page.title
		(view.findViewById(R.id.fragment_integer_page_unit_text_view) as TextView).text = page.unit
		(view.findViewById(R.id.fragment_integer_page_edit_text) as EditText)
				.addTextChangedListener(CompletedTextWatcher())
	}

	private inner class CompletedTextWatcher : TextWatcher {
		override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
		}

		override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
		}

		override fun afterTextChanged(s: Editable) {
			if (s.isBlank()) {
				page.completed = false
				page.value = null
			} else {
				page.completed = true
				page.value = s.toString().toInt()
			}
		}
	}
}
