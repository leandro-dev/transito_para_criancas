package com.leandroideias.transito.ache_os_erros;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.leandroideias.baseutils.BaseFragment;
import com.leandroideias.baseutils.CustomAlert;
import com.leandroideias.transito.R;
import com.leandroideias.transito.bll.AcheOsErrosBll;
import com.leandroideias.transito.custom.MyImageView;
import com.leandroideias.transito.custom.MyImageViewTag;
import com.leandroideias.transito.model.ItemDatabase;

/**
 * Created by leandro.castro on 13/05/2014.
 */
public class FragmentJogarAcheOsErros extends BaseFragment implements View.OnTouchListener {
	private ActivityJogarAcheOsErros mActivity;
	private ImageView icPausar;

	private FrameLayout frameEfeitos;
	private FrameLayout layoutImagens;
	private LinearLayout layoutEstrelas;
	private TextView textViewQuantidadeErros;

	private int erros = 0;
	private int acertos = 0;
	private int total = 0;
	private boolean tutorialMode = false;

	public static FragmentJogarAcheOsErros newInstance(){
		FragmentJogarAcheOsErros fragment = new FragmentJogarAcheOsErros();
		return fragment;
	}

	public FragmentJogarAcheOsErros(){
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = (ActivityJogarAcheOsErros) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		tutorialMode = (getArguments().getInt("extraFase", 0) == 1);
		View view = inflater.inflate(R.layout.jogar_ache_os_erros, container, false);
		initView(view);
		if(tutorialMode){
			startTutorial();
		}
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}

