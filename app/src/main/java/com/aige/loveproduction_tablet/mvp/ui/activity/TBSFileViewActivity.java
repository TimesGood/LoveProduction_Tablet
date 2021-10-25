package com.aige.loveproduction_tablet.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aige.loveproduction_tablet.R;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;

/**
 * 此 Activity 使用 TBS （腾讯浏览服务）查看文件
 * <br/> 默认支持常见文件类型
 * <br/> <b>默认支持类型：</b>
 * <li>doc</li>
 * <li>docx</li>
 * <li>ppt</li>
 * <li>pptx</li>
 * <li>xls</li>
 * <li>xlsx</li>
 * <li>txt</li>
 * <li>pdf</li>
 * <li>epub</li>
 * <br/>
 * intent参数: filePath - 文件路径
 *
 * @author hanlyjiang
 */
public class TBSFileViewActivity extends AppCompatActivity implements TbsReaderView.ReaderCallback {
    public static final String FILE_PATH = "filePath";

    private static final String TAG = "TBSFileViewActivity";

    private TbsReaderView mTbsReaderView;
    private FrameLayout rootViewParent;

    private ViewGroup errorHandleLayout;
    private String filePath;

    //跳转到当前界面
    public static void viewFile(Context context, String localPath) {
        Intent intent = new Intent(context, TBSFileViewActivity.class);
        intent.putExtra(FILE_PATH, localPath);
        context.startActivity(intent);
    }

    public static String getFileName(String filePath) {
        if (filePath == null) {
            return "";
        }
        int lastSlashIndex = filePath.lastIndexOf("/") + 1;
        if (lastSlashIndex == -1) {
            return filePath;
        }
        int lastDotFromSlashIndex = filePath.indexOf(".", lastSlashIndex);
        if (lastDotFromSlashIndex == -1) {
            return filePath.substring(lastSlashIndex);
        }
        return filePath.substring(lastSlashIndex, lastDotFromSlashIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tbs_file_view_layout);
        //碎片界面
        rootViewParent = (FrameLayout) findViewById(R.id.fl_rootview);
        //错误界面
        errorHandleLayout = (ViewGroup) findViewById(R.id.ll_error_handle);
        initErrorHandleLayout(errorHandleLayout);

        filePath = handleIntent();
        if (TextUtils.isEmpty(filePath) || !new File(filePath).isFile()) {
            Toast.makeText(this, "文件不存在", Toast.LENGTH_SHORT).show();
            finish();
        }
        //文件展示，与adapter类似
        mTbsReaderView = new TbsReaderView(this, this);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        );
        mTbsReaderView.setLayoutParams(layoutParams);
        rootViewParent.addView(mTbsReaderView);
        displayFile(filePath);
    }

    private void initErrorHandleLayout(ViewGroup errorHandleLayout) {
        findViewById(R.id.btn_retry_with_tbs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重试，重新加载文件
                displayFile(filePath);
            }
        });
        findViewById(R.id.btn_view_with_other_app).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //使用其他应用打开文件
                //FileViewerUtils.viewFile4_4(v.getContext(), );
            }
        });
    }

    //取得页面回传回来的文件路径
    private String handleIntent() {
        if (getIntent() != null) {
            return getIntent().getStringExtra(FILE_PATH);
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCallBackAction(Integer integer, Object long1, Object long2) {
        Log.d(TAG, "onCallBackAction " + integer + "," + long1 + "," + long2);
    }

    //打开文件显示文件
    private void displayFile(String fileAbsPath) {
        Bundle bundle = new Bundle();
        bundle.putString("filePath", fileAbsPath);
        bundle.putString("tempPath", Environment.getExternalStorageDirectory().getPath());
        // preOpen 需要文件后缀名 用以判断是否支持打开该文件
        boolean result = mTbsReaderView.preOpen(parseFormat(fileAbsPath), true);
        if (result) {
            mTbsReaderView.openFile(bundle);
            mTbsReaderView.setVisibility(View.VISIBLE);
            errorHandleLayout.setVisibility(View.GONE);
        } else {
            mTbsReaderView.setVisibility(View.GONE);
            errorHandleLayout.setVisibility(View.VISIBLE);
        }
    }

    //获取文件后缀名
    private String parseFormat(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTbsReaderView.onStop();
    }
}
