package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(HierarchicalContextRunner.class)
public class WizardPagerAdapterTest {
	public class GivenAWizardPagerAdapter {
		private WizardPagerAdapter wizardPagerAdapter;

		@Before
		public void setUp() throws Exception {
			wizardPagerAdapter = new WizardPagerAdapter(null) {
				@Override
				public void notifyDataSetChanged() {
				}
			};
		}

		@Test
		public void whenSetWizardTree_thenReturnWizardPagerAdapter() throws Exception {
			assertEquals(wizardPagerAdapter, wizardPagerAdapter.setWizardTree(new WizardTree()));
		}

		@Test
		public void whenSetNullWizardTree_thenDoNothing() throws Exception {
			wizardPagerAdapter.setWizardTree(null);
		}

		@Test
		public void whenGetCount_thenReturnZero() throws Exception {
			assertEquals(0, wizardPagerAdapter.getCount());
		}

		public class GivenAWizardTree {
			private WizardTree wizardTree;

			@Before
			public void setUp() throws Exception {
				wizardTree = new WizardTree();
				wizardPagerAdapter.setWizardTree(wizardTree);
			}

			public class GivenOneRequiredPage {
				private Page requiredPage;

				@Before
				public void setUp() throws Exception {
					requiredPage = new TestPage("Page", true);
					wizardTree.setPages(requiredPage);
				}

				@Test
				public void whenGetCount_thenReturnNumberOfPages() throws Exception {
					assertEquals(1, wizardPagerAdapter.getCount());
				}

				@Test
				public void whenGetItem_thenReturnFragmentOfPage() throws Exception {
					assertEquals(requiredPage.getFragment(), wizardPagerAdapter.getItem(0));
				}

				@Test
				public void whenGetItemPosition_thenReturnPositionNone() throws Exception {
					assertEquals(WizardPagerAdapter.POSITION_NONE,
							wizardPagerAdapter.getItemPosition(requiredPage.getFragment()));
				}

				public class GivenCompletedRequiredPage {
					@Before
					public void setUp() throws Exception {
						requiredPage.setCompleted();
					}

					@Test
					public void whenCallIsPageValid_thenReturnTrue() throws Exception {
						assertTrue(wizardPagerAdapter.isPageValid(0));
					}
				}
			}

			public class GivenAPageAndABranchPage {
				private Page pageInTrunkBranch;
				private BranchPage branchPageInTrunkBranch;

				@Before
				public void setUp() throws Exception {
					pageInTrunkBranch = new TestPage("Page in Trunk Branch");
					branchPageInTrunkBranch = new TestBranchPage("Branch Page in Trunk Branch")
							.addBranch("First Branch", new TestPage("Page in first Branch"))
							.addBranch("Second Branch", new TestPage("Page in second Branch"));
					wizardTree.setPages(pageInTrunkBranch, branchPageInTrunkBranch);
				}

				@Test
				public void whenGetCount_thenReturnTwo() throws Exception {
					assertEquals(2, wizardPagerAdapter.getCount());
				}

				public class GivenASelectedBranch {
					@Before
					public void setUp() throws Exception {
						branchPageInTrunkBranch.selectBranch(0);
					}

					@Test
					public void whenGetItemPositionAfterBranchPageFinished_thenReturnPositionNone() throws Exception {
						assertEquals(WizardPagerAdapter.POSITION_UNCHANGED,
								wizardPagerAdapter.getItemPosition(pageInTrunkBranch.getFragment()));
						assertEquals(WizardPagerAdapter.POSITION_UNCHANGED,
								wizardPagerAdapter.getItemPosition(branchPageInTrunkBranch.getFragment()));
					}

					@Test
					public void whenGetCount_thenReturn() throws Exception {
						assertEquals(3, wizardPagerAdapter.getCount());
					}
				}
			}
		}
	}
}