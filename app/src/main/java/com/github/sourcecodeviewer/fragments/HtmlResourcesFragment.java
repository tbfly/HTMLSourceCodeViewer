package com.github.sourcecodeviewer.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.github.sourcecodeviewer.MainActivity;
import com.github.sourcecodeviewer.MyApp;
import com.github.sourcecodeviewer.fragments.utils.ListRow;
import com.github.sourcecodeviewer.fragments.utils.ResourcesArrayAdapter;
import com.github.sourcecodeviewer.fragments.utils.WebUtils;

import java.util.ArrayList;

public class HtmlResourcesFragment extends ListFragment {

	public HtmlResourcesFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		MainActivity mainActivity = (MainActivity) this.getActivity();

		ArrayList<String> jsResources = mainActivity.htmlResources.get(MyApp.JS);
		ArrayList<String> cssResources = mainActivity.htmlResources.get(MyApp.CSS);
		ArrayList<String> bigimgResources = mainActivity.htmlResources.get(MyApp.BIGIMG);
		
		ArrayList<ListRow> result = new ArrayList<ListRow>();
		
		for(int i=0; i<jsResources.size(); i++) {
			result.add(new ListRow(MyApp.JS, WebUtils.getResourceName(jsResources.get(i)), jsResources.get(i)));
		}
		
		for(int j=0; j<cssResources.size(); j++) {
			result.add(new ListRow(MyApp.CSS, WebUtils.getResourceName(cssResources.get(j)), cssResources.get(j)));
		}

		if (bigimgResources != null && bigimgResources.size() > 0) {
			for (int k = 0; k < bigimgResources.size(); k++) {
				result.add(new ListRow(MyApp.BIGIMG, WebUtils.getResourceName(bigimgResources.get(k)), bigimgResources.get(k)));
			}
		}

		ResourcesArrayAdapter<ListRow> adapter = new ResourcesArrayAdapter<ListRow>(
				this.getActivity(), result);
		setListAdapter(adapter);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		ListRow item = (ListRow) getListAdapter().getItem(position);
		Toast.makeText(this.getActivity(), item.getResUrl(),
				Toast.LENGTH_LONG).show();
		
		String url = item.getResUrl();
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(url));
		startActivity(i);
	}
}
