package com.leandroideias.transito.ache_os_erros;

import android.support.v4.app.Fragment;

import com.leandroideias.transito.application.AbstractActivityMenu;

/**
 * Created by leandro.castro on 13/05/2014.
 */
public class ActivityJogarAcheOsErros extends AbstractActivityMenu {

	@Override
	public Fragment getFragment() {
		return FragmentJogarAcheOsErros.newInstance();
	}
}
