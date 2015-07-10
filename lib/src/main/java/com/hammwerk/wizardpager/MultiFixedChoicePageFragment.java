package com.hammwerk.wizardpager;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.hammwerk.wizardpager.core.Page;

import java.util.ArrayList;
import java.util.List;

public class MultiFixedChoicePageFragment extends ListFragment {
	private static final String KEY_CHOICES = "com.hammwerk.wizardpager.key.CHOICES";
	private Page<List<Integer>> page;

	public static MultiFixedChoicePageFragment createInstance(Page<List<Integer>> page, String[] choices) {
		Bundle args = new Bundle();
		args.putStringArray(KEY_CHOICES, choices);
		MultiFixedChoicePageFragment fragment = new MultiFixedChoicePageFragment();
		fragment.setArguments(args);
		fragment.setPage(page);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<>(getActivity(),
				android.R.layout.simple_list_item_multiple_choice,
				android.R.id.text1,
				getArguments().getStringArray(KEY_CHOICES)));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_multi_fixed_choice_page, container, false);
		((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
		return view;
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		if (l.getCheckedItemCount() > 0) {
			page.setCompleted();
		} else {
			page.setNotCompleted();
		}
		page.setResult(getCheckedPositionsList(l.getCheckedItemPositions()));
	}

	private List<Integer> getCheckedPositionsList(SparseBooleanArray checkedItemPositions) {
		if (checkedItemPositions != null) {
			List<Integer> checkedPositions = getCheckedPositions(checkedItemPositions);
			if (!checkedPositions.isEmpty()) {
				return checkedPositions;
			}
		}
		return null;
	}

	private List<Integer> getCheckedPositions(SparseBooleanArray checkedItemPositions) {
		List<Integer> checkedPositions = new ArrayList<>();
		for (int i = 0; i < checkedItemPositions.size(); i++) {
			if (checkedItemPositions.valueAt(i)) {
				checkedPositions.add(i);
			}
		}
		return checkedPositions;
	}

	public void setPage(Page<List<Integer>> page) {
		this.page = page;
	}
}
