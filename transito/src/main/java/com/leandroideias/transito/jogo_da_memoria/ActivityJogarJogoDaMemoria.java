package com.leandroideias.transito.jogo_da_memoria;

import android.support.v4.app.Fragment;

import com.leandroideias.transito.application.AbstractActivityMenu;

/**
 * Created by Leandro on 21/5/2014.
 */
public class ActivityJogarJogoDaMemoria extends AbstractActivityMenu {
	@Override
	public Fragment getFragment() {
		return new FragmentJogarJogoDaMemoria();
	}
}
