package com.leandroideias.transito.jogo_da_memoria;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.leandroideias.transito.R;

/**
 * Created by Leandro on 11/8/2014.
 */
public class AdapterSingleItemChoice extends RecyclerView.Adapter<AdapterSingleItemChoice.Holder> {
	private String[] itens;
	private Integer selectedPosition;
	private Integer wrongPosition;
	private Integer correctPosition;

	public AdapterSingleItemChoice(String[] itens){
		this.itens = itens;
		this.selectedPosition = null;
	}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_single_choice, parent, false);
		Holder holder = new Holder(view);
		view.setTag(holder);
		return holder;
	}

	@Override
	public void onBindViewHolder(Holder holder, int position) {
		holder.getTextView().setMovementMethod(new ScrollingMovementMethod());

		holder.getRadioButton().setChecked(selectedPosition != null && selectedPosition == position);
		if(wrongPosition != null && wrongPosition == position){
			holder.getResultIcon().setImageResource(R.drawable.ic_close_mini);
			holder.getRadioButton().setVisibility(View.INVISIBLE);
		} else if(correctPosition != null && correctPosition == position){
			holder.getResultIcon().setImageResource(R.drawable.ic_correct_mini);
			holder.getRadioButton().setVisibility(View.INVISIBLE);
		} else {
			holder.getResultIcon().setImageBitmap(null);
			holder.getRadioButton().setVisibility(View.VISIBLE);
		}
		holder.getRadioButton().setSelected(selectedPosition != null && selectedPosition == position);
		holder.getTextView().setText(itens[position]);
	}

	@Override
	public int getItemCount() {
		return itens.length;
	}

	public void setSelectedPosition(int position){
		this.selectedPosition = position;
		notifyDataSetChanged();
	}

	public String getItem(int position){
		return itens[position];
	}

	public Integer getSelectedPosition(){
		return selectedPosition;
	}

	public void setWrongPosition(int wrongPosition){
		this.wrongPosition = wrongPosition;
	}

	public void setCorrectPosition(int correctPosition){
		this.correctPosition = correctPosition;
	}

	public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{
		private View rootView;

		private ImageView resultIcon;
		private RadioButton radioButton;
		private TextView textView;

		public Holder(View rootView){
			super(rootView);
			this.rootView = rootView;
			rootView.setOnClickListener(this);
			getRadioButton().setOnClickListener(this);
			getTextView().setOnClickListener(this);
		}

		public ImageView getResultIcon(){
			if(resultIcon == null) resultIcon = (ImageView) rootView.findViewById(R.id.resultIcon);
			return resultIcon;
		}

		public RadioButton getRadioButton(){
			if(radioButton == null) radioButton = (RadioButton) rootView.findViewById(R.id.radioButton);
			return radioButton;
		}

		public TextView getTextView(){
			if(textView == null) textView = (TextView) rootView.findViewById(R.id.textView);
			return textView;
		}

		@Override
		public void onClick(View v) {
			selectedPosition = getAdapterPosition();
			notifyDataSetChanged();
		}
	}
}
