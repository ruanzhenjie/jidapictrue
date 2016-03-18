package com.example.jidaphotos1_0;


import java.util.ArrayList;

import com.example.jidaphotos1_0.ImgControl.onimgdownloadlistener;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/** 
 * @ClassName: MatrixImageView 
 * @Description:  ���Ŵ���С���ƶ�Ч����ImageView
 * @author LinJ
 * @date 2015-1-7 ����11:15:07 
 *  
 */
public class MatrixImageView extends ImageView{
    private final static String TAG="MatrixImageView";
    private GestureDetector mGestureDetector;
    /**  ģ��Matrix�����Գ�ʼ�� */ 
    private  Matrix mMatrix=new Matrix();
    /**  ͼƬ����*/ 
    private float mImageWidth;
    /**  ͼƬ�߶� */ 
    private float mImageHeight;
    private long lastclicktime;
    private boolean initflag;
    
    private float bestscale;
    private float bestleft,besttop,bestrigth,bestbottom;
    private ArrayList<ItemList> mlist;
    private int mnum;
    private ImgControl mimgcontrol;
    private int jumpflag;

    public MatrixImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MatrixTouchListener mListener=new MatrixTouchListener();
        setOnTouchListener(mListener);
        mGestureDetector=new GestureDetector(getContext(), new GestureListener(mListener));
        //��������Ϊbalck
        setBackgroundColor(Color.BLACK);
        //��������������ΪFIT_CENTER����ʾ��ͼƬ����������/��С��View�Ŀ�ȣ�������ʾ
        setScaleType(ScaleType.FIT_CENTER);

        mimgcontrol=ImgControl.get(null, context);
        jumpflag=0;
        /*
        //������ͼƬ�󣬻�ȡ��ͼƬ������任����
        mMatrix.set(getImageMatrix());
        float[] values=new float[9];
        mMatrix.getValues(values);
        //ͼƬ���Ϊ��Ļ��ȳ����ű���
        mImageWidth=getWidth()/values[Matrix.MSCALE_X];
        mImageHeight=(getHeight()-values[Matrix.MTRANS_Y]*2)/values[Matrix.MSCALE_Y];*/
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        // TODO Auto-generated method stub

        super.setImageBitmap(bm);
        //������ͼƬ�󣬻�ȡ��ͼƬ������任����
        mMatrix.set(getImageMatrix());
        float[] values=new float[9];
        mMatrix.getValues(values);

        mImageWidth=bm.getWidth();
        mImageHeight=bm.getHeight();

