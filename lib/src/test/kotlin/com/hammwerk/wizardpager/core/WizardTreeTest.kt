package com.hammwerk.wizardpager.core

import de.bechte.junit.runners.context.HierarchicalContextRunner
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.never
import org.mockito.Mockito.verify

@Suppress("UNUSED")
@RunWith(HierarchicalContextRunner::class)
class WizardTreeTest {
	inner class GivenAWizardTree {
		private lateinit var wizardTree: WizardTree

		@Before
		fun setUp() {
			wizardTree = WizardTree()
		}

		@Test(expected = PageIndexOutOfBoundsException::class)
		fun whenGetFirstPage_thenThrowPageIndexOutOfBoundsException() {
			wizardTree.getPage<Page>(0)
		}

		@Test
		fun whenGetNumberOfAccessablePages_thenReturnZero() {
			assertEquals(0, wizardTree.numberOfAccessablePages)
		}

		@Test
		fun whenGetPositionOfInvalidPage_thenReturnMinusOne() {
			assertEquals(-1, wizardTree.getPositionOfPage(TestPage("Invalid Page")))
		}

		@Test
		fun whenCallIsLastPageWithInvalidPage_thenReturnFalse() {
			assertFalse(wizardTree.isLastPage(TestPage("Invalid Page")))
		}

		inner class GivenAPage {
			private lateinit var page: Page

			@Before
			fun setUp() {
				page = TestPage("Page")
				wizardTree.setPages(page)
			}

			@Test
			fun whenGetFirstPage_thenReturnFirstPage() {
				assertEquals(page, wizardTree.getPage<Page>(0))
			}

			@Test
			fun whenGetNumberOfAccessablePages_thenReturnNumberOfPages() {
				assertEquals(1, wizardTree.numberOfAccessablePages)
			}

			@Test
			fun whenGetPositionOfInvalidPage_thenReturnMinusOne() {
				assertEquals(-1, wizardTree.getPositionOfPage(TestPage("Invalid Page")))
			}

			@Test
			fun whenCallIsLastPageWithPage_thenReturnTrue() {
				assertTrue(wizardTree.isLastPage(page))
			}

			@Test
			fun whenCallIsLastPageWithInvalidPage_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(TestPage("Invalid Page")))
			}

