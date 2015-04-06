package com.leandroideias.transito.ache_os_erros;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import com.leandroideias.baseutils.BaseFragment;
import com.leandroideias.transito.R;

/**
 * Created by Leandro on 24/10/2014.
 */
public class FragmentTutorialAcheOsErros extends DialogFragment {
	private Button btnClose;
	private Button btnNext;

	public static FragmentTutorialAcheOsErros newInstance(){
		return new FragmentTutorialAcheOsErros();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		return inflater.inflate(R.layout.fragment_tutorial_ache_os_erros, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		btnClose = (Button) getView().findViewById(R.id.btnClose);
		btnNext = (Button) getView().findViewById(R.id.btnNext);

		btnClose.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view) {
				dismiss();
			}
		});
	}
}
