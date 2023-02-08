package com.cocoamu.ffmpeg.ffmpeg;

import org.springframework.stereotype.Service;

@Service
public class FFmpegCallbackImpl implements FFmpegCallback {
    @Override
    public void complete(String resultInfo) {
        System.out.println("转换完成：" + resultInfo);
    }
}
