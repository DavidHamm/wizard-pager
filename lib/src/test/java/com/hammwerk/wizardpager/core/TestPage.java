package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public class TestPage extends Page<String> {
	public TestPage(String title) {
		super(title);
	}

	public TestPage(String title, boolean required) {
		super(title, required);
	}

	@Override
	protected Fragment createFragment() {
		return new Fragment();
	}
}
