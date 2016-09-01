package com.hammwerk.wizardpager

import com.hammwerk.wizardpager.core.Page

class IntegerPage @JvmOverloads constructor(title: String, required: Boolean = true, val unit: String) :
		Page(title, IntegerPageFragment::class.java.name, required) {
	var value: Int? = null
}
