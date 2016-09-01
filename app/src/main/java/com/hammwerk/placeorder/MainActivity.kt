package com.hammwerk.placeorder

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import com.hammwerk.wizardpager.*
import com.hammwerk.wizardpager.core.Page
import com.hammwerk.wizardpager.core.WizardPagerAdapter
import com.hammwerk.wizardpager.core.WizardTree

class MainActivity : AppCompatActivity(), Wizardable {
	override fun <T : Page> getPage(position: Int): T {
		return wizardTree.getPage<T>(position)
	}

	private val viewPager: ViewPager by lazy { findViewById(R.id.activity_main_view_pager) as ViewPager }
	private val nextButton: Button by lazy { findViewById(R.id.activity_main_next_button) as Button }
	private val backButton: Button by lazy { findViewById(R.id.activity_main_back_button) as Button }

	private lateinit var adapter: WizardPagerAdapter
	private lateinit var wizardTree: WizardTree

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		if (savedInstanceState != null) {
			wizardTree = savedInstanceState.getParcelable(KEY_WIZARD_TREE)
		} else {
			wizardTree = createWizardTree()
		}

		wizardTree.onTreeChanged = {
			nextButton.text = getString(if (wizardTree.isLastPage(wizardTree.getPage(viewPager.currentItem)))
				R.string.activity_main_review_order
			else
				R.string.activity_main_next)
		}
		wizardTree.onPageValid = { page, index ->
			if (viewPager.currentItem == index) {
				nextButton.isEnabled = true
			}
		}
		wizardTree.onPageInvalid = { page, index ->
			if (viewPager.currentItem == index) {
				nextButton.isEnabled = false
			}
		}

