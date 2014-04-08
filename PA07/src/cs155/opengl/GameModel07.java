package cs155.opengl;

import java.util.ArrayList;

/**
 * This is a demo of a simple game where the
 * avatar throws bombs at the foes from a spot in the
 * corner of a room. The player rotates the view
 * by dragging the mouse and fires by clicking the mouse
 * 
 * This file stores the model which consists of an arraylist
 * of Foes and an arraylist of Bombs. The Foes update their
 * movement by randomly changing their velocity. The Bombs
 * are under the control of gravity
 * @author tim
 *
 */
public class GameModel07 {
	/*
	 * size of the gameboard
	 */
	public int width = 100;
	public int height = 100;
	
	public ArrayList<Foe> foes = new ArrayList<Foe>();
	public Foe avatar;
	
	/**
	 * create the foes and avatar for the game
	 * @param numFoes
	 */
	public GameModel07(int numFoes){
		for(int i=0;i<numFoes;i++){
			foes.add(new Foe(1f,width/2f,height/2f,this));
		}
		avatar = new Foe(1f,width/2f,height/2f,this);
	}
	
	public void update(){
		for(Foe f: foes){
			f.update();
		}
		avatar.update();
	}

}
