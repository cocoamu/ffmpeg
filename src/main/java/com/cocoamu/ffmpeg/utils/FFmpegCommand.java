package com.cocoamu.ffmpeg.utils;

import com.cocoamu.ffmpeg.ffmpeg.FFmpegCallback;
import com.cocoamu.ffmpeg.ffmpeg.FFmpegCommandException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * FFmpeg命令的封装类
 */

public class FFmpegCommand {

    private List<String> command;

    public FFmpegCommand(List<String> command) {
        this.command = command == null ? new ArrayList<String>() : command;
    }

    public List<String> getCommand() {
        return command;
    }

    public void setCommand(List<String> command) {
        this.command = command;
    }


    /**
     * 开始执行命令
     *
     * @param callback 回调
     * @return 命令的信息输出
     * @throws FFmpegCommandException
     */

    public String start(FFmpegCallback callback) throws FFmpegCommandException {
        BufferedReader br = null;
        StringBuffer sbf = new StringBuffer();
        String resultInfo = null;
        try {
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(command);
            //正常信息和错误信息合并输出
            builder.redirectErrorStream(true);
            //开启执行子线程
            Process process = builder.start();
            String line = null;
            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while ((line = br.readLine()) != null) {
                sbf.append(line);
                sbf.append(" ");
            }
            resultInfo = sbf.toString();
            //等待命令子线程执行完成
            int exitValue = process.waitFor();
            //完成后执行回调
            if (exitValue == 0 && callback != null) {
                callback.complete(resultInfo);
            }
            //销毁子线程
            process.destroy();
        } catch (IOException e) {
            e.printStackTrace();
            throw new FFmpegCommandException(e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new FFmpegCommandException("线程阻塞异常:" + e.getMessage());
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return resultInfo;
    }

}
