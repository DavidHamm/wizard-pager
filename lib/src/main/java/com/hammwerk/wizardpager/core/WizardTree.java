package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public class WizardTree {
	private final Branch trunk;
	private final PageListener pageListener;
	private WizardTreeListener listener;
	private WizardTreePageFinishedListener outListener;

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

	public void setListener(WizardTreeListener listener) {
		this.listener = listener;
	}

	public void setWizardTreePageFinishedListener(WizardTreePageFinishedListener outListener) {
		this.outListener = outListener;
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

	private class MyBranchPageListener implements BranchPageListener {
		@Override
		public void onBranchChoosen(BranchPage branchPage) {
			Branch choosenBranch = branchPage.getChoosenBranch();
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
		public void onPageFinished(Page page) {
			if (outListener != null) {
				outListener.onPageFinished(page, getPositionOfPage(page));
			}
		}
	}
}