			inner class GivenAnOnPageValidCallback {
				@Before
				fun setUp() {
					wizardTree.onPageValid = mock()
				}

				@Test
				fun whenPageCompleted_thenCallOnPageValidCallback() {
					page.completed = true
					verify(wizardTree.onPageValid, never())!!.invoke(page, 0)
				}
			}
		}

		inner class GivenATrunkAndTwoEmptyBranches {
			private lateinit var pageInTrunkBranch: Page
			private lateinit var branchPageInTrunkBranch: BranchPage

			@Before
			fun setUp() {
				pageInTrunkBranch = TestPage("Page in Trunk Branch")
				branchPageInTrunkBranch = BranchPage("Branch Page in Trunk Branch", "FragmentName").apply {
					addBranch("First Branch")
					addBranch("Second Branch")
				}
				wizardTree.setPages(pageInTrunkBranch, branchPageInTrunkBranch)
			}

			@Test
			fun whenGetBranchPage_thenReturnBranchPage() {
				assertEquals(branchPageInTrunkBranch, wizardTree.getPage<Page>(1))
			}

			@Test
			fun whenGetNumberOfAccessablePages_thenReturnOne() {
				assertEquals(2, wizardTree.numberOfAccessablePages)
			}

			@Test
			fun whenGetPositionOfBranchPage_thenReturnPositionOfBranchPage() {
				assertEquals(1, wizardTree.getPositionOfPage(branchPageInTrunkBranch))
			}

			@Test
			fun whenCallIsLastPageWithFirstPage_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(pageInTrunkBranch))
			}

			@Test
			fun whenCallIsLastPageWithBranchPage_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch))
			}

			inner class GivenASelectedBranch {
				@Before
				fun setUp() {
					branchPageInTrunkBranch.selectBranch(0)
				}

				@Test
				fun whenCallIsLastPageWithBranchPage_thenReturnTrue() {
					assertTrue(wizardTree.isLastPage(branchPageInTrunkBranch))
				}
			}
		}

		inner class GivenATrunkAndTwoBranches {
			private lateinit var pageInTrunkBranch: Page
			private lateinit var branchPageInTrunkBranch: BranchPage
			private lateinit var pageInFirstBranch: Page

			@Before
			fun setUp() {
				pageInTrunkBranch = TestPage("Page in Trunk Branch")
				pageInFirstBranch = TestPage("Page in first Branch")
				branchPageInTrunkBranch = BranchPage("Branch Page in Trunk Branch", "FragmentName").apply {
					addBranch("First Branch", pageInFirstBranch)
					addBranch("Second Branch", TestPage("Page in second Branch"))
				}
				wizardTree.setPages(pageInTrunkBranch, branchPageInTrunkBranch)
			}

			@Test(expected = PageIndexOutOfBoundsException::class)
			fun whenGetPageAfterBranchPage_thenThrowException() {
				wizardTree.getPage<Page>(2)
			}

			@Test
			fun whenGetNumberOfAccessablePages_thenReturnNumberOfPagesInclBranchPage() {
				assertEquals(2, wizardTree.numberOfAccessablePages)
			}

			@Test
			fun whenGetPositionOfPageAfterBranchPage_thenReturnMinusOne() {
				assertEquals(-1, wizardTree.getPositionOfPage(pageInFirstBranch))
			}

			@Test
			fun whenCallIsLastPageWithFirstPage_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(pageInTrunkBranch))
			}

			@Test
			fun whenCallIsLastPageWithBranchPage_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch))
			}

			@Test
			fun whenCallIsLastPageWithPageInFirstBranch_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(pageInFirstBranch))
			}

			inner class GivenASelectedBranch {
				@Before
				fun setUp() {
					branchPageInTrunkBranch.selectBranch(0)
				}

				@Test
				fun whenGetPageAfterBranchPage_thenReturnPage() {
					assertEquals(pageInFirstBranch, wizardTree.getPage<Page>(2))
				}

				@Test(expected = PageIndexOutOfBoundsException::class)
				fun whenGetPageAfterLastPage_thenThrowException() {
					wizardTree.getPage<Page>(3)
				}

				@Test
				fun whenGetNumberOfAccessablePages_thenReturnNumberOfAllPages() {
					assertEquals(3, wizardTree.numberOfAccessablePages)
				}

				@Test
				fun whenGetPositionOfPageAfterBranchPage_thenReturnPositionOfPage() {
					assertEquals(2, wizardTree.getPositionOfPage(pageInFirstBranch))
				}

				@Test
				fun whenCallIsLastPageWithPageInTrunkBranch_thenReturnFalse() {
					assertFalse(wizardTree.isLastPage(pageInTrunkBranch))
				}

				@Test
				fun whenCallIsLastPageWithBranchPage_thenReturnFalse() {
					assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch))
				}

				@Test
				fun whenCallIsLastPageWithPageInFirstBranch_thenReturnTrue() {
					assertTrue(wizardTree.isLastPage(pageInFirstBranch))
				}

				inner class GivenAnOnPageValidCallback {
					@Before
					fun setUp() {
						wizardTree.onPageValid = mock()
					}

					@Test
					fun whenPageAfterBranchPageCompleted_thenCallOnPageValidCallback() {
						pageInFirstBranch.completed = true
						verify(wizardTree.onPageValid, never())!!.invoke(pageInFirstBranch, 2)
					}
				}
			}
		}

		inner class GivenATrunkAndTwoBranchesAndTwoBranches {
			private lateinit var branchPageInTrunkBranch: BranchPage
			private lateinit var pageInFirstBranch: Page
			private lateinit var branchPageInFirstBranch: BranchPage
			private lateinit var pageInTrunkBranch: Page

			@Before
			fun setUp() {
				pageInTrunkBranch = TestPage("Page in Trunk Branch")
				pageInFirstBranch = TestPage("Page in first Branch")
				branchPageInFirstBranch = BranchPage("Branch Page in first Branch", "FragmentName").apply {
					addBranch("First Branch in first Branch")
					addBranch("Second Branch in first Branch")
				}
				branchPageInTrunkBranch = BranchPage("Branch Page in Trunk Branch", "FragmentName").apply {
					addBranch("First Branch", pageInFirstBranch, branchPageInFirstBranch)
					addBranch("Second Branch")
				}
				wizardTree.setPages(pageInTrunkBranch, branchPageInTrunkBranch)
			}

			@Test
			fun whenCallIsLastPageWithPageInTrunkBranch_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(pageInTrunkBranch))
			}

			@Test
			fun whenCallIsLastPageWithBranchPageInTrunkBranch_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch))
			}

			@Test
			fun whenCallIsLastPageWithPageInFirstBranch_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(pageInFirstBranch))
			}

			@Test
			fun whenCallIsLastPageWithBranchPageInFirstBranch_thenReturnFalse() {
				assertFalse(wizardTree.isLastPage(branchPageInFirstBranch))
			}

			inner class GivenSelectedBranches {
				@Before
				fun setUp() {
					branchPageInTrunkBranch.selectBranch(0)
					branchPageInFirstBranch.selectBranch(0)
				}

				@Test
				fun whenCallIsLastPageWithPageInTrunkBranch_thenReturnFalse() {
					assertFalse(wizardTree.isLastPage(pageInTrunkBranch))
				}

				@Test
				fun whenCallIsLastPageWithBranchPageInTrunkBranch_thenReturnFalse() {
					assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch))
				}

				@Test
				fun whenCallIsLastPageWithPageInFirstBranch_thenReturnFalse() {
					assertFalse(wizardTree.isLastPage(pageInFirstBranch))
				}

				@Test
				fun whenCallIsLastPageWithBranchPageInFirstBranch_thenReturnTrue() {
					assertTrue(wizardTree.isLastPage(branchPageInFirstBranch))
				}
			}

			inner class GivenAnOnTreeChangedCallback {
				@Before
				fun setUp() {
					wizardTree.onTreeChanged = mock()
				}

				inner class GivenSelectedBranches {
					@Before
					fun setUp() {
						branchPageInTrunkBranch.selectBranch(0)
						branchPageInFirstBranch.selectBranch(0)
					}

					@Test
					fun whenSelectBranches_thenCallOnTreeChangedCallback() {
						verify(wizardTree.onTreeChanged)!!.invoke(2)
						verify(wizardTree.onTreeChanged)!!.invoke(4)
					}
				}
			}
		}

		inner class GivenARequiredPage {
			private lateinit var requiredPage: Page

			@Before
			fun setUp() {
				requiredPage = TestPage("Required Page", true)
				wizardTree.setPages(requiredPage)
			}

			inner class GivenAnOnTreeChangedCallback {
				@Before
				fun setUp() {
					wizardTree.onTreeChanged = mock()
				}

				@Test
				fun whenPageCompleted_thenCallOnTreeChangedCallback() {
					requiredPage.completed = true
					verify(wizardTree.onTreeChanged)!!.invoke(1)
				}

				@Test
				fun whenPageNotCompleted_thenCallOnTreeChangedCallback() {
					requiredPage.completed = false
					verify(wizardTree.onTreeChanged)!!.invoke(1)
				}
			}

			inner class GivenAnOnPageValidCallback {
				@Before
				fun setUp() {
					wizardTree.onPageValid = mock()
				}

				@Test
				fun whenPageCompleted_thenCallOnPageValidCallback() {
					requiredPage.completed = true
					verify(wizardTree.onPageValid)!!.invoke(requiredPage, 0)
				}
			}

			inner class GivenAnOnPageInvalidCallback {
				@Before
				fun setUp() {
					wizardTree.onPageInvalid = mock()
				}

				@Test
				fun whenPageNotCompleted_thenCallOnPageInvalidCallback() {
					requiredPage.completed = false
					verify(wizardTree.onPageInvalid)!!.invoke(requiredPage, 0)
				}
			}
		}

		inner class GivenATrunkAndTwoBranchesAndRequiredPages {
			private lateinit var requiredPageInTrunkBranch: Page
			private lateinit var branchPage: BranchPage
			private lateinit var requiredPageInFirstBranch: Page

			@Before
			fun setUp() {
				requiredPageInTrunkBranch = TestPage("Required Page in Trunk Branch", true)
				requiredPageInFirstBranch = TestPage("Required Page in first Branch", true)
				branchPage = BranchPage("Branch Page", "FragmentName").apply {
					addBranch("First Branch", requiredPageInFirstBranch)
					addBranch("Second Branch")
				}
				wizardTree.setPages(requiredPageInTrunkBranch, branchPage)
			}

			inner class GivenASelectedBranch {
				@Before
				fun setUp() {
					branchPage.selectBranch(0)
				}

				inner class GivenAnOnPageValidCallback {
					@Before
					fun setUp() {
						wizardTree.onPageValid = mock()
					}

					@Test
					fun whenPageAfterBranchPageCompleted_thenCallOnPageValidCallback() {
						requiredPageInFirstBranch.completed = true
						verify(wizardTree.onPageValid)!!.invoke(requiredPageInFirstBranch, 2)
					}
				}

				inner class GivenAnOnPageInvalidCallback {
					@Before
					fun setUp() {
						wizardTree.onPageInvalid = mock()
					}

					@Test
					fun whenPageAfterBranchPageNotCompleted_thenCallOnPageInvalidCallback() {
						requiredPageInFirstBranch.completed = false
						verify(wizardTree.onPageInvalid)!!.invoke(requiredPageInFirstBranch, 2)
					}
				}
			}

			inner class GivenAnOnTreeChangedCallback {
				@Before
				fun setUp() {
					wizardTree.onTreeChanged = mock()
				}

				inner class GivenCompletedPageBranches {
					@Before
					fun setUp() {
						requiredPageInTrunkBranch.completed = true
					}

					@Test
					fun whenPageCompleted_thenCallOnTreeChangedCallback() {
						verify(wizardTree.onTreeChanged)!!.invoke(1)
					}
				}
			}
		}

		inner class GivenTwoRequiredPages {
			@Before
			fun setUp() {
				wizardTree.setPages(TestPage("First required Page", true),
						TestPage("Second required Page", true))
			}

			@Test
			fun whenGetNumberOfAccessablePages_thenReturnOne() {
				assertEquals(1, wizardTree.numberOfAccessablePages)
			}
		}
	}
}
