package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public class WizardTree {
	private final Branch trunk;
	private final PageListener pageListener;
	private WizardTreeListener listener;
	private PageValidityListener pageValidityListener;

	public WizardTree(Page... pages) {
		this.trunk = new Branch(pages);
		BranchPageListener branchPageListener = new MyBranchPageListener();
		BranchPage branchPage = trunk.getBranchPage();
		if (branchPage != null) {
			branchPage.setBranchPageListener(branchPageListener);
		}
		pageListener = new MyPageListener();
		for (Page i : pages) {
			i.setPageListener(pageListener);
		}
	}

	public void setWizardTreeListener(WizardTreeListener listener) {
		this.listener = listener;
	}

	public void setPageValidityListener(PageValidityListener pageValidityListener) {
		this.pageValidityListener = pageValidityListener;
	}

	protected Page getPage(int position) {
		int positionInBranch = position;
		Branch branch = trunk;
		while (positionInBranch >= branch.getNumberOfPages()) {
			if (branch.getBranchPage() == null || branch.getBranchPage().getSelectedBranch() == null) {
				throw new PageIndexOutOfBoundsException();
			}
			positionInBranch -= branch.getNumberOfPages();
			branch = branch.getBranchPage().getSelectedBranch();
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
		} while (branch.getBranchPage() != null && (branch = branch.getBranchPage().getSelectedBranch()) != null);
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
		} while (branch.getBranchPage() != null && (branch = branch.getBranchPage().getSelectedBranch()) != null);
		return -1;
	}

	protected int getKnownNumberOfPages() {
		int numberOfPages = trunk.getNumberOfPages();
		Branch branch = trunk;
		while (branch.getBranchPage() != null && branch.getBranchPage().getSelectedBranch() != null) {
			branch = branch.getBranchPage().getSelectedBranch();
			numberOfPages += branch.getNumberOfPages();
		}
		return numberOfPages;
	}

	public static class PageIndexOutOfBoundsException extends RuntimeException {
	}

	private class MyBranchPageListener implements BranchPageListener {
		@Override
		public void onBranchChoosen(BranchPage branchPage) {
			Branch choosenBranch = branchPage.getSelectedBranch();
			for (Page i : choosenBranch.getPages()) {
				i.setPageListener(pageListener);
			}
			BranchPage nextBranchPage = choosenBranch.getBranchPage();
			if (nextBranchPage != null) {
				nextBranchPage.setBranchPageListener(this);
			}
			if (listener != null) {
				listener.onTreeChanged(getPositionOfPage(branchPage) + 1);
			}
		}
	}

	private class MyPageListener implements PageListener {
		@Override
		public void onPageValid(Page page) {
			if (pageValidityListener != null) {
				pageValidityListener.onPageValid(page, getPositionOfPage(page));
			}
		}

		@Override
		public void onPageInvalid(Page page) {
			if (pageValidityListener != null) {
				pageValidityListener.onPageInvalid(page, getPositionOfPage(page));
			}
		}
	}
}
