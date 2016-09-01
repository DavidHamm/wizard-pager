package com.hammwerk.wizardpager.core

import android.os.Parcel
import android.os.Parcelable

class Branch(val name: String? = null) : Parcelable {
	constructor(parcel: Parcel?) : this(parcel?.readString()) {
		pages = parcel?.readParcelableArray(Branch::class.java.classLoader)?.map { it as Page }?.toTypedArray()!!
	}

	var pages: Array<out Page> = arrayOf()
		set(value) {
			if (value.dropLast(1).any { it is BranchPage }) {
				throw PageAfterBranchPageException()
			}
			field = value
		}

	val numberOfPages: Int
		get() = pages.size

	val numberOfValidPages: Int
		get() = pages.takeWhile { it.valid }.count()

	val branchPage: BranchPage?
		get() {
			if (pages.isNotEmpty()) {
				val page = pages.last()
				if (page is BranchPage) {
					return page
				}
			}
			return null
		}

	override fun writeToParcel(dest: Parcel?, flags: Int) {
		dest?.writeParcelableArray(pages, 0)
	}

	override fun describeContents(): Int {
		return 0
	}

	inline fun <reified T : Page> getPage(position: Int): T {
		when {
			position < pages.size -> return pages[position] as T
			else -> throw PageIndexOutOfBoundsException()
		}
	}

	companion object {
		@JvmField val CREATOR = object : Parcelable.Creator<Branch> {
			override fun createFromParcel(source: Parcel?): Branch {
				return Branch(source)
			}

			override fun newArray(size: Int): Array<out Branch?> {
				return arrayOfNulls(size)
			}
		}
	}

	class PageAfterBranchPageException : RuntimeException()
}
