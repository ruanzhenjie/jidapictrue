package com.example.jidaphotos1_0;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

    /** 
     * sd���ĸ�Ŀ¼ 
     */  
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();  
    /** 
     * �ֻ��Ļ����Ŀ¼ 
     */  
    private static String mDataRootPath = null;  
    /** 
     * ����Image��Ŀ¼�� 
     */  
    private final static String FOLDER_NAME = "/AndroidImage";  
      
    private String DIALOG="FileUtils";
      
    public FileUtils(Context context){  
        mDataRootPath = context.getCacheDir().getPath();  
    }  
      
  
    /** 
     * ��ȡ����Image��Ŀ¼ 
     * @return 
     */  
    private String getStorageDirectory(){  
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?  
                mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;  
    }  
      
    /** 
     * ����Image�ķ�������sd���洢��sd����û�оʹ洢���ֻ�Ŀ¼ 
     * @param fileName  
     * @param bitmap    
     * @throws IOException 
     */  
    public void put(String fileName, Bitmap bitmap){  
        if(bitmap == null){  
            return;  
        }  
        String path = getStorageDirectory();
        Log.d(DIALOG, "path="+path);
        File folderFile = new File(path);  
        if(!folderFile.exists()){  
            folderFile.mkdir();  
        }  
        if(isFileExists(fileName)){
        	return ;
        }
        File file = new File(path + File.separator + fileName);  
        try {
			file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);  
        bitmap.compress(CompressFormat.JPEG, 100, fos);  
        fos.flush();  
        fos.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    }  
      
    /** 
     * ���ֻ�����sd����ȡBitmap 
     * @param fileName 
     * @return 
     */  
    public Bitmap getBitmap(String fileName){  
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + fileName);  
    }  

    public Bitmap get(String fileName){
        if(isFileExists(fileName)){
        	Log.d(DIALOG, "getfromfile");
        	return getBitmap(fileName);
        }
        else
        	return null;
    }
      
    /** 
     * �ж��ļ��Ƿ���� 
     * @param fileName 
     * @return 
     */  
    public boolean isFileExists(String fileName){

        //return ((new File(getStorageDirectory() + File.separator + fileName).exists()) && getFileSize(fileName)!=0);
        return new File(getStorageDirectory() + File.separator + fileName).exists();
    }  
      
    /** 
     * ��ȡ�ļ��Ĵ�С 
     * @param fileName 
     * @return 
     */  
    public long getFileSize(String fileName) {  
        return new File(getStorageDirectory() + File.separator + fileName).length();  
    }  
      
      
    /** 
     * ɾ��SD�������ֻ��Ļ���ͼƬ��Ŀ¼ 
     */  
    public void deleteFile() {  
        File dirFile = new File(getStorageDirectory());  
        if(! dirFile.exists()){  
            return;  
        }  
        if (dirFile.isDirectory()) {  
            String[] children = dirFile.list();  
            for (int i = 0; i < children.length; i++) {  
                new File(dirFile, children[i]).delete();  
            }  
        }  
          
        dirFile.delete();  
    }  
    

}
