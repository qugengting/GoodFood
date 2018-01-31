/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.common.library.zxing.android;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.ResultPointCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * This thread does all the heavy lifting of decoding the images.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public class DecodeThread extends Thread {

	public static final String BARCODE_BITMAP = "barcode_bitmap";
	public static final String BARCODE_SCALED_FACTOR = "barcode_scaled_factor";

	private CaptureActivity activity;
	private Map<DecodeHintType, Object> hints;
	private Handler handler;
	private  final CountDownLatch handlerInitLatch;

	DecodeThread(CaptureActivity activity,
				 HashSet<BarcodeFormat> decodeFormats,
				 HashMap<DecodeHintType, ?> baseHints, String characterSet,
				 ResultPointCallback resultPointCallback) {

		this.activity = activity;
		handlerInitLatch = new CountDownLatch(1);

		hints = new HashMap<DecodeHintType, Object>();
		if (baseHints != null) {
			hints.putAll(baseHints);
		}

		// The prefs can't change while the thread is running, so pick them up
		// once here.
		if (decodeFormats == null || decodeFormats.isEmpty()) {
			SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(activity);
			decodeFormats = new HashSet<BarcodeFormat>();
			decodeFormats.add(BarcodeFormat.CODABAR);
			decodeFormats.add(BarcodeFormat.CODE_39);
			decodeFormats.add(BarcodeFormat.CODE_93);
			decodeFormats.add(BarcodeFormat.CODE_128);
			decodeFormats.add(BarcodeFormat.DATA_MATRIX);
			decodeFormats.add(BarcodeFormat.EAN_8);
			decodeFormats.add(BarcodeFormat.EAN_13);
			decodeFormats.add(BarcodeFormat.ITF);
			decodeFormats.add(BarcodeFormat.QR_CODE);
			decodeFormats.add(BarcodeFormat.RSS_EXPANDED);
			decodeFormats.add(BarcodeFormat.UPC_A);
			decodeFormats.add(BarcodeFormat.UPC_E);
			decodeFormats.add(BarcodeFormat.UPC_EAN_EXTENSION);
			/*if (prefs.getBoolean(PreferencesConstants.KEY_DECODE_1D_PRODUCT,
					true)) {
				decodeFormats.addAll(DecodeFormatManager.PRODUCT_FORMATS);
			}
			if (prefs.getBoolean(PreferencesConstants.KEY_DECODE_1D_INDUSTRIAL,
					true)) {
				decodeFormats.addAll(DecodeFormatManager.INDUSTRIAL_FORMATS);
			}
			if (prefs.getBoolean(PreferencesConstants.KEY_DECODE_QR, true)) {
				decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			}
			if (prefs.getBoolean(PreferencesConstants.KEY_DECODE_DATA_MATRIX,
					true)) {
				decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
			}
			if (prefs.getBoolean(PreferencesConstants.KEY_DECODE_AZTEC, false)) {
				decodeFormats.addAll(DecodeFormatManager.AZTEC_FORMATS);
			}
			if (prefs.getBoolean(PreferencesConstants.KEY_DECODE_PDF417, false)) {
				decodeFormats.addAll(DecodeFormatManager.PDF417_FORMATS);
			}*/
		}
		hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

		if (characterSet != null) {
			hints.put(DecodeHintType.CHARACTER_SET, characterSet);
		}
		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK,
				resultPointCallback);
		Log.i("DecodeThread", "Hints: " + hints);
	}

	Handler getHandler() {
		try {
			handlerInitLatch.await();
		} catch (InterruptedException ie) {
			// continue?
		}
		return handler;
	}

	@Override
	public void run() {
		Looper.prepare();
		handler = new DecodeHandler(activity, hints);
		handlerInitLatch.countDown();
		Looper.loop();
	}

}
