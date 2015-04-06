package com.leandroideias.baseutils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by Leandro on 16/4/2014.
 */
public class BaseActivity extends FragmentActivity {
	private boolean destroyActivityWhenWithoutFragments = true;

	public boolean isDestroyActivityWhenWithoutFragments() {
		return destroyActivityWhenWithoutFragments;
	}

	public void setDestroyActivityWhenWithoutFragments(boolean destroyActivityWhenWithoutFragments) {
		this.destroyActivityWhenWithoutFragments = destroyActivityWhenWithoutFragments;
	}

	@Override
	public void onBackPressed() {
		//Check if the current fragment that is appearing should handle this event.
		boolean fragmentHandledEvent = false;
		FragmentManager fm = getSupportFragmentManager();
		if(fm.getBackStackEntryCount() > 0){
			String tag = fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1).getName();
			Fragment lastFragment = fm.findFragmentByTag(tag);
			if(lastFragment instanceof BaseFragment){
				if(((BaseFragment) lastFragment).onBackPressed()){
					fragmentHandledEvent = true;
				}
			}
		}
		if(fragmentHandledEvent) return;

		//If the event wasn't handled, we may call the parent to take action
		super.onBackPressed();

		//If there is no fragment transaction remaining added to backstack, we check to see if we may destroy the activity
		if(destroyActivityWhenWithoutFragments && fm.getBackStackEntryCount() == 0){
			finish();
		}

	}
}
