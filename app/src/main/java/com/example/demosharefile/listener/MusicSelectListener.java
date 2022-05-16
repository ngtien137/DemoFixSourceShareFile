package com.example.demosharefile.listener;

import com.example.demosharefile.model.Music;

import java.util.List;

public interface MusicSelectListener {
    void playQueue(List<Music> musicList);

    void addToQueue(List<Music> music);

    void setShuffleMode(boolean mode);
}
