package com.leandroideias.transito.jogo_da_memoria;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.leandroideias.transito.R;
import com.leandroideias.transito.application.AbstractActivityMenu;
import com.leandroideias.transito.application.Constants;
import com.leandroideias.transito.model.ItemDatabase;

/**
 * Created by leandro.castro on 22/05/2014.
 */
public class FragmentGameCompletedJogoDaMemoria extends DialogFragment implements View.OnClickListener {
	public static final String TAG = "FragmentGameCompletedJogoDaMemoria";
	private ItemDatabase itemDatabase;

	private ImageView star1, star2, star3;
	private TextView textViewPontos;
	private ImageView icMenu, icReload, icNext;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(!(activity instanceof AbstractActivityMenu)){
			throw new RuntimeException("O FragmentLevelCompletedAcheOsErros deve ser instanciado por uma subclasse de AbstractActivityMenu");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			itemDatabase = (ItemDatabase) getArguments().getSerializable("itemDatabaseJogoDaVelha");
			assert(itemDatabase != null);
		}catch(Exception e){
			throw new RuntimeException("Não foi enviado os dados do level para o fragmento de level concluído.");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCancelable(false);
		View view = inflater.inflate(R.layout.fragment_completed_level_jogo_da_memoria, container, false);
		initView(view);

		return view;
	}

	private void initView(View view) {
		star1 = (ImageView) view.findViewById(R.id.imageViewStar1Completed);
		star2 = (ImageView) view.findViewById(R.id.imageViewStar2Completed);
		star3 = (ImageView) view.findViewById(R.id.imageViewStar3Completed);
		textViewPontos = (TextView) view.findViewById(R.id.textViewPontosCompleted);
		icMenu = (ImageView) view.findViewById(R.id.icMenuCompleted);
		icReload = (ImageView) view.findViewById(R.id.icReloadCompleted);
		icNext = (ImageView) view.findViewById(R.id.icNextCompleted);

		textViewPontos.setText("" + itemDatabase.getPontos());

		icMenu.setOnClickListener(this);
		icReload.setOnClickListener(this);
		icNext.setOnClickListener(this);

		int quantEstrelas = itemDatabase.getQuantEstrelas();
		if(quantEstrelas >= 1){
			star1.setImageResource(android.R.drawable.star_big_on);
		} else {
			star1.setImageResource(android.R.drawable.star_big_off);
		}
		if(quantEstrelas >= 2){
			star2.setImageResource(android.R.drawable.star_big_on);
		} else {
			star2.setImageResource(android.R.drawable.star_big_off);
		}
		if(quantEstrelas >= 3){
			star3.setImageResource(android.R.drawable.star_big_on);
		} else {
			star3.setImageResource(android.R.drawable.star_big_off);
		}

		if(itemDatabase.getLevel() == Constants.QUANT_LEVELS_JOGO_DA_MEMORIA){
			icNext.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		AbstractActivityMenu activity = (AbstractActivityMenu) getActivity();
		switch(view.getId()){
			case R.id.icMenuCompleted:
				activity.finish();
				getDialog().dismiss();
				break;
			case R.id.icReloadCompleted:
				activity.recarregarFragment(itemDatabase.getLevel());
				getDialog().dismiss();
				break;
			case R.id.icNextCompleted:
				activity.recarregarFragment(Math.min(Constants.QUANT_LEVELS_JOGO_DA_MEMORIA, itemDatabase.getLevel() + 1));
				getDialog().dismiss();
				break;
		}
	}
}
