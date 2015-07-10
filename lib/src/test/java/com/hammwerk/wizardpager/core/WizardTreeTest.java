package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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

		@Test
		public void whenCallIsLastPageWithPage_thenReturnTrue() throws Exception {
			assertTrue(wizardTree.isLastPage(page));
		}

		@Test
		public void whenCallIsLastPageWithInvalidPage_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(new TestPage("Invalid Page")));
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
		private TestPage pageInTrunkBranch;
		private TestBranchPage branchPageInTrunkBranch;

		@Before
		public void setUp() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			branchPageInTrunkBranch = new TestBranchPage("Branch Page in Trunk Branch",
					new Branch("First Branch"),
					new Branch("Second Branch"));
			wizardTree = new WizardTree(pageInTrunkBranch, branchPageInTrunkBranch);
		}

		@Test
		public void whenGetBranchPage_thenReturnBranchPage() throws Exception {
			assertEquals(branchPageInTrunkBranch, wizardTree.getPage(1));
		}

		@Test
		public void whenGetNumberOfAccessablePages_thenReturnOne() throws Exception {
			assertEquals(2, wizardTree.getNumberOfAccessablePages());
		}

		@Test
		public void whenGetPositionOfBranchPage_thenReturnPositionOfBranchPage() throws Exception {
			assertEquals(1, wizardTree.getPositionOfPage(branchPageInTrunkBranch));
		}

		@Test
		public void whenGetPositionOfBranchPageFragment_thenReturnPositionOfBranchPage() throws Exception {
			assertEquals(1, wizardTree.getPositionOfPageFragment(branchPageInTrunkBranch.getFragment()));
		}

		@Test
		public void whenCallIsLastPageWithFirstPage_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(pageInTrunkBranch));
		}

		@Test
		public void whenCallIsLastPageWithBranchPage_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch));
		}

		public class GivenASelectedBranch {
			@Before
			public void setUp() throws Exception {
				branchPageInTrunkBranch.selectBranch(0);
			}

			@Test
			public void whenCallIsLastPageWithBranchPage_thenReturnTrue() throws Exception {
				assertTrue(wizardTree.isLastPage(branchPageInTrunkBranch));
			}
		}
	}

	public class GivenAWizardTreeWithTrunkAndTwoBranches {
		private WizardTree wizardTree;
		private TestPage pageInTrunkBranch;
		private TestBranchPage branchPageInTrunkBranch;
		private TestPage pageInFirstBranch;

		@Before
		public void setUp() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			pageInFirstBranch = new TestPage("Page in First Branch");
			branchPageInTrunkBranch = new TestBranchPage("Branch Page in Trunk Branch",
					new Branch("First Branch", pageInFirstBranch),
					new Branch("Second Branch", new TestPage("Page in Second Branch")));
			wizardTree = new WizardTree(pageInTrunkBranch, branchPageInTrunkBranch);
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

		@Test
		public void whenCallIsLastPageWithFirstPage_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(pageInTrunkBranch));
		}

		@Test
		public void whenCallIsLastPageWithBranchPage_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch));
		}

		@Test
		public void whenCallIsLastPageWithPageInFirstBranch_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(pageInFirstBranch));
		}

		public class GivenASelectedBranch {
			@Before
			public void setUp() throws Exception {
				branchPageInTrunkBranch.selectBranch(0);
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

			@Test
			public void whenCallIsLastPageWithPageInTrunkBranch_thenReturnFalse() throws Exception {
				assertFalse(wizardTree.isLastPage(pageInTrunkBranch));
			}

			@Test
			public void whenCallIsLastPageWithBranchPage_thenReturnFalse() throws Exception {
				assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch));
			}

			@Test
			public void whenCallIsLastPageWithPageInFirstBranch_thenReturnTrue() throws Exception {
				assertTrue(wizardTree.isLastPage(pageInFirstBranch));
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
		private TestBranchPage branchPageInTrunkBranch;
		private TestPage pageInFirstBranch;
		private TestBranchPage branchPageInFirstBranch;
		private TestPage pageInTrunkBranch;

		@Before
		public void setUp() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			pageInFirstBranch = new TestPage("Page in first Branch");
			branchPageInFirstBranch = new TestBranchPage("Branch Page in first Branch",
					new Branch("First Branch in first Branch"),
					new Branch("Second Branch in first Branch"));
			branchPageInTrunkBranch = new TestBranchPage("Branch Page in Trunk Branch",
					new Branch("First Branch", pageInFirstBranch, branchPageInFirstBranch),
					new Branch("Second Branch"));
			wizardTree = new WizardTree(pageInTrunkBranch, branchPageInTrunkBranch);
		}

		@Test
		public void whenCallIsLastPageWithPageInTrunkBranch_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(pageInTrunkBranch));
		}

		@Test
		public void whenCallIsLastPageWithBranchPageInTrunkBranch_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch));
		}

		@Test
		public void whenCallIsLastPageWithPageInFirstBranch_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(pageInFirstBranch));
		}

		@Test
		public void whenCallIsLastPageWithBranchPageInFirstBranch_thenReturnFalse() throws Exception {
			assertFalse(wizardTree.isLastPage(branchPageInFirstBranch));
		}

		public class GivenSelectedBranches {
			@Before
			public void setUp() throws Exception {
				branchPageInTrunkBranch.selectBranch(0);
				branchPageInFirstBranch.selectBranch(0);
			}

			@Test
			public void whenCallIsLastPageWithPageInTrunkBranch_thenReturnFalse() throws Exception {
				assertFalse(wizardTree.isLastPage(pageInTrunkBranch));
			}

			@Test
			public void whenCallIsLastPageWithBranchPageInTrunkBranch_thenReturnFalse() throws Exception {
				assertFalse(wizardTree.isLastPage(branchPageInTrunkBranch));
			}

			@Test
			public void whenCallIsLastPageWithPageInFirstBranch_thenReturnFalse() throws Exception {
				assertFalse(wizardTree.isLastPage(pageInFirstBranch));
			}

			@Test
			public void whenCallIsLastPageWithBranchPageInFirstBranch_thenReturnTrue() throws Exception {
				assertTrue(wizardTree.isLastPage(branchPageInFirstBranch));
			}
		}

		public class GivenAWizardTreeChangeListener {
			private WizardTreeChangeListener wizardTreeChangeListener;

			@Before
			public void setUp() throws Exception {
				wizardTreeChangeListener = mock(WizardTreeChangeListener.class);
				wizardTree.addWizardTreeChangeListener(wizardTreeChangeListener);
			}

			public class GivenSelectedBranches {
				@Before
				public void setUp() throws Exception {
					branchPageInTrunkBranch.selectBranch(0);
					branchPageInFirstBranch.selectBranch(0);
				}

				@Test
				public void whenSelectBranches_thenCallOnTreeChangedCallback() throws Exception {
					verify(wizardTreeChangeListener, times(1)).onTreeChanged(2);
					verify(wizardTreeChangeListener, times(1)).onTreeChanged(4);
				}
			}
		}

		public class GivenTwoWizardTreeChangeListener {
			private WizardTreeChangeListener firstWizardTreeChangeListener;
			private WizardTreeChangeListener secondWizardTreeChangeListener;

			@Before
			public void setUp() throws Exception {
				firstWizardTreeChangeListener = mock(WizardTreeChangeListener.class);
				secondWizardTreeChangeListener = mock(WizardTreeChangeListener.class);
				wizardTree.addWizardTreeChangeListener(firstWizardTreeChangeListener);
				wizardTree.addWizardTreeChangeListener(secondWizardTreeChangeListener);
			}

			public class GivenSelectedBranches {
				@Before
				public void setUp() throws Exception {
					branchPageInTrunkBranch.selectBranch(0);
					branchPageInFirstBranch.selectBranch(0);
				}

				@Test
				public void whenSelectBranches_thenCallOnTreeChangedCallback() throws Exception {
					verify(firstWizardTreeChangeListener, times(1)).onTreeChanged(2);
					verify(firstWizardTreeChangeListener, times(1)).onTreeChanged(4);
					verify(secondWizardTreeChangeListener, times(1)).onTreeChanged(2);
					verify(secondWizardTreeChangeListener, times(1)).onTreeChanged(4);
				}
			}
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

	public class GivenAWizardTreeWithTrunkAndTwoBranchesAndRequiredPages {
		private WizardTree wizardTree;
		private TestPage requiredPageInTrunkBranch;
		private TestBranchPage branchPage;
		private TestPage requiredPageInFirstBranch;

		@Before
		public void setUp() throws Exception {
			requiredPageInTrunkBranch = new TestPage("Required Page in Trunk Branch", true);
			requiredPageInFirstBranch = new TestPage("Required Page in first Branch", true);
			branchPage = new TestBranchPage("Branch Page",
					new Branch("First Branch", requiredPageInFirstBranch),
					new Branch("Second Branch"));
			wizardTree = new WizardTree(requiredPageInTrunkBranch, branchPage);
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
					requiredPageInFirstBranch.setCompleted();
					verify(pageValidityListener, times(1)).onPageValid(requiredPageInFirstBranch, 2);
				}

				@Test
				public void whenPageAfterBranchPageNotCompleted_thenCallOnPageInvalidCallback() throws Exception {
					requiredPageInFirstBranch.setNotCompleted();
					verify(pageValidityListener, times(1)).onPageInvalid(requiredPageInFirstBranch, 2);
				}
			}
		}

		public class GivenAWizardTreeChangeListener {
			private WizardTreeChangeListener wizardTreeChangeListener;

			@Before
			public void setUp() throws Exception {
				wizardTreeChangeListener = mock(WizardTreeChangeListener.class);
				wizardTree.addWizardTreeChangeListener(wizardTreeChangeListener);
			}

			public class GivenCompletedPageBranches {
				@Before
				public void setUp() throws Exception {
					requiredPageInTrunkBranch.setCompleted();
				}

				@Test
				public void whenPageCompleted_thenCallOnTreeChangedCallback() throws Exception {
					verify(wizardTreeChangeListener, times(1)).onTreeChanged(1);
				}
			}
		}
	}

	public class GivenAWizardTreeWithTwoRequiredPages {
		private WizardTree wizardTree;

		@Before
		public void setUp() throws Exception {
			Page firstRequiredPage = new TestPage("First required Page", true);
			Page secondRequiredPage = new TestPage("Second required Page", true);
			wizardTree = new WizardTree(firstRequiredPage, secondRequiredPage);
		}

		@Test
		public void whenGetNumberOfAccessablePages_thenReturnOne() throws Exception {
			assertEquals(1, wizardTree.getNumberOfAccessablePages());
		}
	}
}