package com.github.sourcecodeviewer.fragments.utils;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;

import com.github.sourcecodeviewer.MyApp;
import com.github.sourcecodeviewer.TypeUrlActivity;
import com.github.sourcecodeviewer.fragments.SourceCodeFragment;

public class JsInterface {
	SourceCodeFragment frgCaller;

	/** Instantiate the interface and set the context */
	public JsInterface(SourceCodeFragment f) {
		frgCaller = f;
	}

	@JavascriptInterface
	public void showFindBar() {		
		frgCaller.showFindBar();
	}
	
	@JavascriptInterface
	public void tryAgain() {
		Intent intent = new Intent(frgCaller.getActivity(), TypeUrlActivity.class);
		frgCaller.webWiewReady = false;
        Bundle b = new Bundle();
        b.putBoolean(MyApp.RETRY, true);
        intent.putExtras(b);
		frgCaller.getActivity().startActivity(intent);
		frgCaller.getActivity().finish();
	}
}
