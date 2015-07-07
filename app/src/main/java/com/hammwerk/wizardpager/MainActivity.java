package com.hammwerk.wizardpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.hammwerk.wizardpager.core.Branch;
import com.hammwerk.wizardpager.core.Page;
import com.hammwerk.wizardpager.core.WizardPagerAdapter;
import com.hammwerk.wizardpager.core.WizardTree;
import com.hammwerk.wizardpager.core.WizardTreePageFinishedListener;

public class MainActivity extends AppCompatActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WizardTree wizardTree = new WizardTree(
				new SingleFixedChoiceBranchPage(getString(R.string.activity_main_order_type_title),
						new Branch(getString(R.string.activity_main_order_type_sandwich)),
						new Branch(getString(R.string.activity_main_order_type_salad))));

		wizardTree.setWizardTreePageFinishedListener(new WizardTreePageFinishedListener() {
			@Override
			public void onPageFinished(Page page, int pageIndex) {
				ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
				if (viewPager.getCurrentItem() == pageIndex) {
					findViewById(R.id.activity_main_next_button).setEnabled(true);
				}
			}
		});

		WizardPagerAdapter adapter = new WizardPagerAdapter(getSupportFragmentManager(), wizardTree);
		ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
		viewPager.setAdapter(adapter);
	}

	public void onBackClick(View view) {
	}

	public void onNextClick(View view) {
	}
}
