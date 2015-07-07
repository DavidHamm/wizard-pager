package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class WizardTreeTest {
	private Page firstPage;
	private Page secondPage;
	private BranchPage branchPage;
	private WizardTree wizardTree;

	@Before
	public void setUp() throws Exception {
		firstPage = new TestPage("Title");
		secondPage = new TestPage("Title");
		branchPage = new TestBranchPage("Title",
				new Branch(secondPage),
				new Branch(new TestPage("Title")));
		wizardTree = new WizardTree(firstPage, branchPage);
	}

	@Test
	public void createWizardTree() throws Exception {
		new WizardTree(firstPage);
	}

	@Test
	public void givenWizardPager_whenGetPage_thenReturnPage() throws Exception {
		WizardTree wizardTree = new WizardTree(firstPage);
		assertEquals(firstPage, wizardTree.getPage(0));
	}

	@Test
	public void givenWizardTreeWithOpenBranchPage_whenGetBranchPage_thenReturnBranchPage() throws Exception {
		assertEquals(branchPage, wizardTree.getPage(1));
	}

	@Test
	public void givenWizardTreeWithFinishedBranchPage_whenGetPageAfterFinishedBranchPage_thenReturnPage()
			throws Exception {
		branchPage.chooseBranch(0);
		assertEquals(secondPage, wizardTree.getPage(2));
	}

	@Test(expected = WizardTree.PageIndexOutOfBoundsException.class)
	public void givenWizardTreeWithOpenBranchPage_whenGetPageAfterOpenBranchPage_thenThrowException()
			throws Exception {
		wizardTree.getPage(2);
	}

	@Test(expected = WizardTree.PageIndexOutOfBoundsException.class)
	public void givenWizardTree_whenGetPageAfterLastPage_thenThrowException() throws Exception {
		branchPage.chooseBranch(0);
		wizardTree.getPage(3);
	}

	@Test
	public void givenWizardTreeWithMultiplePages_whenGetKnownNumberOfPages_thenReturnNumberOfPages() throws Exception {
		WizardTree wizardTree = new WizardTree(firstPage, secondPage);
		assertEquals(2, wizardTree.getKnownNumberOfPages());
	}

	@Test
	public void givenWizardTreeWithOpenBranchPage_whenGetKnownNumberOfPages_thenReturnNumberOfPagesInclOpenBranchPage()
			throws Exception {
		WizardTree wizardTree = new WizardTree(firstPage, secondPage, branchPage);
		assertEquals(3, wizardTree.getKnownNumberOfPages());
	}

	@Test
	public void givenWizardTreeWithFinishedBranchPage_whenGetKnownNumberOfPages_thenReturnNumberOfAllPages()
			throws Exception {
		WizardTree wizardTree = new WizardTree(firstPage, secondPage, branchPage);
		branchPage.chooseBranch(0);
		assertEquals(4, wizardTree.getKnownNumberOfPages());
	}

	@Test
	public void givenWizardTreeWithMultipleFinishedBranchPages_whenGetKnownNumberOfPages_thenReturnNumberOfAllPages()
			throws Exception {
		BranchPage branchPage2 = new TestBranchPage("Title",
				new Branch(firstPage, secondPage),
				new Branch(firstPage, secondPage));
		BranchPage branchPage1 = new TestBranchPage("Title",
				new Branch(firstPage, secondPage, branchPage2),
				new Branch(firstPage, secondPage));
		WizardTree wizardTree = new WizardTree(firstPage, secondPage, branchPage1);
		branchPage1.chooseBranch(0);
		branchPage2.chooseBranch(0);
		assertEquals(8, wizardTree.getKnownNumberOfPages());
	}

	@Test
	public void givenWizardTreeWithBranchPage_whenGetPositionOfBranchPage_thenReturnPositionOfBranchPage()
			throws Exception {
		assertEquals(1, wizardTree.getPositionOfPage(branchPage));
	}

	@Test
	public void givenWizardTreeWithFinishedBranchPage_whenGetPositionOfPageAfterBranchPage_thenReturnPositionOfPage()
			throws Exception {
		branchPage.chooseBranch(0);
		assertEquals(2, wizardTree.getPositionOfPage(secondPage));
	}

	@Test
	public void givenWizardTree_whenGetPositionOfInvalidPage_thenReturnMinusOne() throws Exception {
		WizardTree wizardTree = new WizardTree(firstPage);
		assertEquals(-1, wizardTree.getPositionOfPage(secondPage));
	}

	@Test
	public void givenWizardTreeWithOpenBranchPage_whenGetPositionOfPageAfterBranchPage_thenReturnMinusOne()
			throws Exception {
		assertEquals(-1, wizardTree.getPositionOfPage(secondPage));
	}

	@Test
	public void givenWizardTreeWithBranchPage_whenGetPositionOfBranchPageFragment_thenReturnPositionOfBranchPage()
			throws Exception {
		assertEquals(1, wizardTree.getPositionOfPageFragment(branchPage.getFragment()));
	}

	@Test
	public void
	givenWizardTreeWithFinishedBranchPage_whenGetPositionOfPageFragmentAfterBranchPage_thenReturnPositionOfPage()
			throws Exception {
		branchPage.chooseBranch(0);
		assertEquals(2, wizardTree.getPositionOfPageFragment(secondPage.getFragment()));
	}

	@Test
	public void givenWizardTree_whenGetPositionOfInvalidPageFragment_thenReturnMinusOne() throws Exception {
		WizardTree wizardTree = new WizardTree(firstPage);
		assertEquals(-1, wizardTree.getPositionOfPageFragment(secondPage.getFragment()));
	}

	@Test
	public void givenWizardTreeWithOpenBranchPage_whenGetPositionOfPageFragmentAfterBranchPage_thenReturnMinusOne()
			throws Exception {
		assertEquals(-1, wizardTree.getPositionOfPageFragment(secondPage.getFragment()));
	}

	@Test
	public void givenWizardTreeWithBranchPage_whenChooseBranch_thenCallOnTreeChangedCallback() throws Exception {
		WizardTreeListener wizardTreeListener = mock(WizardTreeListener.class);
		wizardTree.setListener(wizardTreeListener);
		branchPage.chooseBranch(0);
		verify(wizardTreeListener, times(1)).onTreeChanged(2);
	}
}