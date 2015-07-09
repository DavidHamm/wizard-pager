package com.hammwerk.wizardpager;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hammwerk.wizardpager.core.Page;

public class SingleFixedChoicePageFragment extends ListFragment {
	private static final String KEY_CHOICES = "com.hammwerk.wizardpager.key.CHOICES";
	private Page page;

	public static SingleFixedChoicePageFragment createInstance(Page page, String[] choices) {
		Bundle args = new Bundle();
		args.putStringArray(KEY_CHOICES, choices);
		SingleFixedChoicePageFragment fragment = new SingleFixedChoicePageFragment();
		fragment.setArguments(args);
		fragment.setPage(page);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_single_choice,
				android.R.id.text1,
				getArguments().getStringArray(KEY_CHOICES)));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_single_fixed_choice_page, container, false);
		((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		page.setCompleted();
	}

	public void setPage(Page page) {
		this.page = page;
	}
}