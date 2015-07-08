package com.hammwerk.wizardpager;

import com.hammwerk.wizardpager.core.Branch;

import org.junit.Test;

public class SingleFixedChoiceBranchPageTest {
	@Test
	public void createSingleChoicePage() throws Exception {
		new SingleFixedChoiceBranchPage("SingleFixedChoiceBranchPage",
				new Branch("First Choice"),
				new Branch("Second Choice"));
	}
}