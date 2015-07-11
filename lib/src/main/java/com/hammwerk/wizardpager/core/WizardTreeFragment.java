package com.hammwerk.wizardpager.core;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class WizardTreeFragment extends Fragment {
	private WizardTree wizardTree;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	public WizardTree getWizardTree() {
		return wizardTree;
	}

	public void setWizardTree(WizardTree wizardTree) {
		this.wizardTree = wizardTree;
	}
}
