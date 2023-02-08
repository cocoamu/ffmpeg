package com.cocoamu.ffmpeg.ffmpeg;

public enum FFmpegCommandFormatEnum {
    IMAGE("image2");

    FFmpegCommandFormatEnum(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
