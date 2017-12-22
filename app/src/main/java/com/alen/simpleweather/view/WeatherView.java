package com.alen.simpleweather.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.alen.simpleweather.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alen on 2017/11/23.
 */

public class WeatherView<T extends IBaseWeatherData> extends View {
    protected static final String TAG = "WeatherView";
    protected Context context;
    //分别是 圆点 文字 线 的画笔 中间线的画笔
    protected Paint dotPaint, txtPaint, linePaint, middleLinePaint;
    // 默认的高度 dp
    protected static final int DEFAULT_TMINHEIGHT = 100;


    // 默认的宽度 dp
    protected static final int DEFAULT_MINWIDTH = 60;
    // 圆点画笔 配 置
    protected static final int[] DOTCOLOR = {Color.parseColor("#24c3f1"), Color.parseColor("#24c3f1")};

    protected static final int WEADOTRADIU = 2;
    // 默认画布的背景色
    protected static final int CANVASCOLOR = Color.parseColor("#ffffff");
    // 高温线画笔颜色
    protected static final int HIGHLINECOLOR = Color.parseColor("#24c3f1");
    // 低温线画笔颜色
    protected static final int LOWLINECOLOR = Color.parseColor("#24c3f1");
    // 温度线的默认宽度
    protected static final int LINEWIDTH = 2;
    // 文字默认大小
    protected static final int TXTSIZE = 12;
    //文字距离顶部 底部的距离
    protected static final int TXTTOBORDER = 8;
    // 文字与圆点之间的距离
    protected static final int TXTTODOT = 5;
    // 高低温字体颜色数组
    protected static final int[] TXTCOLOR = {Color.BLACK, Color.BLACK};
    // 中间线的颜色
    protected static final int MIDDLE_LINE_COLOR = 0x33ff0000;

    protected static final int MIDDLE_LINE_STROKE_WIDTH = 1;

    private DisplayMetrics dm;

    protected Paint.FontMetrics fontMetrics;

    protected int highestDegree = 1;

    protected int lowestDegree = -1;

    protected int txtSize = -1;

    protected int alltxtColor = -1;

    protected int[] txtColor = TXTCOLOR;

    private int todayHighTxtColor = TXTCOLOR[0];

    private int todayLowTxtColor = TXTCOLOR[1];

    protected int txtToDot = -1;

    protected int txtToBorder = -1;

    protected int lineStrokeWidth = LINEWIDTH;

    protected int lineColor = -1;

    protected int highLineColor = HIGHLINECOLOR;

    protected int lowLineColor = LOWLINECOLOR;

    protected int[] leftLineColor = {-1, -1};

    protected int[] rightLineColor = {-1, -1};

    protected int allDotColor = -1;

    protected int[] dotColor = DOTCOLOR;

    protected int todayHighDotColor = -1;

    protected int todayLowDotColor = -1;

    protected int dotRadiu = -1;

    protected int todayDotRadiu = -1;

    protected int backgroundColor = CANVASCOLOR;

    protected int position;

    protected int height = -1;
    protected int width = -1;

    protected boolean haveMiddleLine;

    protected int middleLineColor = MIDDLE_LINE_COLOR;

    protected int middleLineStrokeWidth = MIDDLE_LINE_STROKE_WIDTH;

    protected boolean isToday;

    protected List<T> datas = new ArrayList();

    /**
     * 设置数据
     * issue: 1.必须设置最低和最高的温度值，以及要展示的数据的position
     * 2.如果你需要在展示数据的时候，需要设置其它属性，那么这个方法必须在最后才能调用，否则控件不会重绘
     * 3.如果，确实需要动态改变属性，可以继承WeatherView，然后重写属性方法, 在调用父类方法之后，在调用  postInvalidate() 或者 invalidate()
     *
     * @param datas
     * @param highestDegree
     * @param lowestDegree
     * @param position
     */
    public void setDatas(List<T> datas, int highestDegree, int lowestDegree, int position) {
        this.datas = datas;
        this.highestDegree = highestDegree;
        this.lowestDegree = lowestDegree;
        this.position = position;
        postInvalidate();
    }

    protected int dip2px(float dip) {
        return (int) (dip * dm.density + 0.5);
    }

    protected int sp2px(float spValue) {
        return (int) (spValue * dm.scaledDensity + 0.5f);
    }

