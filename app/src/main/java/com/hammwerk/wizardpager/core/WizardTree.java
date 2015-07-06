package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public class WizardTree {
	private final Branch trunk;
	private WizardTreeListener listener;

	public WizardTree(Page... pages) {
		this.trunk = new Branch(pages);
		BranchPageListener branchPageListener = new BranchPageListener() {
			@Override
			public void onBranchChoosen(BranchPage branchPage) {
				if (listener != null) {
					listener.onTreeChanged(getPositionOfPage(branchPage) + 1);
				}
			}
		};
		BranchPage branchPage = trunk.getBranchPage();
		if (branchPage != null) {
			branchPage.setListener(branchPageListener);
		}
	}

	public void setListener(WizardTreeListener listener) {
		this.listener = listener;
	}

	protected Page getPage(int position) {
		int positionInBranch = position;
		Branch branch = trunk;
		while (positionInBranch >= branch.getNumberOfPages()) {
			if (branch.getBranchPage() == null || branch.getBranchPage().getChoosenBranch() == null) {
				throw new PageIndexOutOfBoundsException();
			}
			positionInBranch -= branch.getNumberOfPages();
			branch = branch.getBranchPage().getChoosenBranch();
		}
		return branch.getPage(positionInBranch);
	}

	public int getPositionOfPage(Page page) {
		int position = 0;
		Branch branch = trunk;
		do {
			for (Page i : branch.getPages()) {
				if (i.equals(page)) {
					return position;
				}
				position++;
			}
		} while (branch.getBranchPage() != null && (branch = branch.getBranchPage().getChoosenBranch()) != null);
		return -1;
	}

	public int getPositionOfPageFragment(Fragment fragment) {
		int position = 0;
		Branch branch = trunk;
		do {
			for (Page i : branch.getPages()) {
				if (i.getFragment().equals(fragment)) {
					return position;
				}
				position++;
			}
		} while (branch.getBranchPage() != null && (branch = branch.getBranchPage().getChoosenBranch()) != null);
		return -1;
	}

	protected int getKnownNumberOfPages() {
		int numberOfPages = trunk.getNumberOfPages();
		Branch branch = trunk;
		while (branch.getBranchPage() != null && branch.getBranchPage().getChoosenBranch() != null) {
			branch = branch.getBranchPage().getChoosenBranch();
			numberOfPages += branch.getNumberOfPages();
		}
		return numberOfPages;
	}

	public static class PageIndexOutOfBoundsException extends RuntimeException {
	}
}
