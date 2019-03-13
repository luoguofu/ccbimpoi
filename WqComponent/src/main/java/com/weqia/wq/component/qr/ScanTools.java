
package com.weqia.wq.component.qr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.weqia.utils.StrUtil;
import com.weqia.wq.R;
import com.weqia.wq.component.activity.SharedDetailTitleActivity;

import java.util.HashMap;
import java.util.Map;

public class ScanTools {

    public static void toPageQRResukt(SharedDetailTitleActivity ctx, String result) {
        ctx.setContentView(R.layout.ac_page_qrresult);
        ctx.sharedTitleView.initTopBanner("扫描结果");
        TextView tvResult = (TextView) ctx.findViewById(R.id.tvResult);
        if (StrUtil.notEmptyOrNull(result)) {
            tvResult.setText(result);
        }
    }

    public interface ScanCall {
        void getCode(String code);
    }

    /**
     * 扫描当前View上的二维码
     */
    public static void scanCode(View mView, ScanCall mScanCall) {
        Bitmap bitmap = Bitmap.createBitmap(mView.getWidth(), mView.getHeight(), Bitmap.Config.RGB_565);
        final Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.rgb(0xf1, 0xf1, 0xf1));
        mView.draw(canvas);
        if (bitmap != null) {
            //todo:调用扫描
            String code = scanBitmap(bitmap);
            mScanCall.getCode(code);
        }
    }

    /**
     * 解析二维码
     */
    public static String scanBitmap(Bitmap bitmap) {
        try {
            Map<DecodeHintType, String> hints = new HashMap<DecodeHintType, String>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
            MyRGBLuminanceSource rgbLuminanceSource = new MyRGBLuminanceSource(bitmap);
            //将图片转换成二进制图片
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
            //初始化解析对象
            QRCodeReader reader = new QRCodeReader();
            //开始解析
            Result result = null;
            try {
                result = reader.decode(binaryBitmap, hints);
            } catch (Exception e) {
                // TODO: handle exception
            }
            if (result != null) {
                return result.getText();
            }
        }catch (Exception e){

        }
        return "";
    }
}