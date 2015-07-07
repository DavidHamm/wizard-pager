package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

public class BranchTest {
	private BranchPage branchPage;
	private Page firstPage;
	private Page secondPage;

	@Before
	public void setUp() throws Exception {
		firstPage = new TestPage("Title");
		secondPage = new TestPage("Title");
		Branch firstBranch = new Branch(firstPage);
		Branch secondBranch = new Branch(secondPage);
		branchPage = new TestBranchPage("Title", firstBranch, secondBranch);
	}

	@Test
	public void createBranch() throws Exception {
		new Branch(firstPage);
	}

	@Test
	public void createBranchWithName() throws Exception {
		new Branch("Name", firstPage);
	}

	@Test
	public void createEmptyBranch() throws Exception {
		new Branch();
	}

	@Test
	public void createEmptyBranchWithName() throws Exception {
		new Branch("Name");
	}

	@Test(expected = Branch.BranchPageBeforeLastPageException.class)
	public void createBranchWithBranchPageBeforeLastPage_shouldThrowBranchPageBeforeLastPageException()
			throws Exception {
		new Branch(branchPage, firstPage);
	}

	@Test
	public void getFirstPage_shouldReturnFirstPage() throws Exception {
		Branch branch = new Branch(firstPage, secondPage);
		assertEquals(firstPage, branch.getPage(0));
	}

	@Test
	public void getNumberOfPagesFromBranchWithThreePages_shouldReturnThree() throws Exception {
		Branch branch = new Branch(firstPage, secondPage, branchPage);
		assertEquals(3, branch.getNumberOfPages());
	}

	@Test
	public void getBranchPage_shouldReturnBranchPage() throws Exception {
		Branch branch = new Branch(firstPage, secondPage, branchPage);
		assertEquals(branchPage, branch.getBranchPage());
	}

	@Test
	public void getBranchPageFromBranchWithoutBranchPage_shouldReturnNull() throws Exception {
		Branch branch = new Branch(firstPage, secondPage);
		assertNull(branch.getBranchPage());
	}

	@Test
	public void givenEmptyBranch_whenGetBranchPage_then_returnNull() throws Exception {
		Branch branch = new Branch("Branch");
		assertNull(branch.getBranchPage());
	}
}