package com.leandroideias.baseutils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Leandro on 19/4/2014.
 */
public class CustomAlert {
	private Context context;
	private String titulo;
	private Integer iconId;
	private String texto;
	private String textoBotaoOk;
	private String textoBotaoCancelar;
	private String textoBotaoNeutro;
	private Dialog dialog;
	private boolean cancelable;
	private DialogButtonClickListener listener;
	private EditText editText;
	private boolean showEditText = false;

	public CustomAlert(Context context, String titulo, String texto, String textoBotaoOk, String textoBotaoCancelar, boolean cancelable){
		this.context = context;
		this.titulo = titulo;
		this.texto = texto;
		this.textoBotaoOk = textoBotaoOk;
		this.textoBotaoCancelar = textoBotaoCancelar;
		this.cancelable = cancelable;
	}

	public CustomAlert(Context context, int iconId, String titulo, String texto, String textoBotaoOk, String textoBotaoCancelar, boolean cancelable){
		this.context = context;
		this.iconId = iconId;
		this.titulo = titulo;
		this.texto = texto;
		this.textoBotaoOk = textoBotaoOk;
		this.textoBotaoCancelar = textoBotaoCancelar;
		this.cancelable = cancelable;
	}

	public CustomAlert(Context context, String titulo, String texto, String textoBotaoOk, String textoBotaoCancelar, String textoBotaoNeutro, boolean cancelable){
		this.context = context;
		this.titulo = titulo;
		this.texto = texto;
		this.textoBotaoOk = textoBotaoOk;
		this.textoBotaoCancelar = textoBotaoCancelar;
		this.textoBotaoNeutro = textoBotaoNeutro;
		this.cancelable = cancelable;
	}

	public void showEditText(){
		showEditText = true;
	}

	public void setDialogButtonClickListener(DialogButtonClickListener listener){
		this.listener = listener;
	}

	public Dialog createDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(titulo)
				.setMessage(texto)
				.setPositiveButton(textoBotaoOk, new AlertDialog.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						if(listener != null) listener.buttonOkClicked();
					}
				});
		if(iconId != null){
			builder.setIcon(iconId);
		}
		if(showEditText){
			View view = LayoutInflater.from(context).inflate(R.layout.custom_alert_with_edit_text, null);
			editText = (EditText) view.findViewById(R.id.customAlertEditText);
			builder.setView(view);
		}
		if(textoBotaoCancelar != null){
			builder.setNegativeButton(textoBotaoCancelar, new AlertDialog.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					if(listener != null) listener.buttonCancelClicked();
				}
			});
		}
		if(textoBotaoNeutro != null){
			builder.setNeutralButton(textoBotaoNeutro, new AlertDialog.OnClickListener(){
				public void onClick(DialogInterface dialogInterface, int i) {
					if(listener != null) listener.buttonNeutralClicked();
				}
			});
		}
		builder.setCancelable(cancelable);

		dialog = builder.create();
		return dialog;
	}



	public abstract class DialogButtonClickListener{
		public Dialog getDialog(){
			return dialog;
		}
		public String getText(){
			if(editText != null) return editText.getText().toString();
			else return null;
		}

		public abstract void buttonCancelClicked();
		public abstract void buttonOkClicked();
		public void buttonNeutralClicked(){}
	}
}
