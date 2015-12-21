package com.example.imac.javascript_inject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends Activity {
    WebView webview1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview1 = (WebView) findViewById(R.id.myWebview);

        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview1.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });
        webview1.addJavascriptInterface(new MyJavaScriptInterface(), "MYOBJECT");
        webview1.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                StringBuilder sb = new StringBuilder();

// You can also inject the JS code through WebView, when release the codes below。
//                sb.append("function test(){");
//                sb.append("var objAccount;var str = '';");
//                sb.append("var inputs = document.getElementsByTagName('input');");
//                sb.append("for (var i = 0; i < inputs.length; i++) {");
//                sb.append("if (inputs[i].name.toLowerCase() === 'account') {objAccount = inputs[i];}");
//                sb.append("}");
//                sb.append("if (objAccount != null) {str += objAccount.value;}");
//                sb.append("window.MYOBJECT.processHTML(str);");
//                sb.append("return true;");
//                sb.append("};");

                view.loadUrl("javascript:" + sb.toString());
            }

        });

        String sUrl = "file:///android_asset/test.html";
        webview1.loadUrl(sUrl);


    }

    class MyJavaScriptInterface {
        @JavascriptInterface
        public void processHTML(String html) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("AlertDialog from app")
                    .setMessage(html)
                    .setPositiveButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub

                                }
                            })
                    .setCancelable(false).show();

        }
    }
}