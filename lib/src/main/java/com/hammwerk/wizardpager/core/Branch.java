package com.hammwerk.wizardpager.core;

import java.util.Arrays;
import java.util.List;

class Branch {
	private final List<Page> pages;
	private final String name;

	public Branch(Page... pages) {
		this(null, pages);
	}

	public Branch(String name, Page... pages) {
		for (int i = 0; i < pages.length - 1; i++) {
			if (pages[i] instanceof BranchPage) {
				throw new PageAfterBranchPageException();
			}
		}
		this.pages = Arrays.asList(pages);
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public <T extends Page> T getPage(int position) {
		return (T) pages.get(position);
	}

	public int getNumberOfPages() {
		return pages.size();
	}

	public int getNumberOfValidPages() {
		for (int i = 0; i < pages.size(); i++) {
			if (!pages.get(i).isValid()) {
				return i;
			}
		}
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

	public class PageAfterBranchPageException extends RuntimeException {
	}
}
