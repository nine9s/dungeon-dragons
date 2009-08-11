package intel.code.expo;

import java.util.Collections;
import java.util.Vector;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.widget.ImageView;
import android.widget.AbsoluteLayout.LayoutParams;

public class Block extends ImageView {

	private int top=0;
	private int left=0;
	private int height=0;
	private int width=0;
	private Bitmap bitmap=null;
	private int sequence =0;


	public Block(Context context,int left, int top, int width, int height, int sequence,Bitmap bitmap) {
		super(context);
		this.top = top;
		this.left = left;
		this.sequence = sequence;
		this.width=width;
		this.height=height;
		this.bitmap=bitmap;

		this.setImageBitmap(Bitmap.createBitmap(bitmap, left, top, width, height));
		}


	public AbsoluteLayout.LayoutParams getViewLayoutParams(){
		return new LayoutParams(width,height,left,top);
	}
	
	public AbsoluteLayout.LayoutParams getRandomViewLayoutParams(int leftPossible,int topPossible,int seq){
		this.left=leftPossible;
		this.top=topPossible;
		return new LayoutParams(width,height,leftPossible,topPossible);
	}

	public boolean isTapped(float  x, float y){
		return (x>left && x<(left+getWidth()) && y>top && y < (top+getHeight()));
	}
	
	/**
	 * @param top the top to set
	 */
	public void setTop(int top) {
		this.top = top;
	}
	/**
	 * @param left the left to set
	 */
	public void setLeft(int left) {
		this.left = left;
	}
	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}
	
	public String toString(){
		return "sequence="+sequence+", top="+top+", left="+left;
	}
	
	
}
