package com.hammwerk.wizardpager.core;

import java.util.Arrays;
import java.util.List;

public abstract class BranchPage extends Page {
	protected final List<Branch> branches;
	private Branch choosenBranch;
	private BranchPageListener branchPageListener;

	public BranchPage(String title, Branch... branches) {
		super(title);
		if (branches.length < 2) {
			throw new LessThenTwoBranchesException();
		}
		this.branches = Arrays.asList(branches);
	}

	public void chooseBranch(int index) {
		if (!branches.get(index).equals(choosenBranch)) {
			choosenBranch = branches.get(index);
			if (branchPageListener != null) {
				branchPageListener.onBranchChoosen(this);
			}
			finish();
		}
	}

	public Branch getChoosenBranch() {
		return choosenBranch;
	}

	public String[] getChoices() {
		String[] choices = new String[branches.size()];
		for (int i = 0; i < branches.size(); i++) {
			choices[i] = branches.get(i).getName();
		}
		return choices;
	}

	public void setBranchPageListener(BranchPageListener listener) {
		this.branchPageListener = listener;
	}

	public class LessThenTwoBranchesException extends RuntimeException {
	}
}
