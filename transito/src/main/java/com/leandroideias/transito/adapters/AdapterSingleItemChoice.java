package com.leandroideias.transito.adapters;

import android.content.Context;
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
public class AdapterSingleItemChoice extends BaseAdapter {
	private LayoutInflater inflater;
	private String[] itens;
	private Integer selectedPosition;
	private Integer wrongPosition;
	private Integer correctPosition;

	public AdapterSingleItemChoice(Context context, String[] itens){
		inflater = LayoutInflater.from(context);
		this.itens = itens;
		this.selectedPosition = null;
	}

	@Override
	public int getCount() {
		return itens.length;
	}

	@Override
	public Object getItem(int i) {
		return itens[i];
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder;
		if(convertView == null){
			convertView = inflater.inflate(R.layout.item_list_single_choice, parent, false);
			holder = new Holder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.getRadioButton().setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View view, MotionEvent motionEvent) {
				setSelectedPosition(position);
				return true;
			}
		});

		holder.getRadioButton().setChecked(selectedPosition != null && selectedPosition == position);
		if(wrongPosition != null && wrongPosition == position){
			holder.getResultIcon().setImageResource(R.drawable.ic_close_mini);
			holder.getRadioButton().setVisibility(View.INVISIBLE);
		}
		if(correctPosition != null && correctPosition == position){
			holder.getResultIcon().setImageResource(R.drawable.ic_correct_mini);
			holder.getRadioButton().setVisibility(View.INVISIBLE);
		}
		holder.getTextView().setText(itens[position]);

		return convertView;
	}

	public void setSelectedPosition(int position){
		this.selectedPosition = position;
		notifyDataSetChanged();
	}

	public int getSelectedPosition(){
		return selectedPosition;
	}

	public void setWrongPosition(int wrongPosition){
		this.wrongPosition = wrongPosition;
	}

	public void setCorrectPosition(int correctPosition){
		this.correctPosition = correctPosition;
	}

	private class Holder{
		private View rootView;

		private ImageView resultIcon;
		private RadioButton radioButton;
		private TextView textView;

		public Holder(View rootView){
			this.rootView = rootView;
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
	}
}
