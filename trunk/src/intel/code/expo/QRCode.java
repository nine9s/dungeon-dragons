package intel.code.expo;
import android.view.MotionEvent;

import java.io.ByteArrayOutputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;
public final class QRCode extends TabActivity{
	  private static final String TAG = "SearchBookContents";
	  private static final String USER_AGENT = "ZXing/1.3 (Android)";
	  int chkpt=0;
	  private Paint       mPaint;
	  boolean barrier_1=false;
	  boolean barrier_2=false;
	 
	Vector<Integer > rectangleLeftXCoordinates= new Vector<Integer>();
	Vector<Integer > rectangleLeftYCoordinates= new Vector<Integer>();
	 int rectangleStartX=50;
	 int rectangleStartY=50;
	 int rectangleGapX=95;
	 int rectangleGapY=120;
	 int rectangeWidth=50;
	 int rectangeHeight=50;
	  String answer="";
	  EditText answerText;
	  TextView questionLabel;
  @Override
  public void onCreate(Bundle icicle) 
  {
    super.onCreate(icicle);
   TabHost tabHost = getTabHost();
   mPaint = new Paint();
   mPaint.setAntiAlias(true);
   mPaint.setDither(true);
   mPaint.setColor(0xFFFF0000);
   mPaint.setStyle(Paint.Style.STROKE);
   mPaint.setStrokeJoin(Paint.Join.ROUND);
   mPaint.setStrokeCap(Paint.Cap.ROUND);
   mPaint.setStrokeWidth(12);
    LayoutInflater.from(this).inflate(R.layout.tabs, tabHost.getTabContentView(), true);
    
    tabHost.addTab(tabHost.newTabSpec("tab1")
            .setIndicator("tab1")
            .setContent(R.id.view1));
    tabHost.addTab(tabHost.newTabSpec("tab3")
            .setIndicator("tab2")
            .setContent(R.id.view2));
    tabHost.addTab(tabHost.newTabSpec("tab3")
            .setIndicator("tab3")
            .setContent(R.id.view3));
    	
    	//View drawShape=findViewById(R.id.view2);
    	//drawShape.setOnTouchListener(mTouchListener);
    	FrameLayout fl= (FrameLayout)findViewById(R.id.view2);
    	fl.addView(new MyView(this));
    	
        View scan_qr_code = findViewById(R.id.scan_qr_code);
        scan_qr_code.setOnClickListener(mScanQRCode);
        View get_question = findViewById(R.id.preview_view);
        get_question.setOnClickListener(mGetQuestion);
        View check_answer = findViewById(R.id.checkAnswer);
        check_answer.setOnClickListener(mcheckAnswer);
        answerText= (EditText)findViewById(R.id.answerEdit);
        questionLabel= (TextView)findViewById(R.id.questionLabel);
 
  /*  setContentView(R.layout.touch_test);
    View topLayout = this.findViewById(R.id.layout_id);
    // register for events for the view, previously
    topLayout.setOnTouchListener((OnTouchListener) mTouchListener);
*/
        
      }

  
  
  public final Button.OnClickListener mScanQRCode = new Button.OnClickListener() {
    public void onClick(View v) {
      Intent intent = new Intent("com.google.zxing.client.android.SCAN");
      intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
      startActivityForResult(intent, 0);
    }
  };
  
  public final Button.OnClickListener circleShape = new Button.OnClickListener() {
	    public void onClick(View v) {
	    	 Log.e(TAG, "HTTP returned " +"circle");
	    }
	  };
  public final Button.OnClickListener mGetQuestion = new Button.OnClickListener() {
	    public void onClick(View v) {
	      NetworkThread mNetWoNetworkThread=new NetworkThread();
	      mNetWoNetworkThread.get();
	    }
	  };
	  
	  public final Button.OnClickListener mcheckAnswer = new Button.OnClickListener() {
		    public void onClick(View v) {
		      if(answerText.getText().toString().equalsIgnoreCase(answer))
		    	  showDialog(R.string.question,"Correct");
		      else
		    	  showDialog(R.string.question,"Wrong");
		      Log.e(TAG, "HTTP returned " + answerText.getText().toString()+":"+answer);   
		    }
		  };
   @Override
  public void onActivityResult(int requestCode, int resultCode, Intent intent) {
    if (requestCode == 0) {
      if (resultCode == RESULT_OK) {
        String contents = intent.getStringExtra("SCAN_RESULT");
        String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
        showDialog(R.string.result_succeeded, "Format: " + format + "\nContents: " + contents);
      } else if (resultCode == RESULT_CANCELED) {
        showDialog(R.string.result_failed, getString(R.string.result_failed_why));
      }
    }
  }

  
  private void showDialog(int title, String message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(title);
    builder.setMessage(message);
    builder.setPositiveButton("OK", null);
    builder.show();
  }
  
  public class MyView extends View {
      
      private static final float MINP = 0.25f;
      private static final float MAXP = 0.75f;
      
      private Bitmap  mBitmap;
      private Canvas  mCanvas;
      private Path    mPath;
      private Paint   mBitmapPaint;
      
      public MyView(Context c) {
          super(c);
          //setBackgroundColor(R.drawable.shape);
          mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
          mCanvas = new Canvas(mBitmap);
          mPath = new Path();
          mBitmapPaint = new Paint(Paint.DITHER_FLAG);
          for(int i=0;i<3;i++)
        	  for(int j=0;j<3;j++)
        	  {
        		  int rectStartX=rectangleStartX+i*rectangleGapX;
        		  int rectStartY=rectangleStartY+j*rectangleGapY;
        		  mCanvas.drawRect(rectStartX,rectStartY,rectStartX+rectangeWidth,rectStartY+rectangeHeight,mBitmapPaint);
        	  }
      }

