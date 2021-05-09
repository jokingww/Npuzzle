package ui;

public class Point {
	private int x;
    private int y;
 
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Point(Point point) {
    	super();
    	this.x=point.x;
    	this.y=point.y;
    }
 
    public int getX() {
        return x;
    }
 
    public int getY() {
        return y;
    }
 
    public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
    public String toString() {
        return "x:"+x+" y:"+y;
    }
}
