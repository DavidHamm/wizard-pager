package com.hammwerk.wizardpager.core;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(Enclosed.class)
public class BranchPageTest {
	@Test(expected = BranchPage.LessThenTwoBranchesException.class)
	public void createEmptyBranchPage_shouldThrowLessThenTwoBranchesException() throws Exception {
		new TestBranchPage("Branch Page");
	}

	@Test(expected = BranchPage.LessThenTwoBranchesException.class)
	public void createBranchPageWithOneBranch_shouldThrowLessThenTwoBranchesException() throws Exception {
		new TestBranchPage("Branch Page", new Branch("Branch", new TestPage("Page")));
	}

	public abstract static class GivenABranchPage {
		protected BranchPage branchPage;
		protected Branch firstBranch;

		@Before
		public void givenABranchPage() throws Exception {
			firstBranch = new Branch("First Branch", new TestPage("First Page"));
			Branch secondBranch = new Branch("Second Branch", new TestPage("Second Page"));
			branchPage = new TestBranchPage("Branch Page", firstBranch, secondBranch);
		}
	}

	public static class GivenNoSelectedBranch extends GivenABranchPage {
		@Test
		public void whenGetChoosenBranch_thenReturnNull() throws Exception {
			assertNull(branchPage.getSelectedBranch());
		}

		@Test
		public void whenAskForValidity_thenReturnFalse() throws Exception {
			assertFalse(branchPage.isValid());
		}

		@Test
		public void whenGetChoices_thenReturnChoices() throws Exception {
			assertArrayEquals(new String[]{"First Branch", "Second Branch"}, branchPage.getChoices());
		}
	}

	public static class GivenSelectedBranch extends GivenABranchPage {
		@Before
		public void givenSelectedBranch() throws Exception {
			branchPage.selectBranch(0);
		}

		@Test
		public void whenGetSelectedBranch_thenReturnSelectedBranch() throws Exception {
			assertEquals(firstBranch, branchPage.getSelectedBranch());
		}

		@Test
		public void whenAskForValidity_thenReturnTrue() throws Exception {
			assertTrue(branchPage.isValid());
		}
	}

	public static class GivenBranchPageListener extends GivenABranchPage {
		protected BranchPageListener branchPageListener;

		@Before
		public void givenListenerSet() throws Exception {
			branchPageListener = mock(BranchPageListener.class);
			branchPage.setBranchPageListener(branchPageListener);
		}

		@Test
		public void whenChooseBranch_thenCallOnBranchChoosenCallback() throws Exception {
			branchPage.selectBranch(0);
			verify(branchPageListener, times(1)).onBranchChoosen(branchPage);
		}

		@Test
		public void whenChooseBranchMultipleTimes_thenCallOnBranchChoosenCallbackOneTime() throws Exception {
			branchPage.selectBranch(0);
			branchPage.selectBranch(0);
			verify(branchPageListener, times(1)).onBranchChoosen(branchPage);
		}
	}
}