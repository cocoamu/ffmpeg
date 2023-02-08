package com.cocoamu.ffmpeg;

import com.cocoamu.ffmpeg.ffmpeg.FFmpegCallback;
import com.cocoamu.ffmpeg.ffmpeg.FFmpegCommandBuilder;
import com.cocoamu.ffmpeg.ffmpeg.FFmpegCommandException;
import com.cocoamu.ffmpeg.ffmpeg.FFmpegCommandFormatEnum;
import com.cocoamu.ffmpeg.utils.FFmpegCommand;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FFmpegBuilderTest {

    @Autowired
    private FFmpegCallback fFmpegCallback ;

    protected static final ExecutorService callbackExecutor = Executors.newFixedThreadPool(50);

    @Test
   public void image() throws FFmpegCommandException {
        String ffmpegExePath = "/Users/key/Tool/ffmpeg";
        String inputFilePath = "/Users/key/Documents/Learning/尚硅谷Redis6视频课程/视频/18-尚硅谷-Redis6-Jedis操作-测试.mp4";
        String outputFilePath = "/Users/key/Downloads/ff.jpg";
        FFmpegCommandBuilder builder = new FFmpegCommandBuilder(ffmpegExePath);
        builder.input(inputFilePath).format(FFmpegCommandFormatEnum.IMAGE)
                .position("10").duration("0.001").size("320*240").output(outputFilePath);
        FFmpegCommand command = builder.build();
        try {
            String result = command.start(null);
            System.out.println(result);
        } catch (FFmpegCommandException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void convert() throws FFmpegCommandException {
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
    }
}
