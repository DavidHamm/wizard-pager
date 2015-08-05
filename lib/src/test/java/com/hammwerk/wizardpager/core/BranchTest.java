package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(HierarchicalContextRunner.class)
public class BranchTest {
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void createBranch() throws Exception {
		Branch branch = new Branch();
		assertNull(branch.getName());
	}

	@Test
	public void createBranchWithName() throws Exception {
		Branch branch = new Branch("Name");
		assertEquals("Name", branch.getName());
	}

	public class GivenABranch {
		private Branch branch;

		@Before
		public void setUp() throws Exception {
			branch = new Branch();
		}

		@Test
		public void whenSetPages_thenReturnBranch() throws Exception {
			assertEquals(branch, branch.setPages());
		}

		@Test(expected = Branch.PageAfterBranchPageException.class)
		public void whenSetPagesWithPageAfterBranchPage_thenThrowPageAfterBranchPageException() throws Exception {
			branch.setPages(new TestBranchPage("Branch Page"), new TestPage("Page"));
		}

		@Test(expected = PageIndexOutOfBoundsException.class)
		public void whenGetPage_thenThrowPageIndexOutOfBoundsException() throws Exception {
			branch.getPage(0);
		}

		@Test
		public void whenGetNumberOfPages_thenReturnZero() throws Exception {
			assertEquals(0, branch.getNumberOfPages());
		}

		@Test
		public void whenGetBranchPage_thenReturnNull() throws Exception {
			assertNull(branch.getBranchPage());
		}

		@Test
		public void whenGetNumberOfValidPages_thenReturnZero() throws Exception {
			assertEquals(0, branch.getNumberOfValidPages());
		}

		public class GivenTowPages {
			private Page pageInFirstBranch;
			private Page pageInSecondBranch;

			@Before
			public void setUp() throws Exception {
				pageInFirstBranch = new TestPage("Page in first Branch");
				pageInSecondBranch = new TestPage("Page in second Branch");
				branch.setPages(pageInFirstBranch, pageInSecondBranch);
			}

			@Test
			public void whenGetFirstPage_thenReturnFirstPage() throws Exception {
				assertEquals(pageInFirstBranch, branch.getPage(0));
			}

			@Test(expected = PageIndexOutOfBoundsException.class)
			public void whenGetPageAfterLastPage_thenThrowPageIndexOutOfBoundsException() throws Exception {
				branch.getPage(2);
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

		public class GivenTwoPagesAndBranchPage {
			private BranchPage branchPage;
			private Page pageInFirstBranch;
			private Page pageInSecondBranch;

			@Before
			public void setUp() throws Exception {
				pageInFirstBranch = new TestPage("Page in first Branch");
				pageInSecondBranch = new TestPage("Page in second Branch");
				branchPage = new TestBranchPage("Title")
						.addBranch("First Branch", pageInFirstBranch)
						.addBranch("Second Branch", pageInSecondBranch);
				branch.setPages(pageInFirstBranch, pageInSecondBranch, branchPage);
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

		public class GivenARequiredPage {
			private Page requiredPage;

			@Before
			public void setUp() throws Exception {
				requiredPage = new TestPage("Required Page", true);
				branch.setPages(requiredPage);
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

		public class GivenTwoRequiredPages {
			private Page firstRequiredPage;
			private Page secondRequiredPage;

			@Before
			public void setUp() throws Exception {
				firstRequiredPage = new TestPage("First required Page", true);
				secondRequiredPage = new TestPage("Second required Page", true);
				branch.setPages(firstRequiredPage, secondRequiredPage);
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
}