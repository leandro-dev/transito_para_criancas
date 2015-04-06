package com.leandroideias.baseutils;

import android.os.AsyncTask;

/**
 * Created by Leandro on 19/4/2014.
 */
public abstract class GenericAsyncTask extends AsyncTask<Void, Void, Boolean> {
	@Override
	protected final void onPreExecute() {

		onPreExecute2();
	}

	@Override
	protected void onPostExecute(Boolean aBoolean) {
		onPostExecute2();
	}

	/**
	 * Método que será chamado logo após o onPreExecute();
	 */
	protected abstract void onPreExecute2();

	/**
	 * Método que será chamado logo após onPostExecute();
	 */
	protected abstract void onPostExecute2();

}
