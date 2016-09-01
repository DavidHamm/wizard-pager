package com.hammwerk.wizardpager.core

import android.content.Context
import de.bechte.junit.runners.context.HierarchicalContextRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@Suppress("UNUSED")
@RunWith(HierarchicalContextRunner::class)
class WizardPagerAdapterTest {
	inner class GivenAWizardPagerAdapter {
		private lateinit var wizardPagerAdapter: WizardPagerAdapter

		@Before
		fun setUp() {
			wizardPagerAdapter = object : WizardPagerAdapter(mock<Context>(), null) {
				override fun notifyDataSetChanged() {
				}
			}
		}

		@Test
		fun whenGetCount_thenReturnZero() {
			assertEquals(0, wizardPagerAdapter.count)
		}

		inner class GivenAWizardTree {
			private lateinit var wizardTree: WizardTree

			@Before
			fun setUp() {
				wizardTree = WizardTree()
				wizardPagerAdapter.wizardTree = wizardTree
			}

			@Test
			fun whenGetCount_thenReturnZero() {
				assertEquals(0, wizardPagerAdapter.count)
			}

			inner class GivenOneRequiredPage {
				private lateinit var requiredPage: Page

				@Before
				fun setUp() {
					requiredPage = TestPage("Page", true)
					wizardTree.setPages(requiredPage)
				}

				@Test
				fun whenGetCount_thenReturnNumberOfPages() {
					assertEquals(1, wizardPagerAdapter.count)
				}

				inner class GivenCompletedRequiredPage {
					@Before
					fun setUp() {
						requiredPage.completed = true
					}

					@Test
					fun whenCallIsPageValid_thenReturnTrue() {
						assertTrue(wizardPagerAdapter.isPageValid(0))
					}
				}
			}

			inner class GivenAPageAndABranchPage {
				private lateinit var pageInTrunkBranch: Page
				private lateinit var branchPageInTrunkBranch: BranchPage

				@Before
				fun setUp() {
					pageInTrunkBranch = TestPage("Page in Trunk Branch")
					branchPageInTrunkBranch = BranchPage("Branch Page in Trunk Branch", "FragmentName").apply {
						addBranch("First Branch", TestPage("Page in first Branch"))
						addBranch("Second Branch", TestPage("Page in second Branch"))
					}
					wizardTree.setPages(pageInTrunkBranch, branchPageInTrunkBranch)
				}

				@Test
				fun whenGetCount_thenReturnTwo() {
					assertEquals(2, wizardPagerAdapter.count)
				}

				inner class GivenASelectedBranch {
					@Before
					fun setUp() {
						branchPageInTrunkBranch.selectBranch(0)
					}

					@Test
					fun whenGetCount_thenReturn() {
						assertEquals(3, wizardPagerAdapter.count)
					}
				}
			}
		}
	}
}