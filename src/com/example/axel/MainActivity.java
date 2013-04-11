package com.example.axel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static ExecutorService execs;
	public static Button SPButton;
	public static TextView PercentText;
	public static TextView RemainText;
	public static TextView SpeedText;
	public static TextView LinkText;

	static {
		System.loadLibrary("axel");
	}

	private native int nativeDownload(String[] argv);

	private native void nativeCancel(String url);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		execs = Executors.newSingleThreadExecutor();

		SPButton = (Button) findViewById(R.id.button1);
		LinkText = (TextView) findViewById(R.id.textView9);
		PercentText = (TextView) findViewById(R.id.textView3);
		SpeedText = (TextView) findViewById(R.id.textView5);
		RemainText = (TextView) findViewById(R.id.textView7);

		SPButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				nativeCancel("url");
				execs.shutdown();
			}
		});

		execs.execute(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				String[] options = new String[4];

				options[0] = "--num-connections=8";
				options[1] = "--output="
						+ Environment.getExternalStorageDirectory().getPath()
						+ "/Daarkoob";
				options[2] = "http://www.kernel.org/pub/linux/kernel/v2.6/linux-2.6.32.9.tar.gz";
				options[3] = "--alternate";

				nativeDownload(options);
			}
		});

		try {
			execs.awaitTermination(1000, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	public void messageMe(final String text) {
		// System.err.println(text);
		if (text.startsWith("init"))
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					LinkText.setText(text.substring(5));
				}
			});
		else if (text.startsWith("speed"))
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					SpeedText.setText(text.substring(6));
				}
			});
		else if (text.startsWith("remain"))
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					RemainText.setText(text.substring(7));
				}
			});
		else if (text.startsWith("percent"))
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					PercentText.setText(text.substring(8));
				}
			});
	}

	@Override
	public void onBackPressed() {
		execs.shutdown();
		super.onBackPressed();
	}
}
