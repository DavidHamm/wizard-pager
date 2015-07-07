package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class PageTest {
	private Page page;

	@Before
	public void setUp() throws Exception {
		page = new TestPage("Title");
	}

	@Test
	public void createPage() throws Exception {
		assertEquals("Title", page.getTitle());
	}

	@Test
	public void givenPage_whenFinished_thenCallOnFinishedCallback() throws Exception {
		PageListener pageListener = mock(PageListener.class);
		page.setPageListener(pageListener);
		page.finish();
		verify(pageListener, times(1)).onPageFinished(page);
	}
}