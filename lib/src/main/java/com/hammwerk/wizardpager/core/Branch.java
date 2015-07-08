package com.hammwerk.wizardpager.core;

import java.util.Arrays;
import java.util.List;

public class Branch {
	private final List<Page> pages;
	private final String name;

	public Branch(Page... pages) {
		this(null, pages);
	}

	public Branch(String name, Page... pages) {
		for (int i = 0; i < pages.length - 1; i++) {
			if (pages[i] instanceof BranchPage) {
				throw new BranchPageBeforeLastPageException();
			}
		}
		this.pages = Arrays.asList(pages);
		this.name = name;
	}

	public Page getPage(int position) {
		return pages.get(position);
	}

	public int getNumberOfPages() {
		return pages.size();
	}

	public BranchPage getBranchPage() {
		if (!pages.isEmpty()) {
			Page page = pages.get(pages.size() - 1);
			if (page instanceof BranchPage) {
				return (BranchPage) page;
			}
		}
		return null;
	}

	public List<Page> getPages() {
		return pages;
	}

	public String getName() {
		return name;
	}

	public class BranchPageBeforeLastPageException extends RuntimeException {
	}
}
