package com.hammwerk.wizardpager;

import android.support.v4.app.Fragment;

import com.hammwerk.wizardpager.core.Page;

public class IntegerPage extends Page<Integer> {
	private final String unit;

	public IntegerPage(String title, String unit) {
		this(title, true, unit);
	}

	public IntegerPage(String title, boolean required, String unit) {
		super(title, required);
		this.unit = unit;
	}

	@Override
	protected Fragment createFragment() {
		return IntegerPageFragment.createInstance(this, unit);
	}
}
