package com.github.sourcecodeviewer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.github.sourcecodeviewer.MainActivity;
import com.github.sourcecodeviewer.R;
import com.github.sourcecodeviewer.fragments.utils.WebClient;
import com.github.sourcecodeviewer.fragments.utils.WebUtils;


public class PreviewFragment extends WebViewFragment {

	public PreviewFragment() {
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_preview,
				container, false);

		MainActivity mainActivity = (MainActivity)this.getActivity();
		
		webView = (WebView) rootView.findViewById(R.id.site_preview);		
		webView.loadUrl(WebUtils.checkHttpPrefix(mainActivity.enteredUrl));
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new WebClient(this));

		return rootView;
	}
	
}
