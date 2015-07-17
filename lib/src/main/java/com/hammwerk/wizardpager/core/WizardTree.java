package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

public class WizardTree {
	private final Branch trunk;
	private final PageListener pageListener;
	private WizardTreeListener wizardTreeListener;
	private WizardTreeChangeListener wizardTreeChangeListener;

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

	void setWizardTreeListener(WizardTreeListener wizardTreeListener) {
		this.wizardTreeListener = wizardTreeListener;
	}

	public void setWizardTreeChangeListener(WizardTreeChangeListener wizardTreeChangeListener) {
		this.wizardTreeChangeListener = wizardTreeChangeListener;
	}

	public <T extends Page> T getPage(int position) {
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

	protected int getNumberOfAccessablePages() {
		int numberOfValidPages = 0;
		Branch branch = trunk;
		while (branch != null) {
			int numberOfValidPagesInBranch = branch.getNumberOfValidPages();
			numberOfValidPages += numberOfValidPagesInBranch +
					(numberOfValidPagesInBranch < branch.getNumberOfPages() ? 1 : 0);
			branch = getNextBranch(branch);
		}
		return numberOfValidPages;
	}

	private Branch getNextBranch(Branch branch) {
		BranchPage branchPage = branch.getBranchPage();
		if (branchPage != null) {
			return branchPage.getSelectedBranch();
		}
		return null;
	}

	public boolean isLastPage(Page page) {
		int positionOfPage = getPositionOfPage(page);
		if (positionOfPage == -1) {
			return false;
		}
		Branch branch = trunk;
		while (branch != null) {
			BranchPage branchPage = branch.getBranchPage();
			if (branchPage == null ||
					(branchPage.getSelectedBranch() != null &&
							branchPage.getSelectedBranch().getNumberOfPages() == 0)) {
				return branch.getPage(branch.getPages().size() - 1).equals(page);
			}
			branch = getNextBranch(branch);
		}
		return false;
	}

	public static class PageIndexOutOfBoundsException extends RuntimeException {
	}

	private class MyBranchPageListener implements BranchPageListener {
		@Override
		public void onBranchSelected(BranchPage branchPage) {
			Branch selectedBranch = branchPage.getSelectedBranch();
			for (Page i : selectedBranch.getPages()) {
				i.setPageListener(pageListener);
			}
			BranchPage nextBranchPage = selectedBranch.getBranchPage();
			if (nextBranchPage != null) {
				nextBranchPage.setBranchPageListener(this);
			}
		}
	}

	private class MyPageListener implements PageListener {
		@Override
		public void onPageValid(Page page) {
			if (wizardTreeListener != null) {
				wizardTreeListener.onTreeChanged(getPositionOfPage(page) + 1);
			}
			if (wizardTreeChangeListener != null) {
				wizardTreeChangeListener.onTreeChanged(getPositionOfPage(page) + 1);
				wizardTreeChangeListener.onPageValid(page, getPositionOfPage(page));
			}
		}

		@Override
		public void onPageInvalid(Page page) {
			if (wizardTreeChangeListener != null) {
				wizardTreeChangeListener.onPageInvalid(page, getPositionOfPage(page));
			}
		}
	}
}
