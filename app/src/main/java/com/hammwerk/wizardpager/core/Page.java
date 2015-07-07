package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public abstract class Page {
	private final String title;
	private Fragment fragment;
	private PageListener listener;
	private boolean finished;

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

	public void finish() {
		finished = true;
		if (listener != null) {
			listener.onPageFinished(this);
		}
	}

	public void setPageListener(PageListener listener) {
		this.listener = listener;
	}

	public boolean isFinished() {
		return finished;
	}
}
