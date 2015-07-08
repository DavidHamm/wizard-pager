package com.hammwerk.wizardpager.core;

public interface PageValidityListener {
	void onPageValid(Page page, int pageIndex);

	void onPageInvalid(Page page, int pageIndex);
}
