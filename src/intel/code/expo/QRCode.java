package intel.code.expo;

import com.google.tts.*;
import android.view.MotionEvent;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import java.util.List;
import java.util.Vector;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.app.TabActivity;
import android.widget.TabHost;

public final class QRCode extends TabActivity {
	private static final String TAG = "SearchBookContents";
	private static final String USER_AGENT = "ZXing/1.3 (Android)";
	int chkpt = 0;
	private Paint mPaintShapeDraw;
	boolean barrier_1 = false;
	boolean barrier_2 = false;
	String shapeTobeDrawn = "2:0--1:0--0:0--0:1--1:1--2:1--2:2--1:2--0:2";
	Vector<Integer> rectangleLeftXCoordinates = new Vector<Integer>();
	Vector<Integer> rectangleLeftYCoordinates = new Vector<Integer>();
	int rectangleStartX = 50;
	int rectangleStartY = 50;
	int rectangleGapX = 95;
	int rectangleGapY = 120;
	int rectangeWidth = 50;
	int rectangeHeight = 50;
	String answer = "";
	String congratulations = "";
	
	
	 private static final int DIALOG_YES_NO_MESSAGE = 1;
	  private ProgressBar mProgressBar;
	    private int mProgressStatus = 12;
	    private RectF rect;
	    private Paint mPaint;
	    Canvas canvas;
	    private Handler mHandler;

	
	
	
	EditText answerText;
	TextView questionLabel;
	TabHost tabHost;
	private TTS myTts;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		tabHost = getTabHost();
		mPaintShapeDraw = new Paint();
		mPaintShapeDraw.setAntiAlias(true);
		mPaintShapeDraw.setDither(true);
		mPaintShapeDraw.setColor(0xFFFF0000);
		mPaintShapeDraw.setStyle(Paint.Style.STROKE);
		mPaintShapeDraw.setStrokeJoin(Paint.Join.ROUND);
		mPaintShapeDraw.setStrokeCap(Paint.Cap.ROUND);
		mPaintShapeDraw.setStrokeWidth(12);
		LayoutInflater.from(this).inflate(R.layout.tabs,
				tabHost.getTabContentView(), true);

		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Status")
				.setContent(R.id.view1));

		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Map")
				.setContent(R.id.view2));

		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Map")
				.setContent(R.id.view3));

        // View drawShape=findViewById(R.id.view2);
		// drawShape.setOnTouchListener(mTouchListener);
		// FrameLayout fl= (FrameLayout)findViewById(R.id.view2);
		// fl.addView(new MyView(this));

		// this.startActivityFromChild(PicturePuzzle., i, 1);

		/*
		 * View scan_qr_code = findViewById(R.id.scan_qr_code);
		 * scan_qr_code.setOnClickListener(mScanQRCode); View get_question =
		 * findViewById(R.id.preview_view);
		 * get_question.setOnClickListener(mGetQuestion);
		 */
		View check_answer = findViewById(R.id.checkAnswer);
		check_answer.setOnClickListener(mcheckAnswer);
		answerText = (EditText) findViewById(R.id.answerEdit);
		questionLabel = (TextView) findViewById(R.id.questionLabel);
		myTts = new TTS(this, ttsInitListener, true);
		// showDialog(R.string.app_name,"lol");
		/*
		 * setContentView(R.layout.touch_test); View topLayout =
		 * this.findViewById(R.id.layout_id); // register for events for the
		 * view, previously topLayout.setOnTouchListener((OnTouchListener)
		 * mTouchListener);
		 */

        Button twobuttons = (Button) findViewById(R.id.two_buttons);
        twobuttons.setOnClickListener(new OnClickListener() {public void onClick(View v) 
        {
        	Log.e("TAG3","i m printed3");
        	showDialog(DIALOG_YES_NO_MESSAGE);
        }});
        
        
    /*     
        rect = new RectF(250, 270, 310, 330); 
        canvas.drawRect(rect, mPaint);
      */  
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        
        
        
        mHandler= new Handler();
        {
        	mProgressBar.setProgress(mProgressStatus);
        }
       
        
        Log.e("TAG1","i m printed1");
	
        
        
	
       
	    
		
	}

	private TTS.InitListener ttsInitListener = new TTS.InitListener() {
		public void onInit(int version) {
			myTts
					.speak(
							"The SCA building is a building that has artificial intelligence. The building itself controls everything in the building with its programmed AI. One day, terrorists occupied the building and reprogrammed the AI to make the building try to explode itself with a time bomb installed in a secret room to kill all people in the building. The only way to stop the building is finding the secret room and defusing the bomb.You are confined in the building. In order to survive in the building, you have to find the secret room and defuse the time bomb in 15 minutes. The building gives you quizzes and missions related to movies and the history of the building to interrupt you. You have to overcome the building’s knowledge to clear mini games and missions given by the building.  ",
							0, null);
		}
	};

	public final Button.OnClickListener mScanQRCode = new Button.OnClickListener() {
		public void onClick(View v) {
			// Intent intent = new
			// Intent("com.google.zxing.client.android.SCAN");
			// intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			// startActivityForResult(intent, 0);
			Intent i = new Intent(QRCode.this, PicturePuzzle.class);
			startActivity(i);

		}
	};

	public final Button.OnClickListener circleShape = new Button.OnClickListener() {
		public void onClick(View v) {
			Log.e(TAG, "HTTP returned " + "circle");
		}
	};
	public final Button.OnClickListener mGetQuestion = new Button.OnClickListener() {
		public void onClick(View v) {
			NetworkThread mNetWoNetworkThread = new NetworkThread();
			mNetWoNetworkThread.get();
		}
	};

	public final Button.OnClickListener mcheckAnswer = new Button.OnClickListener() {
		public void onClick(View v) {
			if (answerText.getText().toString().equalsIgnoreCase(answer))
				showDialog(R.string.question, "Correct");
			else
				showDialog(R.string.question, "Wrong");
			Log.e(TAG, "HTTP returned " + answerText.getText().toString() + ":"
					+ answer);
		}
	};

	protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_YES_NO_MESSAGE:
        		
            return new AlertDialog.Builder(QRCode.this)
                 
            .setIcon(R.drawable.alert_dialog_icon)
            .setTitle(R.string.alert_dialog_two_buttons_title)
            .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() 
                {
                	
                   public void onClick(DialogInterface dialog, int whichButton) {Log.e("TAG4","i m printed4");}
                })
                 .create();
           
            
        			}
        	return null;
 		}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// showDialog(R.string.result_succeeded, "Format: " + format +
				// "\nContents: " + contents);
				JSONObject json = null;
				try {
					json = new JSONObject(contents);
					questionLabel.setText((String) json.get("question"));
					answer = (String) json.get("answer");
					congratulations=(String) json.get("congrats");
					if(answer.equalsIgnoreCase("NO"))
					{
						showDialog(R.string.question,congratulations);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (resultCode == RESULT_CANCELED) {
				showDialog(R.string.result_failed,
						getString(R.string.result_failed_why));
			}
		}
	}

	public void showDialog(int title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		builder.show();
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		populateMenu(menu);
		return super.onCreateOptionsMenu(menu);
	}

	public static final int GET_QUESTION = Menu.FIRST + 1;
	public static final int SCAN_QR_CODE = Menu.FIRST + 2;
	public static final int GET_PICTURE_PUZZLE = Menu.FIRST + 3;

	public static final int DRAW_SHAPE = Menu.FIRST + 4;
	
	/** create the menu items */
	public void populateMenu(Menu menu) {

		menu.add(0, GET_QUESTION, 0, "GET QUESTION");
		menu.add(0, SCAN_QR_CODE, 0, "SCAN QR CODE");
		menu.add(0, GET_PICTURE_PUZZLE, 0, "GET PICTURE PUZZLE");
		menu.add(0, DRAW_SHAPE, 0, "DRAW SHAPE");
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		TabHost.TabSpec spec = tabHost.newTabSpec("Question");

		spec.setContent(R.id.view3);
		spec.setIndicator("Question");
		switch (item.getItemId()) {
		case GET_QUESTION:
			tabHost.addTab(spec);
			
			NetworkThread mNetWoNetworkThread = new NetworkThread();
			mNetWoNetworkThread.get();
			return true;
		case SCAN_QR_CODE:
			tabHost.addTab(spec);
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);

			return true;
		case GET_PICTURE_PUZZLE:
			
			Intent i = new Intent(QRCode.this, PicturePuzzle.class);
			startActivity(i);
			return true;
		case DRAW_SHAPE:
			tabHost.setCurrentTab(1);
			FrameLayout fl= (FrameLayout)findViewById(R.id.view2);
			fl.addView(new MyView(this));
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public class MyView extends View {

		private static final float MINP = 0.25f;
		private static final float MAXP = 0.75f;

		private Bitmap mBitmap;
		private Canvas mCanvas;
		private Path mPath;
		private Paint mBitmapPaint;

		public MyView(Context c) {
			super(c);
			// setBackgroundColor(R.drawable.shape);
			mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
			mCanvas = new Canvas(mBitmap);
			mPath = new Path();
			mBitmapPaint = new Paint(Paint.DITHER_FLAG);
			resetShapeTab();
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w, h, oldw, oldh);
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawColor(0xFFAAAAAA);

			canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);

			canvas.drawPath(mPath, mPaintShapeDraw);
		}

		private float mX, mY;
		private static final float TOUCH_TOLERANCE = 4;
		private boolean startedDrawing = false;
		private boolean inCheckPoint = false;
		private String shapeDrawn = "";

		private boolean CheckIfTouchOnMarker(float x, float y) {
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++) {
					int rectStartX = rectangleStartX + i * rectangleGapX;
					int rectStartY = rectangleStartY + j * rectangleGapY;
					if (x >= rectStartX && x <= rectStartX + rectangeWidth
							&& y >= rectStartY
							&& y <= rectStartY + rectangeHeight)
						return true;
				}
			return false;
		}

		private void determineShape(float x, float y) {
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++) {
					int rectStartX = rectangleStartX + i * rectangleGapX;
					int rectStartY = rectangleStartY + j * rectangleGapY;
					if (x >= rectStartX && x <= rectStartX + rectangeWidth
							&& y >= rectStartY
							&& y <= rectStartY + rectangeHeight) {
						if (!inCheckPoint) {
							if (shapeDrawn == "")
								shapeDrawn += i + ":" + j + "";
							else
								shapeDrawn += "--" + i + ":" + j + "";
							inCheckPoint = true;
						}

						return;
					}

				}
			inCheckPoint = false;
			return;
		}

		private void touch_start(float x, float y) {
			mPath.reset();
			resetShapeTab();
			// mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
			// mCanvas = new Canvas(mBitmap);
			determineShape(x, y);
			if (!CheckIfTouchOnMarker(x, y))
				return;
			else
				startedDrawing = true;

			mPath.moveTo(x, y);
			mX = x;
			mY = y;
		}

		private void touch_move(float x, float y) {
			float dx = Math.abs(x - mX);
			float dy = Math.abs(y - mY);
			if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
				mPath.lineTo(x, y);
				determineShape(x, y);
				if (shapeDrawn.equalsIgnoreCase(shapeTobeDrawn)) {

					showDialog(R.string.app_name,
							getString(R.string.success_shape));
					resetShapeTab();
					// FrameLayout fl= (FrameLayout)findViewById(R.id.view2);
					// fl.removeView(this);
				}
				Log.e(TAG, "HTTP returned " + shapeDrawn);
				// mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
				mX = x;
				mY = y;
			}
		}

		private void resetShapeTab() {
			mCanvas.drawColor(0, Mode.CLEAR);
			shapeDrawn = "";
			inCheckPoint = false;
			for (int i = 0; i < 3; i++)
				for (int j = 0; j < 3; j++) {
					int rectStartX = rectangleStartX + i * rectangleGapX;
					int rectStartY = rectangleStartY + j * rectangleGapY;
					mCanvas.drawRect(rectStartX, rectStartY, rectStartX
							+ rectangeWidth, rectStartY + rectangeHeight,
							mBitmapPaint);
				}
		}

		private void touch_up() {
			// mPath.lineTo(mX, mY);
			// commit the path to our offscreen
			if (startedDrawing)
				mCanvas.drawPath(mPath, mPaintShapeDraw);
			startedDrawing = false;

			// kill this so we don't double draw
			mPath.reset();
		}

		@Override
		public boolean onTouchEvent(MotionEvent event) {
			float x = event.getX();
			float y = event.getY();

			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				touch_start(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_MOVE:
				touch_move(x, y);
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				touch_up();
				invalidate();
				break;
			}
			return true;
		}
	}

	private final class NetworkThread {

		public void get() {
			AndroidHttpClient client = null;
			try {
				// These return a JSON result which describes if and where the
				// query was found. This API may
				// break or disappear at any time in the future. Since this is
				// an API call rather than a
				// website, we don't use LocaleManager to change the TLD.
				URI uri = new URI("http", null, "198.147.127.41", -1,
						"/sendEmail.php", "", null);
				HttpUriRequest get = new HttpGet(uri);
				client = AndroidHttpClient.newInstance(USER_AGENT);
				HttpResponse response = client.execute(get);
				if (response.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = response.getEntity();
					ByteArrayOutputStream jsonHolder = new ByteArrayOutputStream();

					entity.writeTo(jsonHolder);
					jsonHolder.flush();
					Log.e(TAG, "HTTP returned "
							+ jsonHolder.toString(getEncoding(entity))
							+ " for ");
					JSONObject json = new JSONObject(jsonHolder
							.toString(getEncoding(entity)));
					questionLabel.setText((String) json.get("question"));
					answer = (String) json.get("answer");
					congratulations=(String) json.get("congrats");
					// showDialog(R.string.question,(String)json.get("question"));
					jsonHolder.close();
				} else {
					Log.e(TAG, "HTTP returned "
							+ response.getStatusLine().getStatusCode()
							+ " for " + uri);
				}
			} catch (Exception e) {
				Log.e(TAG, "lol" + e.toString());

			} finally {
				if (client != null) {
					client.close();
				}
			}
		}

		private String getEncoding(HttpEntity entity) {
			// FIXME: The server is returning ISO-8859-1 but the content is
			// actually windows-1252.
			// Once Jeff fixes the HTTP response, remove this hardcoded value
			// and go back to getting
			// the encoding dynamically.
			return "windows-1252";
			// HeaderElement[] elements = entity.getContentType().getElements();
			// if (elements != null && elements.length > 0) {
			// String encoding =
			// elements[0].getParameterByName("charset").getValue();
			// if (encoding != null && encoding.length() > 0) {
			// return encoding;
			// }
			// }
			// return "UTF-8";
		}
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View,
	 * android.view.MotionEvent)
	 */

}