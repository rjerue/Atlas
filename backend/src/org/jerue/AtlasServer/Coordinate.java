package org.jerue.AtlasServer;

public class Coordinate {
	protected double x, y;
	protected String bitmap;
	public Coordinate(String input){
		System.out.println("The Input to be cut up is" + input);
		String[] contents = input.split(",");
		this.x = Double.parseDouble(contents[0]);
		this.y = Double.parseDouble(contents[1]);
		this.bitmap = contents[2];
	}
	public Coordinate(double x, double y, String bitmap){
		this.x = x;
		this.y = y;
		this.bitmap = bitmap;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public String getBitmap() {
		return bitmap;
	}
	public void setBitmap(String bitmap) {
		this.bitmap = bitmap;
	}
	public boolean equals(Coordinate c){
		return c.getX() == this.x && c.getY() == this.getY();
	}
	public String toString(){
		return "" + this.x + "," + this.y;
	}
}