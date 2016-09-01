package com.hammwerk.wizardpager

import android.content.Context
import android.os.Bundle
import com.hammwerk.wizardpager.core.Page

class WizardFragmentHelper<out T : Page>(private val arguments: Bundle?) {
	private lateinit var wizardable: Wizardable

	val page: T
		get() {
			when {
				arguments != null && arguments.containsKey(KEY_POSITION) ->
					return wizardable.getPage(arguments.getInt(KEY_POSITION))
				else -> throw MissingPositionException()
			}
		}

	fun onAttach(context: Context) {
		wizardable = context as Wizardable
	}

	companion object {
		const val KEY_POSITION = "${BuildConfig.APPLICATION_ID}.key.POSITION"
	}
}
