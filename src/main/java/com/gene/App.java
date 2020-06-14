package com.gene;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class App 
{
    private static final int SONG_NUM = 10;
    private static final String VALUE_PARAMS = "mX9cplDJw86EnpHkBIvZE3UcyfqU5gEtefZGKFg1Fw4nZSwiWCYnrRwio95A1BnaZFV84rDC7ko4Gb8g+ky5tkEduK0v5cLXtySjKHly/xe0mgNhxwES9nErCSlNPZ2nNdXLJIya6Emi85YOI5J6rxFXPNjLxIjLpDYLRVwkI6usmT7NiNSCQHa4aBdwB1tX46hhkkXTEN1MfWYqa+5E9VWOTT4eDcDLfJat/x1zdhk=";
    private static final String VALUE_ENCSECKEY = "8cc2704ada0806649f737ac789d91401909f9132fb20c00cb2237f7a42447c048cd27389a2e24d912eaad52278f4bf1bec059fb9387a4af563283e66e0cdb530183f7cab66cc494c752c10b96e840d7e3cc1262c983d172b1a9ad4e7a4cb22c1eb7f66547dc9340bb5b241822b515461e0953f48cebd2ca1c8139bef61f3f722";
    private static final String urlForRecomSong = "http://music.163.com/api/personalized/newsong";
    private static final String urlForHotComment = "https://music.163.com/weapi/v1/resource/hotcomments/R_SO_4_";

    private static RecommondSong[] songInfos = new RecommondSong[SONG_NUM];

    public static void main( String[] args )
    {
        Scanner sc = new Scanner(System.in);

        OkHttpClient okHttpClient = OkHttpManager.getInstance();
        Request request = new Request.Builder()
            .url(urlForRecomSong)
            .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseStr = response.body().string();      //获得json格式的response内容
            //下面对json进行解析
            JSONArray jsonArrays = JSONObject.parseArray(JSONObject.parseObject(responseStr).getString("result"));
            JSONObject object;
            for (int i = 0; i < jsonArrays.size(); i++) {
                object = jsonArrays.getJSONObject(i);
                RecommondSong songInfo = new RecommondSong();
                songInfo.setId(object.getLong("id"));
                songInfo.setName(object.getString("name"));
                object = JSONObject.parseObject(object.getString("song"));  //此时获得song内部的json
                JSONArray artists = JSONObject.parseArray(object.getString("artists"));  //此时获得artists里面的json数组
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < artists.size(); j++) {
                    object = (JSONObject) artists.get(j);
                    builder.append(object.getString("name") + "  ");
                }
                songInfo.setArtists(builder.toString());
                songInfos[i] = songInfo;
            }

            //输出推荐歌曲列表
            System.out.println(" 今日歌曲推荐：");
            for (int i = 0; i < songInfos.length; i++) {
                System.out.println("[ " + i + " ]" + songInfos[i].toString());
            }

            printHotComment(sc);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printHotComment(Scanner sc){
        int songIndex;
        while (true) {
            System.out.println();
            System.out.print("输入要查看热评的歌曲编号（其他输入视为退出）：");
            if(sc.hasNextInt()){
                songIndex = sc.nextInt();
                if (songIndex < 0 || songIndex > SONG_NUM - 1){
                    return;
                }
            }else {
                return;
            }
            System.out.println("______________________________________________________________");
            System.out.println();
            System.out.println(songInfos[songIndex].getName());
            System.out.println("   " + songInfos[songIndex].getArtists());
            System.out.println();
            System.out.println("______________________________________________________________");
            System.out.println("精彩评论:");
            System.out.println();

            List<CommentInfo> commentInfos = getHotComment(songInfos[songIndex].getId());
            for (CommentInfo com : commentInfos){
                System.out.println(com);
                System.out.println();
            }

            System.out.println("______________________________________________________________");
        }
    }

    private static List<CommentInfo> getHotComment(Long songId){
        List<CommentInfo> commentInfos = new ArrayList<>();
        OkHttpClient okHttpClient = OkHttpManager.getInstance();
        RequestBody body = new FormBody.Builder()
            .add("params", VALUE_PARAMS)
            .add("encSecKey", VALUE_ENCSECKEY)
            .build();
        Request request = new Request.Builder()
            .url(urlForHotComment + songId)
            .post(body)
            .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            String responseStr = response.body().string();      //获得json格式的response内容
            //下面对json进行解析
            JSONArray jsonArray = JSONObject.parseArray(JSONObject.parseObject(responseStr).getString("hotComments"));
            JSONObject object;
            for (int i = 0; i < jsonArray.size(); i++) {
                object = jsonArray.getJSONObject(i);
                CommentInfo commentInfo = new CommentInfo();
                commentInfo.setContent(object.getString("content"));
                commentInfo.setLikeCount(object.getString("likedCount"));
                object = object.getJSONObject("user");
                commentInfo.setNickname(object.getString("nickname"));
                commentInfos.add(commentInfo);
            }

            return commentInfos;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
