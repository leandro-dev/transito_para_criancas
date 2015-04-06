package com.leandroideias.transito.jogo_da_memoria;

import android.content.Intent;

import com.leandroideias.transito.application.AbstractLevelSelection;
import com.leandroideias.transito.bll.JogoDaMemoriaBll;
import com.leandroideias.transito.model.ItemLevel;

import java.util.List;

/**
 * Created by Leandro on 21/5/2014.
 */
public class LevelSelectionJogoDaMemoria extends AbstractLevelSelection {
	@Override
	public List<ItemLevel> obtemListaLevels() {
		return JogoDaMemoriaBll.obterListaLevels();
	}

	@Override
	public void onItemClick(int position) {
		Intent intent = new Intent(this, ActivityJogarJogoDaMemoria.class);
		intent.putExtra("extraFase", listaLevels.get(position).getLevel());
		startActivity(intent);
	}
}