      @Override
      protected void onSizeChanged(int w, int h, int oldw, int oldh) {
          super.onSizeChanged(w, h, oldw, oldh);
      }
      
      @Override
      protected void onDraw(Canvas canvas) {
          canvas.drawColor(0xFFAAAAAA);
          
          canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
          
          canvas.drawPath(mPath, mPaint);
      }
      
      private float mX, mY;
      private static final float TOUCH_TOLERANCE = 4;
      private boolean startedDrawing=false;
      private boolean inCheckPoint=false;
      private String shapeDrawn="";
      private boolean CheckIfTouchOnMarker(float x, float y){
    	  for(int i=0;i<3;i++)
        	  for(int j=0;j<3;j++)
        	  {
        		  int rectStartX=rectangleStartX+i*rectangleGapX;
        		  int rectStartY=rectangleStartY+j*rectangleGapY;
        		  if(x>=rectStartX&&x<=rectStartX+rectangeWidth&&y>=rectStartY&&y<=rectStartY+rectangeHeight)
        		  return true;
        	  }
      return false;
      }
      
      private void determineShape(float x, float y){
    	  for(int i=0;i<3;i++)
        	  for(int j=0;j<3;j++)
        	  {
        		  int rectStartX=rectangleStartX+i*rectangleGapX;
        		  int rectStartY=rectangleStartY+j*rectangleGapY;
        		  if(x>=rectStartX&&x<=rectStartX+rectangeWidth&&y>=rectStartY&&y<=rectStartY+rectangeHeight)
        		  {
        			  if(!inCheckPoint)
        			  {
        				  if(shapeDrawn=="")
        					  shapeDrawn+=i+":"+j+"";
        				  else
        			  shapeDrawn+="--"+i+":"+j+"";
        			  inCheckPoint=true;
        			  }
        			  
        				  return;
        		  }
        		  
        	  }
    	  inCheckPoint=false;
      return;
      }
      
      private void touch_start(float x, float y) {
          mPath.reset();
          mCanvas.drawColor(0,Mode.CLEAR);
          shapeDrawn="";
          for(int i=0;i<3;i++)
        	  for(int j=0;j<3;j++)
        	  {
        		  int rectStartX=rectangleStartX+i*rectangleGapX;
        		  int rectStartY=rectangleStartY+j*rectangleGapY;
        		  mCanvas.drawRect(rectStartX,rectStartY,rectStartX+rectangeWidth,rectStartY+rectangeHeight,mBitmapPaint);
        	  }
      
          //mBitmap = Bitmap.createBitmap(320, 480, Bitmap.Config.ARGB_8888);
          //mCanvas = new Canvas(mBitmap);
         if(! CheckIfTouchOnMarker(x,y))
        	 return;
         else
        	 startedDrawing=true;
          mPath.moveTo(x, y);
          mX = x;
          mY = y;
      }
      private void touch_move(float x, float y) {
          float dx = Math.abs(x - mX);
          float dy = Math.abs(y - mY);
          if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
        	  mPath.lineTo(x, y);
        	  determineShape(x,y);
        	  Log.e(TAG, "HTTP returned " + shapeDrawn);
        	  //mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
              mX = x;
              mY = y;
          }
      }
      private void touch_up() {
         // mPath.lineTo(mX, mY);
          // commit the path to our offscreen
    	  if(startedDrawing)
          mCanvas.drawPath(mPath, mPaint);
    	  startedDrawing=false;
    	  
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
  
  private  final class NetworkThread {
	  
	    public void get() {
	      AndroidHttpClient client = null;
	      try {
	        // These return a JSON result which describes if and where the query was found. This API may
	        // break or disappear at any time in the future. Since this is an API call rather than a
	        // website, we don't use LocaleManager to change the TLD.
	        URI uri = new URI("http", null, "207.151.247.51", -1, "/sendEmail.php", "" , null);
	        HttpUriRequest get = new HttpGet(uri);
	        client = AndroidHttpClient.newInstance(USER_AGENT);
	        HttpResponse response = client.execute(get);
	        if (response.getStatusLine().getStatusCode() == 200) {
	          HttpEntity entity = response.getEntity();
	          ByteArrayOutputStream jsonHolder = new ByteArrayOutputStream();
	          
	          entity.writeTo(jsonHolder);
	          jsonHolder.flush();
	          Log.e(TAG, "HTTP returned " + jsonHolder.toString(getEncoding(entity)) + " for " );
	          JSONObject json = new JSONObject(jsonHolder.toString(getEncoding(entity)));
	          questionLabel.setText((String)json.get("question"));
	          answer=(String)json.get("answer");
	          //showDialog(R.string.question,(String)json.get("question"));
	          jsonHolder.close();
	        } else 
	        {
	          Log.e(TAG, "HTTP returned " + response.getStatusLine().getStatusCode() + " for " + uri);
	        }
	      } catch (Exception e) {
	        Log.e(TAG, "lol"+e.toString());
	      
	      } finally {
	        if (client != null) {
	          client.close();
	        }
	      }
	    } 
	    private String getEncoding(HttpEntity entity) {
	        // FIXME: The server is returning ISO-8859-1 but the content is actually windows-1252.
	        // Once Jeff fixes the HTTP response, remove this hardcoded value and go back to getting
	        // the encoding dynamically.
	        return "windows-1252";
//	              HeaderElement[] elements = entity.getContentType().getElements();
//	              if (elements != null && elements.length > 0) {
//	                  String encoding = elements[0].getParameterByName("charset").getValue();
//	                  if (encoding != null && encoding.length() > 0) {
//	                      return encoding;
//	                  }
//	              }
//	              return "UTF-8";
	      }
  }
/* (non-Javadoc)
 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
 */

}