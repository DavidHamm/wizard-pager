package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(HierarchicalContextRunner.class)
public class WizardPagerAdapterTest {
	@Test
	public void createWizardPagerAdapter() throws Exception {
		new WizardPagerAdapter(null, new WizardTree(new TestPage("Page")));
	}

	@Test(expected = WizardPagerAdapter.WizardTreeIsNullException.class)
	public void whenCreateWizardPagerAdapterWithNullWizardTree_thenTrowNullPointerException() throws Exception {
		new WizardPagerAdapter(null, null);
	}

	public class GivenWizardPagerWithOneRequiredPage {
		private TestPage requiredPage;
		private WizardPagerAdapter wizardPagerAdapter;

		@Before
		public void setUp() throws Exception {
			requiredPage = new TestPage("Page", true);
			wizardPagerAdapter = new WizardPagerAdapter(null, new WizardTree(requiredPage)) {
				@Override
				public void notifyDataSetChanged() {
				}
			};
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

	public class GivenWizardPagerWithAPageAndABranchPage {
		private TestPage pageInTrunkBranch;
		private TestBranchPage branchPageInTrunkBranch;
		private WizardPagerAdapter wizardPagerAdapter;

		@Before
		public void setUp() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			branchPageInTrunkBranch = new TestBranchPage("Branch Page",
					new Branch(new TestPage("Page in first Branch")),
					new Branch(new TestPage("Page in second Branch")));
			WizardTree wizardTree = new WizardTree(pageInTrunkBranch, branchPageInTrunkBranch);
			wizardPagerAdapter = new WizardPagerAdapter(null, wizardTree) {
				@Override
				public void notifyDataSetChanged() {
				}
			};
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