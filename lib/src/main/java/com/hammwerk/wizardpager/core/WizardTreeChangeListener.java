package com.hammwerk.wizardpager.core;

public interface WizardTreeChangeListener {
	void onTreeChanged(int pageIndex);

	void onPageValid(Page page, int pageIndex);

	void onPageInvalid(Page page, int pageIndex);
}
