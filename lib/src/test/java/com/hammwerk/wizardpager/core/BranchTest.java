package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(HierarchicalContextRunner.class)
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
	public void createBranchWithPageAfterBranchPage_shouldThrowBranchPageBeforeLastPageException() throws Exception {
		new Branch(branchPage, firstPage);
	}

	public class GivenEmptyBranch {
		private Branch branch;

		@Before
		public void setUp() throws Exception {
			branch = new Branch();
		}

		@Test
		public void givenEmptyBranch_whenGetBranchPage_thenReturnNull() throws Exception {
			assertNull(branch.getBranchPage());
		}
	}

	public class GivenBranchWithTowPages {
		private Branch branch;

		@Before
		public void setUp() throws Exception {
			branch = new Branch(firstPage, secondPage);
		}

		@Test
		public void whenGetFirstPage_thenReturnFirstPage() throws Exception {
			assertEquals(firstPage, branch.getPage(0));
		}

		@Test
		public void whenGetBranchPage_thenReturnNull() throws Exception {
			assertNull(branch.getBranchPage());
		}
	}

	public class GivenBranchWithTwoPagesAndBranchPage {
		private Branch branch;

		@Before
		public void setUp() throws Exception {
			branch = new Branch(firstPage, secondPage, branchPage);
		}

		@Test
		public void whenGetNumberOfPages_thenReturnNumberOfPages() throws Exception {
			assertEquals(3, branch.getNumberOfPages());
		}

		@Test
		public void whenGetBranchPage_thenReturnBranchPage() throws Exception {
			assertEquals(branchPage, branch.getBranchPage());
		}
	}
}