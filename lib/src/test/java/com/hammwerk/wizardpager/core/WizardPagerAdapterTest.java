package com.hammwerk.wizardpager.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class WizardPagerAdapterTest {
	@Test
	public void createWizardPagerAdapter() throws Exception {
		new WizardPagerAdapter(null, new WizardTree(new TestPage("Page")));
	}

	@Test(expected = WizardPagerAdapter.WizardTreeIsNullException.class)
	public void whenCreateWizardPagerAdapterWithNullWizardTree_thenTrowNullPointerException() throws Exception {
		new WizardPagerAdapter(null, null);
	}

	@Test
	public void givenWizardPagerAdapterWithOnePage_whenGetCount_thenReturnOne() throws Exception {
		WizardPagerAdapter adapter = new WizardPagerAdapter(null, new WizardTree(new TestPage("Page")));
		assertEquals(1, adapter.getCount());
	}

	@Test
	public void givenWizardPagerAdapterWithFinishedPage_whenCallIsPageFinished_thenReturnTrue() throws Exception {
		TestPage page = new TestPage("Page");
		WizardPagerAdapter adapter = new WizardPagerAdapter(null, new WizardTree(page));
		page.setCompleted();
		assertTrue(adapter.isPageFinished(0));
	}

	@Test
	public void givenWizardPagerAdapterWithOnePage_whenGetItem_thenReturnFragmentOfPage() throws Exception {
		TestPage page = new TestPage("Page");
		WizardPagerAdapter adapter = new WizardPagerAdapter(null, new WizardTree(page));
		assertEquals(page.getFragment(), adapter.getItem(0));
	}

	@Test
	public void givenWizardPagerAdapterWithOnePage_whenGetItemPosition_thenReturnPositionNone() throws Exception {
		TestPage page = new TestPage("Page");
		WizardPagerAdapter adapter = new WizardPagerAdapter(null, new WizardTree(page));
		assertEquals(WizardPagerAdapter.POSITION_NONE, adapter.getItemPosition(page.getFragment()));
	}

	@Test
	public void
	givenWizardPagerAdapterWithBranchPage_whenGetItemPositionAfterBranchPageFinished_thenReturnPositionNone()
			throws Exception {
		TestPage page = new TestPage("First Page");
		TestBranchPage branchPage = new TestBranchPage("Branch Page",
				new Branch(new TestPage("Second Page")),
				new Branch(new TestPage("Third Page")));
		WizardPagerAdapter adapter = new WizardPagerAdapter(null, new WizardTree(page, branchPage)) {
			@Override
			public void notifyDataSetChanged() {
			}
		};
		branchPage.selectBranch(0);
		assertEquals(WizardPagerAdapter.POSITION_UNCHANGED, adapter.getItemPosition(page.getFragment()));
		assertEquals(WizardPagerAdapter.POSITION_UNCHANGED, adapter.getItemPosition(branchPage.getFragment()));
	}
}