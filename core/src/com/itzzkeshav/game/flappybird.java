package com.itzzkeshav.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import jdk.internal.net.http.common.Log;

public class flappybird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture bg;
	Texture gameOver;
	// Initializing Shape
	//ShapeRenderer shapeRenderer;


	Texture [] birds;
	int flapstate=0;
	float birdY = 0;
	float velocity= 0;
// Using a circle shape
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;


	int gamestate = 0;
	float gravity= 2;
	Texture top;
	Texture bottom;
	float gap =450 ;
	float maxtubeOffset;
	Random randomGenerator;
	float tubeVelocity = 5;
	int noOfTubes=100;
	float[] tubeX = new float[noOfTubes];
	float[] tubeOffset= new float[noOfTubes];
	float distanceBWtubes;
	Rectangle[] toptubeRectangles;
	Rectangle[] bottomtubeRectangles;

	@Override
	public void create () {
		batch = new SpriteBatch();
		bg = new Texture("bggg.png");
		gameOver = new Texture("gameover.png");

		//shapeRenderer= new ShapeRenderer();
		birdCircle=new Circle();

		birds = new Texture[2];

		birds[0]= new Texture("bird.png");
		birds[1]= new Texture("bird2.png");
		birdY = Gdx.graphics.getHeight()/2;        // This is a reference position thorugh which the tappin will start
		font =  new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);

		top =  new Texture("toptube.png");
		bottom =  new Texture("bottomtube.png");
		maxtubeOffset = Gdx.graphics.getHeight()/2-gap/2-100;
		randomGenerator = new Random();
		//tubeX = Gdx.graphics.getWidth()/2 - top.getWidth()/2;       // This is same for both the top and bottom pipes
		distanceBWtubes = Gdx.graphics.getWidth()*3/4;
		toptubeRectangles = new Rectangle[noOfTubes];
		bottomtubeRectangles = new Rectangle[noOfTubes];
		startGame();
	}

	public void startGame(){
		birdY = Gdx.graphics.getHeight()/2;        // This is a reference position thorugh which the tappin will start
		for(int i=0;i<noOfTubes;i++){
			tubeOffset[i] = (randomGenerator.nextFloat()-0.5f) * (Gdx.graphics.getHeight()-gap-1300);
			tubeX[i] = Gdx.graphics.getWidth()/2 - top.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBWtubes;  // This again and again shows the tube // This is because every time we multiply the value and equal distance is created
			// Iniitializing the rectangles
			toptubeRectangles[i] = new Rectangle();
			bottomtubeRectangles[i] = new Rectangle();
		}
	}

	@Override
	public void render () {
		// Bringing the back screen and bird
		batch.begin();
		batch.draw(bg, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gamestate==1) {
			//Checking  if the tube goes to left we will increase score
			if (tubeX[scoringTube]<Gdx.graphics.getWidth()/3){
				Gdx.app.log("score",String.valueOf(score));
				score++;

				if (scoringTube<noOfTubes-1){
					scoringTube++;
				}
				else {
					scoringTube=0;
				}
			}

			if (Gdx.input.justTouched()){
				velocity=-30;

			}
			// Logic for drawing tubes
			for(int i=0;i<noOfTubes;i++) {
				tubeX[i] = tubeX[i] - tubeVelocity;

				batch.draw(top, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottom, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottom.getHeight() + tubeOffset[i]);

				//Rendering the rectangles shape

				toptubeRectangles[i]= new Rectangle(tubeX[i],Gdx.graphics.getHeight()/2+ gap / 2 + tubeOffset[i],top.getWidth(),top.getHeight());
				bottomtubeRectangles[i]= new Rectangle(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottom.getHeight() + tubeOffset[i],bottom.getWidth(),bottom.getHeight());

			}
// Holding the bird to not fall
//			if (birdY>0 ||  velocity<0) {
//				velocity = velocity + gravity;
//				birdY -= velocity;
//			}

			if (birdY>0 ||  velocity<0){
				velocity = velocity + gravity;
				birdY -= velocity;
			}
			else{
				gamestate=2;  // game over state
			}

		}
		else if(gamestate==0) {
			if (Gdx.input.justTouched()){
				gamestate=1;
				Gdx.app.log("Pressed","Yep");
			}

		}
		else if(gamestate==2){
			batch.draw(gameOver,Gdx.graphics.getWidth()/3.6f,Gdx.graphics.getHeight()/2);
			if (Gdx.input.justTouched()){
				gamestate=1;
				startGame();
				score=0;
				scoringTube=0;
				velocity=0;
			}

		}
		// Birds flap moving
		if (flapstate == 0) {
			flapstate = 1;
		} else {
			flapstate = 0;
		}
		batch.draw(birds[flapstate], Gdx.graphics.getWidth() /3, birdY);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();
		// Start the shape renderer

		birdCircle.set(Gdx.graphics.getWidth()/2.5f,birdY+birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);
		//  Once the Collison is checked we have to turn down the shape rendere
		//	shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	    //	shapeRenderer.setColor(Color.RED);
		// directly using the radius x and y cordinate from the above line
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);
		for(int i=0;i<noOfTubes;i++){
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight()/2+ gap / 2 + tubeOffset[i],top.getWidth(),top.getHeight());
			//shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottom.getHeight() + tubeOffset[i],bottom.getWidth(),bottom.getHeight());
		// Checking the collison
			if(Intersector.overlaps(birdCircle,toptubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomtubeRectangles[i])){
				gamestate = 2;
			}
		}

    //		shapeRenderer.end();
	}

}
