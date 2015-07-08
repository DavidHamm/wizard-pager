package com.hammwerk.wizardpager.core;

import android.support.v4.app.Fragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class WizardTreeTest {
	public static class GivenAWizardTree {
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
		public void whenGetKnownNumberOfPages_thenReturnNumberOfPages() throws Exception {
			assertEquals(1, wizardTree.getKnownNumberOfPages());
		}

		@Test
		public void whenGetPositionOfInvalidPage_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPage(new TestPage("Invalid Page")));
		}

		@Test
		public void whenGetPositionOfInvalidPageFragment_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPageFragment(new Fragment()));
		}
	}

	public static class GivenAWizardTreeWithTrunkAndTwoEmptyBranches {
		private WizardTree wizardTree;
		private TestPage page;
		private TestBranchPage branchPage;

		@Before
		public void givenAWizardTreeWithTrunkAndTwoEmptyBranches() throws Exception {
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

	public abstract static class GivenAWizardTreeWithTrunkAndTwoBranches {
		protected WizardTree wizardTree;
		protected TestBranchPage branchPage;
		protected TestPage pageInFirstBranch;
		private TestPage pageInTrunkBranch;

		@Before
		public void givenAWizardTreeWithTrunkAndTwoBranches() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			pageInFirstBranch = new TestPage("Page in First Branch");
			branchPage = new TestBranchPage("Branch Page",
					new Branch("First Branch", pageInFirstBranch),
					new Branch("Second Branch", new TestPage("Page in Second Branch")));
			wizardTree = new WizardTree(pageInTrunkBranch, branchPage);
		}
	}

	public static class GivenNoSelectedBranch extends GivenAWizardTreeWithTrunkAndTwoBranches {
		@Test(expected = WizardTree.PageIndexOutOfBoundsException.class)
		public void whenGetPageAfterBranchPage_thenThrowException() throws Exception {
			wizardTree.getPage(2);
		}

		@Test
		public void whenGetKnownNumberOfPages_thenReturnNumberOfPagesInclBranchPage() throws Exception {
			assertEquals(2, wizardTree.getKnownNumberOfPages());
		}

		@Test
		public void whenGetPositionOfPageAfterBranchPage_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPage(pageInFirstBranch));
		}

		@Test
		public void whenGetPositionOfPageFragmentAfterBranchPage_thenReturnMinusOne() throws Exception {
			assertEquals(-1, wizardTree.getPositionOfPageFragment(pageInFirstBranch.getFragment()));
		}
	}

	public static class GivenSelectedBranch extends GivenAWizardTreeWithTrunkAndTwoBranches {
		@Before
		public void givenSelectedBranch() throws Exception {
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
		public void whenGetKnownNumberOfPages_thenReturnNumberOfAllPages() throws Exception {
			assertEquals(3, wizardTree.getKnownNumberOfPages());
		}

		@Test
		public void whenGetPositionOfPageAfterBranchPage_thenReturnPositionOfPage() throws Exception {
			assertEquals(2, wizardTree.getPositionOfPage(pageInFirstBranch));
		}

		@Test
		public void whenGetPositionOfPageFragmentAfterBranchPage_thenReturnPositionOfPage() throws Exception {
			assertEquals(2, wizardTree.getPositionOfPageFragment(pageInFirstBranch.getFragment()));
		}
	}

	public abstract static class GivenAWizardTreeWithTrunkAndTwoBranchesAndTwoBranches {
		protected WizardTree wizardTree;
		protected TestBranchPage branchPageInTrunk;
		protected TestPage pageInFirstBranch;
		protected TestBranchPage branchPageInFirstBranch;
		private TestPage pageInTrunkBranch;

		@Before
		public void givenAWizardTreeWithTrunkAndTwoBranches() throws Exception {
			pageInTrunkBranch = new TestPage("Page in Trunk Branch");
			pageInFirstBranch = new TestPage("Page in First Branch");
			branchPageInFirstBranch = new TestBranchPage("Branch Page in First Branch",
					new Branch("First Branch in First Branch"),
					new Branch("Second Branch in First Branch"));
			branchPageInTrunk = new TestBranchPage("Branch Page in Trunk Branch",
					new Branch("First Branch", pageInFirstBranch, branchPageInFirstBranch),
					new Branch("Second Branch", new TestPage("Page in Second Branch")));
			wizardTree = new WizardTree(pageInTrunkBranch, branchPageInTrunk);
		}
	}

	public static class GivenSelectededBranchesWithWizardTreeListener
			extends GivenAWizardTreeWithTrunkAndTwoBranchesAndTwoBranches {
		private WizardTreeListener wizardTreeListener;

		@Before
		public void givenSelectedBranch() throws Exception {
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

	public static class GivenAWizardTreeWithPageAndPageValidityListener {
		private WizardTree wizardTree;
		private TestPage page;
		private PageValidityListener pageValidityListener;

		@Before
		public void setUp() throws Exception {
			pageValidityListener = mock(PageValidityListener.class);
			page = new TestPage("Page");
			wizardTree = new WizardTree(page);
			wizardTree.setPageValidityListener(pageValidityListener);
		}

		@Test
		public void whenPageCompleted_thenCallOnPageValidCallback() throws Exception {
			page.setCompleted();
			verify(pageValidityListener, times(0)).onPageValid(page, 0);
		}
	}

	public static class GivenAWizardTreeWithTrunkAndTwoBranchesAndPageAndPageValidityListener {
		private WizardTree wizardTree;
		private TestPage pageInTrunkbranch;
		private PageValidityListener pageValidityListener;
		private TestPage pageInFirstbranch;

		@Before
		public void setUp() throws Exception {
			pageValidityListener = mock(PageValidityListener.class);
			pageInTrunkbranch = new TestPage("Page in Trunk Branch");
			pageInFirstbranch = new TestPage("Page in First Branch");
			TestBranchPage branchPage = new TestBranchPage("Branch Page",
					new Branch("First Branch", pageInFirstbranch),
					new Branch("Second Branch"));
			wizardTree = new WizardTree(pageInTrunkbranch, branchPage);
			wizardTree.setPageValidityListener(pageValidityListener);
			branchPage.selectBranch(0);
		}

		@Test
		public void whenPageAfterBranchPageCompleted_thenCallOnPageValidCallback() throws Exception {
			pageInFirstbranch.setCompleted();
			verify(pageValidityListener, times(0)).onPageValid(pageInFirstbranch, 2);
		}
	}

	public static class GivenAWizardTreeWithRequiredPageAndPageValidityListener {
		private WizardTree wizardTree;
		private TestPage requiredPage;
		private PageValidityListener pageValidityListener;

		@Before
		public void setUp() throws Exception {
			pageValidityListener = mock(PageValidityListener.class);
			requiredPage = new TestPage("Required Page", true);
			wizardTree = new WizardTree(requiredPage);
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

	public static class GivenAWizardTreeWithTrunkAndTwoBranchesAndRequiredPageAndPageValidityListener {
		private WizardTree wizardTree;
		private TestPage pageInTrunkbranch;
		private PageValidityListener pageValidityListener;
		private TestPage requiredPageInFirstbranch;

		@Before
		public void setUp() throws Exception {
			pageValidityListener = mock(PageValidityListener.class);
			pageInTrunkbranch = new TestPage("Page in Trunk Branch");
			requiredPageInFirstbranch = new TestPage("Required Page in First Branch", true);
			TestBranchPage branchPage = new TestBranchPage("Branch Page",
					new Branch("First Branch", requiredPageInFirstbranch),
					new Branch("Second Branch"));
			wizardTree = new WizardTree(pageInTrunkbranch, branchPage);
			wizardTree.setPageValidityListener(pageValidityListener);
			branchPage.selectBranch(0);
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