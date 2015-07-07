package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public abstract class Page {
	private final String title;
	private Fragment fragment;

	public Page(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	protected abstract Fragment createFragment();

	public Fragment getFragment() {
		if (fragment == null) {
			fragment = createFragment();
		}
		return fragment;
	}
}
