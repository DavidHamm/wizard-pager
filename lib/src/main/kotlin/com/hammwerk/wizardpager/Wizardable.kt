package com.hammwerk.wizardpager

import com.hammwerk.wizardpager.core.Page

interface Wizardable {
	fun <T : Page> getPage(position: Int): T
}
