package com.hammwerk.placeorder;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity {
	public static final String EXTRA_ORDER_TYPE = "com.hammwerk.placeorder.extra.ORDER_TYPE";
	public static final String EXTRA_BREAD = "com.hammwerk.placeorder.extra.BREAD";
	public static final String EXTRA_MEATS = "com.hammwerk.placeorder.extra.MEATS";
	public static final String EXTRA_VEGGIES = "com.hammwerk.placeorder.extra.VEGGIES";
	public static final String EXTRA_CHEESES = "com.hammwerk.placeorder.extra.CHEESES";
	public static final String EXTRA_TOASTED = "com.hammwerk.placeorder.extra.TOASTED";
	public static final String EXTRA_TOAST_TIME = "com.hammwerk.placeorder.extra.TOAST_TIME";
	public static final String EXTRA_SALAT_TYPE = "com.hammwerk.placeorder.extra.SALAT_TYPE";
	public static final String EXTRA_DRESSING = "com.hammwerk.placeorder.extra.DRESSING";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);

		ResultAdapter adapter = new ResultAdapter(this);
		adapter.setItems(createResultItems());

		ListView listView = (ListView) findViewById(android.R.id.list);
		listView.setAdapter(adapter);
	}

	private List<ResultAdapter.ResultItem> createResultItems() {
		ArrayList<ResultAdapter.ResultItem> resultItems = new ArrayList<>();
		resultItems.add(createOrderTypeResultItem());
		int orderType = getIntent().getIntExtra(EXTRA_ORDER_TYPE, -1);
		if (orderType == 0) {
			resultItems.add(createBreadResultItem());
			resultItems.add(createMeatsResultItem());
			resultItems.add(createVeggiesResultItem());
			resultItems.add(createCheesesResultItem());
			resultItems.add(createToastedResultItem());
			if (getIntent().getIntExtra(EXTRA_TOASTED, -1) == 0) {
				resultItems.add(createToastTimeResultItem());
			}
		} else if (orderType == 1) {
			resultItems.add(createSalatTypeResultItem());
			resultItems.add(createDressingResultItem());
		}
		return resultItems;
	}

	private ResultAdapter.ResultItem createOrderTypeResultItem() {
		int orderType = getIntent().getIntExtra(EXTRA_ORDER_TYPE, -1);
		if (orderType == 0) {
			return new ResultAdapter.ResultItem(getString(R.string.activity_main_order_type_title),
					getString(R.string.activity_main_order_type_sandwich));
		}
		if (orderType == 1) {
			return new ResultAdapter.ResultItem(getString(R.string.activity_main_order_type_title),
					getString(R.string.activity_main_order_type_salad));
		}
		throw new RuntimeException("No Order Type selected");
	}

	private ResultAdapter.ResultItem createBreadResultItem() {
		Integer bread = getIntent().getIntExtra(EXTRA_BREAD, -1);
		switch (bread) {
			case 0:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_bread_title),
						getString(R.string.activity_main_bread_white));
			case 1:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_bread_title),
						getString(R.string.activity_main_bread_wheat));
			case 2:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_bread_title),
						getString(R.string.activity_main_bread_rye));
			case 3:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_bread_title),
						getString(R.string.activity_main_bread_pretzel));
			case 4:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_bread_title),
						getString(R.string.activity_main_bread_ciabatta));
			default:
				throw new RuntimeException("No Bread selected");
		}
	}

	private ResultAdapter.ResultItem createMeatsResultItem() {
		Integer[] meats = (Integer[]) getIntent().getSerializableExtra(EXTRA_MEATS);
		if (meats != null && meats.length > 0) {
			return new ResultAdapter.ResultItem(getString(R.string.activity_main_meats_title),
					TextUtils.join(", ", getMeatsStrings(meats)));
		}
		throw new RuntimeException("No Meats selected");
	}

	private String[] getMeatsStrings(Integer[] meats) {
		String[] meatsStrings = new String[meats.length];
		for (int i = 0; i < meats.length; i++) {
			switch (meats[i]) {
				case 0:
					meatsStrings[i] = getString(R.string.activity_main_meats_pepperoni);
					break;
				case 1:
					meatsStrings[i] = getString(R.string.activity_main_meats_turkey);
					break;
				case 2:
					meatsStrings[i] = getString(R.string.activity_main_meats_ham);
					break;
				case 3:
					meatsStrings[i] = getString(R.string.activity_main_meats_pastrami);
					break;
				case 4:
					meatsStrings[i] = getString(R.string.activity_main_meats_roast_beef);
					break;
				case 5:
					meatsStrings[i] = getString(R.string.activity_main_meats_bologna);
					break;
				default:
					throw new RuntimeException("Unknown Meat");
			}
		}
		return meatsStrings;
	}

	private ResultAdapter.ResultItem createVeggiesResultItem() {
		Integer[] veggies = (Integer[]) getIntent().getSerializableExtra(EXTRA_VEGGIES);
		if (veggies != null && veggies.length > 0) {
			return new ResultAdapter.ResultItem(getString(R.string.activity_main_veggies_title),
					TextUtils.join(", ", getVeggiesStrings(veggies)));
		}
		return new ResultAdapter.ResultItem(getString(R.string.activity_main_veggies_title),
				getString(R.string.activity_result_veggies_no));
	}

	private String[] getVeggiesStrings(Integer[] veggies) {
		String[] veggiesStrings = new String[veggies.length];
		for (int i = 0; i < veggies.length; i++) {
			switch (veggies[i]) {
				case 0:
					veggiesStrings[i] = getString(R.string.activity_main_veggies_tomatoes);
					break;
				case 1:
					veggiesStrings[i] = getString(R.string.activity_main_veggies_lettuce);
					break;
				case 2:
					veggiesStrings[i] = getString(R.string.activity_main_veggies_onions);
					break;
				case 3:
					veggiesStrings[i] = getString(R.string.activity_main_veggies_pickles);
					break;
				case 4:
					veggiesStrings[i] = getString(R.string.activity_main_veggies_cucumbers);
					break;
				case 5:
					veggiesStrings[i] = getString(R.string.activity_main_veggies_peppers);
					break;
				default:
					throw new RuntimeException("Unknown Veggie");
			}
		}
		return veggiesStrings;
	}

	private ResultAdapter.ResultItem createCheesesResultItem() {
		Integer[] cheeses = (Integer[]) getIntent().getSerializableExtra(EXTRA_CHEESES);
		if (cheeses != null && cheeses.length > 0) {
			return new ResultAdapter.ResultItem(getString(R.string.activity_main_cheeses_title),
					TextUtils.join(", ", getCheesesStrings(cheeses)));
		}
		return new ResultAdapter.ResultItem(getString(R.string.activity_main_cheeses_title),
				getString(R.string.activity_result_cheese_no));
	}

	private String[] getCheesesStrings(Integer[] cheeses) {
		String[] cheesesStrings = new String[cheeses.length];
		for (int i = 0; i < cheeses.length; i++) {
			switch (cheeses[i]) {
				case 0:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_swiss);
					break;
				case 1:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_american);
					break;
				case 2:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_pepperjack);
					break;
				case 3:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_muenster);
					break;
				case 4:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_provolone);
					break;
				case 5:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_white_american);
					break;
				case 6:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_cheddar);
					break;
				case 7:
					cheesesStrings[i] = getString(R.string.activity_main_cheeses_bleu);
					break;
				default:
					throw new RuntimeException("Unknown Cheese");
			}
		}
		return cheesesStrings;
	}

	private ResultAdapter.ResultItem createToastedResultItem() {
		int toasted = getIntent().getIntExtra(EXTRA_TOASTED, -1);
		switch (toasted) {
			case 0:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_toasted_title),
						getString(R.string.activity_main_toasted_yes));
			case 1:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_toasted_title),
						getString(R.string.activity_main_toasted_no));
			default:
				throw new RuntimeException("Toasted not selected");
		}
	}

	private ResultAdapter.ResultItem createToastTimeResultItem() {
		if (getIntent().hasExtra(EXTRA_TOAST_TIME)) {
			return new ResultAdapter.ResultItem(getString(R.string.activity_main_toast_time_title),
					Integer.toString(getIntent().getIntExtra(EXTRA_TOAST_TIME, 0)) + " " +
							getString(R.string.activity_main_toast_time_minutes));
		}
		throw new RuntimeException("Toast Time not set");
	}

	private ResultAdapter.ResultItem createSalatTypeResultItem() {
		Integer saladType = getIntent().getIntExtra(EXTRA_SALAT_TYPE, -1);
		switch (saladType) {
			case 0:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_salad_type_title),
						getString(R.string.activity_main_salad_type_greek));
			case 1:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_salad_type_title),
						getString(R.string.activity_main_salad_type_caesar));
			default:
				throw new RuntimeException("No Salad Type selected");
		}
	}

	private ResultAdapter.ResultItem createDressingResultItem() {
		Integer dressing = getIntent().getIntExtra(EXTRA_DRESSING, -1);
		switch (dressing) {
			case 0:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_dressing_title),
						getString(R.string.activity_main_dressing_no_dressing));
			case 1:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_dressing_title),
						getString(R.string.activity_main_dressing_balsamic));
			case 2:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_dressing_title),
						getString(R.string.activity_main_dressing_oil_and_vinegar));
			case 3:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_dressing_title),
						getString(R.string.activity_main_dressing_thousand_island));
			case 4:
				return new ResultAdapter.ResultItem(getString(R.string.activity_main_dressing_title),
						getString(R.string.activity_main_dressing_italian));
			default:
				throw new RuntimeException("No Dressing selected");
		}
	}

	public void onBackClick(View view) {
		finish();
	}

	public void onSubmitOrderClick(View view) {
	}

	private static class ResultAdapter extends BaseAdapter {
		private final List<ResultItem> items = new ArrayList<>();
		private final LayoutInflater inflater;

		private ResultAdapter(Context context) {
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public ResultItem getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = createView(parent);
			}
			bindView(position, convertView);
			return convertView;
		}

		public void setItems(List<ResultItem> items) {
			this.items.clear();
			this.items.addAll(items);
			notifyDataSetChanged();
		}

		private View createView(ViewGroup parent) {
			return inflater.inflate(R.layout.list_item_result, parent, false);
		}

		private void bindView(int position, View convertView) {
			((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position).title);
			((TextView) convertView.findViewById(android.R.id.text2)).setText(getItem(position).value);
		}

		private static class ResultItem {
			private String title;
			private String value;

			public ResultItem(String title, String value) {
				this.title = title;
				this.value = value;
			}
		}
	}
}
