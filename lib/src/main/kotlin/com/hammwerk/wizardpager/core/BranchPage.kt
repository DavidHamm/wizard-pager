package com.hammwerk.wizardpager.core

open class BranchPage(title: String, fragmentName: String) :
		Page(title, fragmentName, true) {
	protected var branches = mutableListOf<Branch>()
	var onBranchSelected: ((BranchPage) -> Unit)? = null

	val choices: Array<String?>
		get() {
			if (branches.size < 2) {
				throw TwoBranchesRequiredException()
			}
			return branches.map { it.name }.toTypedArray()
		}

	val selectedBranchIndex: Int?
		get() {
			val selectedBranchIndex = branches.indexOf(selectedBranch)
			return when {
				selectedBranchIndex != -1 -> selectedBranchIndex
				else -> null
			}
		}

	var selectedBranch: Branch? = null
		get() = when {
			branches.size >= 2 -> field
			else -> throw TwoBranchesRequiredException()
		}
		private set

	fun addBranch(branchName: String, vararg pages: Page) {
		branches.add(Branch(branchName).apply { this.pages = arrayOf(*pages) })
	}

	fun selectBranch(index: Int) {
		if (branches.size < 2) {
			throw TwoBranchesRequiredException()
		}
		if (branches[index] != selectedBranch) {
			selectedBranch = branches[index]
			completed = true
			onBranchSelected?.let { it(this) }
		}
	}

	class TwoBranchesRequiredException : RuntimeException()
}
