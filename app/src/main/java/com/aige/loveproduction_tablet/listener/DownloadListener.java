package com.aige.loveproduction_tablet.listener;

import java.io.File;

/**
 * Description：下载相关的接口
 */

public interface DownloadListener {
    void onStart();

    void onProgress(int currentLength);

    void onFinish(File file);

    void onFailure(String erroInfo);
}
