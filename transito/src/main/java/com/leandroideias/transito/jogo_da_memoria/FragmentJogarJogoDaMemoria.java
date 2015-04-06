package com.leandroideias.transito.jogo_da_memoria;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.leandroideias.baseutils.BaseFragment;
import com.leandroideias.transito.R;
import com.leandroideias.transito.bll.JogoDaMemoriaBll;
import com.leandroideias.transito.model.ItemDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Leandro on 21/5/2014.
 */
public class FragmentJogarJogoDaMemoria extends BaseFragment implements FragmentSignificadoPlaca.OnFragmentSignificadoPlacaInteractionListener {
	private ActivityJogarJogoDaMemoria mActivity;
	private ImageView iconePausar;
	private GridView gridView;
	private AdapterGridJogoDaMemoria adapter;

	public enum Dificuldade{
		FACIL, MEDIO, DIFICIL
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if(!(activity instanceof ActivityJogarJogoDaMemoria)) throw new RuntimeException("Esse fragmento deve existir dentro de uma atividade do tipo ActivityJogarJogoDaMemoria");
		this.mActivity = (ActivityJogarJogoDaMemoria) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.jogar_jogo_da_memoria, container, false);
		initView(view);
		return view;
	}

	private void initView(View view) {
		iconePausar = (ImageView) view.findViewById(R.id.ic_pausar);
		gridView = (GridView) view.findViewById(R.id.gridViewJogoDaMemoria);

		iconePausar.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				mActivity.openMenu();
			}
		});

		int fase = getArguments().getInt("extraFase", 1);
		List<ItemAdapterGridJogoDaMemoria> lista;
		Dificuldade dificuldade;
		int quantItens;
		switch(fase){
			case 1:
				dificuldade = Dificuldade.FACIL;
				quantItens = 4;
				break;
			case 2:
				dificuldade = Dificuldade.FACIL;
				quantItens = 5;
				break;
			case 3:
				dificuldade = Dificuldade.FACIL;
				quantItens = 6;
				break;
			case 4:
				dificuldade = Dificuldade.MEDIO;
				quantItens = 6;
				break;
			case 5:
				dificuldade = Dificuldade.MEDIO;
				quantItens = 7;
				break;
			case 6:
				dificuldade = Dificuldade.MEDIO;
				quantItens = 8;
				break;
			case 7:
				dificuldade = Dificuldade.DIFICIL;
				quantItens = 8;
				break;
			case 8:
				dificuldade = Dificuldade.DIFICIL;
				quantItens = 9;
				break;
			case 9:
				dificuldade = Dificuldade.DIFICIL;
				quantItens = 10;
				break;
			case 10:
			default:
				dificuldade = Dificuldade.DIFICIL;
				quantItens = 10;
				break;
		}
		lista = generateList(dificuldade, quantItens);
		adapter = new AdapterGridJogoDaMemoria(this, dificuldade, lista);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(adapter);
	}

	private List<ItemAdapterGridJogoDaMemoria> generateList(Dificuldade dificuldade, int quantItens){
		List<Integer> listaResources = new ArrayList<Integer>();
		switch(dificuldade){
			case FACIL:
				listaResources = Arrays.asList(R.drawable.placa_r1, R.drawable.placa_r2, R.drawable.placa_r4b, R.drawable.placa_r26,
						R.drawable.placa_a2b, R.drawable.placa_a14, R.drawable.placa_a45);
				break;
			case MEDIO:
				listaResources = Arrays.asList(R.drawable.placa_r6b, R.drawable.placa_r5a, R.drawable.placa_r19, R.drawable.placa_r25d, R.drawable.placa_r28,
						R.drawable.placa_a1b, R.drawable.placa_a7b, R.drawable.placa_a18, R.drawable.placa_a32b, R.drawable.placa_a24);
				break;
			case DIFICIL:
				listaResources = Arrays.asList(R.drawable.placa_r12, R.drawable.placa_r25b, R.drawable.placa_r29,
						R.drawable.placa_r7, R.drawable.placa_r6a, R.drawable.placa_r6c, R.drawable.placa_r15,
						R.drawable.placa_a19, R.drawable.placa_a20a, R.drawable.placa_a22, R.drawable.placa_a35,
						R.drawable.placa_a44, R.drawable.placa_a42a);
				break;
			default:
				return null;
		}
		if(listaResources.size() < quantItens) throw new RuntimeException("Não existem a quantidade de itens requisitados para a dificuldade escolhida.");
		Collections.shuffle(listaResources);
		List<ItemAdapterGridJogoDaMemoria> listaItens = new ArrayList<ItemAdapterGridJogoDaMemoria>(2*quantItens);
		for(int a = 0; a < quantItens; a++){
			listaItens.add(new ItemAdapterGridJogoDaMemoria(listaResources.get(a)));
			listaItens.add(new ItemAdapterGridJogoDaMemoria(listaResources.get(a)));
		}
		Collections.shuffle(listaItens);

		return listaItens;
	}

	//Chamado pelo adapter quando tiver terminado a fase.
	public void notifyFinished(int estrelas, int pontos, int erros){
		FragmentGameCompletedJogoDaMemoria frag = new FragmentGameCompletedJogoDaMemoria();
		Bundle args = new Bundle();
		int level = getArguments().getInt("extraFase", 1);

		ItemDatabase itemDatabase = new ItemDatabase(2, level, estrelas, erros, pontos);
		JogoDaMemoriaBll.persisteResultado(itemDatabase);
		args.putSerializable("itemDatabaseJogoDaVelha", itemDatabase);
		frag.setArguments(args);
		frag.setCancelable(false);
		frag.show(getActivity().getSupportFragmentManager(), FragmentGameCompletedJogoDaMemoria.TAG);
//		"itemDatabaseJogoDaVelha"
	}

	@Override
	public void onSignificadoPlacaResult(boolean isCorrect) {
		adapter.onSignificadoPlacaResult(isCorrect);
	}
}
