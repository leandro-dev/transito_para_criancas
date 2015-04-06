package com.leandroideias.transito.application;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.leandroideias.baseutils.BaseActivity;
import com.leandroideias.transito.R;
import com.leandroideias.transito.adapters.LevelSelectionAdapter;
import com.leandroideias.transito.model.ItemLevel;

import java.util.List;

/**
 * Created by Leandro on 12/5/2014.
 */
public abstract class AbstractLevelSelection extends BaseActivity implements AdapterView.OnItemClickListener {
	private GridView gridViewLevels;
	protected List<ItemLevel> listaLevels;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.level_selection);

		gridViewLevels = (GridView) findViewById(R.id.gridViewLevels);
	}

	@Override
	protected void onStart() {
		super.onStart();
		listaLevels = obtemListaLevels();
		LevelSelectionAdapter adapter = new LevelSelectionAdapter(this, listaLevels);
		gridViewLevels.setAdapter(adapter);
		gridViewLevels.setOnItemClickListener(this);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id){
		if(position < 0 || position >= listaLevels.size()) return;
		if(listaLevels.get(position).isBlocked()){
			//Nível bloqueado
		} else {
			onItemClick(position);
		}
	}

	public abstract List<ItemLevel> obtemListaLevels();
	public abstract void onItemClick(int position);
}
