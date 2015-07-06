package com.hammwerk.wizardpager.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PageTest {
	@Test
	public void createPage() throws Exception {
		Page page = new TestPage("Title");
		assertEquals("Title", page.getTitle());
	}
}