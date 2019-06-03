package br.com.richard.piano;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;

import java.util.Random;

import static br.com.richard.piano.Constants.*;

public class PianoTiles extends ApplicationAdapter {

	private ShapeRenderer shapeRenderer;
	private SpriteBatch batch;

	private Array<Fileira> fileiras;
	private float tempoTotal;
	private int indexInf;
	private int pontos;
	private Random rand;
	private int estado;

	private Texture texIniciar;
	private Piano piano;

	private BitmapFont fonte;
	private GlyphLayout glyphLayout;

	@Override
	public void create () {
		shapeRenderer = new ShapeRenderer();
		shapeRenderer.setAutoShapeType(true);

		batch = new SpriteBatch();

		fileiras = new Array<Fileira>();
		rand = new Random();

		texIniciar = new Texture("iniciar.png");

		piano = new Piano("natal");

		glyphLayout = new GlyphLayout();


		FreeTypeFontGenerator.setMaxTextureSize(2048);
		FreeTypeFontGenerator genarator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (int)(0.07f *screeny);
		parameter.color = Color.BLACK;
		fonte = genarator.generateFont(parameter);
		genarator.dispose();

		iniciar();
	}

	@Override
	public void render () {
		input();
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		shapeRenderer.begin();

		for(Fileira f: fileiras){
			f.draw(shapeRenderer);
		}

		shapeRenderer.end();

		batch.begin();

		if(estado == 0)	batch.draw(texIniciar, 0, tileHeight /4, screenx, tileHeight/2);

		fonte.draw(batch, pontos+"", 0, screeny);
		fonte.draw(batch, String.format("%.3f", velAtual/tileHeight), screenx - getWidth(fonte, String.format("%.3f", velAtual/tileHeight)), screeny);

		batch.end();
	}

	private void input() {
		if(Gdx.input.justTouched()){
			int x = Gdx.input.getX();
			int y = screeny - Gdx.input.getY();

			if(estado == 0) estado = 1;
			if(estado == 1){
				for(int i = 0; i < fileiras.size; i++){
					int retorno = fileiras.get(i).toque(x, y);
					if(retorno != 0){
						if(retorno == 1 && i == indexInf){
							pontos++;
							indexInf++;
							piano.tocar();
						} else if(retorno == 1){
							fileiras.get(indexInf).erro();
							finalizar(0);
						} else {
							finalizar(0);
						}
						break;
					}
				}
			}
			 else if(estado == 2) iniciar();
		}
	}

	private void finalizar(int opt) {
		Gdx.input.vibrate(200);
		estado = 2;
		if(opt == 1){
			for(Fileira f : fileiras){
				f.y += tileHeight;
			}
		}
	}

	private void update(float deltaTime) {
		if(estado == 1){
			tempoTotal += deltaTime;

			velAtual = velIni+ tileHeight * tempoTotal / 8f;

			for(int i = 0; i < fileiras.size; i++){
				int retorno = fileiras.get(i).update(deltaTime);
				fileiras.get(i).anim(deltaTime);
				if(retorno != 0){
					if(retorno == 1) {
						fileiras.removeIndex(i);
						i--;
						indexInf--;
						adicionar();
					} else if(retorno ==2){
						finalizar(1);
					}
				}
			}
		} else if(estado == 2){
			for(Fileira f : fileiras) {
				f.anim(deltaTime);
			}
		}
	}

	private void adicionar() {
		float y = fileiras.get(fileiras.size-1).y + tileHeight;
		fileiras.add(new Fileira(y, rand.nextInt(4)));
	}

	public void iniciar(){
		tempoTotal = 0;
		indexInf = 0;
		pontos = 0;

		fileiras.clear();
		fileiras.add(new Fileira(tileHeight, rand.nextInt(4)));
		adicionar();
		adicionar();
		adicionar();
		adicionar();

		estado = 0;
		velAtual = 0;

		piano.reset();
	}

	public float getWidth(BitmapFont font, String texto){
		glyphLayout.reset();
		glyphLayout.setText(font, texto);
		return glyphLayout.width;
	}

	@Override
	public void dispose () {
		shapeRenderer.dispose();
		batch.dispose();
		texIniciar.dispose();
		piano.dispose();
	}
}
