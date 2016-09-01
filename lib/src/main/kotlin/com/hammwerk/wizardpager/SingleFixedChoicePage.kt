package com.hammwerk.wizardpager

import com.hammwerk.wizardpager.core.Page

class SingleFixedChoicePage @JvmOverloads constructor(title: String, vararg val choices: String,
													  required: Boolean = true) :
		Page(title, SingleFixedChoicePageFragment::class.qualifiedName!!, required) {
	var selectedChoice: Int? = null
}
