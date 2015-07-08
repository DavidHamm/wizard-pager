package com.hammwerk.wizardpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hammwerk.wizardpager.core.Page;

public class IntegerPageFragment extends Fragment {
	private static final String KEY_UNIT = "com.hammwerk.wizardpager.key.UNIT";
	private Page page;

	public static IntegerPageFragment createInstance(Page page, String unit) {
		Bundle args = new Bundle();
		args.putString(KEY_UNIT, unit);
		IntegerPageFragment fragment = new IntegerPageFragment();
		fragment.setArguments(args);
		fragment.setPage(page);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_integer_page, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((TextView) view.findViewById(android.R.id.title)).setText(page.getTitle());
		((TextView) view.findViewById(R.id.fragment_integer_page_unit_text_view))
				.setText(getArguments().getString(KEY_UNIT));
		((EditText) view.findViewById(R.id.fragment_integer_page_edit_text))
				.addTextChangedListener(new CompletedTextWatcher());
	}

	public void setPage(Page page) {
		this.page = page;
	}

	private class CompletedTextWatcher implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (TextUtils.isEmpty(s)) {
				page.setNotCompleted();
			} else {
				page.setCompleted();
			}
		}
	}
}
