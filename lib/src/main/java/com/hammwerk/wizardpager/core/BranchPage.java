package com.hammwerk.wizardpager.core;

import java.util.ArrayList;
import java.util.List;

public abstract class BranchPage extends Page<Integer> {
	protected List<Branch> branches = new ArrayList<>();
	private Branch selectedBranch;
	private BranchPageListener branchPageListener;

	public BranchPage(String title) {
		super(title, true);
	}

	public BranchPage addBranch(String branchName, Page... pages) {
		branches.add(new Branch(branchName)
				.setPages(pages));
		return this;
	}

	public void selectBranch(int index) {
		if (branches.size() < 2) {
			throw new TwoBranchesRequiredException();
		}
		if (!branches.get(index).equals(selectedBranch)) {
			selectedBranch = branches.get(index);
			setResult(index);
			setCompleted();
			if (branchPageListener != null) {
				branchPageListener.onBranchSelected(this);
			}
		}
	}

	public Branch getSelectedBranch() {
		if (branches.size() < 2) {
			throw new TwoBranchesRequiredException();
		}
		return selectedBranch;
	}

	public String[] getChoices() {
		if (branches.size() < 2) {
			throw new TwoBranchesRequiredException();
		}
		String[] choices = new String[branches.size()];
		for (int i = 0; i < branches.size(); i++) {
			choices[i] = branches.get(i).getName();
		}
		return choices;
	}

	void setBranchPageListener(BranchPageListener listener) {
		this.branchPageListener = listener;
	}

	public class TwoBranchesRequiredException extends RuntimeException {
	}
}
