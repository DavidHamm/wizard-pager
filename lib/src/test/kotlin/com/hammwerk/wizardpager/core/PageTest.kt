package com.hammwerk.wizardpager.core

import de.bechte.junit.runners.context.HierarchicalContextRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

@RunWith(HierarchicalContextRunner::class)
class PageTest {
	inner class GivenAPage {
		private lateinit var page: Page

		@Before
		fun setUp() {
			page = TestPage("Page")
		}

		@Test
		fun whenGetTitle_ThenReturnTitle() {
			assertEquals("Page", page.title)
		}

		@Test
		fun whenGetFragmentName_ThenReturnFragmentName() {
			assertEquals("FragmentName", page.fragmentName)
		}

		@Test
		fun whenAskForValidity_thenReturnTrue() {
			assertTrue(page.valid)
		}

		inner class GivenAnOnPageValidCallback {
			@Before
			fun setUp() {
				page.onPageValid = mock()
			}

			@Test
			fun whenSetCompleted_thenDontCallOnPageValidCallback() {
				page.completed = true
				verify(page.onPageValid, never())!!.invoke(page)
			}
		}

		inner class GivenAnOnPageInvalidCallback {
			@Before
			fun setUp() {
				page.onPageInvalid = mock()
			}

			@Test
			fun whenSetNotCompleted_thenDontCallOnPageInvalidCallback() {
				page.completed = false
				verify(page.onPageInvalid, never())!!.invoke(page)
			}
		}
	}

	inner class GivenARequiredPage {
		private lateinit var requiredPage: Page

		@Before
		fun setUp() {
			requiredPage = TestPage("Required Page", true)
		}

		@Test
		fun requiredPageShouldBeInvalidWhenCreated() {
			assertFalse(requiredPage.valid)
		}

		@Test
		fun whenSetCompleted_thenPageIsValid() {
			requiredPage.completed = true
			assertTrue(requiredPage.valid)
		}

		@Test
		fun whenSetNotCompletedAfterItWasCompleted_thenPageIsInvalid() {
			requiredPage.completed = true
			requiredPage.completed = false
			assertFalse(requiredPage.valid)
		}

		inner class GivenAnOnPageValidCallback {
			@Before
			fun setUp() {
				requiredPage.onPageValid = mock()
			}

			@Test
			fun whenSetCompleted_thenCallOnPageValidCallback() {
				requiredPage.completed = true
				verify(requiredPage.onPageValid)!!.invoke(requiredPage)
			}
		}

		inner class GivenAnOnPageInvalidCallback {
			@Before
			fun setUp() {
				requiredPage.onPageInvalid = mock()
			}

			@Test
			fun whenSetNotCompleted_thenCallOnPageInvalidCallback() {
				requiredPage.completed = false
				verify(requiredPage.onPageInvalid)!!.invoke(requiredPage)
			}
		}
	}
}