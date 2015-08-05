package com.hammwerk.wizardpager.core;

import java.util.Arrays;
import java.util.List;

class Branch {
	private final String name;
	private List<Page> pages;

	public Branch() {
		this(null);
	}

	public Branch(String name) {
		this.name = name;
	}

	@SuppressWarnings("unchecked")
	public <T extends Page> T getPage(int position) {
		if (pages == null || position >= pages.size()) {
			throw new PageIndexOutOfBoundsException();
		}
		return (T) pages.get(position);
	}

	public int getNumberOfPages() {
		return pages != null ? pages.size() : 0;
	}

	public int getNumberOfValidPages() {
		if (pages == null) {
			return 0;
		}
		for (int i = 0; i < pages.size(); i++) {
			if (!pages.get(i).isValid()) {
				return i;
			}
		}
		return pages.size();
	}

	public BranchPage getBranchPage() {
		if (pages != null && !pages.isEmpty()) {
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

	public Branch setPages(Page... pages) {
		for (int i = 0; i < pages.length - 1; i++) {
			if (pages[i] instanceof BranchPage) {
				throw new PageAfterBranchPageException();
			}
		}
		this.pages = Arrays.asList(pages);
		return this;
	}

	public String getName() {
		return name;
	}

	public class PageAfterBranchPageException extends RuntimeException {
	}
}
