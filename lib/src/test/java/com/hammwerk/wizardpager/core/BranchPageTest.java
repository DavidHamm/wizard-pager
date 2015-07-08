package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class BranchPageTest {
	private BranchPage branchPage;
	private Branch firstBranch;

	@Before
	public void setUp() throws Exception {
		Page page = new TestPage("Title");
		firstBranch = new Branch("First Branch", page);
		branchPage = new TestBranchPage("Title", firstBranch, new Branch("Second Branch", page));
	}

	@Test(expected = BranchPage.LessThenTwoBranchesException.class)
	public void createEmptyBranchPage_shouldThrowLessThenTwoBranchesException() throws Exception {
		new TestBranchPage("Title");
	}

	@Test(expected = BranchPage.LessThenTwoBranchesException.class)
	public void createBranchPageWithOneBranch_shouldThrowLessThenTwoBranchesException() throws Exception {
		new TestBranchPage("Title", firstBranch);
	}

	@Test
	public void givenBranchPage_whenNoBranchIsChoosen_thenReturnNull() throws Exception {
		assertNull(branchPage.getChoosenBranch());
	}

	@Test
	public void givenBranchPage_whenChooseBranch_thenReturnChoosenBranch() throws Exception {
		branchPage.chooseBranch(0);
		assertEquals(firstBranch, branchPage.getChoosenBranch());
	}

	@Test
	public void givenBranchPage_whenChooseBranch_thenCallOnBranchChoosenCallback() throws Exception {
		BranchPageListener listener = mock(BranchPageListener.class);
		branchPage.setBranchPageListener(listener);
		branchPage.chooseBranch(0);
		verify(listener, times(1)).onBranchChoosen(branchPage);
	}

	@Test
	public void givenBranchPage_whenChooseBranchMultipleTimes_thenCallOnBranchChoosenCallbackOneTime()
			throws Exception {
		BranchPageListener listener = mock(BranchPageListener.class);
		branchPage.setBranchPageListener(listener);
		branchPage.chooseBranch(0);
		branchPage.chooseBranch(0);
		verify(listener, times(1)).onBranchChoosen(branchPage);
	}

	@Test
	public void givenBranchPageWithTwoBranches_whenGetChoices_thenReturnChoices() throws Exception {
		assertArrayEquals(new String[]{"First Branch", "Second Branch"}, branchPage.getChoices());
	}
}