package com.leandroideias.transito.jogo_da_memoria;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;

import com.leandroideias.transito.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Classe responsável por controlar o jogo da memória.
 *
 * Created by Leandro on 20/04/2015.
 */
public class GameManager {
	public enum Dificuldade{
		FACIL, MEDIO, DIFICIL
	}

	private FragmentJogarJogoDaMemoria fragment;
	private AdapterGridJogoDaMemoria adapter;

	//Variáveis de controle
	private Dificuldade dificuldade;
	private ItemAdapterGridJogoDaMemoria item1, item2;
	private int position1, position2;
	//Lock utilizado para dois cliques simultâneos não alterarem a lista ao memso tempo.
	private Object mLock = new Object();
	private Handler handler = new Handler(Looper.getMainLooper());

	//Variáveis de controle do desempenho do jogador
	private int quantAcertos;
	private int quantErros;
	private int quantSignificadosCorretos;
	private int quantSignificadosErrados;

	public GameManager(FragmentJogarJogoDaMemoria fragment) {
		this.fragment = fragment;
	}

	public AdapterGridJogoDaMemoria getAdapter() {
		return adapter;
	}

	public void setAdapter(AdapterGridJogoDaMemoria adapter) {
		this.adapter = adapter;
	}

	/**
	 * Define a dificuldade e constroi a lista de placas que serão selecionadas para o jogo.
	 * @param fase
	 * @return
	 */
	public List<ItemAdapterGridJogoDaMemoria> construirListaItens(int fase){
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

	public synchronized void performClick(ItemAdapterGridJogoDaMemoria item, int position) {
		synchronized (mLock) {
			if (item1 != null && item2 != null) return;
			if(item.isFound() || !item.isHidden()) return;

			if (item1 == null) {
				//Primeiro item foi aberto.
				item1 = item;
				position1 = position;
				item1.setHidden(false);
				getAdapter().notifyItemChanged(position);
			} else {
				//Segundo item foi aberto. Devemos comparar e ver se acertou ou errou

				item2 = item;
				position2 = position;
				item2.setHidden(false);
				getAdapter().notifyItemChanged(position2);

				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						comparaItens();
					}
				}, 1000);
			}
		}
	}

	/**
	 * Compara se os itens correspondem ou são diferentes.
	 */
	private synchronized void comparaItens(){
		synchronized (mLock){
			if(item1.equals(item2)) {
				item1.setFound(true);
				item2.setFound(true);
				adapter.notifyItemChanged(position1);
				adapter.notifyItemChanged(position2);
				quantAcertos++;
				abrirDialogoPergunta();
			} else {
				item1.setHidden(true);
				item2.setHidden(true);
				adapter.notifyItemChanged(position1);
				adapter.notifyItemChanged(position2);

				//Não podemos definir os itens como nulos para o caso de acerto porque o usuário ainda deve responder a pergunta.
				item1 = item2 = null;
				position1 = position2 = 0;
			}
		}
	}

	private void abrirDialogoPergunta(){
		FragmentSignificadoPlaca frag = FragmentSignificadoPlaca.newInstance(dificuldade, item1);
		frag.setTargetFragment(fragment, 1);
		fragment.getActivity().getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragmentPlaceholder2, frag, FragmentSignificadoPlaca.TAG)
				.addToBackStack(FragmentSignificadoPlaca.TAG)
				.commit();
	}

	public void tratarRespostaDialogoPergunta(boolean acertou){
		if(acertou) quantSignificadosCorretos++;
		else quantSignificadosErrados++;

		synchronized (mLock){
			item1 = item2 = null;
			position1 = position2 = 0;
		}

		if(quantAcertos*2 == adapter.getItemCount()){
			//Acabou a fase. Calcula a pontuação e mostra ao usuário.
			calculaResultado();
		}
	}

	private void calculaResultado() {
		int quantPlacasDistintas = getAdapter().getItemCount() / 2;

		int quantErrosPermitidos = quantPlacasDistintas * 3 / 2;

		quantErros -= quantErrosPermitidos;
		if(quantErros < 0){
			quantErros = 0;
		}

		int pontosMemoria = quantAcertos*50 - quantErros*20;
		int pontosSignificados = quantSignificadosCorretos*30;
		int pontuacaoMaxima = quantPlacasDistintas*50 + quantSignificadosCorretos*30;
		int score = pontosMemoria + pontosSignificados;
		if(score < 0) score = 0;

		int stars;
		if(score*5 >= pontuacaoMaxima*4){
			// 4/5 da pontuação máxima ou mais é considerado 3 estrelas;
			stars = 3;
		} else if(score*5 >= pontuacaoMaxima*3){
			// entre 3/5 e 4/5 da pontuação máxima é considerado 2 estrelas;
			stars = 2;
		} else if(score*5 >= pontuacaoMaxima*2){
			// entre 2/5 e 3/5 da pontuação máxima é considerado 1 estrela;
			stars = 1;
		} else {
			stars = 0;
		}


		/*int total = lista.size() / 2;
		int errosPermitidos = total/2;
		int pontos = quantAcertos*100;
		quantErros -= errosPermitidos;
		if(quantErros < 0) quantErros = 0;
		pontos -= 20*quantErros;
		if(pontos < 0) pontos = 0;

		int stars;

		int max = (quantAcertos*100) * 3;
		int pt3 = pontos*3;
		if(pt3 == max){
			stars = 3;
		} else if(pt3 >= (2*max/3)){
			stars = 2;
		} else if(pt3 >= (1*max/3)){
			stars = 1;
		} else {
			stars = 0;
		}*/

		fragment.notifyFinished(stars, score, quantErros);
	}

}
