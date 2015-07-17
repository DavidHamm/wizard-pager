package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.bechte.junit.runners.context.HierarchicalContextRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(HierarchicalContextRunner.class)
public class BranchPageTest {
	public class GivenABranchPage {
		private TestBranchPage branchPage;

		@Before
		public void setUp() {
			branchPage = new TestBranchPage("Branch Page");
		}

		@Test(expected = BranchPage.TwoBranchesRequiredException.class)
		public void whenGetSelectedBranch_throwTwoBranchesRequiredException() throws Exception {
			branchPage.getSelectedBranch();
		}

		@Test(expected = BranchPage.TwoBranchesRequiredException.class)
		public void whenSelectBranch_throwTwoBranchesRequiredException() throws Exception {
			branchPage.selectBranch(0);
		}

		@Test(expected = BranchPage.TwoBranchesRequiredException.class)
		public void whenGetChoices_throwTwoBranchesRequiredException() throws Exception {
			branchPage.getChoices();
		}

		@Test
		public void whenAskForValidity_returnInvalid() throws Exception {
			branchPage.isValid();
		}

		@Test
		public void whenGetResult_returnNull() throws Exception {
			assertNull(branchPage.getResult());
		}

		public class GivenAnEmptyBranch {
			@Before
			public void setUp() {
				branchPage.addBranch("Branch");
			}

			@Test(expected = BranchPage.TwoBranchesRequiredException.class)
			public void whenGetSelectedBranch_throwTwoBranchesRequiredException() throws Exception {
				branchPage.getSelectedBranch();
			}

			@Test(expected = BranchPage.TwoBranchesRequiredException.class)
			public void whenSelectBranch_throwTwoBranchesRequiredException() throws Exception {
				branchPage.selectBranch(0);
			}

			@Test(expected = BranchPage.TwoBranchesRequiredException.class)
			public void whenGetChoices_throwTwoBranchesRequiredException() throws Exception {
				branchPage.getChoices();
			}

			@Test
			public void whenAskForValidity_returnNull() throws Exception {
				branchPage.isValid();
			}

			@Test
			public void whenGetResult_returnNull() throws Exception {
				assertNull(branchPage.getResult());
			}
		}

		public class GivenTwoEmptyBranches {
			@Before
			public void setUp() {
				branchPage
						.addBranch("First Branch")
						.addBranch("Second Branch");
			}

			@Test
			public void whenGetSelectedBranch_returnNull() throws Exception {
				assertNull(branchPage.getSelectedBranch());
			}

			@Test
			public void whenAskForValidity_returnInvalid() throws Exception {
				assertFalse(branchPage.isValid());
			}

			@Test
			public void whenGetChoices_returnBranchNames() throws Exception {
				assertArrayEquals(new String[]{"First Branch", "Second Branch"}, branchPage.getChoices());
			}

			@Test
			public void whenGetResult_returnNull() throws Exception {
				assertNull(branchPage.getResult());
			}

			public class GivenASelectedBranch {
				@Before
				public void setUp() {
					branchPage.selectBranch(0);
				}

				@Test
				public void whenGetSelectedBranch_returnSelectedBranch() throws Exception {
					assertEquals("First Branch", branchPage.getSelectedBranch().getName());
				}

				@Test
				public void whenAskForValidity_returnValid() throws Exception {
					assertTrue(branchPage.isValid());
				}

				@Test
				public void whenGetResult_returnSelectedBranchIndex() throws Exception {
					assertEquals(Integer.valueOf(0), branchPage.getResult());
				}
			}

			public class GivenABranchPageListener {
				private BranchPageListener branchPageListener;

				@Before
				public void setup() throws Exception {
					branchPageListener = mock(BranchPageListener.class);
					branchPage.setBranchPageListener(branchPageListener);
				}

				public class GivenASelectedBranch {
					@Before
					public void setup() throws Exception {
						branchPage.selectBranch(0);
					}

					@Test
					public void verifyOnBranchSelectedWasCalledOnce() throws Exception {
						verify(branchPageListener, times(1)).onBranchSelected(branchPage);
					}
				}

				public class GivenABranchSelectedMultipleTimes {
					@Before
					public void setup() throws Exception {
						branchPage.selectBranch(0);
						branchPage.selectBranch(0);
					}

					@Test
					public void verifyOnBranchSelectedWasCalledOnce() throws Exception {
						verify(branchPageListener, times(1)).onBranchSelected(branchPage);
					}
				}
			}
		}

		public class GivenTwoBranchesWithPages {
			private Page pageInFirstBranch;

			@Before
			public void setup() throws Exception {
				pageInFirstBranch = new TestPage("Page in first Branch");
				branchPage
						.addBranch("First Branch", pageInFirstBranch)
						.addBranch("Second Branch", new TestPage("Page in second Branch"));
			}

			public class GivenASelectedBranch {
				@Before
				public void setup() throws Exception {
					branchPage.selectBranch(0);
				}

				@Test
				public void whenGetPageInSelectedBranch_returnPageInSelectedBranch() throws Exception {
					assertEquals(pageInFirstBranch, branchPage.getSelectedBranch().getPage(0));
				}
			}
		}
	}
}