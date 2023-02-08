package com.cocoamu.ffmpeg.controller;

import com.cocoamu.ffmpeg.ffmpeg.FFmpegCallback;
import com.cocoamu.ffmpeg.ffmpeg.FFmpegCommandBuilder;
import com.cocoamu.ffmpeg.ffmpeg.FFmpegCommandException;
import com.cocoamu.ffmpeg.utils.FFmpegCommand;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class TestController {

//    protected static final ExecutorService callbackExecutor = Executors.newFixedThreadPool(50);

    @Autowired
    private FFmpegCallback fFmpegCallback ;
    protected static final ExecutorService callbackExecutor = Executors.newFixedThreadPool(50);

    @RequestMapping("test1")
    public String test() throws FFmpegCommandException {
        String ffmpegExePath = "/Users/key/Tool/ffmpeg";
        String inputFilePath = "/Users/key/Documents/Learning/尚硅谷Redis6视频课程/视频/01-尚硅谷-Redis6-课程介绍.mp4";
        String outputFilePath = "/Users/key/Downloads/ff.mp4";
        FFmpegCommandBuilder builder = new FFmpegCommandBuilder(ffmpegExePath);
        builder.input(inputFilePath).quality("32").bitrate("0.5M").minBitreate("0.5M").maxBitreate("1M").bufsize("1M").output(outputFilePath);
        FFmpegCommand command = builder.build();
        callbackExecutor.execute(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                command.start(fFmpegCallback);
            }
        });
        return "ok";
    }

//    /**
//     *
//     * @param inppath 文件路径 00/00/xxxx.mp4
//     * @param outppath 要转的文件名 例如 xxxx.flv
//     * @return
//     */
//    @RequestMapping("test1")
//    public String test1(@RequestParam(value = "inpath",required = false) String inppath,@RequestParam(value = "outpath",required = false) String outppath ){
//        try {
//            String videoInputPath = "xxxx.mp4";
//            String coverOutputPath = "xxxxx.flv";
//            if (inppath != null) {
//                videoInputPath = inppath;
//            }
//            if (outppath != null) {
//                coverOutputPath = outppath;
//            }
//            String finalVideoInputPath = videoInputPath;
//            String finalCoverOutputPath = coverOutputPath;
//            return FFMpegUtil.convetor(finalVideoInputPath, finalCoverOutputPath);
//            callbackExecutor.execute(new Runnable() {
//                @SneakyThrows
//                @Override
//                public void run() {
//                    FFMpegUtil.convetor(finalVideoInputPath, finalCoverOutputPath);
//                }
//            });
//        } catch (Exception e ) {
//            e.printStackTrace();
//        }
//        return  "转换完成";
//    }
}
