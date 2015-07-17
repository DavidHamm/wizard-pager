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
		branchPage = new TestBranchPage("Title")
				.addBranch("First Branch", firstPage)
				.addBranch("Second Branch", secondPage);
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

	@Test(expected = Branch.PageAfterBranchPageException.class)
	public void createBranchWithPageAfterBranchPage_shouldThrowPageAfterBranchPageException() throws Exception {
		new Branch(branchPage, firstPage);
	}

	public class GivenEmptyBranch {
		private Branch branch;

		@Before
		public void setUp() throws Exception {
			branch = new Branch();
		}

		@Test
		public void whenGetBranchPage_thenReturnNull() throws Exception {
			assertNull(branch.getBranchPage());
		}

		@Test
		public void whenGetNumberOfValidPages_thenReturnZero() throws Exception {
			assertEquals(0, branch.getNumberOfValidPages());
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

		@Test
		public void whenGetNumberOfValidPages_thenReturnTwo() throws Exception {
			assertEquals(2, branch.getNumberOfValidPages());
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

	public class GivenBranchWithOneRequiredPage {
		private Branch branch;
		private Page requiredPage;

		@Before
		public void setUp() throws Exception {
			requiredPage = new TestPage("Required Page", true);
			branch = new Branch(requiredPage);
		}

		@Test
		public void whenGetNumberOfValidPages_thenReturnZero() throws Exception {
			assertEquals(0, branch.getNumberOfValidPages());
		}

		public class GivenCompletedRequiredPage {
			@Before
			public void setUp() throws Exception {
				requiredPage.setCompleted();
			}

			@Test
			public void whenGetNumberOfValidPages_thenReturnOne() throws Exception {
				assertEquals(1, branch.getNumberOfValidPages());
			}
		}
	}

	public class GivenBranchWithTwoRequiredPages {
		private Branch branch;
		private Page firstRequiredPage;
		private Page secondRequiredPage;

		@Before
		public void setUp() throws Exception {
			firstRequiredPage = new TestPage("First required Page", true);
			secondRequiredPage = new TestPage("Second required Page", true);
			branch = new Branch(firstRequiredPage, secondRequiredPage);
		}

		@Test
		public void whenGetNumberOfValidPages_thenReturnOne() throws Exception {
			assertEquals(0, branch.getNumberOfValidPages());
		}

		public class GivenCompletedFirstRequiredPage {
			@Before
			public void setUp() throws Exception {
				firstRequiredPage.setCompleted();
			}

			@Test
			public void whenGetNumberOfValidPages_thenReturnOne() throws Exception {
				assertEquals(1, branch.getNumberOfValidPages());
			}
		}

		public class GivenCompletedAllRequiredPage {
			@Before
			public void setUp() throws Exception {
				firstRequiredPage.setCompleted();
				secondRequiredPage.setCompleted();
			}

			@Test
			public void whenGetNumberOfValidPages_thenReturnOne() throws Exception {
				assertEquals(2, branch.getNumberOfValidPages());
			}
		}
	}
}