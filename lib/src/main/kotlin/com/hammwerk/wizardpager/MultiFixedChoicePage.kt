package com.hammwerk.wizardpager

import com.hammwerk.wizardpager.core.Page

class MultiFixedChoicePage @JvmOverloads constructor(title: String, vararg var choices: String,
													 required: Boolean = false) :
		Page(title, MultiFixedChoicePageFragment::class.qualifiedName!!, required) {
	var selectedChoices: List<Int>? = null
}
