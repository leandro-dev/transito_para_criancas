package com.leandroideias.transito.jogo_da_memoria;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.leandroideias.baseutils.BaseFragment;
import com.leandroideias.transito.R;
import com.leandroideias.transito.bll.JogoDaMemoriaBll;
import com.leandroideias.transito.model.ItemDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Leandro on 21/5/2014.
 */
public class FragmentJogarJogoDaMemoria extends BaseFragment implements FragmentSignificadoPlaca.OnFragmentSignificadoPlacaInteractionListener {
	private ActivityJogarJogoDaMemoria mActivity;
	private ImageView iconePausar;
	private GameManager gameManager;
	private RecyclerView recyclerView;
	private AdapterGridJogoDaMemoria adapter;


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
		recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

		iconePausar.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				mActivity.openMenu();
			}
		});

		int fase = getArguments().getInt("extraFase", 1);
		gameManager = new GameManager(this);
		List<ItemAdapterGridJogoDaMemoria> lista = gameManager.construirListaItens(fase);
		adapter = new AdapterGridJogoDaMemoria(gameManager, lista);
		gameManager.setAdapter(adapter);
		recyclerView.setAdapter(adapter);

		recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				ViewGroup.MarginLayoutParams mlp = ((ViewGroup.MarginLayoutParams) recyclerView.getLayoutParams());
				int totalWidth = recyclerView.getWidth() - recyclerView.getPaddingRight() - recyclerView.getPaddingLeft() - mlp.rightMargin - mlp.leftMargin;
				int totalHeight = recyclerView.getHeight() - recyclerView.getPaddingTop() - recyclerView.getPaddingBottom() - mlp.topMargin - mlp.bottomMargin;
				DisposicaoItensGrid disp = calcularTamanhoItem(totalWidth, totalHeight, 20);
				GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), disp.getColunas());
				recyclerView.setLayoutManager(layoutManager);

				if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
					recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
				} else {
					//noinspection deprecation
					recyclerView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}
				recyclerView.invalidate();
			}
		});
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
	}

	@Override
	public void onSignificadoPlacaResult(boolean isCorrect) {
		gameManager.tratarRespostaDialogoPergunta(isCorrect);
	}

	/**
	 * Buscamos maximizar o tamanho do cartão, mas sem exceder os limites da tela, para evitar o scroll.
	 * @param screenWidth Largura da tela
	 * @param screenHeight Altura da tela
	 * @param numberOfItens Quantidade de itens que devem ser inseridos.
	 */
	public DisposicaoItensGrid calcularTamanhoItem(int screenWidth, int screenHeight, int numberOfItens){
		boolean flagTroca = false; //Flag para trocar altura e largura
		if(screenHeight > screenWidth){
			flagTroca = true;
			int aux = screenHeight;
			screenHeight = screenWidth;
			screenWidth = aux;
		}
		double dif = ((double) screenHeight)/screenWidth;
		double desiredNumberOfColumns = Math.sqrt(((double)numberOfItens) / dif);
		double desiredNumberOfRows = desiredNumberOfColumns * dif;

		//Existem 3 possíveis respostas...
		List<DisposicaoItensGrid> list = new ArrayList<DisposicaoItensGrid>();
		int auxCols = (int) Math.floor(desiredNumberOfColumns);
		int auxRows = (int) Math.floor(desiredNumberOfRows);
		list.add(new DisposicaoItensGrid((int) Math.ceil(desiredNumberOfColumns), (int) Math.ceil(desiredNumberOfRows), screenWidth, screenHeight, numberOfItens));
		list.add(new DisposicaoItensGrid((int) Math.ceil(((double) numberOfItens) / auxRows), auxRows, screenWidth, screenHeight, numberOfItens));
		list.add(new DisposicaoItensGrid(auxCols, (int) Math.ceil(((double) numberOfItens) / auxCols), screenWidth, screenHeight, numberOfItens));
		Collections.sort(list);

		DisposicaoItensGrid disp = list.get(0);
		if(flagTroca){
			disp.swapOrientation();
		}
		return disp;
	}

	public static class DisposicaoItensGrid implements Comparable<DisposicaoItensGrid>{
		private int colunas;
		private int linhas;
		private int screenWidth;
		private int screenHeight;
		private int numberOfCards;
		private int menorLado;
		private int maiorLado;

		private DisposicaoItensGrid(int colunas, int linhas, int screenWidth, int screenHeight, int numberOfCards) {
			this.colunas = colunas;
			this.linhas = linhas;
			this.screenWidth = screenWidth;
			this.screenHeight = screenHeight;
			this.numberOfCards = numberOfCards;

			measure();
		}

		public int getMenorLado() {
			return menorLado;
		}

		public int getMaiorLado() {
			return maiorLado;
		}

		public int getColunas() {
			return colunas;
		}

		public int getLinhas() {
			return linhas;
		}

		public void swapOrientation(){
			int aux;
			aux = colunas;
			colunas = linhas;
			linhas = aux;
			aux = screenWidth;
			screenWidth = screenHeight;
			screenHeight = aux;
		}

		private void measure(){
			int ladoA = (int) Math.floor(((double)screenWidth) / colunas);
			int ladoB = (int) Math.floor(((double)screenHeight) / linhas);
			menorLado = Math.min(ladoA, ladoB);
			maiorLado = Math.max(ladoA, ladoB);
		}

		@Override
		public int compareTo(DisposicaoItensGrid another) {
			if(this == another) return 0;

			if(menorLado != another.getMenorLado()){
				return another.getMenorLado() - menorLado;
			} else {
				return another.getMaiorLado() - maiorLado;
			}
		}
	}
}
