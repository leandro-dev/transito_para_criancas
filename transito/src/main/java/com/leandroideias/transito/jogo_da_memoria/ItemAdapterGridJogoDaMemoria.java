package com.leandroideias.transito.jogo_da_memoria;

import java.io.Serializable;

/**
 * Created by Leandro on 21/5/2014.
 */
public class ItemAdapterGridJogoDaMemoria implements Serializable{
	private int imageResId;
	private boolean hidden;
	private boolean found;

	public ItemAdapterGridJogoDaMemoria(int imageResId) {
		this.imageResId = imageResId;
		setHidden(true);
		setFound(false);
	}

	public int getImageResId() {

		return imageResId;
	}

	public void setHidden(boolean hidden) {
		this.hidden = hidden;
	}

	public boolean isHidden() {
		return hidden;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	@Override
	public boolean equals(Object object) {
		if(this == object) return true;
		if(!(object instanceof ItemAdapterGridJogoDaMemoria)) return false;

		ItemAdapterGridJogoDaMemoria item = ((ItemAdapterGridJogoDaMemoria) object);
		return ((imageResId == item.getImageResId())
				&& (hidden == item.isHidden())
				&& (found == item.isFound()));
	}
}
