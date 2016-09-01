package com.hammwerk.wizardpager

import com.hammwerk.wizardpager.core.BranchPage

class SingleFixedChoiceBranchPage(title: String) :
		BranchPage(title, SingleFixedChoiceBranchPageFragment::class.qualifiedName!!)
