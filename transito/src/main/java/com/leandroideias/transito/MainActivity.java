package com.leandroideias.transito;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements View.OnTouchListener {
	private ImageView background;
	private ImageView[] errors;
	private TextView textViewCorrect;
	private TextView textViewWrong;
	private int acertos;
	private int erros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textViewCorrect = (TextView) findViewById(R.id.textViewCorrect);
		textViewWrong = (TextView) findViewById(R.id.textViewWrong);
		background = (ImageView) findViewById(R.id.backgroundImage);
		errors = new ImageView[]{
				(ImageView) findViewById(R.id.erro_um),
				(ImageView) findViewById(R.id.erro_dois)
		};

//		background.setOnClickListener(this);
		background.setOnTouchListener(this);
		for(ImageView image : errors){
			image.setOnTouchListener(this);
//			image.setOnClickListener(this);
		}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.action_refresh:
				acertos = erros = 0;
				for(ImageView image : errors) image.setVisibility(View.VISIBLE);
				updateTextViews();
				return true;
			case R.id.action_save:

				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
    }

	private void updateTextViews(){
		textViewCorrect.setText("" + acertos);
		textViewWrong.setText("" + erros);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) return false;
		if(v.getId() == background.getId()){
			erros++;
			updateTextViews();
			return true;
		} else for(ImageView image : errors) if(image.getId() == v.getId()){
			float touchX = event.getX();
			float touchY = event.getY();
			float[] touchXY = new float[]{touchX, touchY};

			Matrix invertMatrix = new Matrix();
			image.getImageMatrix().invert(invertMatrix);

			invertMatrix.mapPoints(touchXY);
			int x = Integer.valueOf((int) touchXY[0]);
			int y = Integer.valueOf((int) touchXY[1]);

			Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
			if(x < 0) x = 0;
			else if(x >= bitmap.getWidth()) x = bitmap.getWidth() - 1;

			if(y < 0) y = 0;
			else if(y >= bitmap.getHeight()) y = bitmap.getHeight() - 1;


			//Verifica o canal alfa daquela parte do clique.
			int alpha = Color.alpha(bitmap.getPixel(x, y));
			if(alpha > 126) {
				//Considero transparente
				acertos++;
				image.setVisibility(View.GONE);
				updateTextViews();
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
