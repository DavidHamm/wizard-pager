package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class WizardPagerAdapter extends FragmentStatePagerAdapter {
	private final WizardTree wizardTree;
	private int lastChangedPageIndex;

	public WizardPagerAdapter(FragmentManager fm, WizardTree wizardTree) {
		super(fm);
		this.wizardTree = wizardTree;
		wizardTree.setListener(new WizardTreeListener() {
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
		return wizardTree.getKnownNumberOfPages();
	}

	public boolean isPageFinished(int position) {
		return wizardTree.getPage(position).isFinished();
	}
}
