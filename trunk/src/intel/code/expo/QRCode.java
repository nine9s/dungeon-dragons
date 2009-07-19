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
import android.widget.Toast;

public final class QRCode extends Activity {
	  private static final String TAG = "SearchBookContents";
	  private static final String USER_AGENT = "ZXing/1.3 (Android)";
  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    setContentView(R.layout.test);
        View scan_qr_code = findViewById(R.id.scan_qr_code);
        scan_qr_code.setOnClickListener(mScanQRCode);
        View get_question = findViewById(R.id.preview_view);
        get_question.setOnClickListener(mGetQuestion);
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
	      mNetWoNetworkThread.start();
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
  private  final class NetworkThread extends Thread {
	  @Override
	    public void run() {
	      AndroidHttpClient client = null;
	      try {
	        // These return a JSON result which describes if and where the query was found. This API may
	        // break or disappear at any time in the future. Since this is an API call rather than a
	        // website, we don't use LocaleManager to change the TLD.
	        URI uri = new URI("http", null, "68.181.237.10", -1, "/sendEmail.php", "vid=isbn" , null);
	        HttpUriRequest get = new HttpGet(uri);
	        client = AndroidHttpClient.newInstance(USER_AGENT);
	        HttpResponse response = client.execute(get);
	        if (response.getStatusLine().getStatusCode() == 200) {
	          HttpEntity entity = response.getEntity();
	          ByteArrayOutputStream jsonHolder = new ByteArrayOutputStream();
	          entity.writeTo(jsonHolder);
	          jsonHolder.flush();
	          JSONObject json = new JSONObject(jsonHolder.toString(getEncoding(entity)));
	          showDialog(R.string.result_succeeded,(String)json.get("resumeID"));
	          jsonHolder.close();

	        
	        } else {
	          Log.e(TAG, "HTTP returned " + response.getStatusLine().getStatusCode() + " for " + uri);
	        
	        }
	      } catch (Exception e) {
	        Log.e(TAG, e.toString());
	      
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