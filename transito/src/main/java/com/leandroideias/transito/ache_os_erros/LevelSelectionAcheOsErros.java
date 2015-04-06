package com.leandroideias.transito.ache_os_erros;

import android.content.Intent;

import com.leandroideias.transito.application.AbstractLevelSelection;
import com.leandroideias.transito.bll.AcheOsErrosBll;
import com.leandroideias.transito.model.ItemLevel;

import java.util.List;

/**
 * Created by Leandro on 12/5/2014.
 */
public class LevelSelectionAcheOsErros extends AbstractLevelSelection {
	@Override
	public void onItemClick(int position) {
		Intent intent = new Intent(this, ActivityJogarAcheOsErros.class);
		intent.putExtra("extraFase", listaLevels.get(position).getLevel());
		startActivity(intent);
	}

	@Override
	public List<ItemLevel> obtemListaLevels() {
		return AcheOsErrosBll.obterListaLevels();
	}
}
