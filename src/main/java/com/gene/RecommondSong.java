package com.gene;

public class RecommondSong {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    private String artists;

    @Override
    public String toString(){
        return  " id: " + id + "    Song: " + name + "    Artists: " + artists ;
    }
}
