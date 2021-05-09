package ui;

public class PuzzleFieldStyle {
	private String color;
    private int number;
 
    public PuzzleFieldStyle(int number,String color) {
        super();
        this.number = number;
        this.color = color;
    }
 
    public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getColor() {
        return color;
    }
 
    public void setColor(String color) {
        this.color = color;
    }
 
    @Override
    public String toString() {
        return color;
    }
}
