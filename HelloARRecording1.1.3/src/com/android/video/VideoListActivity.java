	package com.android.video;

import java.io.File;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.android.video.widget.MediaHelp;
import com.android.video.widget.VideoSuperPlayer;
import com.android.video.widget.VideoSuperPlayer.VideoPlayCallbackImpl;
import com.example.helloarrecording.MainActivity;
import com.example.helloarrecording.R;
import com.example.helloarrecording.R.string;
import com.lidroid.xutils.view.annotation.event.OnItemLongClick;

public class VideoListActivity extends Activity  {
	
	private String url = "/sdcard/arvideo";
	private List<VideoBean> mList;
	private ListView mListView;
	private boolean isPlaying;
	private int indexPostion = -1;
	private MAdapter mAdapter;
	private File[] listFiles;
 //   private Button shareBtn;    
	private ImageView vbackIV;
	private SelectPopupWindow shareSelectPopupWindow;
	private LinearLayout linearLayout;
    private static File fileurl = null;
    
	@Override
	protected void onDestroy() {
		MediaHelp.release();
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		MediaHelp.resume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		MediaHelp.pause();
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		MediaHelp.getInstance().seekTo(data.getIntExtra("position", 0));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		vbackIV = (ImageView) findViewById(R.id.v_back_iv);
		vbackIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(VideoListActivity.this,MainActivity.class);
				startActivity(intent);
			}
		});
 
		linearLayout = (LinearLayout) findViewById(R.id.videolayout);
		
		mListView = (ListView) findViewById(R.id.list);
		mList = new ArrayList<VideoBean>();
		File file = new File(url);
		listFiles = file.listFiles();
		TextView tv_videoName=(TextView) findViewById(R.id.tv_start);
		for (int i = 0; i < listFiles.length; i++) {	
			 mList.add( new VideoBean(listFiles[i].getAbsolutePath()));
		}
		
		mAdapter = new MAdapter(this);
		mListView.setAdapter(mAdapter);
		initPop();
//�����¼�
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(VideoListActivity.this);
				builder.setMessage("ȷ��ɾ����");
				builder.setTitle("��ʾ");
				//���AlertDialog.Builder�����setPositiveButton()����  
				builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if (mList.remove(position)!=null) {
							System.out.println("success");
							File delFile = listFiles[position].getAbsoluteFile();
							delFile.delete();
	
						}else {
							System.out.println("failed");
						}
						mAdapter.notifyDataSetChanged();
						Toast.makeText(getBaseContext(), "��ɾ��", Toast.LENGTH_SHORT).show();
					}
				});
			builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			builder.create().show();
			return false;
			}
		});
		
	


//����¼�
//		mListView.setOnItemClickListener(new OnItemClickListener(){
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
		//					int position, long id) {//				  
