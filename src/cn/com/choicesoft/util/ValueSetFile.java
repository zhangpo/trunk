package cn.com.choicesoft.util;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * By M。c on 2015-04-17
 * jnwsczh@163.com
 */
public class ValueSetFile<T> {
    public static final String PACKAGE_NAME = "cn.com.choicesoft";// 包名
    public String mBuffPath = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/"; // 在手机里存放数据库的位置

    public ValueSetFile(String fileName) {
        mBuffPath=mBuffPath+fileName;
    }

    /**
     * @param list
     *            向本地写入的缓存数据
     *            本地缓存的最大数据量
     * */
    public synchronized void write(List<T> list) {
        if (list == null) {
            return;
        }
        del();
        // 写入本地
        put(list);
    }




    /**
     * 读取缓存数据
     *
     * @return 缓存数据，数据为空时返回长度为0的list
     * */
    public synchronized List<T> read() {
        return get();
    }

    /**
     * 向本地写入数据
     * */
    private void put(List<T> list) {

        try {
            // 打开文件
            FileOutputStream fos = new FileOutputStream(mBuffPath);
            // 将数据写入文件
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);

            // 释放资源
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从本地读取数据
     * */
    @SuppressWarnings("unchecked")
    private List<T> get() {
        List<T> list = new ArrayList<T>();
        try {
            File file = new File(mBuffPath);
            if (!file.exists()) {
                return list;
            }
            // 打开文件
            FileInputStream fis = new FileInputStream(mBuffPath);
            // 读取文件
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (List<T>) ois.readObject();
            // 释放资源
            ois.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 删除文件
     */
    public void del(){
        File file = new File(mBuffPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
