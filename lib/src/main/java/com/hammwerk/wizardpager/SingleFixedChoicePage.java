package com.hammwerk.wizardpager;

import android.support.v4.app.Fragment;

import com.hammwerk.wizardpager.core.Page;

public class SingleFixedChoicePage extends Page<Integer> {
	private final String[] choices;

	public SingleFixedChoicePage(String title, String... choices) {
		this(title, true, choices);
	}

	public SingleFixedChoicePage(String title, boolean required, String... choices) {
		super(title, required);
		this.choices = choices;
	}

	@Override
	protected Fragment createFragment() {
		return SingleFixedChoicePageFragment.createInstance(this, choices);
	}
}
