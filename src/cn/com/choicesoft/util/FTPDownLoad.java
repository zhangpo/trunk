package cn.com.choicesoft.util;

import cn.com.choicesoft.R;
import it.sauronsoftware.ftp4j.*;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;
import cn.com.choicesoft.util.SimpleThread.OnThreadRun;

/**
 * FTP下载
 * @Author:M.c
 * @CreateDate:2014-1-22
 * @Email:JNWSCZH@163.COM
 */
public class FTPDownLoad {
	private String PACKAGE_NAME="cn.com.choicesoft";
	private ProgressDialog dialog;
	public ProgressDialog getDialog(Activity activity) {
		if(dialog==null){
			dialog = new ProgressDialog(activity);
			dialog.setTitle(R.string.data_down);
			dialog.setCancelable(false);
		}
		return dialog;
	}

	/**
	 *
	 * @param activity
	 * @param url
	 * @param name
	 * @param pwd
	 */
	public  void downLoad(final Activity activity,final String url,final String name,final String pwd,final ICallBack callBack){
		SimpleThread sth=new SimpleThread();
		sth.setOnThreadRun(new OnThreadRun() {
			@Override
			public void onRun(final Handler h) {
				FTPClient client=new FTPClient();
				String [] spliteStr = url.split(":");
				try {
					client.connect(spliteStr[0], Integer.valueOf(spliteStr[1]));
					client.login(name, pwd);
					try {
                        client.setAutoNoopTimeout(30000);
                        FTPFile[] files= new FTPFile[0];
                        try {
                            files = client.list("/booksystem");
                        } catch (FTPListParseException e) {
                            e.printStackTrace();
                        }catch (FTPException e){
                            e.printStackTrace();
                        }
                        String url="/booksystem/BookSystem.sqlite";
                        if(files==null||files.length==0){
                            url="/BookSystem.sqlite";
                        }
                        client.download(url, new java.io.File(getSDPath(),"BookSystem.sqlite"),new FTPDataTransferListener() {
							int sumLength=0;
							/**
							 * 执行进度
							 */
							@Override
							public void transferred(int length) {
								Message msg = new Message();
								msg.what = 2;
								sumLength+=length;
								msg.arg1=sumLength;
								h.sendMessage(msg);
							}
							/**
							 * 开始执行
							 */
							@Override
							public void started() {
								Message msg = new Message();
								msg.what = 1;
								h.sendMessage(msg);
							}
							/**
							 * 执行失败
							 */
							@Override
							public void failed() {
								Message msg = new Message();
								msg.what = 0;
								h.sendMessage(msg);
								if(callBack!=null){
									callBack.loseBack(activity.getString(R.string.update_handle_error));
								}
							}
							/**
							 * 执行成功
							 */
							@Override
							public void completed() {
								Message msg = new Message();
								msg.what = 3;
								h.sendMessage(msg);
							}
							/**
							 * 执行为完成
							 */
							@Override
							public void aborted() {
							}
						});
					} catch (FTPDataTransferException e) {
						if(callBack!=null){
							error(h);
							callBack.loseBack(activity.getString(R.string.ftp_transfer_error));
						}
					} catch (FTPAbortedException e) {
						if(callBack!=null){
							error(h);
							callBack.loseBack(activity.getString(R.string.ftp_suspend_error));
						}
					}
				} catch (IllegalStateException e) {
					if(callBack!=null){
						error(h);
						callBack.loseBack(activity.getString(R.string.ftp_update_state_error));
					}
				} catch (IOException e) {
					if(callBack!=null){
						error(h);
						callBack.loseBack(activity.getString(R.string.ftp_mutual_error));
					}
				} catch (FTPIllegalReplyException e) {
					if(callBack!=null){
						error(h);
						callBack.loseBack(activity.getString(R.string.ftp_msg_error));
					}
				} catch (FTPException e) {
					if(callBack!=null){
						error(h);
						callBack.loseBack(activity.getString(R.string.ftp_update_error));
					}
				}
			}
			public void error(Handler h){
				Message msg = new Message();
				msg.what = 0;
				h.sendMessage(msg);
			}
			@Override
			public void onHandleMessage(Message msg) {
				if(msg.what==2){//进行中
					getDialog(activity).setMessage(activity.getString(R.string.off_the_stocks)+msg.arg1+"...");
				}
				if(msg.what==0){//失败
					Toast.makeText(activity, R.string.down_error, Toast.LENGTH_LONG).show();
					getDialog(activity).dismiss();
				}
				if(msg.what==1){//开始
					getDialog(activity).setMessage(activity.getString(R.string.start_down));
					getDialog(activity).show();
				}
				if(msg.what==3){//完成
					getDialog(activity).dismiss();
					if(callBack!=null){
						callBack.runBack();
					}
				}

			}
		});
		sth.run();
	}
	public String getSDPath() {
		String dir="/data"+Environment.getDataDirectory().getAbsolutePath()+"/"+PACKAGE_NAME+"/databases";// 获取跟目录
		File f=new File(dir);
		if(!f.exists()){
			f.mkdirs();
		}
		File [] files = f.listFiles();
		if(files==null){
			return dir;
		}
		for(File file:files){
			if(file.getName().equalsIgnoreCase("BookSystem.sqlite")){
				file.delete();
			}
		}
		return dir;
	}
	public interface ICallBack{
		void runBack();
		void loseBack(String msg);
	}
}