        float width=getWidth();
        float height=getHeight();
        lastclicktime=System.currentTimeMillis();
        initflag=true;
        //ͼƬ���Ϊ��Ļ��ȳ����ű���
        //mImageWidth=getWidth()/values[Matrix.MSCALE_X];
        //mImageHeight=(getHeight()-values[Matrix.MTRANS_Y]*2)/values[Matrix.MSCALE_Y];
    }
    
    
    public void setImageBitmap(ArrayList<ItemList> list,int num) {
    	mlist=list;
    	mnum=num;
    	if(num<0 || num>=list.size())
    		return;
    	setImageBitmap(mimgcontrol.getrealImg(list.get(num).getphotoplace(), new onimgdownloadlistener() {
			
			@Override
			public void onimgdownload(Bitmap bm, String url) {
				// TODO Auto-generated method stub
				if(bm!=null)
				{
					if(mlist.get(mnum).getphotoplace().equals(url))
					{
				        setScaleType(ScaleType.FIT_CENTER);
				        mMatrix.set(getImageMatrix());
				        mMatrix.setScale(1, 1);
				        setImageMatrix(mMatrix);
						setImageBitmap(bm);
					}
				}
				
			}
		}));
    }

    
    private void jumpPhoto(int num){
    	if(null==mlist || 0==num)
    		return;
    	int tnum=num+mnum;
    	if(tnum<0 || tnum>=mlist.size())
    		return;

    	mnum=tnum;
        setScaleType(ScaleType.FIT_CENTER);
        mMatrix.set(getImageMatrix());
        mMatrix.setScale(1, 1);
        setImageMatrix(mMatrix);
        
    	setImageBitmap(mlist, mnum);
    }
    
    public class MatrixTouchListener implements OnTouchListener{
        /** ������Ƭģʽ */
        private static final int MODE_DRAG = 1;
        /** �Ŵ���С��Ƭģʽ */
        private static final int MODE_ZOOM = 2;
        /**  ��֧��Matrix */ 
        private static final int MODE_UNABLE=3;
        /**   ������ż���*/ 
        float mMaxScale=6;
        /**   ˫��ʱ�����ż���*/ 
        float mDobleClickScale=2;
        private int mMode = 0;// 
        /**  ���ſ�ʼʱ����ָ��� */ 
        private float mStartDis;
        /**   ��ǰMatrix*/ 
        private Matrix mCurrentMatrix = new Matrix();    

        /** ���ڼ�¼��ʼʱ�������λ�� */
        private PointF startPoint = new PointF();
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            	if(initflag)
            		init();
                //�����϶�ģʽ
                mMode=MODE_DRAG;
                startPoint.set(event.getX(), event.getY());
                isMatrixEnable();
                DobleClickScale();
                break;
            case MotionEvent.ACTION_UP:
            	jumpPhoto(jumpflag);
                jumpflag=0;
            case MotionEvent.ACTION_CANCEL:
                reSetMatrix();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mMode == MODE_ZOOM) {
                    setZoomMatrix(event);
                }else if (mMode==MODE_DRAG) {
                    setDragMatrix(event);
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if(mMode==MODE_UNABLE) return true;
                mMode=MODE_ZOOM;
                mStartDis = distance(event);
                break;
            default:
                break;
            }

            //return mGestureDetector.onTouchEvent(event);
            return true;
        }
        
        private void init(){

            float width=getWidth();
            float height=getHeight();
            mCurrentMatrix.set(getImageMatrix());
            float[] values=new float[9];
            float[] values2=new float[9];
            mCurrentMatrix.getValues(values);
            if(values[Matrix.MSCALE_X]<=1){
            	mMatrix=new Matrix(mCurrentMatrix);
            	mMatrix.getValues(values2);
            	bestleft=values[Matrix.MTRANS_X];
            	besttop=values[Matrix.MTRANS_Y];
            	bestrigth=values[Matrix.MTRANS_X]+mImageWidth*values[Matrix.MSCALE_X];
            	bestbottom=values[Matrix.MTRANS_Y]+mImageHeight*values[Matrix.MSCALE_Y];
            	bestscale=values[Matrix.MSCALE_X];
            }
            else{
            	/*float dx,dy,scale;
            	dx=values[Matrix.MTRANS_X];
            	dy=values[Matrix.MTRANS_Y];
            	scale=values[Matrix.MSCALE_X];*/
            	bestleft=(width-mImageWidth)/2;
            	besttop=(height-mImageHeight)/2;
            	bestrigth=bestleft+mImageWidth;
            	bestbottom=besttop+mImageHeight;
            	bestscale=1;
            }
            
            initflag=false;
        }
        
        private void DobleClickScale(){
        	long now=System.currentTimeMillis();
        	if(now-lastclicktime<200){
        		onDoubleClick();
        		Log.d(TAG, "now-lastclicktime"+(now-lastclicktime));
        	}
        	lastclicktime=now;
        }

        public void setDragMatrix(MotionEvent event) {
            if(isZoomChanged()){
                float dx = event.getX() - startPoint.x; // �õ�x����ƶ�����
                float dy = event.getY() - startPoint.y; // �õ�x����ƶ�����
                //�����˫����ͻ,����10f�������϶�
                if(Math.sqrt(dx*dx+dy*dy)>10f){
                    startPoint.set(event.getX(), event.getY());
                    //�ڵ�ǰ�������ƶ�
                    mCurrentMatrix.set(getImageMatrix());
                    float[] values=new float[9];
                    mCurrentMatrix.getValues(values);
                    dx=checkDxBound(values,dx);
                    dy=checkDyBound(values,dy);    
                    mCurrentMatrix.postTranslate(dx, dy);
                    setImageMatrix(mCurrentMatrix);
                }
            }
            else{
            	float dx = event.getX() - startPoint.x; // �õ�x����ƶ�����
                float dy = event.getY() - startPoint.y; // �õ�x����ƶ�����
                //�����˫����ͻ,����10f�������϶�
                if(Math.sqrt(dx*dx+dy*dy)>10f){
                    //�ڵ�ǰ�������ƶ�
                    mCurrentMatrix.set(getImageMatrix());
                    float[] values=new float[9];
                    mCurrentMatrix.getValues(values);
                    dx=checkDxBound(values,dx);
                }
            }
        }

        /**  
         *  �ж����ż����Ƿ��Ǹı��
         *  @return   true��ʾ�ǳ�ʼֵ,false��ʾ��ʼֵ
         */
        private boolean isZoomChanged() {
            float[] values=new float[9];
            getImageMatrix().getValues(values);
            //��ȡ��ǰX�����ż���
            float scale=values[Matrix.MSCALE_X];
            //��ȡģ���X�����ż����������Ƚ�
            mMatrix.getValues(values);
            return scale!=values[Matrix.MSCALE_X];
        }

        /**  
         *  �͵�ǰ����Աȣ�����dy��ʹͼ���ƶ��󲻻ᳬ��ImageView�߽�
         *  @param values
         *  @param dy
         *  @return   
         */
        private float checkDyBound(float[] values, float dy) {
            float height=getHeight();
            if((mImageHeight*(values[Matrix.MSCALE_Y]))<=height)
            {
                return 0;
            }
            if(values[Matrix.MTRANS_Y]+dy>0)
            {
                dy=-values[Matrix.MTRANS_Y];
            }
            else if(values[Matrix.MTRANS_Y]+dy<(-(mImageHeight*values[Matrix.MSCALE_Y]-height)))
            {
                dy=height-(mImageHeight*values[Matrix.MSCALE_Y])-values[Matrix.MTRANS_Y];
            }
            return dy;
        }

        /**  
         *�͵�ǰ����Աȣ�����dx��ʹͼ���ƶ��󲻻ᳬ��ImageView�߽�
         *  @param values
         *  @param dx
         *  @return   
         */
        private float checkDxBound(float[] values,float dx) {
            float width=getWidth();
            if((mImageWidth*(values[Matrix.MSCALE_X]))<=width){
            	if(dx>0)
            		jumpflag=-1;
            	else if(dx<0)
            		jumpflag=1;
                return 0;
            }
            if((values[Matrix.MTRANS_X]+dx)>0)
            //if((values[Matrix.MTRANS_X]+dx)>bestleft)
            {
                dx=-values[Matrix.MTRANS_X];
                //dx=bestleft-values[Matrix.MTRANS_X];
                //jumpflag=-1;
            }
            else if(values[Matrix.MTRANS_X]+dx<(-(mImageWidth*values[Matrix.MSCALE_X]-width)))
            //else if(values[Matrix.MTRANS_X]+dx<(-(mImageWidth*bestscale-width)))
            {
                dx=width-(mImageWidth*values[Matrix.MSCALE_X])-values[Matrix.MTRANS_X];
            	//dx=width-(mImageWidth*bestscale)-values[Matrix.MTRANS_X];
                //jumpflag=1;
            }
            else{
            	float a=dx;
            }
            return dx;
        }

        /**  
         *  ��������Matrix
         *  @param event   
         */
        private void setZoomMatrix(MotionEvent event) {
            //ֻ��ͬʱ�����������ʱ���ִ��
            if(event.getPointerCount()<2) return;
            float endDis = distance(event);// ��������
            if (endDis > 10f) { // ������ָ��£��һ���ʱ�����ش���10
                float scale = endDis / mStartDis;// �õ����ű���
                mStartDis=endDis;//���þ���
                mCurrentMatrix.set(getImageMatrix());//��ʼ��Matrix
                float[] values=new float[9];
                mCurrentMatrix.getValues(values);

                scale = checkMaxScale(scale, values);
                setImageMatrix(mCurrentMatrix);
                fit();
            }
        }

        void fit(){
        	float dx,dy;
            float[] values=new float[9];
            mCurrentMatrix.getValues(values);
            dx=0;
            dy=0;
            
            if(values[Matrix.MTRANS_X]>bestleft){
            	dx=bestleft-values[Matrix.MTRANS_X];
            }
            if((values[Matrix.MTRANS_X]+mImageWidth*values[Matrix.MSCALE_X])<bestrigth){
            	dx=bestrigth-(values[Matrix.MTRANS_X]+mImageWidth*values[Matrix.MSCALE_X]);
            }
            
            if(values[Matrix.MTRANS_Y]>besttop){
            	dy=besttop-values[Matrix.MTRANS_Y];
            }
            if((values[Matrix.MTRANS_Y]+mImageHeight*values[Matrix.MSCALE_Y])<bestbottom)
            {
            	dy=bestbottom-(values[Matrix.MTRANS_Y]+mImageHeight*values[Matrix.MSCALE_Y]);
            }
            
            /*
            if(values[Matrix.MTRANS_X]>0){
            	dx=-values[Matrix.MTRANS_X];
            }
            if(values[Matrix.MTRANS_Y]>0){
            	dy=-values[Matrix.MTRANS_Y];
            }*/
            mCurrentMatrix.postTranslate(dx, dy);
            setImageMatrix(mCurrentMatrix);
        }
        /**  
         *  ����scale��ʹͼ�����ź󲻻ᳬ�������
         *  @param scale
         *  @param values
         *  @return   
         */
        private float checkMaxScale(float scale, float[] values) {
            if(scale*values[Matrix.MSCALE_X]>mMaxScale) 
                scale=mMaxScale/values[Matrix.MSCALE_X];
            else if(scale*values[Matrix.MSCALE_X]<bestscale)
            	scale=bestscale/values[Matrix.MSCALE_X];
            mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);
            return scale;
        }

        /**  
         *   ����Matrix
         */
        private void reSetMatrix() {
            if(checkRest()){
                mCurrentMatrix.set(mMatrix);
                setImageMatrix(mCurrentMatrix);
            }
        }

        /**  
         *  �ж��Ƿ���Ҫ����
         *  @return  ��ǰ���ż���С��ģ�����ż���ʱ������ 
         */
        private boolean checkRest() {
            // TODO Auto-generated method stub
            float[] values=new float[9];
            getImageMatrix().getValues(values);
            //��ȡ��ǰX�����ż���
            float scale=values[Matrix.MSCALE_X];
            //��ȡģ���X�����ż����������Ƚ�
            mMatrix.getValues(values);
            return scale<values[Matrix.MSCALE_X];
        }

        /**  
         *  �ж��Ƿ�֧��Matrix
         */
        private void isMatrixEnable() {
            //�����س���ʱ����������
            if(getScaleType()!=ScaleType.CENTER){
                setScaleType(ScaleType.MATRIX);
            }else {
                mMode=MODE_UNABLE;//����Ϊ��֧������
            }
        }

        /**  
         *  ����������ָ��ľ���
         *  @param event
         *  @return   
         */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** ʹ�ù��ɶ���������֮��ľ��� */
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        /**  
         *   ˫��ʱ����
         */
        public void onDoubleClick(){
        	float scale;
        	float dx,dy;
        	
        	mCurrentMatrix.set(getImageMatrix());
        	if(isZoomChanged()){
                //��ʼ��Matrix
        		/*
                float[] values=new float[9];
                mCurrentMatrix.getValues(values);
                scale=1/values[Matrix.MSCALE_X];
                

                float width=getWidth();
                float height=getHeight();
                dx=(width-mImageWidth*(values[Matrix.MSCALE_X]))/2-values[Matrix.MTRANS_X];
                dy=(height-mImageHeight*(values[Matrix.MSCALE_Y]))/2-values[Matrix.MTRANS_Y];
                mCurrentMatrix.postTranslate(dx, dy);*/
        		mCurrentMatrix.setTranslate(bestleft, besttop);
        		mCurrentMatrix.postScale(bestscale, bestscale,bestleft,besttop);
        	}
        	else{
        		scale=mDobleClickScale;
                mCurrentMatrix.postScale(scale, scale,getWidth()/2,getHeight()/2);    
        	}
        	
        	
            /*scale=isZoomChanged()?1:mDobleClickScale;
            mCurrentMatrix.set(mMatrix);//��ʼ��Matrix*/
        	
            setImageMatrix(mCurrentMatrix);
        }
    }


    private class  GestureListener extends SimpleOnGestureListener{
        private final MatrixTouchListener listener;
        public GestureListener(MatrixTouchListener listener) {
            this.listener=listener;
        }
        @Override
        public boolean onDown(MotionEvent e) {
            //����Down�¼�
            return true;
        }
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //����˫���¼�
            listener.onDoubleClick();
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onSingleTapUp(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                float velocityY) {
            // TODO Auto-generated method stub

            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onShowPress(MotionEvent e) {
            // TODO Auto-generated method stub
            super.onShowPress(e);
        }

        

        

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onDoubleTapEvent(e);
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // TODO Auto-generated method stub
            return super.onSingleTapConfirmed(e);
        }

    }


}