package com.hammwerk.wizardpager.core;

import java.util.Arrays;
import java.util.List;

public abstract class BranchPage extends Page {
	private final List<Branch> branches;
	private Branch choosenBranch;
	private BranchPageListener listener;

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
			if (listener != null) {
				listener.onBranchChoosen(this);
			}
		}
	}

	public Branch getChoosenBranch() {
		return choosenBranch;
	}

	public void setListener(BranchPageListener listener) {
		this.listener = listener;
	}

	public class LessThenTwoBranchesException extends RuntimeException {
	}
}
