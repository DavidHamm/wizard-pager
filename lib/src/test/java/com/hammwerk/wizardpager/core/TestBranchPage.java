package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public class TestBranchPage extends BranchPage {
	public TestBranchPage(String title) {
		super(title);
	}

	@Override
	protected Fragment createFragment() {
		return new Fragment();
	}
}
