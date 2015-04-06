package com.leandroideias.transito.jogo_da_memoria;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.leandroideias.transito.R;

import java.util.List;

/**
 * Created by Leandro on 21/5/2014.
 */
public class AdapterGridJogoDaMemoria extends BaseAdapter implements AdapterView.OnItemClickListener, FragmentSignificadoPlaca.OnFragmentSignificadoPlacaInteractionListener {
	private FragmentJogarJogoDaMemoria fragment;
	private FragmentJogarJogoDaMemoria.Dificuldade mDificuldade;
	private final ColorMatrixColorFilter filter;
	private LayoutInflater inflater;
	private List<ItemAdapterGridJogoDaMemoria> lista;
	private int defaultImageResId = R.drawable.brasil2;
	private int quantAcertos;
	private int quantErros;
	private int quantSignificadosCorretos;
	private int quantSignificadosErrados;

	public AdapterGridJogoDaMemoria(FragmentJogarJogoDaMemoria fragment, FragmentJogarJogoDaMemoria.Dificuldade dificuldade, List<ItemAdapterGridJogoDaMemoria> lista) {
		this.fragment = fragment;
		this.mDificuldade = dificuldade;
		this.lista = lista;
		inflater = LayoutInflater.from(fragment.getActivity());
		quantAcertos = 0;
		quantErros = 0;

//		...

		ColorMatrix matrix = new ColorMatrix();
		matrix.set(new float[]{1, 0, 0, 0, -100,
							   0, 1, 0, 0, -100,
							   0, 0, 1, 0, -100,
							   0, 0, 0, 1, 0});
		ColorMatrix saturedMatrix = new ColorMatrix();
		saturedMatrix.setSaturation(0);
		matrix.postConcat(saturedMatrix);
		filter = new ColorMatrixColorFilter(matrix);

	}

	@Override
	public int getCount() {
		return lista.size();
	}

	@Override
	public Object getItem(int position) {
		return lista.get(position);
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_grid_jogar_jogo_da_memoria, parent, false);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.atualiza(lista.get(position));

		return convertView;
	}



	private int quantItensAbertos = 0;
	private int itemAbertoUm = -1;
	private int itemAbertoDois = -1;
	private boolean enabled = true;
	private Object mLock = new Object();
	private Handler handler = new Handler();
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		synchronized (mLock){
			if(!enabled) return;

			if(quantItensAbertos == 0){
				itemAbertoUm = position;
				lista.get(position).setHidden(false);
				quantItensAbertos++;
				notifyDataSetChanged();
			} else if(quantItensAbertos == 1){
				if(position == itemAbertoUm) return;
				if(lista.get(position).isFound()) return;
				itemAbertoDois = position;
				lista.get(position).setHidden(false);
				quantItensAbertos++;
				enabled = false;
				notifyDataSetChanged();
				handler.postDelayed(new Runnable() {
					public void run() {
						ItemAdapterGridJogoDaMemoria itemUm = lista.get(itemAbertoUm);
						ItemAdapterGridJogoDaMemoria itemDois = lista.get(itemAbertoDois);
						if(itemUm.getImageResId() == itemDois.getImageResId()){
							quantAcertos++;
							itemUm.setFound(true);
							itemDois.setFound(true);
							final ItemAdapterGridJogoDaMemoria item = itemUm;
							fragment.getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									FragmentSignificadoPlaca frag = FragmentSignificadoPlaca.newInstance(mDificuldade, item);
									frag.setTargetFragment(fragment, 1);
									frag.show(fragment.getFragmentManager(), FragmentSignificadoPlaca.TAG);
								}
							});
						} else {
							quantErros++;
							itemUm.setHidden(true);
							itemDois.setHidden(true);

							synchronized (mLock){
								enabled = true;
								quantItensAbertos = 0;
							}
						}
						notifyDataSetChanged();
					}
				}, 1000);
			}
		}
	}

	private void calculaResultado() {
		int quantPlacasDistintas = lista.size() / 2;

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

	@Override
	public void onSignificadoPlacaResult(boolean isCorrect) {
		if(isCorrect) quantSignificadosCorretos++;
		else quantSignificadosErrados++;

		synchronized (mLock){
			enabled = true;
			quantItensAbertos = 0;
		}

		if(quantAcertos*2 == lista.size()){
			//Acabou a fase. Calcula a pontuação e mostra ao usuário.
			calculaResultado();
		}
	}

	private class Holder {
		private View rootView;
		private ImageView imageViewItemGridJogarJogoDaMemoria;

		private Holder(View rootView) {
			this.rootView = rootView;
		}

		public ImageView getImageViewItemGridJogarJogoDaMemoria() {
			if(imageViewItemGridJogarJogoDaMemoria == null) imageViewItemGridJogarJogoDaMemoria = (ImageView) rootView.findViewById(R.id.imageViewItemGridJogarJogoDaMemoria);
			return imageViewItemGridJogarJogoDaMemoria;
		}

		public void atualiza(ItemAdapterGridJogoDaMemoria item){
			if(item.isFound()) {
				getImageViewItemGridJogarJogoDaMemoria().setImageResource(item.getImageResId());
				getImageViewItemGridJogarJogoDaMemoria().setColorFilter(filter);
			} else {
				getImageViewItemGridJogarJogoDaMemoria().setColorFilter(null);
				if(item.isHidden()){
					getImageViewItemGridJogarJogoDaMemoria().setImageResource(defaultImageResId);
				} else {
					getImageViewItemGridJogarJogoDaMemoria().setImageResource(item.getImageResId());
				}
			}
		}
	}


}
