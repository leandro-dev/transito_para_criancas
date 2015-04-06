package com.leandroideias.transito.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.leandroideias.transito.R;
import com.leandroideias.transito.model.ItemLevel;

import java.util.List;

/**
 * Created by Leandro on 12/5/2014.
 */
public class LevelSelectionAdapter extends BaseAdapter {
	private List<ItemLevel> listaLevels;
	private LayoutInflater inflater;

	public LevelSelectionAdapter(Context context, List<ItemLevel> listaLevels) {
		this.inflater = LayoutInflater.from(context);
		this.listaLevels = listaLevels;
	}

	@Override
	public int getCount() {
		return listaLevels.size();
	}

	@Override
	public Object getItem(int position) {
		return listaLevels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_level_selection, parent, false);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		holder.popula(listaLevels.get(position));

		return convertView;
	}

	private class Holder{
		private View rootView;

		private View viewDisabledLevel;
		private View viewEnabledLevel;
		private TextView textViewNumeroFase;
		private ImageView imageViewStar1;
		private ImageView imageViewStar2;
		private ImageView imageViewStar3;

		public Holder(View rootView){
			this.rootView = rootView;
		}


		public View getViewDisabledLevel() {
			if(viewDisabledLevel == null) viewDisabledLevel = rootView.findViewById(R.id.viewDisabledLevel);
			return viewDisabledLevel;
		}

		public View getViewEnabledLevel() {
			if(viewEnabledLevel == null) viewEnabledLevel = rootView.findViewById(R.id.viewEnabledLevel);
			return viewEnabledLevel;
		}

		public TextView getTextViewNumeroFase() {
			if(textViewNumeroFase == null) textViewNumeroFase = (TextView) rootView.findViewById(R.id.textViewNumeroFase);
			return textViewNumeroFase;
		}

		public ImageView getImageViewStar1() {
			if(imageViewStar1 == null) imageViewStar1 = (ImageView) rootView.findViewById(R.id.imageViewStar1);
			return imageViewStar1;
		}

		public ImageView getImageViewStar2() {
			if(imageViewStar2 == null) imageViewStar2 = (ImageView) rootView.findViewById(R.id.imageViewStar2);
			return imageViewStar2;
		}

		public ImageView getImageViewStar3() {
			if(imageViewStar3 == null) imageViewStar3 = (ImageView) rootView.findViewById(R.id.imageViewStar3);
			return imageViewStar3;
		}

		public void popula(ItemLevel item){
			if(item.isBlocked()){
				getViewDisabledLevel().setVisibility(View.VISIBLE);
				getViewEnabledLevel().setVisibility(View.GONE);
			} else {
				getViewDisabledLevel().setVisibility(View.GONE);
				getViewEnabledLevel().setVisibility(View.VISIBLE);
				getTextViewNumeroFase().setText("" + item.getLevel());
				int quantStars = item.getStars();
				if (quantStars >= 1) {
					getImageViewStar1().setImageResource(android.R.drawable.star_big_on);
				} else {
					getImageViewStar1().setImageResource(android.R.drawable.star_big_off);
				}
				if (quantStars >= 2) {
					getImageViewStar2().setImageResource(android.R.drawable.star_big_on);
				} else {
					getImageViewStar2().setImageResource(android.R.drawable.star_big_off);
				}
				if (quantStars >= 3) {
					getImageViewStar3().setImageResource(android.R.drawable.star_big_on);
				} else {
					getImageViewStar3().setImageResource(android.R.drawable.star_big_off);
				}
			}
		}

	}
}
