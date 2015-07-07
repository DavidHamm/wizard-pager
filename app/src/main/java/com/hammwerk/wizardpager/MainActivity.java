package com.hammwerk.wizardpager;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hammwerk.wizardpager.core.Branch;
import com.hammwerk.wizardpager.core.Page;
import com.hammwerk.wizardpager.core.WizardPagerAdapter;
import com.hammwerk.wizardpager.core.WizardTree;
import com.hammwerk.wizardpager.core.WizardTreePageFinishedListener;

public class MainActivity extends AppCompatActivity {
	private WizardPagerAdapter adapter;
	private ViewPager viewPager;
	private Button nextButton;
	private Button backButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		WizardTree wizardTree = new WizardTree(
				new SingleFixedChoiceBranchPage(getString(R.string.activity_main_order_type_title),
						new Branch(getString(R.string.activity_main_order_type_sandwich),
								new SingleFixedChoicePage(getString(R.string.activity_main_bread_title),
										getString(R.string.activity_main_bread_white),
										getString(R.string.activity_main_bread_wheat),
										getString(R.string.activity_main_bread_rye),
										getString(R.string.activity_main_bread_pretzel),
										getString(R.string.activity_main_bread_ciabatta))),
						new Branch(getString(R.string.activity_main_order_type_salad),
								new SingleFixedChoicePage(getString(R.string.activity_main_salad_type_title),
										getString(R.string.activity_main_salad_type_greek),
										getString(R.string.activity_main_salad_type_caesar)))));

		wizardTree.setWizardTreePageFinishedListener(new WizardTreePageFinishedListener() {
			@Override
			public void onPageFinished(Page page, int pageIndex) {
				if (viewPager.getCurrentItem() == pageIndex) {
					nextButton.setEnabled(true);
				}
			}
		});

		adapter = new WizardPagerAdapter(getSupportFragmentManager(), wizardTree);
		viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				backButton.setVisibility(position > 0 ? View.VISIBLE : View.INVISIBLE);
				nextButton.setEnabled(adapter.isPageFinished(position));
			}
		});

		nextButton = (Button) findViewById(R.id.activity_main_next_button);
		backButton = (Button) findViewById(R.id.activity_main_back_button);
	}

	public void onBackClick(View view) {
		viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
		nextButton.setEnabled(true);
		if (viewPager.getCurrentItem() == 0) {
			backButton.setVisibility(View.INVISIBLE);
		}
	}

	public void onNextClick(View view) {
		if (viewPager.getCurrentItem() < adapter.getCount() - 1) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
			nextButton.setEnabled(adapter.isPageFinished(viewPager.getCurrentItem()));
			backButton.setVisibility(View.VISIBLE);
		}
	}
}
