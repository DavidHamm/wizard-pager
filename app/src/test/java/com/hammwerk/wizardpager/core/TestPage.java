package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public class TestPage extends Page {
	public TestPage(String title) {
		super(title);
	}

	@Override
	protected Fragment createFragment() {
		return new Fragment();
	}
}
