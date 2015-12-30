/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package com.example.pc700demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import cn.com.choicesoft.activity.ChoiceApplication;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

public abstract class SerialPortActivity extends Activity {

	protected ChoiceApplication mApplication;
	protected SerialPort mSerialPort;
	protected OutputStream mOutputStream;
	private InputStream mInputStream;
	private ReadThread mReadThread;

	private class ReadThread extends Thread {

		@Override
		public void run() {
			super.run();
			while (!isInterrupted()) {
				int size;
				try {
					byte[] buffer = new byte[64];
					if (mInputStream == null)
						return;
					size = mInputStream.read(buffer);
					if (size > 0) {
						onDataReceived(buffer, size);
					}
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			}
		}
	}

	private void DisplayError(int resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//SerialPortActivity.this.finish();
				dialog.cancel();
			}
		});
		b.show();
	}

	private void DisplayError(String resourceId) {
		AlertDialog.Builder b = new AlertDialog.Builder(this);
		b.setTitle("Error");
		b.setMessage(resourceId);
		b.setPositiveButton("OK", new OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				//SerialPortActivity.this.finish();
				dialog.cancel();
			}
		});
		b.show();
	}

	protected Boolean OpenSerialPort() {
	
		try {
			mApplication = (ChoiceApplication) getApplication();
			mSerialPort = mApplication.getSerialPort();
			mOutputStream = mSerialPort.getOutputStream();
			mInputStream = mSerialPort.getInputStream();

			/* Create a receiving thread */
			mReadThread = new ReadThread();
			mReadThread.start();
		} catch (SecurityException e) {
			return false;
		} catch (IOException e) {
			return false;
		} catch (InvalidParameterException e) {
			return false;
		} catch (Exception e) {
			DisplayError(e.getMessage());
			return false;
		}
		return true;
	}

	protected Boolean CloseSerialPort() {
		try{
		if (mReadThread != null)
			mReadThread.interrupt();
		mApplication.closeSerialPort();
		mSerialPort = null;
		}catch(Exception e)
		{
			DisplayError(e.getMessage());
			return false;
		}
		return true;
	}

	protected abstract void onDataReceived(final byte[] buffer, final int size);

}
