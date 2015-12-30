package cn.com.choicesoft.view;


import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import cn.com.choicesoft.R;

public class LoadingDialog extends Dialog{
	
	private String mLoadingText = null;
	private Context mContext = null;
	private int mAnimId;
	private Animation mAnim = null;
	private ImageView mAnimImage = null;
	private TextView mTextView = null;
	
	public LoadingDialog(Context context, int theme, int animation, String msg) {
		super(context, theme);
		mContext = context;
		mLoadingText = msg;
		mAnimId = animation;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View v = inflater.inflate(R.layout.loading_dialog, null);
        mAnimImage = (ImageView) v.findViewById(R.id.img);
        mTextView = (TextView) v.findViewById(R.id.tipTextView);
        
        beginAnimatiom();
        showText();
        setCancelable(false);
        setContentView(v);
	}
	
	private void beginAnimatiom() {

//		if(mAnim == null) {
//			mAnim = AnimationUtils.loadAnimation(mContext, mAnimId);
//		}
		if(mAnimImage != null){
//			mAnimImage.startAnimation(mAnim);
	        AnimationDrawable animationDrawable = (AnimationDrawable) mAnimImage.getBackground();
	        animationDrawable.start();
		}
	}
	
	private void showText() {
		if(mTextView != null)
			mTextView.setText(mLoadingText);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		beginAnimatiom();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mAnimImage.clearAnimation();
	}

}
