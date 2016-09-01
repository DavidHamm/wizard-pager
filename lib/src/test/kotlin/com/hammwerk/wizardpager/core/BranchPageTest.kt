package com.hammwerk.wizardpager.core

import de.bechte.junit.runners.context.HierarchicalContextRunner
import junit.framework.Assert.*
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@Suppress("UNUSED")
@RunWith(HierarchicalContextRunner::class)
class BranchPageTest {
	private lateinit var branchPage: BranchPage

	@Before
	fun setUp() {
		branchPage = BranchPage("Branch Page", "FragmentName")
	}

	@Test(expected = BranchPage.TwoBranchesRequiredException::class)
	fun whenGetSelectedBranch_throwTwoBranchesRequiredException() {
		branchPage.selectedBranch
	}

	@Test(expected = BranchPage.TwoBranchesRequiredException::class)
	fun whenSelectBranch_throwTwoBranchesRequiredException() {
		branchPage.selectBranch(0)
	}

	@Test(expected = BranchPage.TwoBranchesRequiredException::class)
	fun whenGetChoices_throwTwoBranchesRequiredException() {
		branchPage.choices
	}

	@Test
	fun whenAskForValidity_returnInvalid() {
		branchPage.valid
	}

	@Test(expected = BranchPage.TwoBranchesRequiredException::class)
	fun whenGetSelectedBranchIndex_returnNull() {
		branchPage.selectedBranchIndex
	}

	inner class GivenAnEmptyBranch {
		@Before
		fun setUp() {
			branchPage.addBranch("Branch")
		}

		@Test(expected = BranchPage.TwoBranchesRequiredException::class)
		fun whenGetSelectedBranch_throwTwoBranchesRequiredException() {
			branchPage.selectedBranch
		}

		@Test(expected = BranchPage.TwoBranchesRequiredException::class)
		fun whenSelectBranch_throwTwoBranchesRequiredException() {
			branchPage.selectBranch(0)
		}

		@Test(expected = BranchPage.TwoBranchesRequiredException::class)
		fun whenGetChoices_throwTwoBranchesRequiredException() {
			branchPage.choices
		}

		@Test
		fun whenAskForValidity_returnNull() {
			branchPage.valid
		}

		@Test(expected = BranchPage.TwoBranchesRequiredException::class)
		fun whenGetSelectedBranchIndex_returnNull() {
			branchPage.selectedBranchIndex
		}
	}

	inner class GivenTwoEmptyBranches {
		@Before
		fun setUp() {
			branchPage.apply {
				addBranch("First Branch")
				addBranch("Second Branch")
			}
		}

		@Test
		fun whenGetSelectedBranch_returnNull() {
			assertNull(branchPage.selectedBranch)
		}

		@Test
		fun whenAskForValidity_returnInvalid() {
			assertFalse(branchPage.valid)
		}

		@Test
		fun whenGetChoices_returnBranchNames() {
			assertArrayEquals(arrayOf("First Branch", "Second Branch"), branchPage.choices)
		}

		@Test
		fun whenGetSelectedBranchIndex_returnNull() {
			assertNull(branchPage.selectedBranchIndex)
		}

		inner class GivenASelectedBranch {
			@Before
			fun setUp() {
				branchPage.selectBranch(0)
			}

			@Test
			fun whenGetSelectedBranch_returnSelectedBranch() {
				assertEquals("First Branch", branchPage.selectedBranch!!.name)
			}

			@Test
			fun whenAskForValidity_returnValid() {
				assertTrue(branchPage.valid)
			}

			@Test
			fun whenGetSelectedBranchIndex_returnSelectedBranchIndex() {
				assertEquals(Integer.valueOf(0), branchPage.selectedBranchIndex)
			}
		}

		inner class GivenAnOnBranchSelectedCallback {
			@Before
			fun setup() {
				branchPage.onBranchSelected = mock()
			}

			inner class GivenASelectedBranch {
				@Before
				fun setup() {
					branchPage.selectBranch(0)
				}

				@Test
				fun verifyOnBranchSelectedWasCalledOnce() {
					verify(branchPage.onBranchSelected)!!.invoke(branchPage)
				}
			}

			inner class GivenABranchSelectedMultipleTimes {
				@Before
				fun setup() {
					branchPage.selectBranch(0)
					branchPage.selectBranch(0)
				}

				@Test
				fun verifyOnBranchSelectedWasCalledOnce() {
					verify(branchPage.onBranchSelected)!!.invoke(branchPage)
				}
			}
		}
	}

	inner class GivenTwoBranchesWithPages {
		private lateinit var pageInFirstBranch: Page

		@Before
		fun setup() {
			pageInFirstBranch = TestPage("Page in first Branch")
			branchPage.apply {
				addBranch("First Branch", pageInFirstBranch)
				addBranch("Second Branch", TestPage("Page in second Branch"))
			}
		}

		inner class GivenASelectedBranch {
			@Before
			fun setup() {
				branchPage.selectBranch(0)
			}

			@Test
			fun whenGetPageInSelectedBranch_returnPageInSelectedBranch() {
				assertEquals(pageInFirstBranch, branchPage.selectedBranch!!.getPage<Page>(0))
			}
		}
	}
}