	private void initView(View view){
		frameEfeitos = (FrameLayout) view.findViewById(R.id.frameEfeitos);
		layoutImagens = (FrameLayout) view.findViewById(R.id.frameImages);
		layoutEstrelas = (LinearLayout) view.findViewById(R.id.layoutEstrelas);
		textViewQuantidadeErros = (TextView) view.findViewById(R.id.textViewQuantidadeErros);
		icPausar = (ImageView) view.findViewById(R.id.ic_pausar);
		icPausar.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				mActivity.openMenu();
			}
		});

		int fase = getArguments().getInt("extraFase", 1);
		MyImageViewTag[] tags = null;
		switch(fase){
			case 1:
				tags = new MyImageViewTag[]{
					new MyImageViewTag(R.drawable.level_um_background, false, 0, 0, 0, 0, 0, 0),
					new MyImageViewTag(R.drawable.level_um_erro_um, true, 1080f, 530f, 94f, 101f, 485f, 225f)
				};
				break;
			case 2:
				tags = new MyImageViewTag[]{
						new MyImageViewTag(R.drawable.level_dois_background, false, 0, 0, 0, 0, 0, 0),
						new MyImageViewTag(R.drawable.level_dois_erro_um, true, 1080f, 530f, 53f, 82f, 607f, 331f),
						new MyImageViewTag(R.drawable.level_dois_erro_dois, true, 1080f, 530f, 147f, 109f, 504f, 262f)
				};
				break;
			case 3:
				tags = new MyImageViewTag[]{
						new MyImageViewTag(R.drawable.level_tres_background, false, 0, 0, 0, 0, 0, 0),
						new MyImageViewTag(R.drawable.level_tres_erro_um, true, 1080f, 530f, 76f, 77f, 784f, 325f),
						new MyImageViewTag(R.drawable.level_tres_erro_dois, true, 1080f, 530f, 145f, 109f, 900f, 415f),
						new MyImageViewTag(R.drawable.level_tres_erro_tres, true, 1080f, 530f, 163f, 124f, 147f, 326f)
				};
				break;
			case 4:
				tags = new MyImageViewTag[]{
						new MyImageViewTag(R.drawable.level_quatro_background, false, 0, 0, 0, 0, 0, 0),
						new MyImageViewTag(R.drawable.level_quatro_erro_um, true, 1080f, 530f, 153f, 115f, 279f, 180f),
						new MyImageViewTag(R.drawable.level_quatro_erro_dois, true, 1080f, 530f, 72f, 106f, 443f, 146f),
						new MyImageViewTag(R.drawable.level_quatro_erro_tres, true, 1080f, 530f, 148f, 100f, 641f, 10f),
						new MyImageViewTag(R.drawable.level_quatro_erro_quatro, true, 1080f, 530f, 145f, 115f, 910f, 397f)
				};
				break;
			case 5:
				tags = new MyImageViewTag[]{
						new MyImageViewTag(R.drawable.level1_background, false, 0, 0, 0, 0, 0, 0),
						new MyImageViewTag(R.drawable.level1_erro1, true, 815f, 328f, 193f, 62f, 264f, 197f),
						new MyImageViewTag(R.drawable.level1_erro2, true, 815f, 328f, 63f, 52f, 630f, 227f),
						new MyImageViewTag(R.drawable.level1_erro3, true, 815f, 328f, 73f, 45f, 9f, 273f),
						new MyImageViewTag(R.drawable.level1_erro4, true, 815f, 328f, 54f, 54f, 488f, 162f),
						new MyImageViewTag(R.drawable.level1_erro5, true, 815f, 328f, 45f, 49f, 152f, 126f),
						new MyImageViewTag(R.drawable.level1_erro6, true, 815f, 328f, 153f, 62f, 8f, 213f)
				};
				break;
			default:
				CustomAlert alert = new CustomAlert(getActivity(), "Atenção", "Essa fase ainda não existe.", "Ok", null, false);
				alert.setDialogButtonClickListener(alert.new DialogButtonClickListener() {
					public void buttonCancelClicked() {
					}
					public void buttonOkClicked() {
						getDialog().dismiss();
						getActivity().finish();
					}
				});
				alert.createDialog().show();
				return;
		}
		if(tags != null){
			total = tags.length - 1;
			//Cria as ImageViews
			for(int i = 0; i <= total; i++){
				criaView(tags[i]);
			}
			criaEstrelas();
			updateTop();
		}


	}

	private void updateTop(){
		if(acertos > total) acertos = total;
		else if(acertos < 0) acertos = 0;

		for(int a = 0; a < acertos; a++){
			View child = layoutEstrelas.getChildAt(a);
			if(child instanceof ImageView){
				((ImageView) child).setImageResource(android.R.drawable.star_big_on);
			}
		}
		textViewQuantidadeErros.setText("" + erros);

		//Verificações para ver se o jogo acabou
		if(acertos == total){
			FragmentLevelCompletedAcheOsErros frag = new FragmentLevelCompletedAcheOsErros();
			Bundle args = new Bundle();
			int level = getArguments().getInt("extraFase", 1);
			int estrelas = Math.max(0, 3 - (int) (Math.ceil(((double) (erros))/3)));
			int pontos = acertos*100 - erros*10;
			ItemDatabase itemDatabase = new ItemDatabase(1, level, estrelas, erros, pontos);
			AcheOsErrosBll.persisteResultado(itemDatabase);
			args.putSerializable("itemDatabaseAcheOsErros", itemDatabase);
			frag.setArguments(args);
			frag.setCancelable(false);
			frag.show(getActivity().getSupportFragmentManager(), FragmentLevelCompletedAcheOsErros.TAG);
		} else if(erros >= 10){
			FragmentLevelCompletedAcheOsErros frag = new FragmentLevelCompletedAcheOsErros();
			Bundle args = new Bundle();
			int level = getArguments().getInt("extraFase", 1);
			int estrelas = Math.max(0, 3 - (int) (Math.ceil(((double) (erros))/3)));
			int pontos = acertos*100 - erros*10;
			ItemDatabase itemDatabase = new ItemDatabase(1, level, estrelas, erros, pontos);
			args.putSerializable("itemDatabaseAcheOsErros", itemDatabase);
			frag.setArguments(args);
			frag.setCancelable(false);
			frag.show(getActivity().getSupportFragmentManager(), FragmentLevelCompletedAcheOsErros.TAG);
		}
	}

	/**
	 * Método responsável por criar uma imagem na tela representando se o usuário acertou ou errou.
	 * @param posX Posição no eixo X aonde a imagem deve ser criada
	 * @param posY Posição no eixo Y aonde a imagem deve ser criada
	 * @param acerto Booleano que indica se o usuário acertou ou errou
	 */
	private void criaEfeito(int posX, int posY, boolean acerto){
		posX -= frameEfeitos.getLeft();
		posY -= frameEfeitos.getTop();

		//Definindo o tamanho da imagem que será exibida (80dp)
		int pixelSize = (int) (getResources().getDisplayMetrics().density * 80 + 0.5f);

		final ImageView imageView = new ImageView(getActivity());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(pixelSize, pixelSize);
		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.leftMargin = posX;
		params.topMargin = posY;
		imageView.setLayoutParams(params);
		if(acerto) {
			imageView.setImageResource(android.R.drawable.star_big_on);
		} else {
			imageView.setImageResource(R.drawable.ic_close);
		}
		Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.animation_click);
		animation.setAnimationListener(new Animation.AnimationListener() {
			public void onAnimationStart(Animation animation) {}
			public void onAnimationEnd(Animation animation) {
				imageView.postDelayed(new Runnable(){
					public void run() {
						try {
							frameEfeitos.removeView(imageView);
						} catch(Exception e){}
					}
				}, 300);
			}
			public void onAnimationRepeat(Animation animation) {}
		});

		frameEfeitos.addView(imageView);
		animation.setStartOffset(0);
		imageView.startAnimation(animation);
	}

	private void criaEstrelas(){
		for(int a = 0; a < total; a++){
			ImageView imageView = new ImageView(getActivity());
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			imageView.setLayoutParams(params);
			imageView.setImageResource(android.R.drawable.star_big_off);
			layoutEstrelas.addView(imageView);
		}
	}

	/**
	 * Método responsável por criar as imagens do jogo na tela.
	 * @param tag Contém informações sobre a imagem que deve ser criada.
	 */
	private void criaView(MyImageViewTag tag){
		if(!tag.isPositive()){
			//Background image
			ImageView imageView = new ImageView(getActivity());
			imageView.setImageResource(tag.getImageResId());
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			imageView.setLayoutParams(params);
			imageView.setAdjustViewBounds(true);
			imageView.setOnTouchListener(this);
			imageView.setScaleType(ImageView.ScaleType.FIT_XY);
			imageView.setTag(tag);
			layoutImagens.addView(imageView);
		} else {
			//Error image
			ImageView imageView = new MyImageView(getActivity());
			imageView.setImageResource(tag.getImageResId());
			FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
			imageView.setLayoutParams(params);
			imageView.setOnTouchListener(this);
			imageView.setTag(tag);
			layoutImagens.addView(imageView);
		}
	}

	@Override
	public boolean onTouch(View view, MotionEvent event) {
		if(event.getAction() != MotionEvent.ACTION_DOWN) return false;
		if(mActivity.isDrawerOpen()) return false;
		if(!(view instanceof ImageView)) return false;
		ImageView image = ((ImageView) view);
		if(!(image.getTag() instanceof MyImageViewTag)) return false;
		MyImageViewTag tag = (MyImageViewTag) image.getTag();
		boolean positivo = tag.isPositive();
		if(!positivo){
			erros++;
			criaEfeito((int) event.getX() + view.getLeft(), (int) event.getY() + view.getTop(), false);
			updateTop();
			return true;
		} else {
			//Verifica se clicou na região correta de uma imagem ou não.
			float touchX = event.getX();
			float touchY = event.getY();
			Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
			float scaleXFactor = ((float) image.getWidth() / (float) tag.getFullImageWidth());
			float scaleYFactor = ((float) image.getHeight() / (float) tag.getFullImageHeight());

			int x = (int) (touchX /scaleXFactor);
			int y = (int) (touchY / scaleYFactor);

			Rect rect = new Rect(
					(int) tag.getStartX(),
					(int) tag.getStartY(),
					(int) (tag.getStartX() + tag.getRegionWidth()),
					(int) (tag.getStartY() + tag.getRegionHeight())
			);
			if(rect.contains(x, y)){
				acertos++;
				view.setVisibility(View.GONE);
				criaEfeito((int) event.getX() + view.getLeft(), (int) event.getY() + view.getTop(), true);
				updateTop();
				return true;
			} else {
				return false;
			}


			//TODO Verificar o canal alfa da região exata do clique. Se for transparente, deve considerar um erro.
			//Verifica o canal alfa daquela parte do clique.
			/*int alpha = Color.alpha(bitmap.getPixel(x, y));
			if(alpha > 126) {
				//Considero transparente
				acertos++;
				view.setVisibility(View.GONE);
				criaEfeito((int) event.getX() + view.getLeft(), (int) event.getY() + view.getTop(), true);
				updateTop();
				return true;
			} else {
				return false;
			}*/
		}
	}

	private void startTutorial(){
		ShowcaseView sv = new ShowcaseView.Builder(getActivity())
				.setTarget(new ViewTarget(icPausar))
				.setContentTitle("This is a demo")
//				.setStyle(R.style.Transparent)
				.build();
	}

	/*private class TutorialShowcaseListener implements OnShowcaseEventListener {
		private int tutorialStep;

		public TutorialShowcaseListener(){
			tutorialStep = 0;
		}

		@Override
		public void onShowcaseViewHide(ShowcaseView showcaseView) {
			showShowcase(++tutorialStep);
		}

		@Override
		public void onShowcaseViewDidHide(ShowcaseView showcaseView) {

		}

		@Override
		public void onShowcaseViewShow(ShowcaseView showcaseView) {

		}
	}*/
}
