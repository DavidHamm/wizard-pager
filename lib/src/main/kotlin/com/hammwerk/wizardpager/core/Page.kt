package com.hammwerk.wizardpager.core

import android.os.Parcel
import android.os.Parcelable

abstract class Page : Parcelable {
	val title: String
	val fragmentName: String
	private val required: Boolean

	constructor(source: Parcel) {
		fun Parcel.readBoolean(): Boolean {
			return readByte() == 1.toByte()
		}

		this.title = source.readString()
		this.fragmentName = source.readString()
		this.required = source.readBoolean()
		this.completed = source.readBoolean()
	}

	constructor(title: String, fragmentName: String, required: Boolean = false) {
		this.title = title
		this.fragmentName = fragmentName
		this.required = required
	}

	var onPageValid: ((Page) -> Unit)? = null
	var onPageInvalid: ((Page) -> Unit)? = null
	var completed: Boolean = false
		set(value) {
			field = value
			if (required) {
				if (value) {
					onPageValid?.let { it(this) }
				} else {
					onPageInvalid?.let { it(this) }
				}
			}
		}

	val valid: Boolean
		get() = completed or !required

	override fun writeToParcel(dest: Parcel?, flags: Int) {
		fun Parcel.writeBoolean(value: Boolean) {
			writeByte(if (value) 1 else 0)
		}

		dest?.writeString(title)
		dest?.writeString(fragmentName)
		dest?.writeBoolean(required)
		dest?.writeBoolean(completed)
	}

	override fun describeContents(): Int {
		return 0
	}
}
