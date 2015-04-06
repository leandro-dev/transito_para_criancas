package com.leandroideias.transito.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.leandroideias.baseutils.BaseActivity;
import com.leandroideias.transito.R;
import com.leandroideias.transito.ache_os_erros.LevelSelectionAcheOsErros;
import com.leandroideias.transito.jogo_da_memoria.LevelSelectionJogoDaMemoria;

/**
 * Created by Leandro on 12/5/2014.
 */
public class GamesSelection extends BaseActivity implements View.OnClickListener {
	private Button buttonAcheOsErros;
	private Button buttonJogoDaMemoria;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_selection);

		buttonAcheOsErros = (Button) findViewById(R.id.buttonAcheOsErros);
		buttonJogoDaMemoria = (Button) findViewById(R.id.buttonJogoDaMemoria);

		buttonAcheOsErros.setOnClickListener(this);
		buttonJogoDaMemoria.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.buttonAcheOsErros){
			Intent intent = new Intent(this, LevelSelectionAcheOsErros.class);
			startActivity(intent);
		} else if(v.getId() == R.id.buttonJogoDaMemoria){
			Intent intent = new Intent(this, LevelSelectionJogoDaMemoria.class);
			startActivity(intent);
		}
	}
}
