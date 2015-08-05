package com.hammwerk.wizardpager;

import android.support.v4.app.Fragment;

import com.hammwerk.wizardpager.core.Page;

import java.util.List;

public class MultiFixedChoicePage extends Page<List<Integer>> {
	private String[] choices;

	public MultiFixedChoicePage(String title) {
		this(title, false);
	}

	public MultiFixedChoicePage(String title, boolean required) {
		super(title, required);
	}

	@Override
	protected Fragment createFragment() {
		return MultiFixedChoicePageFragment.createInstance(this, choices);
	}

	public MultiFixedChoicePage setChoices(String... choices) {
		this.choices = choices;
		return this;
	}
}
