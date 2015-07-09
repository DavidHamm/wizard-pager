package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WizardPagerAdapter extends FragmentStatePagerAdapter {
	private final WizardTree wizardTree;
	private int lastChangedPageIndex;

	public WizardPagerAdapter(FragmentManager fm, WizardTree wizardTree) {
		super(fm);
		if (wizardTree == null) {
			throw new WizardTreeIsNullException();
		}
		this.wizardTree = wizardTree;
		this.wizardTree.setWizardTreeListener(new WizardTreeListener() {
			@Override
			public void onTreeChanged(int pageIndex) {
				lastChangedPageIndex = pageIndex;
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public int getItemPosition(Object object) {
		int positionOfPageFragment = wizardTree.getPositionOfPageFragment((Fragment) object);
		if (positionOfPageFragment != -1 && positionOfPageFragment < lastChangedPageIndex) {
			return POSITION_UNCHANGED;
		}
		return POSITION_NONE;
	}

	@Override
	public Fragment getItem(int position) {
		Page page = wizardTree.getPage(position);
		return page.getFragment();
	}

	@Override
	public int getCount() {
		return wizardTree.getNumberOfAccessablePages();
	}

	public boolean isPageValid(int position) {
		return wizardTree.getPage(position).isValid();
	}

	public static class WizardTreeIsNullException extends RuntimeException {
	}
}
