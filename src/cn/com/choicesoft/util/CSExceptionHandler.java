package cn.com.choicesoft.util;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import cn.com.choicesoft.R;

import java.io.*;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * �쳣����
 */
public class CSExceptionHandler implements UncaughtExceptionHandler{
    private static String LOG_PATH_SDCARD_DIR= Environment.getExternalStorageDirectory()+"/data/cn.com.choicesoft.Pad/log/";// ��־�ļ���sdcard�е�·��
    private static String LOG_PATH_MEMORY_DIR="/data" + Environment.getDataDirectory().getAbsolutePath() + "/cn.com.choicesoft/log/";//��־�ļ����ڴ��е�·��(��־�ļ��ڰ�װĿ¼�е�·��)="/sdcard/data/cn.com.choicesoft.phone/log";// ��־�ļ���sdcard�е�·��
    private static String LOGFILENAME = "ChoiceLog.txt";// �����������־�ļ�����
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// ��־�ļ���ʽ
    private static SimpleDateFormat logSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// ��־�������ʽ
	private static CSExceptionHandler crashHandler;
    private static Context mContext;

    @Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (crashHandler != null) {
			try {
                Date nowtime = new Date();
                String needWriteFiel = logfile.format(nowtime);
                File f=new File(getCurrLogType());
                if(!f.exists()){
                    f.mkdirs();
                }
                File file=new File(f, needWriteFiel+ LOGFILENAME);
                if(!file.exists()){
                    file.createNewFile();
                    CSLog.delFile();
                }
                FileOutputStream  filerWriter = new  FileOutputStream (file, true);//����������������ǲ���Ҫ�����ļ���ԭ�������ݣ������и���
				PrintStream printStream = new PrintStream(filerWriter);
                printStream.append("---------------"+logSdf.format(nowtime)+"---start-------------------\n");
				ex.printStackTrace(printStream);
				// TODO ����쳣
				Log.e("logError", "-------------------------logError---------------------------",ex);
                printStream.append("---------------"+logSdf.format(nowtime)+"---end-------------------\n");
				printStream.flush();
				printStream.close();
                filerWriter.close();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        ToastUtil.toast(mContext, R.string.system_error);
                        Looper.loop();
                        //�˳�����
                        ExitApplication.getInstance().exit();
                        System.exit(1);
                    }
                }).start();
			} catch (FileNotFoundException e) {
                CSLog.e("CSExceptionHandler-FileNotFoundException",e.toString());
			} catch (IOException e) {
                CSLog.e("CSExceptionHandler-IOException", e.toString());
			}finally {
                try {//�ȵ����룬������ʾToast��Ϣ��ʾ
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    CSLog.e("CSExceptionHandler-InterruptedException", "error : "+ e);
                }
                //�˳�����
                ExitApplication.getInstance().exit();
                System.exit(1);
            }
        }
	}
	//����Ĭ�ϴ�����
	public void init(Context context) {
        mContext=context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	private CSExceptionHandler() {}
	//����
	public static CSExceptionHandler instance() {
        if (crashHandler!=null) {    //δͬ��ʱ��ֻҪ�Ѵ������ɷ��أ��Լ���ͬ���ɱ�
            return crashHandler;
        }
        synchronized (CSExceptionHandler.class) {
            if(crashHandler==null) {
                crashHandler = new CSExceptionHandler();
            }
            return crashHandler;
        }
	}
    public static String getCurrLogType(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return LOG_PATH_MEMORY_DIR;
        }else{
            return LOG_PATH_SDCARD_DIR;
        }
    }
}