//		                 OnekeyShare oks = new OnekeyShare();
//		                 //�ر�sso��Ȩ
//		                 oks.disableSSOWhenAuthorize();
//		                 
//						//
//		                 oks.setFilePath(listFiles[position].getAbsolutePath());
//		                 // title���⣬ӡ��ʼǡ����䡢��Ϣ��΢�š���������QQ�ռ��ʹ��
//		                 oks.setTitle("�ҵ���Ƶ");
//		                 // titleUrl�Ǳ�����������ӣ�QQ��QQ�ռ��ʹ��
//		               //  oks.setTitleUrl("http://115.159.220.110/ar_project/video/701.mp4");
//		                 // text�Ƿ����ı�������ƽ̨����Ҫ����ֶ�
//		                 oks.setText("�����ı�����������Ƶ����ҿ���Χ�ۣ�����");
//		                 // imagePath��ͼƬ�ı���·����Linked-In�����ƽ̨��֧�ִ˲���
//		                // oks.setImagePath("/pictures/screenshots/s70508-152757.jpg");//ȷ��SDcard������ڴ���ͼƬ
//		                 // url����΢�ţ��������Ѻ�����Ȧ����ʹ��
//		                // oks.setUrl("http://115.159.220.110/ar_project/video/701.mp4");
//		                 // comment���Ҷ�������������ۣ�������������QQ�ռ�ʹ��
//		               //  oks.setComment("���ǲ��������ı�");
//		                 // site�Ƿ�������ݵ���վ���ƣ�����QQ�ռ�ʹ��
//		                 oks.setSite(getString(R.string.app_name));
//		                 // siteUrl�Ƿ�������ݵ���վ��ַ������QQ�ռ�ʹ��
//		                // oks.setSiteUrl("http://115.159.220.110/ar_project/video/701.mp4");
//		                 //
//		                 oks.setVideoUrl(listFiles[position].getName());
//		                 // ��������GUI
//		                 oks.show(VideoListActivity.this);
//				
//			}	
//		});
		
		
    
        
		mListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if ((indexPostion < mListView.getFirstVisiblePosition() || indexPostion > mListView
						.getLastVisiblePosition()) && isPlaying) {
					indexPostion = -1;
					isPlaying = false;
					mAdapter.notifyDataSetChanged();
					MediaHelp.release();
				}
			}
		});
	}
	
	 // popupWindow ����¼�����
    private View.OnClickListener selectItemsOnClick = new View.OnClickListener() {
        public void onClick(View v) {
        	//File file = new File(listFiles[0].getAbsolutePath());
        	 
            switch (v.getId()) {
                //����popupWindow �����ļ��е�id ��ִ����Ӧ�ĵ���¼�
                case R.id.ll_share_qq:
                	Intent share = new Intent(Intent.ACTION_SEND);
            		ComponentName component = new ComponentName("com.tencent.mobileqq",
            				"com.tencent.mobileqq.activity.JumpActivity");
            		share.setComponent(component);
            		//File file = new File(listFiles[0].getAbsolutePath());
            		System.out.println("file " + fileurl.exists());
            		share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileurl));
            		share.setType("*/*");
            		startActivity(Intent.createChooser(share, "����"));
                    break;
                case R.id.ll_share_wechat:
                	Intent intent = new Intent(); 
            	    ComponentName componentName = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"); 
            	    intent.setComponent(componentName); 
            	    intent.setAction(Intent.ACTION_SEND); 
            	    intent.setType("video/*"); 
            	    intent.putExtra(Intent.EXTRA_TEXT, "����΢��"); 
            	   // File file1 = new File(listFiles[0].getAbsolutePath());
            	    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(fileurl)); 
            	    startActivity(intent);
                    break;
            }
            //ÿ�ε��popupWindow�е����ⰴť���ǵùرմ�popupWindow��
            shareSelectPopupWindow.dismiss();
        }
    };

	
	  private void initPop() {
		// TODO Auto-generated method stub
		  mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					 fileurl = new File(listFiles[position].getAbsolutePath());
				    shareSelectPopupWindow = new SelectPopupWindow(VideoListActivity.this, selectItemsOnClick);
	                // ����popupWindow��ʾ��λ��
	                // ��ʱ���ڽ���ײ�����ˮƽ����
	                shareSelectPopupWindow.showAtLocation(linearLayout,
	                        Gravity.BOTTOM| Gravity.CENTER_HORIZONTAL, 0, 0);
	                // ��popupWindow ���ֵ�ʱ�� ��Ļ��͸����  ����Ϊ0.5 ����͸�� ��ɫЧ��
	                backgroundAlpha(0.5f);
	                // ����popupWindowȡ���ĵ���¼�����popupWindow��ʧ����Ļ��͸���ȣ�ȫ͸�����ͻظ�ԭ״̬
	                shareSelectPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
	                    @Override
	                    public void onDismiss() {
	                        backgroundAlpha(1f);
	                    }
	                });
				}
			});			
	}

	/**
     * ���������Ļ�ı���͸����
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }
	
	class MAdapter extends BaseAdapter {
		private Context context;
		LayoutInflater inflater;

		public MAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		@Override
		public VideoBean getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public View getView(final int position, View v, ViewGroup parent) {
			GameVideoViewHolder holder = null;
			if (v == null) {
				holder = new GameVideoViewHolder();
				v = inflater.inflate(R.layout.list_video_item, parent, false);
				holder.mVideoViewLayout = (VideoSuperPlayer) v
						.findViewById(R.id.video);
				holder.mPlayBtnView = (ImageView) v.findViewById(R.id.play_btn);
				TextView tv_videoName=(TextView) v.findViewById(R.id.tv_start);
				tv_videoName.setText(listFiles[position].getName());
				v.setTag(holder);
			} else {
				holder = (GameVideoViewHolder) v.getTag();
			}
			holder.mPlayBtnView.setOnClickListener(new MyOnclick(
					holder.mPlayBtnView, holder.mVideoViewLayout, position));
			if (indexPostion == position) {
				holder.mVideoViewLayout.setVisibility(View.VISIBLE);
			} else {
				holder.mVideoViewLayout.setVisibility(View.GONE);
				holder.mVideoViewLayout.close();
			}
			return v;
		}

		class MyOnclick implements OnClickListener {
			VideoSuperPlayer mSuperVideoPlayer;
			ImageView mPlayBtnView;
			int position;

			public MyOnclick(ImageView mPlayBtnView,
					VideoSuperPlayer mSuperVideoPlayer, int position) {
				this.position = position;
				this.mSuperVideoPlayer = mSuperVideoPlayer;
				this.mPlayBtnView = mPlayBtnView;
			}

			@Override
			public void onClick(View v) {
				MediaHelp.release();
				indexPostion = position;
				isPlaying = true;
				mSuperVideoPlayer.setVisibility(View.VISIBLE);
				mSuperVideoPlayer.loadAndPlay(MediaHelp.getInstance(), mList
						.get(position).getUrl(), 0, false);
				mSuperVideoPlayer.setVideoPlayCallback(new MyVideoPlayCallback(
						mPlayBtnView, mSuperVideoPlayer, mList.get(position)));
				notifyDataSetChanged();
			}
		}

		class MyVideoPlayCallback implements VideoPlayCallbackImpl {
			ImageView mPlayBtnView;
			VideoSuperPlayer mSuperVideoPlayer;
			VideoBean info;

			public MyVideoPlayCallback(ImageView mPlayBtnView,
					VideoSuperPlayer mSuperVideoPlayer, VideoBean info) {
				this.mPlayBtnView = mPlayBtnView;
				this.info = info;
				this.mSuperVideoPlayer = mSuperVideoPlayer;
			}

			@Override
			public void onCloseVideo() {
				closeVideo();
			}

			@Override
			public void onSwitchPageType() {
				if (((Activity) context).getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
					Intent intent = new Intent(new Intent(context,
							FullVideoActivity.class));
					intent.putExtra("video", info);
					intent.putExtra("position",
							mSuperVideoPlayer.getCurrentPosition());
					((Activity) context).startActivityForResult(intent, 1);
				}
			}

			@Override
			public void onPlayFinish() {
				closeVideo();
			}

			private void closeVideo() {
				isPlaying = false;
				indexPostion = -1;
				mSuperVideoPlayer.close();
				MediaHelp.release();
				mPlayBtnView.setVisibility(View.VISIBLE);
				mSuperVideoPlayer.setVisibility(View.GONE);
			}

		}

		class GameVideoViewHolder {

			private VideoSuperPlayer mVideoViewLayout;
			private ImageView mPlayBtnView;

		}

	}


}
