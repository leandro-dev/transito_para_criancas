package com.leandroideias.transito.application;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.leandroideias.baseutils.BaseActivity;
import com.leandroideias.transito.R;

/**
 * Created by leandro.castro on 13/05/2014.
 */
public abstract class AbstractActivityMenu extends BaseActivity implements View.OnClickListener {
	private DrawerLayout drawerLayout;
	private LinearLayout menuEsquerdo;
	private int extraFase;

	private ImageView icPlay;
	private ImageView icReload;
	private ImageView icMenu;
	private ImageView icMusic;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT >= 11) {
			getActionBar().hide();
		}
		getWindow().setFormat(PixelFormat.RGB_565);
		setContentView(R.layout.activity_jogo);

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		menuEsquerdo = (LinearLayout) findViewById(R.id.menu_esquerdo);

		drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		drawerLayout.setScrimColor(Color.parseColor("#CC000000"));
		drawerLayout.setOnTouchListener(new View.OnTouchListener(){
			public boolean onTouch(View view, MotionEvent motionEvent) {
				return true;
			}
		});

		icPlay = (ImageView) findViewById(R.id.ic_play);
		icPlay.setOnClickListener(this);
		icReload = (ImageView) findViewById(R.id.ic_reload);
		icReload.setOnClickListener(this);
		icMenu = (ImageView) findViewById(R.id.ic_menu);
		icMenu.setOnClickListener(this);
		icMusic = (ImageView) findViewById(R.id.ic_music);
		icMusic.setOnClickListener(this);

		carregaFragment();
	}

	public void openMenu() {
		drawerLayout.openDrawer(menuEsquerdo);
	}

	public void closeMenu() {
		drawerLayout.closeDrawer(menuEsquerdo);
	}

	public boolean isDrawerOpen(){
		return drawerLayout.isDrawerOpen(menuEsquerdo);
	}

	public abstract Fragment getFragment();

	private void carregaFragment() {
		//Mandando como extra o inteiro que representa a fase.
		extraFase = getIntent().getIntExtra("extraFase", 1);
		Fragment fragment = getFragment();
		Bundle args = fragment.getArguments();
		if (args == null) args = new Bundle();
		args.putInt("extraFase", extraFase);
		fragment.setArguments(args);

		FragmentTransaction fm = getSupportFragmentManager().beginTransaction().replace(R.id.fragmentPlaceholder, fragment).addToBackStack("FragmentJogo");

		/*if(extraFase == 1){
			//Tutorial mode
			Fragment tutorialFragment = getTutorialDialogFragment();
			if(tutorialFragment != null){
				//fm.replace(R.id.fragmentPlaceholder, tutorialFragment).addToBackStack("FragmentTutorial");
			}
			tutorialFragment.
		}*/
		fm.commit();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.ic_play:
				closeMenu();
				break;
			case R.id.ic_reload:
				carregaFragment();
				closeMenu();
				break;
			case R.id.ic_menu:
				finish();
				break;
		}
	}

	public void recarregarFragment(int level){
		Bundle extras = getIntent().getExtras();
		extras.putInt("extraFase", level);
		getIntent().putExtras(extras);
		carregaFragment();
	}

	@Override
	public void onBackPressed() {
		openMenu();
	}
}