		adapter = WizardPagerAdapter(this, supportFragmentManager).apply {
			wizardTree = this@MainActivity.wizardTree
		}
		viewPager.apply {
			adapter = this@MainActivity.adapter
			addOnPageChangeListener(MySimpleOnPageChangeListener())
		}
		backButton.apply {
			visibility = if (viewPager.currentItem > 0) View.VISIBLE else View.INVISIBLE
			setOnClickListener { viewPager.currentItem = viewPager.currentItem - 1 }
		}
		nextButton.apply {
			isEnabled = adapter.isPageValid(viewPager.currentItem)
			text = if (wizardTree.isLastPage(wizardTree.getPage(viewPager.currentItem)))
				getString(R.string.activity_main_review_order)
			else
				getString(R.string.activity_main_next)
			setOnClickListener {
				if (viewPager.currentItem < adapter.count - 1) {
					viewPager.currentItem = viewPager.currentItem + 1
				} else {
					startActivity(createResultIntent())
				}
			}
		}
	}

	override fun onSaveInstanceState(outState: Bundle?) {
		super.onSaveInstanceState(outState)
		outState?.putParcelable(KEY_WIZARD_TREE, wizardTree)
	}

	private fun createWizardTree(): WizardTree {
		return WizardTree().apply {
			setPages(SingleFixedChoiceBranchPage(getString(R.string.activity_main_order_type_title)).apply {
				addBranch(getString(R.string.activity_main_order_type_sandwich),
						SingleFixedChoicePage(getString(R.string.activity_main_bread_title),
								getString(R.string.activity_main_bread_white),
								getString(R.string.activity_main_bread_wheat),
								getString(R.string.activity_main_bread_rye),
								getString(R.string.activity_main_bread_pretzel),
								getString(R.string.activity_main_bread_ciabatta)),
						MultiFixedChoicePage(getString(R.string.activity_main_meats_title),
								getString(R.string.activity_main_meats_pepperoni),
								getString(R.string.activity_main_meats_turkey),
								getString(R.string.activity_main_meats_ham),
								getString(R.string.activity_main_meats_pastrami),
								getString(R.string.activity_main_meats_roast_beef),
								getString(R.string.activity_main_meats_bologna),
								required = true),
						MultiFixedChoicePage(getString(R.string.activity_main_veggies_title),
								getString(R.string.activity_main_veggies_tomatoes),
								getString(R.string.activity_main_veggies_lettuce),
								getString(R.string.activity_main_veggies_onions),
								getString(R.string.activity_main_veggies_pickles),
								getString(R.string.activity_main_veggies_cucumbers),
								getString(R.string.activity_main_veggies_peppers)),
						MultiFixedChoicePage(getString(R.string.activity_main_cheeses_title),
								getString(R.string.activity_main_cheeses_swiss),
								getString(R.string.activity_main_cheeses_american),
								getString(R.string.activity_main_cheeses_pepperjack),
								getString(R.string.activity_main_cheeses_muenster),
								getString(R.string.activity_main_cheeses_provolone),
								getString(R.string.activity_main_cheeses_white_american),
								getString(R.string.activity_main_cheeses_cheddar),
								getString(R.string.activity_main_cheeses_bleu)),
						SingleFixedChoiceBranchPage(getString(R.string.activity_main_toasted_title)).apply {
							addBranch(getString(R.string.activity_main_toasted_yes),
									IntegerPage(getString(R.string.activity_main_toast_time_title),
											unit = getString(R.string.activity_main_toast_time_minutes)))
							addBranch(getString(R.string.activity_main_toasted_no))
						})
				addBranch(getString(R.string.activity_main_order_type_salad),
						SingleFixedChoicePage(getString(R.string.activity_main_salad_type_title),
								getString(R.string.activity_main_salad_type_greek),
								getString(R.string.activity_main_salad_type_caesar)),
						SingleFixedChoicePage(getString(R.string.activity_main_dressing_title),
								getString(R.string.activity_main_dressing_no_dressing),
								getString(R.string.activity_main_dressing_balsamic),
								getString(R.string.activity_main_dressing_oil_and_vinegar),
								getString(R.string.activity_main_dressing_thousand_island),
								getString(R.string.activity_main_dressing_italian)))
			})
		}
	}

	private fun createResultIntent(): Intent {
		fun Intent.addResultExtras() {

			fun addOrderTypeExtra() {
				wizardTree.getPage<SingleFixedChoiceBranchPage>(0).selectedBranchIndex
						?.let { putExtra(ResultActivity.EXTRA_ORDER_TYPE, it) }
			}

			fun isSandwichSelected() = wizardTree.getPage<SingleFixedChoiceBranchPage>(0).selectedBranchIndex === 0

			fun addSandwichExtras() {
				fun isToastedSelected() = wizardTree.getPage<SingleFixedChoiceBranchPage>(5).selectedBranchIndex === 0

				wizardTree.getPage<SingleFixedChoicePage>(1).selectedChoice?.let {
					putExtra(ResultActivity.EXTRA_BREAD, it)
				}
				wizardTree.getPage<MultiFixedChoicePage>(2).selectedChoices?.let {
					putExtra(ResultActivity.EXTRA_MEATS, it.toTypedArray())
				}
				wizardTree.getPage<MultiFixedChoicePage>(3).selectedChoices?.let {
					putExtra(ResultActivity.EXTRA_VEGGIES, it.toTypedArray())
				}
				wizardTree.getPage<MultiFixedChoicePage>(4).selectedChoices?.let {
					putExtra(ResultActivity.EXTRA_CHEESES, it.toTypedArray())
				}
				wizardTree.getPage<SingleFixedChoiceBranchPage>(5).selectedBranchIndex?.let {
					putExtra(ResultActivity.EXTRA_TOASTED, it)
				}
				if (isToastedSelected()) {
					wizardTree.getPage<IntegerPage>(6).value?.let { putExtra(ResultActivity.EXTRA_TOAST_TIME, it) }
				}
			}

			fun addSaladExtras() {
				wizardTree.getPage<SingleFixedChoicePage>(1).selectedChoice?.let {
					putExtra(ResultActivity.EXTRA_SALAT_TYPE, it)
				}
				wizardTree.getPage<SingleFixedChoicePage>(2).selectedChoice?.let {
					putExtra(ResultActivity.EXTRA_DRESSING, it)
				}
			}

			addOrderTypeExtra()
			if (isSandwichSelected()) {
				addSandwichExtras()
			} else {
				addSaladExtras()
			}
		}

		return Intent(this, ResultActivity::class.java).apply {
			addResultExtras()
		}
	}

	private inner class MySimpleOnPageChangeListener : ViewPager.SimpleOnPageChangeListener() {
		override fun onPageSelected(position: Int) {
			backButton.visibility = if (position > 0) View.VISIBLE else View.INVISIBLE
			nextButton.isEnabled = adapter.isPageValid(position)
			nextButton.text = if (wizardTree.isLastPage(wizardTree.getPage(position)))
				getString(R.string.activity_main_review_order)
			else
				getString(R.string.activity_main_next)
		}
	}

	companion object {
		private const val KEY_WIZARD_TREE = "${BuildConfig.APPLICATION_ID}.key.WIZARD_TREE"
	}
}
