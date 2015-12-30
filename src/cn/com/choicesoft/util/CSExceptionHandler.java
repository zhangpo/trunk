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
 * 异常捕获
 */
public class CSExceptionHandler implements UncaughtExceptionHandler{
    private static String LOG_PATH_SDCARD_DIR= Environment.getExternalStorageDirectory()+"/data/cn.com.choicesoft.Pad/log/";// 日志文件在sdcard中的路径
    private static String LOG_PATH_MEMORY_DIR="/data" + Environment.getDataDirectory().getAbsolutePath() + "/cn.com.choicesoft/log/";//日志文件在内存中的路径(日志文件在安装目录中的路径)="/sdcard/data/cn.com.choicesoft.phone/log";// 日志文件在sdcard中的路径
    private static String LOGFILENAME = "ChoiceLog.txt";// 本类输出的日志文件名称
    private static SimpleDateFormat logfile = new SimpleDateFormat("yyyy-MM-dd");// 日志文件格式
    private static SimpleDateFormat logSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 日志的输出格式
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
                FileOutputStream  filerWriter = new  FileOutputStream (file, true);//后面这个参数代表是不是要接上文件中原来的数据，不进行覆盖
				PrintStream printStream = new PrintStream(filerWriter);
                printStream.append("---------------"+logSdf.format(nowtime)+"---start-------------------\n");
				ex.printStackTrace(printStream);
				// TODO 输出异常
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
                        //退出程序
                        ExitApplication.getInstance().exit();
                        System.exit(1);
                    }
                }).start();
			} catch (FileNotFoundException e) {
                CSLog.e("CSExceptionHandler-FileNotFoundException",e.toString());
			} catch (IOException e) {
                CSLog.e("CSExceptionHandler-IOException", e.toString());
			}finally {
                try {//等等三秒，用作显示Toast信息提示
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    CSLog.e("CSExceptionHandler-InterruptedException", "error : "+ e);
                }
                //退出程序
                ExitApplication.getInstance().exit();
                System.exit(1);
            }
        }
	}
	//设置默认处理器
	public void init(Context context) {
        mContext=context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}
	private CSExceptionHandler() {}
	//单例
	public static CSExceptionHandler instance() {
        if (crashHandler!=null) {    //未同步时，只要已创建即可返回，以减少同步成本
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