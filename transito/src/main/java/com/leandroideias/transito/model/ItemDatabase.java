package com.leandroideias.transito.model;

import java.io.Serializable;

/**
 * Created by Leandro on 17/5/2014.
 */
public class ItemDatabase implements Serializable {
	private int mundo;
	private int level;
	private int quantEstrelas;
	private int quantErros;
	private int pontos;

	public ItemDatabase(int mundo, int level, int quantEstrelas, int quantErros, int pontos) {
		this.mundo = mundo;
		this.level = level;
		this.quantEstrelas = quantEstrelas;
		this.quantErros = quantErros;
		this.pontos = pontos;
	}

	public int getMundo() {
		return mundo;
	}

	public void setMundo(int mundo) {
		this.mundo = mundo;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getQuantEstrelas() {
		return quantEstrelas;
	}

	public void setQuantEstrelas(int quantEstrelas) {
		this.quantEstrelas = quantEstrelas;
	}

	public int getQuantErros() {
		return quantErros;
	}

	public void setQuantErros(int quantErros) {
		this.quantErros = quantErros;
	}

	public int getPontos() {
		return pontos;
	}

	public void setPontos(int pontos) {
		this.pontos = pontos;
	}
}
