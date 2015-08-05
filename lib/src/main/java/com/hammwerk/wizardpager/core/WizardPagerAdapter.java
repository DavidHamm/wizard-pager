package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WizardPagerAdapter extends FragmentStatePagerAdapter {
	private WizardTree wizardTree;
	private int lastChangedPageIndex;

	public WizardPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public WizardPagerAdapter setWizardTree(WizardTree wizardTree) {
		this.wizardTree = wizardTree;
		if (wizardTree != null) {
			this.wizardTree.setWizardTreeListener(new MyWizardTreeListener());
		}
		return this;
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
		return wizardTree != null ? wizardTree.getNumberOfAccessablePages() : 0;
	}

	public boolean isPageValid(int position) {
		return wizardTree.getPage(position).isValid();
	}

	private class MyWizardTreeListener implements WizardTreeListener {
		@Override
		public void onTreeChanged(int pageIndex) {
			lastChangedPageIndex = pageIndex;
			notifyDataSetChanged();
		}
	}
}