    public WeatherView(Context context) {
        super(context);
        init(context, null);
    }

    public WeatherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.context = context;
        if (attrs != null)
            initAttrs(attrs);//初始化XML属性
        dm = getResources().getDisplayMetrics();
        setUpAttr();//根据属性的优先级，设置冲突的属性
        initPaints();//初始化所要用的三支画笔
    }

    /**
     * 根据属性的优先级，设置冲突的属性
     */
    protected void setUpAttr() {
        if (txtSize == -1)
            txtSize = sp2px(TXTSIZE);
        if (dotRadiu == -1)
            dotRadiu = dip2px(WEADOTRADIU);
        if (todayDotRadiu == -1)
            todayDotRadiu = dotRadiu;
        if (lineStrokeWidth == -1)
            lineStrokeWidth = dip2px(LINEWIDTH);
        if (txtToDot == -1)
            txtToDot = dip2px(TXTTODOT);
        if (txtToBorder == -1)
            txtToBorder = dip2px(TXTTOBORDER);
        if (width == -1)
            width = dip2px(DEFAULT_MINWIDTH);
        if (height == -1)
            height = dip2px(DEFAULT_TMINHEIGHT);
        if (allDotColor != -1)
            dotColor[0] = dotColor[1] = allDotColor = DOTCOLOR[0];
        if (todayHighDotColor == -1 || todayLowDotColor == -1)
            todayHighDotColor = todayLowDotColor = DOTCOLOR[0];
        if (alltxtColor != -1)
            txtColor[0] = txtColor[1] = alltxtColor = TXTCOLOR[0];
        if (lineColor != -1)
            highLineColor = lowLineColor = lineColor;
        if (leftLineColor[0] == -1 || rightLineColor[0] == -1)
            leftLineColor[0] = rightLineColor[0] = highLineColor;
        if (leftLineColor[1] == -1 || rightLineColor[1] == -1)
            leftLineColor[1] = rightLineColor[1] = lowLineColor;
        if (isToday) {
            dotColor[0] = todayHighDotColor;
            dotColor[1] = todayLowDotColor;
            txtColor[0] = todayHighTxtColor;
            txtColor[1] = todayLowTxtColor;
            dotRadiu = todayDotRadiu;
        }
    }
    /**
     * 初始化XML属性
     *
     * @param attrs
     */
    protected void initAttrs(AttributeSet attrs) {
        TypedArray types = context.obtainStyledAttributes(attrs, R.styleable.ForecastView);
        int count = types.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = types.getIndex(i);
            if (attr == R.styleable.ForecastView_txtSize)
                txtSize = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_dotRadiu)
                dotRadiu = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_todayDotRadiu)
                todayDotRadiu = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_dotColor)
                allDotColor = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_highDotColor)
                dotColor[0] = types.getColor(attr, DOTCOLOR[0]);
            else if (attr == R.styleable.ForecastView_lowDotColor)
                dotColor[1] = types.getColor(attr, DOTCOLOR[1]);
            else if (attr == R.styleable.ForecastView_todayHighDotColor)
                todayHighDotColor = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_todayLowDotColor)
                todayLowDotColor = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_lineStrokWidth)
                lineStrokeWidth = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_txtToBorder)
                txtToBorder = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_txtToDot)
                txtToDot = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_txtColor)
                alltxtColor = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_highTxtColor)
                txtColor[0] = types.getColor(attr, TXTCOLOR[0]);
            else if (attr == R.styleable.ForecastView_lowTxtColor)
                txtColor[1] = types.getColor(attr, TXTCOLOR[1]);
            else if (attr == R.styleable.ForecastView_todayHighTxtColor)
                todayHighTxtColor = types.getColor(attr, TXTCOLOR[0]);
            else if (attr == R.styleable.ForecastView_todayLowTxtColor) ;
            else if (attr == R.styleable.ForecastView_lineColor)
                lineColor = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_highLineColor)
                highLineColor = types.getColor(attr, HIGHLINECOLOR);
            else if (attr == R.styleable.ForecastView_lowLineColor)
                lowLineColor = types.getColor(attr, LOWLINECOLOR);
            else if (attr == R.styleable.ForecastView_leftHighLineColor)
                leftLineColor[0] = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_rightHighLineColor)
                rightLineColor[0] = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_leftLowLineColor)
                leftLineColor[1] = types.getColor(attr, leftLineColor[1]);
            else if (attr == R.styleable.ForecastView_rightLowLineColor)
                rightLineColor[1] = types.getColor(attr, -1);
            else if (attr == R.styleable.ForecastView_haveMiddleLine)
                haveMiddleLine = types.getBoolean(attr, false);
            else if (attr == R.styleable.ForecastView_middleLineColor)
                middleLineColor = types.getColor(attr, MIDDLE_LINE_COLOR);
            else if (attr == R.styleable.ForecastView_middleLineStrokeWidth)
                middleLineStrokeWidth = types.getDimensionPixelSize(attr, MIDDLE_LINE_STROKE_WIDTH);
            else if (attr == R.styleable.ForecastView_width)
                width = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_height)
                height = types.getDimensionPixelSize(attr, -1);
            else if (attr == R.styleable.ForecastView_backgroundColor)
                backgroundColor = types.getColor(attr, CANVASCOLOR);
            else if (attr == R.styleable.ForecastView_istoday)
                isToday = types.getBoolean(attr, false);
        }
        types.recycle();
    }

    /**
     * 初始化所要用的三支画笔
     */
    protected void initPaints() {
        dotPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dotPaint.setStyle(Paint.Style.FILL);


        txtPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setStyle(Paint.Style.STROKE);


        middleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        middleLinePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int widthResult = getSize(width, widthMode, 0);
        int heightResult = getSize(height, heightMode, 1);
        setMeasuredDimension(widthResult, heightResult);
    }

    /**
     * 计算宽高
     *
     * @param size
     * @param sizeMode
     * @param type
     * @return
     */
    protected int getSize(int size, int sizeMode, int type) {
        int result = -1;
        if (sizeMode == MeasureSpec.EXACTLY)
            result = size;
        else {
            if (type == 0)
                result = width + getPaddingLeft() + getPaddingRight();
            else
                result = height + getPaddingBottom() + getPaddingTop();
            if (sizeMode == MeasureSpec.AT_MOST)
                result = Math.min(size, result);
        }
        return result;
    }
    protected int baseY, degreeHeight;
    protected int w, h;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(backgroundColor);

        //温度数据的异常判断，如果最高温最低温一样....
        if (highestDegree == lowestDegree) {
            Log.e(TAG, " n 天内 最高温 和 最低温 居然一样：highestDegree = lowestDegree ！！！！ I can't believe it!!!");
            return;
        }

        txtPaint.setTextSize(txtSize);
        fontMetrics = txtPaint.getFontMetrics();

        //宽高
        w = getWidth();
        h = getHeight();

        // 画当天的中间线，墨迹天气的效果
        drawMiddleLine(canvas);

        //------------------------在绘制控件之前的必要数据 base data, 整个控件的绘制都是基于这几个数据---------------------------

        //baseY：代表的是 文字距离顶部/底部的距离 + 文字距离dot的距离 + 文字本身的高度
        baseY = txtToBorder + txtToDot + (int) (fontMetrics.bottom - fontMetrics.ascent);

        //最重要的一个数据 baseHeight: 可以看到 h - 2*baseY 其实就是在计算真实可用的温度线的高度区间
        int baseHeight = h - 2 * baseY;

        //在计算出温度线所在的区间后，计算每一度的高度
        degreeHeight = baseHeight / (highestDegree - lowestDegree);

        //------------------------------------------------------------------------------------------
        //计算圆点坐标
        T t = datas.get(position);
        int[] y = new int[2];

        Point[] points = new Point[2];//圆点坐标数组
        Point[] baseLinePoints = new Point[2];//文字坐标数组


        for (int i = 0; i <= t.getDegree().length - 1; i++){
            y[i] = baseY + dotRadiu / 2 + (highestDegree - t.getDegree()[i]) * degreeHeight;
            points[i] = new Point(w / 2, y[i]);
            int[] baseLineY = {points[i].y - (dotRadiu / 2 + txtToDot),
                    points[i].y + (dotRadiu / 2 + txtToDot) + (Math.abs((int) (fontMetrics.bottom - fontMetrics.ascent))) / 3 * 2};


            //计算温度文字的基线的起始点
            baseLinePoints[i] = new Point((w - (int) txtPaint.measureText(t.getDegree()[i] + "")) / 2, baseLineY[i]);
            //绘制温度文本
            txtPaint.setColor(txtColor[i]);
            canvas.drawText(t.getDegree()[i] + "", baseLinePoints[i].x, baseLinePoints[i].y, txtPaint);
            //只要 position >0 都需要画左边的连线
            if (position > 0) {
                T preT = datas.get(position - 1);
                int highY = baseY + dotRadiu / 2 + (highestDegree - preT.getDegree()[i]) * degreeHeight;
                Point highPoint = new Point(w / 2, highY);
                Point leftPoints = new Point(0, (points[i].y + highPoint.y) / 2);
                drawLines(canvas, points[i], leftPoints, 0, i);
            }

            //只要 position 不是最后一个 都需要画右边的连线
            if (position < datas.size() - 1) {
                T nextT = datas.get(position + 1);
                int highY = baseY + dotRadiu / 2 + (highestDegree - nextT.getDegree()[i]) * degreeHeight;
                Point highPoint = new Point(w / 2, highY);
                Point rightPoints = new Point(w, (points[i].y + highPoint.y) / 2);
                drawLines(canvas, points[i], rightPoints, 1, i);
            }
            //画出圆点
            dotPaint.setColor(Color.WHITE);
            canvas.drawCircle(points[i].x, points[i].y, dotRadiu * 2, dotPaint);
            dotPaint.setColor(dotColor[i]);
            canvas.drawCircle(points[i].x, points[i].y, dotRadiu * 3 /2, dotPaint);
            dotPaint.setColor(Color.WHITE);
            canvas.drawCircle(points[i].x, points[i].y, dotRadiu, dotPaint);
        }



    }
    protected void drawMiddleLine(Canvas canvas) {
        if (haveMiddleLine) {
            middleLinePaint.setColor(middleLineColor);
            middleLinePaint.setStrokeWidth(middleLineStrokeWidth);
            canvas.drawLine(w / 2, 0, w / 2, h, middleLinePaint);
        }
    }

    /**
     * 跟据坐标点画温度线
     *
     * @param canvas
     * @param tPoints
     * @param points
     */
    protected void drawLines(Canvas canvas, Point tPoints, Point points, int direct, int i) {
        linePaint.setStrokeWidth(lineStrokeWidth);
        if (direct == 0)
            linePaint.setColor(leftLineColor[i]);
        else
            linePaint.setColor(rightLineColor[i]);
        canvas.drawLine(tPoints.x, tPoints.y, points.x, points.y, linePaint);
    }

    /**
     * 计算当天温度 dot 圆心的坐标
     *
     * @param t
     * @return
     */
    protected Point[] getTPoints(T t) {
        Point[] points = new Point[2];
        int highY = baseY + dotRadiu / 2 + (highestDegree - t.getDegree()[0]) * degreeHeight;
        Point highPoint = new Point(w / 2, highY);
        points[0] = highPoint;
        return points;
    }


    /**
     * 设置WeatherView宽高
     *
     * @param width
     * @param height
     */
    public void setWH(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 是否需要中间线
     *
     * @param haveMiddleLine
     */
    public void setHaveMiddleLine(boolean haveMiddleLine) {
        this.haveMiddleLine = haveMiddleLine;
    }

    /**
     * set middle line params
     *
     * @param middleLineStrokeWidth
     * @param middleLineColor
     */
    public void setMiddleLineParams(int middleLineStrokeWidth, int middleLineColor) {
        this.middleLineStrokeWidth = middleLineStrokeWidth;
        this.middleLineColor = middleLineColor;
        setUpAttr();
    }

    /**
     * 设置是否是当天
     *
     * @param isToday
     */
    public void setToday(boolean isToday) {
        this.isToday = isToday;
        setUpAttr();
    }

    public void setTodayParams(int todayDotRadiu, int todayDotColor, int todayTxtColor) {
        setTodayParams(todayDotRadiu, todayDotColor, todayDotColor, todayTxtColor, todayTxtColor);
    }
    /**
     * 设置当天温度的显示
     *
     * @param todayDotRadiu
     * @param todayHighDotColor
     * @param todayLowDotColor
     * @param todayHighTxtColor
     * @param todayLowTxtColor
     */
    public void setTodayParams(int todayDotRadiu, int todayHighDotColor, int todayLowDotColor, int todayHighTxtColor, int todayLowTxtColor) {
        this.dotRadiu = this.todayDotRadiu = todayDotRadiu;
        this.dotColor[0] = this.todayHighDotColor = todayHighDotColor;
        this.dotColor[1] = this.todayLowDotColor = todayLowDotColor;
        this.txtColor[0] = this.todayHighTxtColor = todayHighTxtColor;
        this.txtColor[1] = this.todayLowTxtColor = todayLowTxtColor;
        setUpAttr();
    }

    /**
     * 设置画布背景色
     *
     * @param color
     */
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        setUpAttr();
    }

    public void setBackgroundColor(String color) {
        setBackgroundColor(Color.parseColor(color));
    }

    public void setTxtParams(int txtSize, int txtColor, int txtToBorder, int txtToDot) {
        setTxtParams(txtSize, txtColor, txtColor, txtToBorder, txtToDot);
    }

    /**
     * 设置和文本相关的参数, px is default
     *
     * @param txtSize      字体大小
     * @param highTXTColor 高度的文字颜色
     * @param lowTXTColor  低度的文字颜色
     * @param txtToBorder  文字距离顶部/底部的距离
     * @param txtToDot     文字距离 dot 距离
     */
    public void setTxtParams(int txtSize, int highTXTColor, int lowTXTColor, int txtToBorder, int txtToDot) {
        this.txtSize = txtSize;
        this.txtColor[0] = highTXTColor;
        this.txtColor[1] = lowTXTColor;
        this.txtToBorder = txtToBorder;
        this.txtToDot = txtToDot;
        setUpAttr();
    }

    public void setDotParams(int dotRadiu, int dotColor) {
        setDotParams(dotRadiu, dotColor, dotColor);
    }

    public void setDotParams(int dotRadiu, int highDotColor, int lowDotColor) {
        setDotParams(dotRadiu, dotRadiu, highDotColor, lowDotColor);
    }

    public void setDotParams(int dotRadiu, int todayDotRadiu, int highDotColor, int lowDotColor) {
        setDotParams(dotRadiu, todayDotRadiu, highDotColor, lowDotColor, highDotColor, lowDotColor);
    }
    /**
     * 设置 dot 相关参数， px is default
     *
     * @param dotRadiu          圆半径
     * @param todayDotRadiu     当天圆半径
     * @param todayHighDotColor 当天高温圆点颜色
     * @param todayLowDotColor  当天低温圆点颜色
     * @param highDotColor      高温圆点颜色
     * @param lowDotColor       低温圆点颜色
     */
    public void setDotParams(int dotRadiu, int todayDotRadiu, int todayHighDotColor, int todayLowDotColor, int highDotColor, int lowDotColor) {
        this.dotRadiu = dotRadiu;
        this.todayDotRadiu = todayDotRadiu;
        this.todayHighDotColor = todayHighDotColor;
        this.todayLowDotColor = todayLowDotColor;
        this.dotColor[0] = highDotColor;
        this.dotColor[1] = lowDotColor;
        setUpAttr();
    }

    public void setLineParams(int lineStrokeWidth, int lineColor) {
        setLineParams(lineStrokeWidth, lineColor, lineColor);
    }

    public void setLineParams(int lineStrokeWidth, int highLineColor, int lowLineColor) {
        setLineParams(lineStrokeWidth, highLineColor, lowLineColor, highLineColor, lowLineColor);
    }

    /**
     * 设置温度线的各种参数
     *
     * @param lineStrokeWidth
     * @param leftHighLineColor
     * @param leftLowLineColor
     * @param rightHighLineColor
     * @param rightLowLineColor
     */
    public void setLineParams(int lineStrokeWidth, int leftHighLineColor, int leftLowLineColor, int rightHighLineColor, int rightLowLineColor) {
        this.lineStrokeWidth = lineStrokeWidth;
        this.lineColor = this.highLineColor = this.leftLineColor[0] = leftHighLineColor;
        this.lowLineColor = this.leftLineColor[1] = leftLowLineColor;
        this.rightLineColor[0] = rightHighLineColor;
        this.rightLineColor[1] = rightLowLineColor;
        setUpAttr();
    }
}
