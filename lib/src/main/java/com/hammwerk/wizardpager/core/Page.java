package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public abstract class Page<T> {
	private final String title;
	private final boolean required;
	private Fragment fragment;
	private PageListener listener;
	private boolean completed;
	private T result;

	public Page(String title) {
		this(title, false);
	}

	public Page(String title, boolean required) {
		this.title = title;
		this.required = required;
	}

	public String getTitle() {
		return title;
	}

	public Fragment getFragment() {
		if (fragment == null) {
			fragment = createFragment();
		}
		return fragment;
	}

	public void setCompleted() {
		completed = true;
		if (listener != null && required) {
			listener.onPageValid(this);
		}
	}

	public void setNotCompleted() {
		completed = false;
		if (listener != null && required) {
			listener.onPageInvalid(this);
		}
	}

	public boolean isValid() {
		return !required | completed;
	}

	public void setPageListener(PageListener listener) {
		this.listener = listener;
	}

	public final T getResult() {
		return result;
	}

	public final void setResult(T result) {
		this.result = result;
	}

	protected abstract Fragment createFragment();
}
