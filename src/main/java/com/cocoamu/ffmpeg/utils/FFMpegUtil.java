package com.cocoamu.ffmpeg.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FFMpegUtil {

    //Linux与mac下
    private static String FFMPEG_LINUX;
    //Linux与mac下
    private static String VIDEO_SOURCE_PATH_LINUX;

    private static String VIDEO_OUT_PATH_LINUX;



    //Linux与mac下  ffmpeg的路径
    private static String FFMPEG_WINDOWS;

    private static String VIDEO_SOURCE_PATH_WINDOWS;

    private static String VIDEO_OUT_PATH_WINDOWS;



    @Value("${ffmpeg.linux}")
    public void setFfmpegLinux(String ffmpegLinux) {
        FFMPEG_LINUX = ffmpegLinux;
    }

    @Value("${ffmpeg.linux.video_source_path}")
    public void setVideoSourcePathLinux(String videoSourcePathLinux) {
        VIDEO_SOURCE_PATH_LINUX = videoSourcePathLinux;
    }

    @Value("${ffmpeg.linux.video_out_path}")
    public void setVideoOutPathLinux(String videoOutPathLinux) {
        VIDEO_OUT_PATH_LINUX = videoOutPathLinux;
    }

    @Value("${ffmpeg.windows}")
    public void setFfmpegWindows(String ffmpegWindows) {
        FFMPEG_WINDOWS = ffmpegWindows;
    }

    @Value("${ffmpeg.windows.video_source_path}")
    public void setVideoSourcePathWindows(String videoSourcePathWindows) {
        VIDEO_SOURCE_PATH_WINDOWS = videoSourcePathWindows;
    }

    @Value("${ffmpeg.windows.video_out_path}")
    public void setVideoOutPathWindows(String videoOutPathWindows) {
        VIDEO_OUT_PATH_WINDOWS = videoOutPathWindows;
    }

    public static String convetor(String inFileName, String outFileName) throws Exception {
        String os = System.getProperty("os.name");
        if (os != null && os.toLowerCase().startsWith("windows")) {
            convetorWindows(inFileName,outFileName);
            return VIDEO_OUT_PATH_WINDOWS + outFileName;
        } else if (os != null && os.toLowerCase().startsWith("linux")) {//Linux操作系统
            convetorLinux(inFileName,outFileName);
            return VIDEO_OUT_PATH_LINUX + outFileName;
        } else { //其它操作系统

        }
        return "";
    }

    /**
     * @param inFileName 视频的输入路径 linux真实路径
     * @param outFileName   视频的输出路径 linux真实路径
     * @throws Exception
     */
    // 拷贝视频，并指定新的视频的名字以及格式
    // ffmpeg.exe -i old.mp4 new.avi
    public static void convetorLinux(String inFileName, String outFileName) throws Exception {
        String videoCommend = FFMPEG_LINUX + " -i " + VIDEO_SOURCE_PATH_LINUX + inFileName + " -qscale 6 -ab 64 -ac 2 -ar 22050 -r 24 -y " + VIDEO_OUT_PATH_LINUX + outFileName;
        log.info(videoCommend);
        try {
            Runtime rt = Runtime.getRuntime();
            Process process = rt.exec(new String[]{"sh", "-c", videoCommend});
            dealStream(process);
            process.waitFor();
            log.info("执行完成 ");
        } catch (Exception t) {
            t.printStackTrace();
        }
    }

    public static void convetorWindows(String videoInputPath, String videoOutPath) throws Exception {
        // 创建一个List集合来保存转换视频文件为flv格式的命令
        List convert = new ArrayList();
        convert.add(FFMPEG_WINDOWS); // 添加转换工具路径
        convert.add("-i"); // 添加参数＂-i＂，该参数指定要转换的文件
        convert.add(VIDEO_SOURCE_PATH_WINDOWS + "\\" + videoInputPath); // 添加要转换格式的视频文件的路径
        convert.add("-qscale");     //指定转换的质量
        convert.add("6");
        convert.add("-ab");        //设置音频码率
        convert.add("64");
        convert.add("-ac");        //设置声道数
        convert.add("2");
        convert.add("-ar");        //设置声音的采样频率
        convert.add("22050");
        convert.add("-r");        //设置帧频
        convert.add("24");
        convert.add("-y"); // 添加参数＂-y＂，该参数指定将覆盖已存在的文件
        convert.add(VIDEO_OUT_PATH_WINDOWS+"\\"+videoOutPath);
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(convert);
        builder.redirectErrorStream(true);
        Process process = null;
        InputStream errorStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader br = null;
        try {
            process = builder.start();
            // 使用这种方式会在瞬间大量消耗CPU和内存等系统资源，所以这里我们需要对流进行处理
            dealStream(process);
            process.waitFor();
            System.out.println("ffmpeg.exe运行完毕！");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                br.close();
            }
            if (inputStreamReader != null) {
                inputStreamReader.close();
            }
            if (errorStream != null) {
                errorStream.close();
            }
        }
    }

    /**
     * 处理process输出流和错误流，防止进程阻塞
     * 在process.waitFor();前调用
     * @param process
     */
    private static void dealStream(Process process) {
        if (process == null) {
            log.info("process is null");
            return;
        }
        // 处理InputStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line = null;
                try {
                    while ((line = in.readLine()) != null) {
                        log.info("output: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        // 处理ErrorStream的线程
        new Thread() {
            @Override
            public void run() {
                BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                String line = null;
                try {
                    while ((line = err.readLine()) != null) {
                        log.info("err: " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        err.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
