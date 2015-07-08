package com.hammwerk.wizardpager;

import android.support.v4.app.Fragment;

import com.hammwerk.wizardpager.core.Page;

public class MultiFixedChoicePage extends Page {
	private final String[] choices;

	public MultiFixedChoicePage(String title, String... choices) {
		this(title, false, choices);
	}

	public MultiFixedChoicePage(String title, boolean required, String... choices) {
		super(title, required);
		this.choices = choices;
	}

	@Override
	protected Fragment createFragment() {
		return MultiFixedChoicePageFragment.createInstance(this, choices);
	}
}
