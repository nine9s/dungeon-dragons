package intel.code.expo;
import java.io.ByteArrayOutputStream;
import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.util.List;

import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TextView;
public final class QRCode extends TabActivity {
	  private static final String TAG = "SearchBookContents";
	  private static final String USER_AGENT = "ZXing/1.3 (Android)";
	  int chkpt=0;
	  boolean barrier_1=false;
	  boolean barrier_2=false;
	  String answer="";
	  EditText answerText;
	  TextView questionLabel;
  @Override
  public void onCreate(Bundle icicle) 
  {
    super.onCreate(icicle);
    TabHost tabHost = getTabHost();
    
    LayoutInflater.from(this).inflate(R.layout.tabs, tabHost.getTabContentView(), true);

    tabHost.addTab(tabHost.newTabSpec("tab1")
            .setIndicator("tab1")
            .setContent(R.id.view1));
    tabHost.addTab(tabHost.newTabSpec("ta3")
            .setIndicator("tab2")
            .setContent(R.id.view2));
    tabHost.addTab(tabHost.newTabSpec("tab3")
            .setIndicator("tab3")
            .setContent(R.id.view3));
        View scan_qr_code = findViewById(R.id.scan_qr_code);
        scan_qr_code.setOnClickListener(mScanQRCode);
        View get_question = findViewById(R.id.preview_view);
        get_question.setOnClickListener(mGetQuestion);
        View check_answer = findViewById(R.id.checkAnswer);
        check_answer.setOnClickListener(mcheckAnswer);
        answerText= (EditText)findViewById(R.id.answerEdit);
        questionLabel= (TextView)findViewById(R.id.questionLabel);
      }
  public final Button.OnClickListener mScanQRCode = new Button.OnClickListener() {
    public void onClick(View v) {
      Intent intent = new Intent("com.google.zxing.client.android.SCAN");
      intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
      startActivityForResult(intent, 0);
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
  private  final class NetworkThread {
	  
	    public void get() {
	      AndroidHttpClient client = null;
	      try {
	        // These return a JSON result which describes if and where the query was found. This API may
	        // break or disappear at any time in the future. Since this is an API call rather than a
	        // website, we don't use LocaleManager to change the TLD.
	        URI uri = new URI("http", null, "207.151.247.51", -1, "/sendEmail.php", "vid=isbn" , null);
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
}