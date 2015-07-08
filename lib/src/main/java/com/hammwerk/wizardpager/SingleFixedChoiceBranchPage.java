package com.hammwerk.wizardpager;

import android.support.v4.app.Fragment;

import com.hammwerk.wizardpager.core.Branch;
import com.hammwerk.wizardpager.core.BranchPage;

public class SingleFixedChoiceBranchPage extends BranchPage {
	public SingleFixedChoiceBranchPage(String title, Branch... branches) {
		super(title, branches);
	}

	@Override
	protected Fragment createFragment() {
		return SingleFixedChoiceBranchPageFragment.createInstance(this, getChoices());
	}
}
