package net.accelf.cronettest;

import android.util.Log;
import android.widget.TextView;

import org.chromium.net.CronetException;
import org.chromium.net.UrlRequest;
import org.chromium.net.UrlResponseInfo;

import java.nio.ByteBuffer;
import java.util.Locale;

public class CronetCallback extends UrlRequest.Callback {

    private MainActivity activity;
    private ByteBuffer buffer;

    CronetCallback(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onRedirectReceived(UrlRequest request, UrlResponseInfo info, String newLocationUrl) {
        log("onRedirectReceived : " + newLocationUrl);
        request.followRedirect();
    }

    @Override
    public void onResponseStarted(UrlRequest request, UrlResponseInfo info) {
        int httpStatusCode = info.getHttpStatusCode();
        log("onResponseStarted : " + httpStatusCode);
        buffer = ByteBuffer.allocateDirect(4096);
        request.read(buffer);
    }

    @Override
    public void onReadCompleted(UrlRequest request, UrlResponseInfo info, ByteBuffer byteBuffer) {
        log("onReadCompleted");
        byteBuffer.position(0);
        byte[] bytes = byteBuffer.array();
        log(new String(bytes));
        buffer = ByteBuffer.allocateDirect(4096);
        request.read(buffer);
    }

    @Override
    public void onSucceeded(UrlRequest request, UrlResponseInfo info) {
        log("onSucceeded");
    }

    @Override
    public void onFailed(UrlRequest request, UrlResponseInfo info, CronetException error) {
        log("onFailed : " + error.getMessage());
    }

    private void log(String text) {
        Log.d("CronetCallback", text);
        activity.runOnUiThread(() -> {
            TextView textView = activity.findViewById(R.id.textView);
            textView.setText(String.format(Locale.getDefault(), "%s\n%s", textView.getText(), text));
        });
    }
}
