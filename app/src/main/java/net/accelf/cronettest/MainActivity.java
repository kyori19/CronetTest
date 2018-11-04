package net.accelf.cronettest;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import org.chromium.net.CronetEngine;
import org.chromium.net.UrlRequest;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    CronetEngine engine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CronetEngine.Builder engineBuilder = new CronetEngine.Builder(this);
        engine = engineBuilder.build();

        findViewById(R.id.button).setOnClickListener(v -> executeRequest(((EditText) findViewById(R.id.editText)).getText().toString()));
    }

    private void executeRequest(String url) {
        Executor executor = Executors.newSingleThreadExecutor();
        CronetCallback callback = new CronetCallback(this);
        UrlRequest.Builder requestBuilder = engine.newUrlRequestBuilder(url, callback, executor);
        UrlRequest request = requestBuilder.build();
        request.start();
    }
}
