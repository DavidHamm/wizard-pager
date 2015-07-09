package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(HierarchicalContextRunner.class)
public class WizardTreeTest {
	public class GivenAWizardTreeWithAPage {
		private WizardTree wizardTree;
		private TestPage page;

		@Before
		public void setUp() throws Exception {
			page = new TestPage("Page");
			wizardTree = new WizardTree(page);
		}

		@Test
		public void whenGetFirstPage_thenReturnFirstPage() throws Exception {
			assertEquals(page, wizardTree.getPage(0));
		}

		@Test
		public void whenGetNumberOfAccessablePages_thenReturnNumberOfPages() throws Exception {
			assertEquals(1, wizardTree.getNumberOfAccessablePages());
		}

		@Test
		public void whenGetPositionOfInvalidPage_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPage(new TestPage("Invalid Page")));
		}

		@Test
		public void whenGetPositionOfInvalidPageFragment_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPageFragment(new Fragment()));
		}

		public class GivenAPageValidityListener {
			private PageValidityListener pageValidityListener;

			@Before
			public void setUp() throws Exception {
				pageValidityListener = mock(PageValidityListener.class);
				wizardTree.setPageValidityListener(pageValidityListener);
			}

			@Test
			public void whenPageCompleted_thenCallOnPageValidCallback() throws Exception {
				page.setCompleted();
				verify(pageValidityListener, times(0)).onPageValid(page, 0);
			}
		}
	}

	public class GivenAWizardTreeWithTrunkAndTwoEmptyBranches {
		private WizardTree wizardTree;
		private TestPage page;
		private TestBranchPage branchPage;

		@Before
		public void setUp() throws Exception {
			page = new TestPage("Page");
			branchPage = new TestBranchPage("Branch Page", new Branch("First Branch"), new Branch("Second Branch"));
			wizardTree = new WizardTree(page, branchPage);
		}

		@Test
		public void whenGetBranchPage_thenReturnBranchPage() throws Exception {
			assertEquals(branchPage, wizardTree.getPage(1));
		}

		@Test
		public void whenGetPositionOfBranchPage_thenReturnPositionOfBranchPage() throws Exception {
			assertEquals(1, wizardTree.getPositionOfPage(branchPage));
		}

		@Test
		public void whenGetPositionOfBranchPageFragment_thenReturnPositionOfBranchPage() throws Exception {
			assertEquals(1, wizardTree.getPositionOfPageFragment(branchPage.getFragment()));
		}
	}

	public class GivenAWizardTreeWithTrunkAndTwoBranches {
		private WizardTree wizardTree;
		private TestBranchPage branchPage;
		private TestPage pageInFirstBranch;
		private TestPage pageInTrunkBranch;

		@Before
		public void setUp() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			pageInFirstBranch = new TestPage("Page in First Branch");
			branchPage = new TestBranchPage("Branch Page",
					new Branch("First Branch", pageInFirstBranch),
					new Branch("Second Branch", new TestPage("Page in Second Branch")));
			wizardTree = new WizardTree(pageInTrunkBranch, branchPage);
		}

		@Test(expected = WizardTree.PageIndexOutOfBoundsException.class)
		public void whenGetPageAfterBranchPage_thenThrowException() throws Exception {
			wizardTree.getPage(2);
		}

		@Test
		public void whenGetNumberOfAccessablePages_thenReturnNumberOfPagesInclBranchPage() throws Exception {
			assertEquals(2, wizardTree.getNumberOfAccessablePages());
		}

		@Test
		public void whenGetPositionOfPageAfterBranchPage_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPage(pageInFirstBranch));
		}

		@Test
		public void whenGetPositionOfPageFragmentAfterBranchPage_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPageFragment(pageInFirstBranch.getFragment()));
		}

		public class GivenASelectedBranch {
			@Before
			public void setUp() throws Exception {
				branchPage.selectBranch(0);
			}

			@Test
			public void whenGetPageAfterBranchPage_thenReturnPage() throws Exception {
				assertEquals(pageInFirstBranch, wizardTree.getPage(2));
			}

			@Test(expected = WizardTree.PageIndexOutOfBoundsException.class)
			public void whenGetPageAfterLastPage_thenThrowException() throws Exception {
				wizardTree.getPage(3);
			}

			@Test
			public void whenGetNumberOfAccessablePages_thenReturnNumberOfAllPages() throws Exception {
				assertEquals(3, wizardTree.getNumberOfAccessablePages());
			}

			@Test
			public void whenGetPositionOfPageAfterBranchPage_thenReturnPositionOfPage() throws Exception {
				assertEquals(2, wizardTree.getPositionOfPage(pageInFirstBranch));
			}

			@Test
			public void whenGetPositionOfPageFragmentAfterBranchPage_thenReturnPositionOfPage() throws Exception {
				assertEquals(2, wizardTree.getPositionOfPageFragment(pageInFirstBranch.getFragment()));
			}

			public class GivenAPageValidityListener {
				private PageValidityListener pageValidityListener;

				@Before
				public void setUp() throws Exception {
					pageValidityListener = mock(PageValidityListener.class);
					wizardTree.setPageValidityListener(pageValidityListener);
				}

				@Test
				public void whenPageAfterBranchPageCompleted_thenCallOnPageValidCallback() throws Exception {
					pageInFirstBranch.setCompleted();
					verify(pageValidityListener, times(0)).onPageValid(pageInFirstBranch, 2);
				}
			}
		}
	}

	public class GivenAWizardTreeWithTrunkAndTwoBranchesAndTwoBranches {
		private WizardTree wizardTree;
		private TestBranchPage branchPageInTrunk;
		private TestPage pageInFirstBranch;
		private TestBranchPage branchPageInFirstBranch;
		private TestPage pageInTrunkBranch;
		private WizardTreeListener wizardTreeListener;

		@Before
		public void setUp() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			pageInFirstBranch = new TestPage("Page in First Branch");
			branchPageInFirstBranch = new TestBranchPage("Branch Page in First Branch",
					new Branch("First Branch in First Branch"),
					new Branch("Second Branch in First Branch"));
			branchPageInTrunk = new TestBranchPage("Branch Page in Trunk Branch",
					new Branch("First Branch", pageInFirstBranch, branchPageInFirstBranch),
					new Branch("Second Branch", new TestPage("Page in Second Branch")));
			wizardTree = new WizardTree(pageInTrunkBranch, branchPageInTrunk);
			wizardTreeListener = mock(WizardTreeListener.class);
			wizardTree.setWizardTreeListener(wizardTreeListener);
			branchPageInTrunk.selectBranch(0);
			branchPageInFirstBranch.selectBranch(0);
		}

		@Test
		public void whenSelectBranches_thenCallOnTreeChangedCallback() throws Exception {
			verify(wizardTreeListener, times(1)).onTreeChanged(2);
			verify(wizardTreeListener, times(1)).onTreeChanged(4);
		}
	}

	public class GivenAWizardTreeWithARequiredPage {
		private WizardTree wizardTree;
		private TestPage requiredPage;

		@Before
		public void setUp() throws Exception {
			requiredPage = new TestPage("Required Page", true);
			wizardTree = new WizardTree(requiredPage);
		}

		public class GivenAPageValidityListener {
			private PageValidityListener pageValidityListener;

			@Before
			public void setUp() throws Exception {
				pageValidityListener = mock(PageValidityListener.class);
				wizardTree.setPageValidityListener(pageValidityListener);
			}

			@Test
			public void whenPageCompleted_thenCallOnPageValidCallback() throws Exception {
				requiredPage.setCompleted();
				verify(pageValidityListener, times(1)).onPageValid(requiredPage, 0);
			}

			@Test
			public void whenPageNotCompleted_thenCallOnPageInvalidCallback() throws Exception {
				requiredPage.setNotCompleted();
				verify(pageValidityListener, times(1)).onPageInvalid(requiredPage, 0);
			}
		}
	}

	public class GivenAWizardTreeWithTrunkAndTwoBranchesAndRequiredPageInFirstBranch {
		private WizardTree wizardTree;
		private TestPage pageInTrunkbranch;
		private TestBranchPage branchPage;
		private TestPage requiredPageInFirstbranch;

		@Before
		public void setUp() throws Exception {
			pageInTrunkbranch = new TestPage("Page in Trunk Branch");
			requiredPageInFirstbranch = new TestPage("Required Page in First Branch", true);
			branchPage = new TestBranchPage("Branch Page",
					new Branch("First Branch", requiredPageInFirstbranch),
					new Branch("Second Branch"));
			wizardTree = new WizardTree(pageInTrunkbranch, branchPage);
		}

		public class GivenASelectedBranch {
			@Before
			public void setUp() throws Exception {
				branchPage.selectBranch(0);
			}

			public class GivenAPageValidityListener {
				private PageValidityListener pageValidityListener;

				@Before
				public void setUp() throws Exception {
					pageValidityListener = mock(PageValidityListener.class);
					wizardTree.setPageValidityListener(pageValidityListener);
				}

				@Test
				public void whenPageAfterBranchPageCompleted_thenCallOnPageValidCallback() throws Exception {
					requiredPageInFirstbranch.setCompleted();
					verify(pageValidityListener, times(1)).onPageValid(requiredPageInFirstbranch, 2);
				}

				@Test
				public void whenPageAfterBranchPageNotCompleted_thenCallOnPageInvalidCallback() throws Exception {
					requiredPageInFirstbranch.setNotCompleted();
					verify(pageValidityListener, times(1)).onPageInvalid(requiredPageInFirstbranch, 2);
				}
			}
		}
	}
}