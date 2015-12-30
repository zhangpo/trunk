package cn.com.choicesoft.util;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * By M��c on 2015-04-17
 * jnwsczh@163.com
 */
public class ValueSetFile<T> {
    public static final String PACKAGE_NAME = "cn.com.choicesoft";// ����
    public String mBuffPath = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME + "/databases/"; // ���ֻ��������ݿ��λ��

    public ValueSetFile(String fileName) {
        mBuffPath=mBuffPath+fileName;
    }

    /**
     * @param list
     *            �򱾵�д��Ļ�������
     *            ���ػ�������������
     * */
    public synchronized void write(List<T> list) {
        if (list == null) {
            return;
        }
        del();
        // д�뱾��
        put(list);
    }




    /**
     * ��ȡ��������
     *
     * @return �������ݣ�����Ϊ��ʱ���س���Ϊ0��list
     * */
    public synchronized List<T> read() {
        return get();
    }

    /**
     * �򱾵�д������
     * */
    private void put(List<T> list) {

        try {
            // ���ļ�
            FileOutputStream fos = new FileOutputStream(mBuffPath);
            // ������д���ļ�
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);

            // �ͷ���Դ
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * �ӱ��ض�ȡ����
     * */
    @SuppressWarnings("unchecked")
    private List<T> get() {
        List<T> list = new ArrayList<T>();
        try {
            File file = new File(mBuffPath);
            if (!file.exists()) {
                return list;
            }
            // ���ļ�
            FileInputStream fis = new FileInputStream(mBuffPath);
            // ��ȡ�ļ�
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (List<T>) ois.readObject();
            // �ͷ���Դ
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
     * ɾ���ļ�
     */
    public void del(){
        File file = new File(mBuffPath);
        if (file.exists()) {
            file.delete();
        }
    }
}
