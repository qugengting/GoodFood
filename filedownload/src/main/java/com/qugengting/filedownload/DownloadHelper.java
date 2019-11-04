package com.qugengting.filedownload;

import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:xuruibin
 * @date:2019/10/28 Description:
 */
public class DownloadHelper {
    // 多任务下载
    private FileDownloadListener downloadListener;

    private DownloadCallback mCallBack;

    private int number;

    public DownloadHelper(DownloadCallback callback) {
        this.mCallBack = callback;
    }

    //路径是/sdcard/Android/data/com.qugengting.goodfood/cache/
    public String mSaveFolder = FileDownloadUtils.getDefaultSaveRootPath() + File.separator;

    public interface DownloadCallback {
        /**
         * 文件下载成功
         */
        void success();

        /**
         * 文件下载失败
         * @param msg 失败提示
         */
        void failed(String msg);
    }

    public FileDownloadListener createLis() {
        return new FileDownloadSampleListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                Log.d("feifei", "pending taskId:" + task.getId() + ",fileName:" + task.getFilename() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                Log.d("feifei", "progress taskId:" + task.getId() + ",fileName:" + task.getFilename() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes + ",speed:" + task.getSpeed());
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                number--;
                if (number == 0) {
                    mCallBack.success();
                }
                Log.d("feifei", "blockComplete taskId:" + task.getId() + ",filePath:" + task.getPath() + ",fileName:" + task.getFilename() + ",speed:" + task.getSpeed() + ",isReuse:" + task.reuse());
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                number--;
                if (number == 0) {
                    mCallBack.success();
                }
                Log.d("feifei", "completed taskId:" + task.getId() + ",isReuse:" + task.reuse());
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                Log.d("feifei", "paused taskId:" + task.getId() + ",soFarBytes:" + soFarBytes + ",totalBytes:" + totalBytes + ",percent:" + soFarBytes * 1.0 / totalBytes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                stop_multi();
                mCallBack.failed("下载失败");
                Log.d("feifei", "error taskId:" + task.getId() + ",e:" + e.getLocalizedMessage());
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                if (task.getListener() != downloadListener) {
                    return;
                }
                Log.d("feifei", "warn taskId:" + task.getId());
            }
        };
    }

    /**
     * 开始多任务下载
     *
     * @param urlList  下载连接集合
     * @param nameList 下载到本地保存的文件名称集合（不包含完整路径）
     */
    public void startMultiTask(List<String> urlList, List<String> nameList) {
        if (urlList == null || nameList == null || urlList.size() != nameList.size() || urlList.size() < 1) {
            mCallBack.failed("下载参数为空或参数有错");
            return;
        }
        number = nameList.size();
        downloadListener = createLis();
        //(1) 创建 FileDownloadQueueSet
        final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);

        //(2) 创建Task 队列
        final List<BaseDownloadTask> tasks = new ArrayList<>();
        for (int i = 0; i < urlList.size(); i++) {
            BaseDownloadTask task = FileDownloader.getImpl().create(urlList.get(i))
                    .setPath(mSaveFolder + nameList.get(i), false);
            tasks.add(task);
        }
        //(3) 设置参数

        // 每个任务的进度 无回调
        //queueSet.disableCallbackProgressTimes();
        // do not want each task's download progress's callback,we just consider which task will completed.

        queueSet.setCallbackProgressTimes(100);
        queueSet.setCallbackProgressMinInterval(100);
        //失败 重试次数
        queueSet.setAutoRetryTimes(3);

        //避免掉帧
        FileDownloader.enableAvoidDropFrame();

        //(4)串行下载
        queueSet.downloadSequentially(tasks);

        //(5)任务启动
        queueSet.start();
    }

    public void stop_multi() {
        FileDownloader.getImpl().pause(downloadListener);
    }

    public void deleteAllFile() {

        //清除所有的下载任务
        FileDownloader.getImpl().clearAllTaskData();

        //清除所有下载的文件
        int count = 0;
        File file = new File(FileDownloadUtils.getDefaultSaveRootPath());
        do {
            if (!file.exists()) {
                break;
            }

            if (!file.isDirectory()) {
                break;
            }

            File[] files = file.listFiles();

            if (files == null) {
                break;
            }

            for (File file1 : files) {
                count++;
                file1.delete();
            }

        } while (false);
    }

}
