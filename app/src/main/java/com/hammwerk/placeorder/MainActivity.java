package com.hammwerk.placeorder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.hammwerk.wizardpager.IntegerPage;
import com.hammwerk.wizardpager.MultiFixedChoicePage;
import com.hammwerk.wizardpager.SingleFixedChoiceBranchPage;
import com.hammwerk.wizardpager.SingleFixedChoicePage;
import com.hammwerk.wizardpager.core.Branch;
import com.hammwerk.wizardpager.core.Page;
import com.hammwerk.wizardpager.core.PageValidityListener;
import com.hammwerk.wizardpager.core.WizardPagerAdapter;
import com.hammwerk.wizardpager.core.WizardTree;

import java.util.List;

public class MainActivity extends AppCompatActivity {
	private WizardPagerAdapter adapter;
	private ViewPager viewPager;
	private Button nextButton;
	private Button backButton;
	private WizardTree wizardTree;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		wizardTree = createWizardTree();
		wizardTree.setPageValidityListener(new MyPageValidityListener());

		adapter = new WizardPagerAdapter(getSupportFragmentManager(), wizardTree);
		viewPager = (ViewPager) findViewById(R.id.activity_main_view_pager);
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new MySimpleOnPageChangeListener());

		nextButton = (Button) findViewById(R.id.activity_main_next_button);
		backButton = (Button) findViewById(R.id.activity_main_back_button);
	}

	public void onBackClick(View view) {
		viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
	}

	public void onNextClick(View view) {
		if (viewPager.getCurrentItem() < adapter.getCount() - 1) {
			viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
		} else {
			startActivity(createResultIntent());
		}
	}

	private WizardTree createWizardTree() {
		return new WizardTree(
				new SingleFixedChoiceBranchPage(getString(R.string.activity_main_order_type_title),
						new Branch(getString(R.string.activity_main_order_type_sandwich),
								new SingleFixedChoicePage(getString(R.string.activity_main_bread_title),
										getString(R.string.activity_main_bread_white),
										getString(R.string.activity_main_bread_wheat),
										getString(R.string.activity_main_bread_rye),
										getString(R.string.activity_main_bread_pretzel),
										getString(R.string.activity_main_bread_ciabatta)),
								new MultiFixedChoicePage(getString(R.string.activity_main_meats_title), true,
										getString(R.string.activity_main_meats_pepperoni),
										getString(R.string.activity_main_meats_turkey),
										getString(R.string.activity_main_meats_ham),
										getString(R.string.activity_main_meats_pastrami),
										getString(R.string.activity_main_meats_roast_beef),
										getString(R.string.activity_main_meats_bologna)),
								new MultiFixedChoicePage(getString(R.string.activity_main_veggies_title),
										getString(R.string.activity_main_veggies_tomatoes),
										getString(R.string.activity_main_veggies_lettuce),
										getString(R.string.activity_main_veggies_onions),
										getString(R.string.activity_main_veggies_pickles),
										getString(R.string.activity_main_veggies_cucumbers),
										getString(R.string.activity_main_veggies_peppers)),
								new MultiFixedChoicePage(getString(R.string.activity_main_cheeses_title),
										getString(R.string.activity_main_cheeses_swiss),
										getString(R.string.activity_main_cheeses_american),
										getString(R.string.activity_main_cheeses_pepperjack),
										getString(R.string.activity_main_cheeses_muenster),
										getString(R.string.activity_main_cheeses_provolone),
										getString(R.string.activity_main_cheeses_white_american),
										getString(R.string.activity_main_cheeses_cheddar),
										getString(R.string.activity_main_cheeses_bleu)),
								new SingleFixedChoiceBranchPage(getString(R.string.activity_main_toasted_title),
										new Branch(getString(R.string.activity_main_toasted_yes),
												new IntegerPage(getString(R.string.activity_main_toast_time_title),
														getString(R.string.activity_main_toast_time_minutes))),
										new Branch(getString(R.string.activity_main_toasted_no)))),
						new Branch(getString(R.string.activity_main_order_type_salad),
								new SingleFixedChoicePage(getString(R.string.activity_main_salad_type_title),
										getString(R.string.activity_main_salad_type_greek),
										getString(R.string.activity_main_salad_type_caesar)),
								new SingleFixedChoicePage(getString(R.string.activity_main_dressing_title),
										getString(R.string.activity_main_dressing_no_dressing),
										getString(R.string.activity_main_dressing_balsamic),
										getString(R.string.activity_main_dressing_oil_and_vinegar),
										getString(R.string.activity_main_dressing_thousand_island),
										getString(R.string.activity_main_dressing_italian))))) {

		};
	}

	private Intent createResultIntent() {
		Intent resultIntent = new Intent(this, ResultActivity.class);
		addResultExtras(resultIntent);
		return resultIntent;
	}

	private void addResultExtras(Intent resultIntent) {
		addOrderTypeExtra(resultIntent);
		if (isSandwichSelected()) {
			addSandwichExtras(resultIntent);
		} else {
			addSaladExtras(resultIntent);
		}
	}

	private void addOrderTypeExtra(Intent resultIntent) {
		Integer orderType = wizardTree.<SingleFixedChoiceBranchPage>getPage(0).getResult();
		resultIntent.putExtra(ResultActivity.EXTRA_ORDER_TYPE, orderType);
	}

	private boolean isSandwichSelected() {
		return wizardTree.<SingleFixedChoiceBranchPage>getPage(0).getResult() == 0;
	}

	private void addSandwichExtras(Intent resultIntent) {
		Integer bread = wizardTree.<SingleFixedChoicePage>getPage(1).getResult();
		List<Integer> meats = wizardTree.<MultiFixedChoicePage>getPage(2).getResult();
		List<Integer> veggies = wizardTree.<MultiFixedChoicePage>getPage(3).getResult();
		List<Integer> cheeses = wizardTree.<MultiFixedChoicePage>getPage(4).getResult();
		Integer toasted = wizardTree.<SingleFixedChoiceBranchPage>getPage(5).getResult();
		resultIntent
				.putExtra(ResultActivity.EXTRA_BREAD, bread)
				.putExtra(ResultActivity.EXTRA_MEATS, meats.toArray(new Integer[meats.size()]))
				.putExtra(ResultActivity.EXTRA_VEGGIES, veggies.toArray(new Integer[veggies.size()]))
				.putExtra(ResultActivity.EXTRA_CHEESES, cheeses.toArray(new Integer[cheeses.size()]))
				.putExtra(ResultActivity.EXTRA_TOASTED, toasted);

		if (isToastedSelected()) {
			addToastTimeExtra(resultIntent);
		}
	}

	private void addSaladExtras(Intent resultIntent) {
		Integer saladType = wizardTree.<SingleFixedChoicePage>getPage(1).getResult();
		Integer dressing = wizardTree.<SingleFixedChoicePage>getPage(2).getResult();
		resultIntent
				.putExtra(ResultActivity.EXTRA_SALAT_TYPE, saladType)
				.putExtra(ResultActivity.EXTRA_DRESSING, dressing);
	}

	private boolean isToastedSelected() {
		return wizardTree.<SingleFixedChoiceBranchPage>getPage(5).getResult() == 0;
	}

	private void addToastTimeExtra(Intent resultIntent) {
		Integer toastTimeInMinutes = wizardTree.<IntegerPage>getPage(6).getResult();
		resultIntent.putExtra(ResultActivity.EXTRA_TOAST_TIME, toastTimeInMinutes);
	}

	private class MyPageValidityListener implements PageValidityListener {
		@Override
		public void onPageValid(Page page, int pageIndex) {
			if (viewPager.getCurrentItem() == pageIndex) {
				nextButton.setEnabled(true);
			}
		}

		@Override
		public void onPageInvalid(Page page, int pageIndex) {
			if (viewPager.getCurrentItem() == pageIndex) {
				nextButton.setEnabled(false);
			}
		}
	}

	private class MySimpleOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener {
		@Override
		public void onPageSelected(int position) {
			backButton.setVisibility(position > 0 ? View.VISIBLE : View.INVISIBLE);
			nextButton.setEnabled(adapter.isPageValid(position));
			nextButton.setText(wizardTree.isLastPage(wizardTree.getPage(position)) ?
					getString(R.string.activity_main_review_order) :
					getString(R.string.activity_main_next));
		}
	}
}
