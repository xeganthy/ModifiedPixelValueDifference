public class Block {
	public int leftPixel;
	public int basePixel;
	public int rightPixel;
	//coordinate of basepixel
	public int xic; 
	public int yic;
	public int xl;
	public int yl;
	public int xr;
	public int yr;
	
	Block(int leftPixel, int basePixel, int rightPixel, int x, int y) {
		this.leftPixel = leftPixel;
		this.basePixel = basePixel;
		this.rightPixel = rightPixel;
		this.xic = x;
		this.yic = y;
		xl = x-1;
		yl = y;
		xr = x+1;
		yr = y;
	}
	
	public String toString(Block block) {
		return "L: "+leftPixel+" "+"B: "+basePixel+" R: "+rightPixel;
	}
}
