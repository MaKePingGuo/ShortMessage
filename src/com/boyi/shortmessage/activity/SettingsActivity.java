package com.boyi.shortmessage.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.android.volley.Response.Listener;
import com.baidu.mobstat.StatService;
import com.boyi.shortmessage.Constants;
import com.boyi.shortmessage.R;
import com.boyi.shortmessage.ShortMessageApp;
import com.boyi.shortmessage.model.UpdateVersion;
import com.boyi.shortmessage.utils.AppUtils;
import com.boyi.shortmessage.utils.SPUtils;
import com.boyi.shortmessage.widget.CustomDialog;
import com.boyi.shortmessage.widget.DisplayDaySelectWindow;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initUI();
    }

    private void initUI() {
        ImageView iv = (ImageView) findViewById(R.id.titlebar_back);
        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SettingsActivity.this.finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.title_text);
        tv.setText(getResources().getString(R.string.app_settings));
        tv = (TextView) findViewById(R.id.account_right);
        tv.setText(ShortMessageApp.mUser.Mobile);
        tv = (TextView) findViewById(R.id.call_number);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtils.showDialConfirmDialog(SettingsActivity.this);
            }
        });
        tv = (TextView) findViewById(R.id.check_update_right);
        tv.setText("V" + AppUtils.getVersionName(SettingsActivity.this));

        final Button small = (Button) findViewById(R.id.small);
        final Button medium = (Button) findViewById(R.id.medium);
        final Button large = (Button) findViewById(R.id.large);
        small.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                small.setSelected(true);
                medium.setSelected(false);
                large.setSelected(false);
                SPUtils.setTextSize(SettingsActivity.this, SPUtils.TEXT_SIZE_SMALL);
                MainActivity.removeAllViews();
            }
        });
        medium.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                small.setSelected(false);
                medium.setSelected(true);
                large.setSelected(false);
                SPUtils.setTextSize(SettingsActivity.this, SPUtils.TEXT_SIZE_MEDIUM);
                MainActivity.removeAllViews();
            }
        });
        large.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                small.setSelected(false);
                medium.setSelected(false);
                large.setSelected(true);
                SPUtils.setTextSize(SettingsActivity.this, SPUtils.TEXT_SIZE_LARGE);
                MainActivity.removeAllViews();
            }
        });
        String size = SPUtils.getTextSize(SettingsActivity.this);
        if (size.equals(SPUtils.TEXT_SIZE_SMALL)) {
            small.setSelected(true);
        } else if (size.equals(SPUtils.TEXT_SIZE_MEDIUM)) {
            medium.setSelected(true);
        } else {
            large.setSelected(true);
        }
        final RelativeLayout week = (RelativeLayout) findViewById(R.id.display_day_dev);
        tv = (TextView) week.findViewById(R.id.display_day_right);
        tv.setText(SPUtils.getDayCount(SettingsActivity.this) == 14 ? "二周" : "一周");
        week.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {  
                //实例化SelectPicPopupWindow  
                DisplayDaySelectWindow menuWindow = new DisplayDaySelectWindow(
                        SettingsActivity.this, new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                TextView tv = (TextView) week.findViewById(R.id.display_day_right);
                                tv.setText(v.getId() == R.id.btn_one_week ? "一周" : "二周");
                                SPUtils.setDayCount(SettingsActivity.this,v.getId() == R.id.btn_one_week ? 7 : 14);
                            }
                        });
                //显示窗口  
                //设置layout在PopupWindow中显示的位置
                menuWindow.showAtLocation(
                        SettingsActivity.this.findViewById(R.id.display_day_dev),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
            }
        });
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.clear_cache_dev);
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SPUtils.clearAll(SettingsActivity.this);
                CustomDialog.showMessageDialog(SettingsActivity.this, "全部缓存已经清除");
                TextView tv = (TextView) week.findViewById(R.id.display_day_right);
                tv.setText("一周");
            }
        });
        rl = (RelativeLayout) findViewById(R.id.feedback_dev);
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, FeedbackActivity.class);
                SettingsActivity.this.startActivity(intent);
            }
        });
        rl = (RelativeLayout) findViewById(R.id.about_us_dev);
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                SettingsActivity.this.startActivity(intent);
            }
        });
        rl = (RelativeLayout) findViewById(R.id.check_update_dev);
        rl.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doCheckUpdate();
            }
        });
        Button btn = (Button) findViewById(R.id.logoff);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, QuickSearchActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                SettingsActivity.this.startActivity(intent);
                MainActivity.finishMainActivity();
                SettingsActivity.this.finish();
            }
        });
    }

    private void doCheckUpdate() {
        ShortMessageApp.doOperation(SettingsActivity.this,
                Constants.URL_GET_LATEST_VERSION,
                new Listener<String>() {
                    @Override
                    public void onResponse(String arg0) {
                        UpdateVersion version = null;
                        int destCode = 0;
                        long fileSize = 0;
                        try {
                            version = Constants.mGson.fromJson(arg0, UpdateVersion.class);
                            destCode = Integer.parseInt(version.VersionCode);
                            fileSize = Long.parseLong(version.FileSize);
                        } catch (Exception e) {
                            
                        }
                        if (version == null || AppUtils.getVersionCode(SettingsActivity.this) >= destCode) {
                            CustomDialog.showMessageDialog(SettingsActivity.this, "当前已是最新版本");
                        } else {
                            CustomDialog cd = new CustomDialog(SettingsActivity.this);
                            cd.setTitle("版本更新");
                            View v = (View) LayoutInflater.from(SettingsActivity.this).inflate(
                                    R.layout.app_have_new_version, null);
                            TextView tv = (TextView) v.findViewById(R.id.app_have_new_version_cur);
                            tv.setText(AppUtils.getVersionName(SettingsActivity.this));
                            tv = (TextView) v.findViewById(R.id.app_have_new_version_name);
                            tv.setText(version.VersionName);
                            tv = (TextView) v.findViewById(R.id.app_have_new_version_time);
                            tv.setText(version.PublishDate);
                            tv = (TextView) v.findViewById(R.id.app_have_new_version_size);
                            tv.setText(AppUtils.formetFileSize(fileSize, SettingsActivity.this));
                            tv = (TextView) v.findViewById(R.id.app_have_new_version_releasenote);
                            tv.setText(version.ChangeLog);
                            cd.setView(v);
                            final UpdateVersion ver = version;
                            cd.setPositiveButton("下载安装", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doDownloadApk(ver.Path);
//                                    doDownloadApk(URLEncoder.encode(ver.Path));
                                }
                            });
                            cd.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            cd.show();
                        }
                    }
                }, "正在检查更新");
    }

    private void doDownloadApk(String url) {
        DownloadTask download = new DownloadTask(url);
        download.execute("");
    }

    public class DownloadTask extends AsyncTask<String, String, Boolean> {
        private boolean mCancel = false;
        private String mUrl;
        private CustomDialog mCD = null;
        private File mFile;

        public DownloadTask(String url) {
            mUrl = url;
        }

        private InputStream getIs(String url_str) {
            HttpURLConnection conn;
            InputStream is = null;
            try {
                URL url = new URL(url_str);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(5 * 1000);
                is = conn.getInputStream();
            } catch (Exception e) {
            }
            return is;
        }

        @Override
        protected void onPreExecute() {
            mCD = CustomDialog.showLoading(SettingsActivity.this, "正在下载", new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    mCancel = true;
                    Log.d(Constants.TAG, "cancel");
                }
            });
        }

        @Override
        protected void onPostExecute(Boolean result) {
            mCD.dismiss();
            if (mCancel == false) {
                if (result) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);  
                    intent.setDataAndType(
                            Uri.fromFile(mFile),
                            "application/vnd.android.package-archive");  
                    SettingsActivity.this.startActivity(intent);  
                } else {
                    CustomDialog.showMessageDialog(SettingsActivity.this, "下载文件失败");
                }
            }
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean write_success = false;
            InputStream is = getIs(mUrl);
            mFile = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/apk.apk");
            mFile.deleteOnExit();
            FileOutputStream fos = null;
            try {
                mFile.createNewFile();
                fos = new FileOutputStream(mFile);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int i = 0;
                while ((i = bis.read(buffer)) > 0) {
                    if (mCancel) {
                        fos.flush();
                        fos.close();
                        bis.close();
                        is.close();
                        write_success = false;
                        return false;
                    }
                    fos.write(buffer, 0, i);
                }
                fos.flush();
                fos.close();
                bis.close();
                is.close();
                write_success = true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }
            return write_success;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        StatService.onPageStart(this, this.getClass().getSimpleName());
    }

    @Override
    public void onPause() {
        super.onPause();

        StatService.onPageEnd(this, this.getClass().getSimpleName());
    }
}
