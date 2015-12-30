package cn.com.choicesoft.activity;

import android.content.Intent;
import android.os.Bundle;
import cn.com.choicesoft.chinese.activity.ChineseWelcomeActivity;
import cn.com.choicesoft.util.IsPad;
import cn.com.choicesoft.util.SharedPreferencesUtils;

/**
 * 中快餐中转Activity
 * Created by M.c on 2014/6/10.
 */
public class ChioceActivity extends BaseActivity {
	//是否是pad
    public  static boolean ispad;

    /**
     * 中快餐中转Activity
     * @param savedInstanceState
     */
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
        ispad= IsPad.isPad(getApplicationContext());
        if(SharedPreferencesUtils.getChineseSnack(this)==0){
            startActivity(new Intent(this,WelcomeActivity.class));
        }else{
            startActivity(new Intent(this, ChineseWelcomeActivity.class));
        }
    }
}