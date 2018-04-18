package com.mobike.iotcloud.backend.framework.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * 二维码生成工具
 */
public class QRCodeUtil {

    public static boolean generator(String code, OutputStream stream,
                                    int width, int height) {
        return QRCodeUtil.generator(code, stream, width, height,
                MatrixToImageConfig.WHITE, 0x00000000, ErrorCorrectionLevel.L);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean generator(String code, OutputStream stream,
                                    int width, int height, int bgColor, int codeColor,
                                    ErrorCorrectionLevel level) {
        try {
            String format = "jpg";
            Map hints = new HashMap();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hints.put(EncodeHintType.MARGIN, 0);
            hints.put(EncodeHintType.ERROR_CORRECTION, level);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(code,
                    BarcodeFormat.QR_CODE, width, height, hints);

            // 未支付状态
            MatrixToImageConfig config = new MatrixToImageConfig(codeColor,
                    bgColor);

            MatrixToImageWriter
                    .writeToStream(bitMatrix, format, stream, config);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static boolean generator2Local(String code, File file, int width,
                                          int height) {
        try {
            String format = "png";
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//			hints.put(EncodeHintType.MARGIN, value)
            BitMatrix bitMatrix = new MultiFormatWriter().encode(code,
                    BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToFile(bitMatrix, format, file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean generator(String code, String outFile, int width,
                                    int height, int bgColor, int codeColor, ErrorCorrectionLevel level) {
        FileOutputStream fo = null;

        try {
            fo = new FileOutputStream(outFile);
            return generator(code, fo, width, height, bgColor, codeColor, level);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(fo);
        }
        return false;
    }
}
