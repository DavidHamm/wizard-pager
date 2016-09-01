package com.hammwerk.wizardpager.core

import android.os.Parcel
import android.os.Parcelable

class WizardTree : Parcelable {
	val trunk: Branch

	var onTreeChanged: ((Int) -> Unit)? = null
	var onPageValid: ((Page, Int) -> Unit)? = null
	var onPageInvalid: ((Page, Int) -> Unit)? = null

	init {
		trunk = Branch()
	}

	override fun writeToParcel(dest: Parcel?, flags: Int) {
		dest?.writeParcelableArray(trunk.pages, 0)
	}

	override fun describeContents(): Int {
		return 0
	}

	val pages: List<Page>
		get() {
			val list: MutableList<Page> = mutableListOf()
			var branch: Branch? = trunk
			while (branch != null) {
				list.addAll(branch.pages)
				branch = branch.branchPage?.selectedBranch
			}
			return list.toList()
		}

	val numberOfAccessablePages: Int
		get() {
			val pages = this.pages
			val numberOfValidPages = pages.takeWhile { it.valid }.count()
			return numberOfValidPages + if (numberOfValidPages < pages.count()) 1 else 0
		}

	fun setPages(vararg pages: Page) {
		trunk.pages = pages.apply {
			forEach {
				it.onPageValid = onPageValid()
				it.onPageInvalid = onPageInvalid()
			}
		}
		trunk.branchPage?.onBranchSelected = onBranchSelected()
	}

	fun <T : Page> getPage(position: Int): T {
		val pages = pages
		when {
			position < pages.count() -> return pages[position] as T
			else -> throw PageIndexOutOfBoundsException()
		}
	}

	fun getPositionOfPage(page: Page) = pages.indexOf(page)

	fun isLastPage(page: Page): Boolean {
		val pages = pages
		return when {
			pages.isNotEmpty() -> {
				val lastPage = pages.last()
				lastPage == page && (lastPage !is BranchPage || lastPage.selectedBranch != null)
			}
			else -> false
		}
	}

	private fun onBranchSelected(): (BranchPage) -> Unit = {
		it.selectedBranch?.apply {
			pages.forEach {
				it.onPageValid = onPageValid()
				it.onPageInvalid = onPageInvalid()
			}
			branchPage?.onBranchSelected = onBranchSelected()
		}
	}

	private fun onPageValid(): (Page) -> Unit = { page ->
		onTreeChanged?.let { it(getPositionOfPage(page) + 1) }
		onPageValid?.let { it(page, getPositionOfPage(page)) }
	}

	private fun onPageInvalid(): (Page) -> Unit = { page ->
		onTreeChanged?.let { it(getPositionOfPage(page) + 1) }
		onPageInvalid?.let { it(page, getPositionOfPage(page)) }
	}

	companion object {
		@JvmField val CREATOR = object : Parcelable.Creator<WizardTree> {
			override fun createFromParcel(source: Parcel?): WizardTree {
				return WizardTree().apply {
					setPages(*source?.readParcelableArray(WizardTree::class.java.classLoader)
							?.map { it as Page }
							?.toTypedArray()
							.orEmpty())
				}
			}

			override fun newArray(size: Int): Array<out WizardTree?> {
				return arrayOfNulls(size)
			}
		}
	}
}
