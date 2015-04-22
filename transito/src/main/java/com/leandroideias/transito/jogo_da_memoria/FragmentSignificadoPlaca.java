package com.leandroideias.transito.jogo_da_memoria;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.leandroideias.baseutils.BaseFragment;
import com.leandroideias.transito.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FragmentSignificadoPlaca extends BaseFragment implements View.OnClickListener {
	public static final String TAG = "FragmentSignificadoPlaca";
	public static final String ARG_ITEM = "item";
	public static final String ARG_DIFICULDADE = "dificuldade";
	public static final String STATE_SIGNIFICADOS = "PossiveisSignificados";
	public static final String STATE_SIGNIFICADO_CORRETO = "PosicaoDoSignificadoCorreto";

	private GameManager.Dificuldade dificuldade;
	private ItemAdapterGridJogoDaMemoria item;
	private OnFragmentSignificadoPlacaInteractionListener mListener;

	private String[] possiveisSignificados;
	private int posicaoSignificadoCorreto;

	private AdapterSingleItemChoice adapterSingleItemChoice;
	private LinearLayout linearLayoutSignificados;
	private ImageView imageViewPlaca;
	private RecyclerView recyclerViewRespostas;
	private Button buttonConfirmar;


    public static FragmentSignificadoPlaca newInstance(GameManager.Dificuldade dificuldade, ItemAdapterGridJogoDaMemoria item) {
        FragmentSignificadoPlaca fragment = new FragmentSignificadoPlaca();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ITEM, item);
		args.putInt(ARG_DIFICULDADE, dificuldade.ordinal());
        fragment.setArguments(args);
        return fragment;
    }
    public FragmentSignificadoPlaca() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		try{
            item = (ItemAdapterGridJogoDaMemoria) getArguments().getSerializable(ARG_ITEM);
			dificuldade = GameManager.Dificuldade.values()[getArguments().getInt(ARG_DIFICULDADE)];
        } catch(Exception e){
			throw new RuntimeException("Não foi possível obter a dificuldade ou o o item que deve ser usado. Está utilizando FragmentSignificadoPlaca.newInstance() ?");
		}

		if(savedInstanceState != null && savedInstanceState.containsKey(STATE_SIGNIFICADOS) && savedInstanceState.containsKey(STATE_SIGNIFICADO_CORRETO)){
			possiveisSignificados = savedInstanceState.getStringArray(STATE_SIGNIFICADOS);
			posicaoSignificadoCorreto = savedInstanceState.getInt(STATE_SIGNIFICADO_CORRETO);
		} else {
			geraListaSignificados();
		}
    }

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putStringArray(STATE_SIGNIFICADOS, possiveisSignificados);
		outState.putInt(STATE_SIGNIFICADO_CORRETO, posicaoSignificadoCorreto);
	}

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_significado_placa, container, false);

		linearLayoutSignificados = (LinearLayout) view.findViewById(R.id.linearLayoutSignificados);
		imageViewPlaca = (ImageView) view.findViewById(R.id.imageViewPlaca);
		recyclerViewRespostas = (RecyclerView) view.findViewById(R.id.recyclerView);
		buttonConfirmar = (Button) view.findViewById(R.id.buttonConfirmar);

		imageViewPlaca.setImageResource(item.getImageResId());
		adapterSingleItemChoice = new AdapterSingleItemChoice(possiveisSignificados);
		recyclerViewRespostas.setAdapter(adapterSingleItemChoice);
		recyclerViewRespostas.setLayoutManager(new MyLinearLayoutManager(getActivity()));

		buttonConfirmar.setOnClickListener(this);
		return view;
    }

	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
	    if(getTargetFragment() instanceof OnFragmentSignificadoPlacaInteractionListener){
		    mListener = (OnFragmentSignificadoPlacaInteractionListener) getTargetFragment();
	    } else if(activity instanceof  OnFragmentSignificadoPlacaInteractionListener){
		    mListener = (OnFragmentSignificadoPlacaInteractionListener) activity;
	    } else {
		    throw new RuntimeException("A classe ou o fragmento que invocou o FragmentSignificadoPlaca devem implementar a interface OnFragmentSifnificadoPlacaInteractionListener.");
	    }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

	@Override
	public synchronized void onClick(View view) {
		switch(view.getId()){
			case R.id.buttonConfirmar:
				Integer position = adapterSingleItemChoice.getSelectedPosition();
				if(position == null) return;
				buttonConfirmar.setOnClickListener(null);
				String significadoPlaca = obterSignificadoPlaca(item.getImageResId());
				final boolean isCorrectAnswer = significadoPlaca.equals(adapterSingleItemChoice.getItem(position));
				if(isCorrectAnswer){
					adapterSingleItemChoice.setCorrectPosition(position);
					buttonConfirmar.setBackgroundResource(R.drawable.shape_button_green);
					buttonConfirmar.setText(R.string.correct);
				} else {
					adapterSingleItemChoice.setWrongPosition(position);
					buttonConfirmar.setBackgroundResource(R.drawable.shape_button_red);
					buttonConfirmar.setText(R.string.wrong);
					//Faz a busca para saber quem que é o correto!
					for(int a = 0; a < adapterSingleItemChoice.getItemCount(); a++){
						if(significadoPlaca.equals(adapterSingleItemChoice.getItem(a))){
							adapterSingleItemChoice.setCorrectPosition(a);
							break;
						}
					}
				}
				adapterSingleItemChoice.notifyDataSetChanged();

				Handler handler = new Handler();
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						if(mListener != null){
							mListener.onSignificadoPlacaResult(isCorrectAnswer);
						}
						getFragmentManager().popBackStack();
					}
				}, (isCorrectAnswer? 1500 : 3000));
				break;
		}
	}

	public interface OnFragmentSignificadoPlacaInteractionListener {
		public void onSignificadoPlacaResult(boolean isCorrect);
    }


	private void geraListaSignificados(){
		int correct = item.getImageResId();
		int[] errados;
		switch(correct){
			case R.drawable.placa_a1b:
				errados = new int[]{R.drawable.placa_a2b, R.drawable.placa_r26, R.drawable.placa_r4b};
				break;
			case R.drawable.placa_a2b:
				errados = new int[]{R.drawable.placa_a1b, R.drawable.placa_r26, R.drawable.placa_r4b};
				break;
			case R.drawable.placa_a7b:
				errados = new int[]{R.drawable.placa_a14, R.drawable.placa_a24, R.drawable.placa_a45};
				break;
			case R.drawable.placa_a14:
				errados = new int[]{R.drawable.placa_r1, R.drawable.placa_r26, R.drawable.placa_a32b};
				break;
			case R.drawable.placa_a18:
				errados = new int[]{R.drawable.placa_a19, R.drawable.placa_a24, R.drawable.placa_a35};
				break;
			case R.drawable.placa_a19:
				errados = new int[]{R.drawable.placa_a18, R.drawable.placa_a22, R.drawable.placa_a20a};
				break;
			case R.drawable.placa_a20a:
				errados = new int[]{R.drawable.placa_a24, R.drawable.placa_a22, R.drawable.placa_a44};
				break;
			case R.drawable.placa_a22:
				errados = new int[]{R.drawable.placa_a18, R.drawable.placa_a19, R.drawable.placa_a32b};
				break;
			case R.drawable.placa_a24:
				errados = new int[]{R.drawable.placa_r1, R.drawable.placa_r2, R.drawable.placa_a32b};
				break;
			case R.drawable.placa_a32b:
				errados = new int[]{R.drawable.placa_a35, R.drawable.placa_r2, R.drawable.placa_r26};
				break;
			case R.drawable.placa_a35:
				errados = new int[]{R.drawable.placa_r1, R.drawable.placa_r3, R.drawable.placa_a45};
				break;
			case R.drawable.placa_a42a:
				errados = new int[]{R.drawable.placa_a22, R.drawable.placa_a24, R.drawable.placa_a2b};
				break;
			case R.drawable.placa_a43:
				errados = new int[]{R.drawable.placa_a44, R.drawable.placa_a24, R.drawable.placa_r3};
				break;
			case R.drawable.placa_a44:
				errados = new int[]{R.drawable.placa_a35, R.drawable.placa_a42a, R.drawable.placa_a20a};
				break;
			case R.drawable.placa_a45:
				errados = new int[]{R.drawable.placa_a14, R.drawable.placa_a7b, R.drawable.placa_r3};
				break;
			case R.drawable.placa_r1:
				errados = new int[]{R.drawable.placa_r26, R.drawable.placa_r2, R.drawable.placa_r3};
				break;
			case R.drawable.placa_r2:
				errados = new int[]{R.drawable.placa_r7, R.drawable.placa_r28, R.drawable.placa_a43};
				break;
			case R.drawable.placa_r3:
				errados = new int[]{R.drawable.placa_r1, R.drawable.placa_r7, R.drawable.placa_a2b};
				break;
			case R.drawable.placa_r4b:
				errados = new int[]{R.drawable.placa_r25d, R.drawable.placa_a1b, R.drawable.placa_a7b};
				break;
			case R.drawable.placa_r5a:
				errados = new int[]{R.drawable.placa_r26, R.drawable.placa_r25d, R.drawable.placa_r28};
				break;
			case R.drawable.placa_r6a:
				errados = new int[]{R.drawable.placa_r6c, R.drawable.placa_r6b, R.drawable.placa_r1};
				break;
			case R.drawable.placa_r6b:
				errados = new int[]{R.drawable.placa_r6a, R.drawable.placa_r6c, R.drawable.placa_r1};
				break;
			case R.drawable.placa_r6c:
				errados = new int[]{R.drawable.placa_r6a, R.drawable.placa_r6b, R.drawable.placa_r1};
				break;
			case R.drawable.placa_r7:
				errados = new int[]{R.drawable.placa_r28, R.drawable.placa_r26, R.drawable.placa_a22};
				break;
			case R.drawable.placa_r12:
				errados = new int[]{R.drawable.placa_r7, R.drawable.placa_r1, R.drawable.placa_r2};
				break;
			case R.drawable.placa_r15:
				errados = new int[]{R.drawable.placa_r19, R.drawable.placa_a42a, R.drawable.placa_a45};
				break;
			case R.drawable.placa_r19:
				errados = new int[]{R.drawable.placa_r15, R.drawable.placa_r7, R.drawable.placa_r12};
				break;
			case R.drawable.placa_r25b:
				errados = new int[]{R.drawable.placa_a1b, R.drawable.placa_r4b, R.drawable.placa_a7b};
				break;
			case R.drawable.placa_r25d:
				errados = new int[]{R.drawable.placa_a7b, R.drawable.placa_a2b, R.drawable.placa_a14};
				break;
			case R.drawable.placa_r26:
				errados = new int[]{R.drawable.placa_r3, R.drawable.placa_r4b, R.drawable.placa_a14};
				break;
			case R.drawable.placa_r28:
				errados = new int[]{R.drawable.placa_r7, R.drawable.placa_r12, R.drawable.placa_r26};
				break;
			case R.drawable.placa_r29:
				errados = new int[]{R.drawable.placa_a24, R.drawable.placa_a32b, R.drawable.placa_a35};
				break;
			default:
				Log.e(getString(R.string.app_name), "Erro ao gerar definições erradas para a placa '" + correct + "'");
				errados = new int[]{R.drawable.placa_r1, R.drawable.placa_r2, R.drawable.placa_r3};
				break;
		}
		List<String> significados = new ArrayList<String>();
		switch(dificuldade){
			case DIFICIL:
				significados.add(obterSignificadoPlaca(errados[2]));
			case MEDIO:
				significados.add(obterSignificadoPlaca(errados[1]));
			case FACIL:
				significados.add(obterSignificadoPlaca(errados[0]));
				break;
		}
		Collections.shuffle(significados);
		posicaoSignificadoCorreto = (int) Math.floor(Math.random() * (significados.size() + 1));
		significados.add(posicaoSignificadoCorreto, obterSignificadoPlaca(correct));

		possiveisSignificados = significados.toArray(new String[significados.size()]);
	}


	private String obterSignificadoPlaca(int imageResIdPlaca){
		switch(imageResIdPlaca){
			case R.drawable.placa_a1b:
				return getString(R.string.sig_placa_a1b);
			case R.drawable.placa_a2b:
				return getString(R.string.sig_placa_a2b);
			case R.drawable.placa_a7b:
				return getString(R.string.sig_placa_a7b);
			case R.drawable.placa_a14:
				return getString(R.string.sig_placa_a14);
			case R.drawable.placa_a18:
				return getString(R.string.sig_placa_a18);
			case R.drawable.placa_a19:
				return getString(R.string.sig_placa_a19);
			case R.drawable.placa_a20a:
				return getString(R.string.sig_placa_a20a);
			case R.drawable.placa_a22:
				return getString(R.string.sig_placa_a22);
			case R.drawable.placa_a24:
				return getString(R.string.sig_placa_a24);
			case R.drawable.placa_a32b:
				return getString(R.string.sig_placa_a32b);
			case R.drawable.placa_a35:
				return getString(R.string.sig_placa_a35);
			case R.drawable.placa_a42a:
				return getString(R.string.sig_placa_a42a);
			case R.drawable.placa_a43:
				return getString(R.string.sig_placa_a43);
			case R.drawable.placa_a44:
				return getString(R.string.sig_placa_a44);
			case R.drawable.placa_a45:
				return getString(R.string.sig_placa_a45);
			case R.drawable.placa_r1:
				return getString(R.string.sig_placa_r1);
			case R.drawable.placa_r2:
				return getString(R.string.sig_placa_r2);
			case R.drawable.placa_r3:
				return getString(R.string.sig_placa_r3);
			case R.drawable.placa_r4b:
				return getString(R.string.sig_placa_r4b);
			case R.drawable.placa_r5a:
				return getString(R.string.sig_placa_r5a);
			case R.drawable.placa_r6a:
				return getString(R.string.sig_placa_r6a);
			case R.drawable.placa_r6b:
				return getString(R.string.sig_placa_r6b);
			case R.drawable.placa_r6c:
				return getString(R.string.sig_placa_r6c);
			case R.drawable.placa_r7:
				return getString(R.string.sig_placa_r7);
			case R.drawable.placa_r12:
				return getString(R.string.sig_placa_r12);
			case R.drawable.placa_r15:
				return getString(R.string.sig_placa_r15);
			case R.drawable.placa_r19:
				return getString(R.string.sig_placa_r19);
			case R.drawable.placa_r25b:
				return getString(R.string.sig_placa_r25b);
			case R.drawable.placa_r25d:
				return getString(R.string.sig_placa_r25d);
			case R.drawable.placa_r26:
				return getString(R.string.sig_placa_r26);
			case R.drawable.placa_r28:
				return getString(R.string.sig_placa_r28);
			case R.drawable.placa_r29:
				return getString(R.string.sig_placa_r29);
		}

		return null;
	}

	@Override
	public boolean onBackPressed() {
		return true;
	}
}









































