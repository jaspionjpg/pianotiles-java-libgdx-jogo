package br.com.richard.piano;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

public class Constants {

    public static Color verde = new Color(0, 0.4f, 0, 1);
    public static Color certo = new Color(1f, 0.988f, 0.604f, 1);
    public static Color errado = new Color(0.71f, 0.282f, 0.302f, 1);

    public static int screenx = Gdx.graphics.getWidth();
    public static int screeny = Gdx.graphics.getHeight();

    public static int tileWidth = screenx/4;
    public static int tileHeight = screeny/4;

    public static float velIni = 2*tileHeight/1f;
    public static float velAtual = 0;
}
