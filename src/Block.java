public class Block {
	private int leftPixel;
	private int basePixel;
	private int rightPixel;
	//coordinate of pixels
	private int xic; 
	private int yic;
	private int xl;
	private int yl;
	private int xr;
	private int yr;
	
	private int leftRange;
	private int rightRange;
	private String embedded;
	private String extracted;
	public String getEmbedded() {
		return embedded;
	}
	public void setEmbedded(String embedded) {
		this.embedded = embedded;
	}
	public String getExtracted() {
		return extracted;
	}
	public void setExtracted(String extracted) {
		this.extracted = extracted;
	}
	public int getLeftPixel() {
		return leftPixel;
	}

	public void setLeftPixel(int leftPixel) {
		this.leftPixel = leftPixel;
	}

	public int getBasePixel() {
		return basePixel;
	}

	public void setBasePixel(int basePixel) {
		this.basePixel = basePixel;
	}

	public int getRightPixel() {
		return rightPixel;
	}

	public void setRightPixel(int rightPixel) {
		this.rightPixel = rightPixel;
	}

	public int getLeftRange() {
		return leftRange;
	}

	public void setLeftRange(int leftRange) {
		this.leftRange = leftRange;
	}

	public int getRightRange() {
		return rightRange;
	}

	public void setRightRange(int rightRange) {
		this.rightRange = rightRange;
	}
	
	public int getXic() {
		return xic;
	}

	public void setXic(int xic) {
		this.xic = xic;
	}

	public int getYic() {
		return yic;
	}

	public void setYic(int yic) {
		this.yic = yic;
	}

	public int getXl() {
		return xl;
	}

	public void setXl(int xl) {
		this.xl = xl;
	}

	public int getYl() {
		return yl;
	}

	public void setYl(int yl) {
		this.yl = yl;
	}

	public int getXr() {
		return xr;
	}

	public void setXr(int xr) {
		this.xr = xr;
	}

	public int getYr() {
		return yr;
	}

	public void setYr(int yr) {
		this.yr = yr;
	}
	
	public int getRangeIndex(String pixel) {
		if(pixel.equals("right")) {
			return getRightRange();
		} else {
			return getLeftRange();
		}
	}

	Block(int leftPixel, int basePixel, int rightPixel, int x, int y) {
		this.leftPixel = leftPixel;
		this.basePixel = basePixel;
		this.rightPixel = rightPixel;
		this.xic = x;
		this.yic = y;
		this.xl = x-1;
		this.yl = y;
		this.xr = x+1;
		this.yr = y;
	}
	
	public String toString() {
		return "L: "+leftPixel+" "+"B: "+basePixel+" R: "+rightPixel;
	}
}
