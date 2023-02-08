package com.cocoamu.ffmpeg.ffmpeg;

import com.cocoamu.ffmpeg.utils.FFmpegCommand;

import java.util.ArrayList;
import java.util.List;

public class FFmpegCommandBuilder {
    List<String> command = new ArrayList<String>();


    public FFmpegCommandBuilder(String exePath) throws FFmpegCommandException {
        if (exePath == null) {
            throw new FFmpegCommandException("ffmpeg.exe路径不得为空");
        }
        //添加命令的exe执行文件位置
        command.add(exePath);
    }


    /**
     * 添加输入文件的路径
     *
     * @param inputFilePath
     */
    public FFmpegCommandBuilder input(String inputFilePath) {
        if (inputFilePath != null) {
            command.add("-i");
            command.add(inputFilePath);
        }
        return this;

    }

    /**
     * 添加输出文件的路径
     *
     * @param outputFilePath
     */
    public FFmpegCommandBuilder output(String outputFilePath) {
        if (outputFilePath != null) {
            command.add(outputFilePath);
        }
        return this;

    }


    /**
     * 覆盖输出文件
     */
    public FFmpegCommandBuilder override() {
        command.add("-y");
        return this;
    }


    /**
     * 强制输出格式
     *
     * @param format 输出格式
     */
    public FFmpegCommandBuilder format(FFmpegCommandFormatEnum format) {
        if (format != null) {
            command.add("-f");
            command.add(format.getValue());
        }
        return this;
    }


    /**
     * 设置录制/转码的时长
     *
     * @param duration 形如 0.001 表示0.001秒，hh:mm:ss[.xxx]格式的记录时间也支持
     */

    public FFmpegCommandBuilder duration(String duration) {
        if (duration != null) {
            command.add("-t");
            command.add(duration);
        }
        return this;
    }


    /**
     * 搜索到指定的起始时间
     *
     * @param position 形如 17 表示17秒，[-]hh:mm:ss[.xxx]的格式也支持
     */

    public FFmpegCommandBuilder position(String position) {
        if (position != null) {
            command.add("-ss");
            command.add(position);
        }
        return this;
    }


    /**
     * 设置帧大小
     *
     * @param size 形如 xxx*xxx
     * @return
     */
    public FFmpegCommandBuilder size(String size) {
        if (size != null) {
            command.add("-s");
            command.add(size);
        }
        return this;
    }

    public FFmpegCommandBuilder quality(String quality){
        if (quality != null) {
            command.add("-crf");
            command.add(quality);
        }
        return this;
    }

    public FFmpegCommandBuilder bitrate(String bitrate){
        if (bitrate != null) {
            command.add("-b");
            command.add(bitrate);
        }
        return this;
    }

    public FFmpegCommandBuilder minBitreate(String minBitreate){
        if (minBitreate != null) {
            command.add("-minrate");
            command.add(minBitreate);
        }
        return this;
    }

    public FFmpegCommandBuilder maxBitreate(String maxBitreate){
        if (maxBitreate != null) {
            command.add("-maxrate");
            command.add(maxBitreate);
        }
        return this;
    }

    public FFmpegCommandBuilder bufsize(String bufsize){
        if (bufsize != null) {
            command.add("-bufsize");
            command.add(bufsize);
        }
        return this;
    }




    /**
     * 创建FFmpegCommand命令封装类
     *
     * @return FFmpegCommand
     */
    public FFmpegCommand build() {
        return new FFmpegCommand(command);
    }

}
