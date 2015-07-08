package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class PageTest {
	public abstract static class GivenAPage {
		protected Page page;

		@Before
		public void givenAPage() throws Exception {
			page = new TestPage("Page");
		}
	}

	public static class GivenPageAndNoPageListener extends GivenAPage {
		@Test
		public void titleShouldBeSetProperly() throws Exception {
			assertEquals("Page", page.getTitle());
		}

		@Test
		public void whenAskForValidity_thenReturnTrue() throws Exception {
			assertTrue(page.isValid());
		}
	}

	public static class GivenPageAndPageListener extends GivenAPage {
		private PageListener pageListener;

		@Before
		public void givenPageListener() throws Exception {
			pageListener = mock(PageListener.class);
			page.setPageListener(pageListener);
		}

		@Test
		public void whenSetCompleted_thenDontCallOnPageValidCallback() throws Exception {
			page.setCompleted();
			verify(pageListener, times(0)).onPageValid(page);
		}

		@Test
		public void whenSetNotCompleted_thenDontCallOnPageInvalidCallback() throws Exception {
			page.setNotCompleted();
			verify(pageListener, times(0)).onPageInvalid(page);
		}
	}

	public abstract static class GivenARequiredPage {
		protected TestPage requiredPage;

		@Before
		public void givenRequiredPage() throws Exception {
			requiredPage = new TestPage("Required Page", true);
		}
	}

	public static class GivenRequiredPageAndNoPageListener extends GivenARequiredPage {
		@Test
		public void requiredPageShouldBeInvalidWhenCreated() throws Exception {
			assertFalse(requiredPage.isValid());
		}

		@Test
		public void whenSetCompleted_thenPageIsValid() throws Exception {
			requiredPage.setCompleted();
			assertTrue(requiredPage.isValid());
		}

		@Test
		public void whenSetCompletedAfterItWasCompleted_thenPageIsInvalid() throws Exception {
			requiredPage.setCompleted();
			requiredPage.setNotCompleted();
			assertFalse(requiredPage.isValid());
		}
	}

	public static class GivenRequiredPageAndPageListener extends GivenARequiredPage {
		private PageListener pageListener;

		@Before
		public void givenPageListener() throws Exception {
			pageListener = mock(PageListener.class);
			requiredPage.setPageListener(pageListener);
		}

		@Test
		public void whenSetCompleted_thenCallOnPageValidCallback() throws Exception {
			requiredPage.setCompleted();
			verify(pageListener, times(1)).onPageValid(requiredPage);
		}

		@Test
		public void whenSetNotCompleted_thenCallOnPageInvalidCallback() throws Exception {
			requiredPage.setNotCompleted();
			verify(pageListener, times(1)).onPageInvalid(requiredPage);
		}
	}
}