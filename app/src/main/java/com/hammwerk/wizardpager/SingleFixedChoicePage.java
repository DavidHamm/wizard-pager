package com.hammwerk.wizardpager;

import android.support.v4.app.Fragment;

import com.hammwerk.wizardpager.core.Page;

public class SingleFixedChoicePage extends Page {
	private final String[] choices;

	public SingleFixedChoicePage(String title, String... choices) {
		super(title);
		this.choices = choices;
	}

	@Override
	protected Fragment createFragment() {
		return SingleFixedChoicePageFragment.createInstance(this, choices);
	}
}
