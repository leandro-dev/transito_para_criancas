package com.leandroideias.baseutils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Leandro on 16/4/2014.
 */
public class BaseFragment extends Fragment {

	/**
	 * Implements this if your fragments should handle the event of Back-Button pressed.
	 * @return true if the event was handled by your fragment, false otherwise
	 */
	public boolean onBackPressed(){
		boolean childFragmentHandledEvent = false;
		FragmentManager fm = getChildFragmentManager();
		if(fm.getBackStackEntryCount() > 0){
			String tag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
			Fragment lastFragment = fm.findFragmentByTag(tag);
			if(lastFragment instanceof BaseFragment){
				if(((BaseFragment) lastFragment).onBackPressed()){
					childFragmentHandledEvent = true;
				}
			}
			if(childFragmentHandledEvent){
				return true;
			} else {
				fm.popBackStack();
				return true;
			}
		}
		return false;
	}
}
