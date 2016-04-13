package com.davebilotta.whatzit;

import java.util.ArrayList;
import java.util.Collections;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TileManager {
	private WhatzIt game;
	private Texture tileImage;
	private boolean hasTiles;
	private int tileSize,tileHoriz,tileVert;
	private int[][] tileMatrix;
	private ArrayList<Integer> tileRemovalList;
	int removalCount;
	private int tileValue;
	
	public TileManager(WhatzIt game) { 
		// TODO: Figure out tile size - base this on SPEED variable
		this.game = game;
		this.setTileSize(128);
		this.hasTiles = true;
		this.tileValue = 10;
				
		Utils.log("There are " + tileHoriz + " tiles on the X and " + tileVert + " tiles on the Y");
	}

	public void renderTiles(SpriteBatch sb) {
		
		Texture tile_img = this.tileImage;
		int s = this.tileSize;
			
		/* Iterate through array of tiles - if removed, don't display */
		for (int j = 0; j < this.tileVert; j++) {
			for (int i = 0; i < this.tileHoriz; i++) {
				if (this.tileMatrix[j][i] == 0 ) {
					sb.draw(tile_img,(s * i),(s * j),s,s);
				}
			}
		}
	}

	/* -------------------------------------------------------------------------------------- */
	// Move these to a Tile class
	public void removeTile(int i, int j) {
		this.tileMatrix[i][j] = 1;
	}
	
	public void buildTileMatrix() { 
		this.tileHoriz = this.game.WIDTH / tileSize;
		this.tileVert = this.game.HEIGHT / tileSize;
		tileMatrix = new int[tileVert][tileHoriz];
	}
	
	/* Determines what tiles will be removed - do this up front to avoid having to find random tiles 
	 * each time / check to see that they haven't already been removed, repeat if necessary, etc.
	 */
	public void buildTileRemovalList() {
		tileRemovalList = new ArrayList<Integer>();
		removalCount = 0;
		
		int s = this.tileSize;
	    // XXXXX = 0,1,2,3,4
		// XXXXX = 5,6,7,8,9
		for (int i = 0; i < (this.tileHoriz * this.tileVert); i++) {		
			tileRemovalList.add(i);
		
		}
		Utils.log("TILES ARE " + Utils.pretty(tileRemovalList,","));
		Collections.shuffle(tileRemovalList);		
		Utils.log("(SHUFFLED TILES ARE " + Utils.pretty(tileRemovalList,","));
	}
	
	public void removeNextTile() {
		if (!tileRemovalList.isEmpty()) {
			Integer tile = tileRemovalList.remove(0);
			
			int[] i = toTwoDimension(tile);
			this.tileMatrix[i[1]][i[0]] = 1;
			//Utils.log("Removed " + tile);
			removalCount++;
			Utils.log("Have removed " + removalCount + " tiles so far");
		}
		else { 
			//TODO: How to handle this gameover = true;
			Utils.log("Nothing to remove, question over");
			this.hasTiles = false;
			
		}
	}
	
	public boolean hasTiles() { return this.hasTiles; }
	
	public int toOneDimension(int i, int j) {
		return i + (j * this.tileSize);
	}
	
	public int[] toTwoDimension(int tileNum) {
		// given tilenum (e.g. 100), return [x,y]
		int y = tileNum / this.tileHoriz;
		int x = tileNum % this.tileVert;
		 	
		//Utils.log("tilenum " + tileNum + " is at " + x + "," + y);
		return new int[]{x,y};		
	}
	
	public int getTileSize() { 
		return this.tileSize;
	}
	
	public void setTileSize(int num) {
		this.tileSize = num;
		this.tileImage = this.game.im.getTileImage(num);
		
		buildTileMatrix();
		buildTileRemovalList();
		
	}
	
	public void nextQuestion() { 
		// Triggered on each new question 
		// Build list of tiles that we're going to remove this question 
		this.hasTiles = true;
		buildTileMatrix();
		buildTileRemovalList();
				
	}
	
	public int getWinScore() { 
		return this.tileRemovalList.size() * 10;
	}
	
	public int getTileValue() { 
		return this.tileValue;
	}
	
}
