package com.leandroideias.transito.jogo_da_memoria;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
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
public class AdapterGridJogoDaMemoria extends RecyclerView.Adapter<AdapterGridJogoDaMemoria.Holder> {
	private GameManager gameManager;
	private final ColorMatrixColorFilter filter;
	private List<ItemAdapterGridJogoDaMemoria> lista;
	private int defaultImageResId = R.drawable.brasil2;

	public AdapterGridJogoDaMemoria( GameManager gameManager, List<ItemAdapterGridJogoDaMemoria> lista) {
		this.gameManager = gameManager;
		this.lista = lista;

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
	public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_jogar_jogo_da_memoria, parent, false);
		Holder holder = new Holder(view);
		view.setTag(holder);
		return holder;

	}

	@Override
	public void onBindViewHolder(Holder holder, int position) {
		holder.atualiza(lista.get(position));
	}

	@Override
	public int getItemCount() {
		return lista.size();
	}

	public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
		private View rootView;
		private ImageView imageViewItemGridJogarJogoDaMemoria;

		public Holder(View rootView) {
			super(rootView);
			this.rootView = rootView;
			rootView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			ItemAdapterGridJogoDaMemoria item = lista.get(getAdapterPosition());
			gameManager.performClick(item, getAdapterPosition());
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
