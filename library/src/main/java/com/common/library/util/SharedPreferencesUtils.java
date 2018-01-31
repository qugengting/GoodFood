package com.common.library.util;
import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
	/**
	 * 保存在手机里面的文件名
	 */
	private static final String FILE_NAME = "fp_sp";
	
	/**
	* @Title: putData 
	* @param @param context
	* @param @param key 
	* @param @param object   
	* @return void 
	* @throws
	 */
	public static void putData(Context context , String key, Object object){
		
		String type = object.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		
		if("String".equals(type)){
			editor.putString(key, (String)object);
		}
		else if("Integer".equals(type)){
			editor.putInt(key, (Integer)object);
		}
		else if("Boolean".equals(type)){
			editor.putBoolean(key, (Boolean)object);
		}
		else if("Float".equals(type)){
			editor.putFloat(key, (Float)object);
		}
		else if("Long".equals(type)){
			editor.putLong(key, (Long)object);
		}
		editor.commit();
	}

	/***
	* @Title: getData 
	* @param @param context
	* @param @param key
	* @param @param defaultObject
	* @param @return   
	* @return Object
	 * @throws
	 */
	public static Object getData(Context context , String key, Object defaultObject){
		String type = defaultObject.getClass().getSimpleName();
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		
		if("String".equals(type)){
			return sp.getString(key, (String)defaultObject);
		}
		else if("Integer".equals(type)){
			return sp.getInt(key, (Integer)defaultObject);
		}
		else if("Boolean".equals(type)){
			return sp.getBoolean(key, (Boolean)defaultObject);
		}
		else if("Float".equals(type)){
			return sp.getFloat(key, (Float)defaultObject);
		}
		else if("Long".equals(type)){
			return sp.getLong(key, (Long)defaultObject);
		}
		
		return null;
	}

	public static void clearData(Context context) {
		SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.clear().apply();
	}
}
