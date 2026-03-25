package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {

    private String name;
    private Song[] songs;

    public Playlist(String name) {
        this.name = name;
        this.songs = new Song[0];
    }

    public String getName() {
        return name;
    }

    public void addSong(Song song) {
        Song[] newSongs = new Song[songs.length + 1];
        for (int i = 0; i < songs.length; i++) {
            newSongs[i] = songs[i];
        }
        newSongs[songs.length] = song;
        songs = newSongs;
    }

    public void printSortedByTitle() {
        Song[] copy = songs.clone();
        Arrays.sort(copy);
        for (Song s : copy) {
            System.out.println(s);
        }
    }

    public void printSortedByDuration() {
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator());
        for (Song s : copy) {
            System.out.println(s);
        }
    }

    public int getTotalDuration() {
        int sum = 0;
        for (Song s : songs) {
            sum += s.durationSeconds();
        }
        return sum;
    }
}