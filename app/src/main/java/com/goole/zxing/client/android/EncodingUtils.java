package com.goole.zxing.client.android;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class EncodingUtils {
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xffffffff;

	public static Bitmap createQRCode(String str, int widthAndHeight) throws WriterException {
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		BitMatrix matrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				} else {
					pixels[y * width + x] = WHITE;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	public static Bitmap generateBitmap(String content,int width, int height) {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();  
    Map<EncodeHintType, String> hints = new HashMap<>();
    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");  
    try {  
        BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);  
        int[] pixels = new int[width * height];  
        for (int i = 0; i < height; i++) {  
            for (int j = 0; j < width; j++) {  
                if (encode.get(j, i)) {  
                    pixels[i * width + j] = 0x00000000;  
                } else {  
                    pixels[i * width + j] = 0xffffffff;  
                }  
            }  
        }  
        return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);  
    } catch (WriterException e) {  
        e.printStackTrace();  
    }  
    return null;  
	}  
}
