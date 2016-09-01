package com.hammwerk.wizardpager.core

import de.bechte.junit.runners.context.HierarchicalContextRunner
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("UNUSED")
@RunWith(HierarchicalContextRunner::class)
class BranchTest {
	inner class GivenABranch {
		private lateinit var branch: Branch

		@Before
		fun setUp() {
			branch = Branch()
		}

		@Test
		fun whenGetName_thenReturnNull() {
			assertNull(branch.name)
		}

		@Test(expected = Branch.PageAfterBranchPageException::class)
		fun whenSetPagesWithPageAfterBranchPage_thenThrowPageAfterBranchPageException() {
			branch.pages = arrayOf(BranchPage("Branch Page", "FragmentName"), TestPage("Page"))
		}

		@Test(expected = PageIndexOutOfBoundsException::class)
		fun whenGetPage_thenThrowPageIndexOutOfBoundsException() {
			branch.getPage<Page>(0)
		}

		@Test
		fun whenGetNumberOfPages_thenReturnZero() {
			assertEquals(0, branch.numberOfPages)
		}

		@Test
		fun whenGetBranchPage_thenReturnNull() {
			assertNull(branch.branchPage)
		}

		@Test
		fun whenGetNumberOfValidPages_thenReturnZero() {
			assertEquals(0, branch.numberOfValidPages)
		}

		inner class GivenTowPages {
			private lateinit var firstPage: Page
			private lateinit var secondPage: Page

			@Before
			fun setUp() {
				firstPage = TestPage("First Page")
				secondPage = TestPage("Second Page")
				branch.pages = arrayOf(firstPage, secondPage)
			}

			@Test
			fun whenGetFirstPage_thenReturnFirstPage() {
				assertEquals(firstPage, branch.getPage<Page>(0))
			}

			@Test(expected = PageIndexOutOfBoundsException::class)
			fun whenGetPageAfterLastPage_thenThrowPageIndexOutOfBoundsException() {
				branch.getPage<Page>(2)
			}

			@Test
			fun whenGetBranchPage_thenReturnNull() {
				assertNull(branch.branchPage)
			}

			@Test
			fun whenGetNumberOfValidPages_thenReturnTwo() {
				assertEquals(2, branch.numberOfValidPages)
			}
		}

		inner class GivenTwoPagesAndBranchPage {
			private lateinit var firstPage: Page
			private lateinit var secondPage: Page
			private lateinit var branchPage: BranchPage
			private lateinit var pageInFirstBranch: Page
			private lateinit var pageInSecondBranch: Page

			@Before
			fun setUp() {
				firstPage = TestPage("First Page")
				secondPage = TestPage("Second Page")
				pageInFirstBranch = TestPage("Page in first Branch")
				pageInSecondBranch = TestPage("Page in second Branch")
				branchPage = BranchPage("Title", "FragmentName").apply {
					addBranch("First Branch", pageInFirstBranch)
					addBranch("Second Branch", pageInSecondBranch)
				}
				branch.pages = arrayOf(firstPage, secondPage, branchPage)
			}

			@Test
			fun whenGetNumberOfPages_thenReturnNumberOfPages() {
				assertEquals(3, branch.numberOfPages)
			}

			@Test
			fun whenGetBranchPage_thenReturnBranchPage() {
				assertEquals(branchPage, branch.branchPage)
			}
		}

		inner class GivenARequiredPage {
			private lateinit var requiredPage: Page

			@Before
			fun setUp() {
				requiredPage = TestPage("Required Page", true)
				branch.pages = arrayOf(requiredPage)
			}

			@Test
			fun whenGetNumberOfValidPages_thenReturnZero() {
				assertEquals(0, branch.numberOfValidPages)
			}

			inner class GivenCompletedRequiredPage {
				@Before
				fun setUp() {
					requiredPage.completed = true
				}

				@Test
				fun whenGetNumberOfValidPages_thenReturnOne() {
					assertEquals(1, branch.numberOfValidPages)
				}
			}
		}

		inner class GivenTwoRequiredPages {
			private lateinit var firstRequiredPage: Page
			private lateinit var secondRequiredPage: Page

			@Before
			fun setUp() {
				firstRequiredPage = TestPage("First required Page", true)
				secondRequiredPage = TestPage("Second required Page", true)
				branch.pages = arrayOf(firstRequiredPage, secondRequiredPage)
			}

			@Test
			fun whenGetNumberOfValidPages_thenReturnOne() {
				assertEquals(0, branch.numberOfValidPages)
			}

			inner class GivenCompletedFirstRequiredPage {
				@Before
				fun setUp() {
					firstRequiredPage.completed = true
				}

				@Test
				fun whenGetNumberOfValidPages_thenReturnOne() {
					assertEquals(1, branch.numberOfValidPages)
				}
			}

			inner class GivenCompletedAllRequiredPage {
				@Before
				fun setUp() {
					firstRequiredPage.completed = true
					secondRequiredPage.completed = true
				}

				@Test
				fun whenGetNumberOfValidPages_thenReturnOne() {
					assertEquals(2, branch.numberOfValidPages)
				}
			}
		}
	}

	inner class GivenABranchWithName {
		private lateinit var branchWithName: Branch

		@Before
		fun setUp() {
			branchWithName = Branch("Name")
		}

		@Test
		fun whenGetName_thenReturnName() {
			assertEquals("Name", branchWithName.name)
		}
	}
}
