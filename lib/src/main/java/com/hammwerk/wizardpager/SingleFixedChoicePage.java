package com.hammwerk.wizardpager;

import android.support.v4.app.Fragment;

import com.hammwerk.wizardpager.core.Page;

public class SingleFixedChoicePage extends Page<Integer> {
	private String[] choices;

	public SingleFixedChoicePage(String title) {
		this(title, true);
	}

	public SingleFixedChoicePage(String title, boolean required) {
		super(title, required);
	}

	@Override
	protected Fragment createFragment() {
		return SingleFixedChoicePageFragment.createInstance(this, choices);
	}

	public SingleFixedChoicePage setChoices(String... choices) {
		this.choices = choices;
		return this;
	}
}
