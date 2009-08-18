package intel.code.expo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.widget.AbsoluteLayout;
import android.widget.Toast;
import android.widget.AbsoluteLayout.LayoutParams;

public class PicturePuzzle extends Activity {

	public static final Level[] LEVELS = new Level[] { new Level("Easy", 3, 3),
			new Level("Medium", 4, 4), new Level("Hard", 5, 5) };

	private int locations[] = new int[] { 0, 0 };

	private List<Block> blocks = new ArrayList<Block>();
	private String originalShape="";
	private AbsoluteLayout absoluteLayout = null;
	private Block selectedBlock = null;
	private Vector<String> possiblePosition= new Vector<String>();
	private Vector<Integer> topPossible= new Vector<Integer>();
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		blocks.clear();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.image);

		Level level = LEVELS[1];
		Display display = getWindowManager().getDefaultDisplay();

		bitmap = bitmap.createScaledBitmap(bitmap, display.getWidth(), display
				.getHeight(), false);
		int width = display.getWidth() / level.getColumns();
		int height = display.getHeight() / level.getRows();

		absoluteLayout = new AbsoluteLayout(this);
		
		int count = 0;
		for (int row = 0; row < level.getRows(); row++) {

			for (int column = 0; column < level.getColumns(); column++) {
				Block block = new Block(this, column * width, row * height,
						width, height, count++, bitmap);
				possiblePosition.add(""+column * width+":"+row * height);
				//topPossible.add(row * height);
				blocks.add(block);
		}
			
		}
		shufflePicture();
		
		//Log.e("my"+blocks.size(),topPossible.toString());
		for(int i=0;i<blocks.size();i++)
		{
			originalShape+=blocks.get(i).toString();
			//Log.e("my"+blocks.size(), leftPossible.get(i)+":"+topPossible.get(i)+" "+blocks.get(i).toString());
			String[] position=possiblePosition.get(i).split(":");
			
			absoluteLayout.addView(blocks.get(i), blocks.get(i).getRandomViewLayoutParams(Integer.parseInt(position[0]),Integer.parseInt(position[1]),i));
		}

		this.setContentView(absoluteLayout);

	}
	public String getCurrentShape(){
		String currentShape="";
        BlocksComparator comparator = new BlocksComparator();
		Collections.sort(blocks, comparator);
		for(int i=0;i<blocks.size();i++)
		{
			currentShape+=blocks.get(i).toString();
		//Log.e("my"+blocks.size(), leftPossible.remove(0)+":"+topPossible.remove(0)+" "+blocks.get(i).toString());
			
		}
	return currentShape;
	}
	public void shufflePicture()
	{
		Collections.shuffle(possiblePosition);
		
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		absoluteLayout.getLocationInWindow(locations);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			selectedBlock = getBlock(event.getX() - locations[0], event.getY()
					- locations[1]);
			log("Selected " + blocks.indexOf(selectedBlock));
			return true;

		case MotionEvent.ACTION_MOVE:
			if (selectedBlock != null) {

				absoluteLayout.removeView(selectedBlock);
				absoluteLayout.invalidate();
				LayoutParams layoutParams = selectedBlock.getViewLayoutParams();
				int x = (int) (event.getX() - locations[0]);
				int y = (int) (event.getY() - locations[1]);

				layoutParams.x = x - (layoutParams.width / 2);
				layoutParams.y = y - (layoutParams.height / 2);

				absoluteLayout.addView(selectedBlock, layoutParams);
				absoluteLayout.refreshDrawableState();
				//log("Moving " + blocks.indexOf(selectedBlock) + " to "						+ layoutParams.x + "," + layoutParams.y);
			}
			return true;
		case MotionEvent.ACTION_UP:
			boolean swapped = false;
			
			
			if (selectedBlock != null) {
				for (Block endingBlock : blocks) {
					if (!swapped
							&& endingBlock.isTapped(event.getX(),
									(event.getY() - locations[1]))) {
						// If swapping with itself, restore the position
						if (endingBlock.equals(selectedBlock)) {
							restoreSelected();
						} else {
							LayoutParams newSelectedBlockLayout = endingBlock
									.getViewLayoutParams();
							LayoutParams newEndingBlockLayout = selectedBlock
									.getViewLayoutParams();

							absoluteLayout.removeView(selectedBlock);
							absoluteLayout.removeView(endingBlock);

							absoluteLayout.addView(selectedBlock,
									newSelectedBlockLayout);
							absoluteLayout.addView(endingBlock,
									newEndingBlockLayout);

							selectedBlock.setLeft(newSelectedBlockLayout.x);
							selectedBlock.setTop(newSelectedBlockLayout.y);
							endingBlock.setLeft(newEndingBlockLayout.x);
							endingBlock.setTop(newEndingBlockLayout.y);
							log("Swapped " + blocks.indexOf(selectedBlock)
									+ " with " + blocks.indexOf(endingBlock));
							Collections.swap(blocks, blocks
									.indexOf(selectedBlock), blocks
									.indexOf(endingBlock));
							absoluteLayout.invalidate();
							swapped = true;
						}
					}
				}
				String currentPuzzleState=getCurrentShape();
				Log.e("tag", (originalShape)+":"+(currentPuzzleState));
				//Log.e("tag", );
				if((originalShape).equalsIgnoreCase((currentPuzzleState)))
					{
					Log.e("tag", "Yeye");
					Toast toast = Toast.makeText(this, "Correct Now click the back button", Toast.LENGTH_LONG);
					toast.show();
					}
			}
			return true;

		default:
			restoreSelected();
			return super.onTouchEvent(event);
		}
	}

	public  String stringToHex(String base)
    {
     StringBuffer buffer = new StringBuffer();
     int intValue;
     for(int x = 0; x < base.length(); x++)
         {
         int cursor = 0;
         intValue = base.charAt(x);
         String binaryChar = new String(Integer.toBinaryString(base.charAt(x)));
         for(int i = 0; i < binaryChar.length(); i++)
             {
             if(binaryChar.charAt(i) == '1')
                 {
                 cursor += 1;
             }
         }
         if((cursor % 2) > 0)
             {
             intValue += 128;
         }
         buffer.append(Integer.toHexString(intValue) + " ");
     }
     return buffer.toString();
}

	private void restoreSelected() {
		LayoutParams originalBlockLayout = selectedBlock.getViewLayoutParams();

		absoluteLayout.removeView(selectedBlock);

		absoluteLayout.addView(selectedBlock, originalBlockLayout);
		selectedBlock.setLeft(originalBlockLayout.x);
		selectedBlock.setTop(originalBlockLayout.y);
	}

	private Block getBlock(float x, float y) {

		log("Block Order = " + blocks.toString());
		for (Block block : blocks) {
			if (block.isTapped(x, y)) {
				return block;
			}
		}
		return null;

	}

	static void log(String msg) {
		Log.d("Picture Puzzle", msg);
	}
	

	public class BlocksComparator implements Comparator<Block>{

	    @Override
	    public int compare(Block movie1, Block movie2) {

	        int rank1 = movie1.getSequence();
	        int rank2 = movie2.getSequence();

	        if (rank1 > rank2){
	            return +1;
	        }else if (rank1 < rank2){
	            return -1;
	        }else{
	            return 0;
	        }
	    }
	}


}
