package org.jerue.AtlasServer;

import java.util.ArrayList;

public class CoordinateDatabase {
	private ArrayList<Coordinate> coords;
	public CoordinateDatabase(){
		this.coords = new ArrayList<Coordinate>();
	}
	public void add(Coordinate c){
		coords.add(c);
	}
	public String toString(){
		if(coords.size() == 0)
			return "empty";
		String r = "";
		for(int c = 0; c < coords.size(); c++){
			Coordinate coord = coords.get(c);
			r = r + coord.toString() + "," + coord.getBitmap() + "~";
		}
		return r; //sends the entire database because yolo
	}
}
