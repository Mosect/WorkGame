package com.mosect.workgame.graphics;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

/**
 * 正三角形
 */
public class RegularTriangle extends Graphics {

    private static Path srcPath;

    private static Path getSrcPath() {
        if (null == srcPath) {
            synchronized (RegularTriangle.class) {
                if (null == srcPath) {
                    float v = (float) (Math.sqrt(3) / 2);
                    float y = (1 - v) / 2;
                    srcPath = new Path();
                    srcPath.moveTo(0.5f, y);
                    srcPath.lineTo(0, y + v);
                    srcPath.lineTo(1, y + v);
                    srcPath.close();
                }
            }
        }
        return srcPath;
    }

    private Path path;
    private Matrix matrix;
    private float angle;
    private Paint paint;

    public RegularTriangle() {
        path = new Path();
        matrix = new Matrix();
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void update() {
        path.reset();
        matrix.reset();
        matrix.postRotate(angle, 0.5f, 0.5f);
        matrix.postScale(getWidth(), getHeight());
        Path srcPath = getSrcPath();
        srcPath.transform(matrix, path);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(path, paint);
    }
}
