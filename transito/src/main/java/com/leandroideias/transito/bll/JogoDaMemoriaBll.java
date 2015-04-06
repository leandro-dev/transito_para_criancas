package com.leandroideias.transito.bll;

import com.leandroideias.transito.application.Constants;
import com.leandroideias.transito.application.MyApplication;
import com.leandroideias.transito.dal.Database;
import com.leandroideias.transito.model.ItemDatabase;
import com.leandroideias.transito.model.ItemLevel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leandro on 21/5/2014.
 */
public class JogoDaMemoriaBll {
	private static final int MUNDO = 2;

	public static void persisteResultado(ItemDatabase item) {
		int mundo = MUNDO;
		int level = item.getLevel();

		Database dal = new Database(MyApplication.getAppContext());
		ItemDatabase itemGravado = dal.obtemLevel(mundo, level);

		if (itemGravado == null) {
			dal.insertLevel(item.getMundo(), item.getLevel(), item.getQuantEstrelas(), item.getQuantErros(), item.getPontos());
		} else if (item.getPontos() > itemGravado.getPontos()) {
			dal.updateLevel(item.getMundo(), item.getLevel(), item.getQuantEstrelas(), item.getQuantErros(), item.getPontos());
		}
		dal.close();
	}

	public static List<ItemLevel> obterListaLevels(){
		List<ItemLevel> lista = new ArrayList<ItemLevel>();

		for(int a = 1; a <= Constants.QUANT_LEVELS_JOGO_DA_MEMORIA; a++) {
			lista.add(new ItemLevel(MUNDO, a, 0, false, true));
		}


		Database dal = new Database(MyApplication.getAppContext());
		List<ItemDatabase> dadosSalvos = dal.getListaLevels(MUNDO);
		dal.close();

		for(ItemDatabase item : dadosSalvos){
			int level = item.getLevel();
			if(level <= 0 || level > lista.size()) continue;

			ItemLevel itemLevel = lista.get(level - 1);
			itemLevel.setBlocked(false);
			itemLevel.setCompleted(true);
			itemLevel.setStars(item.getQuantEstrelas());
		}

		//O próximo mundo do ultimo completado também deve ser desbloqueado
		for(ItemLevel itemLevel : lista){
			if(itemLevel.isBlocked()){
				itemLevel.setBlocked(false);
				break;
			}
		}
		return lista;
	}
}
