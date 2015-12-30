package cn.com.choicesoft.util;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.*;

/**
 * ∂‘œÛ∏¥÷∆
 * Created by M.c on 2014/9/5.
 */
public class ValueCopy<T> {
    public T objectCopy(T t){
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        ByteArrayInputStream bis;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(t);
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois= new ObjectInputStream(bis);
            return (T) (ois != null ? ois.readObject() : null);
        } catch (IOException e) {
            CSLog.e("ValueCopy",e.getMessage());
        } catch (ClassNotFoundException e) {
            CSLog.e("ValueCopy",e.getMessage());
        }
        return null;
    }
}
