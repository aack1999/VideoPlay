package com.qxhd.yellow.ui.adapter;

import android.support.v4.app.Fragment;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;

import java.util.List;

public class FragmentAdapter implements FragmentNavigatorAdapter {

	List<Fragment> fragments;

	public FragmentAdapter(List<Fragment> fragments) {
		this.fragments = fragments;
	}

	@Override
	public Fragment onCreateFragment(int position) {
		return fragments.get(position);
	}

	@Override
	public String getTag(int position) {
		// an simple unique tag
		return position + "";
	}

	@Override
	public int getCount() {
		return fragments.size();
	}
}