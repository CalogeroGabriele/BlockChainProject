package com.ITSecurity.BlockChainProject;

import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;
import java.util.*;


public class PubSub extends Thread{

    BlockChain blockChain;
    final String channel = "My_Channel";
    Jedis publisher;
    Jedis subscriber;
    static int flag = 0;

    public PubSub(BlockChain blockChain){
        this.blockChain = blockChain;
        this.publisher = new Jedis();
        this.subscriber = new Jedis();
        if(flag<1){
            redisPublish();
            flag++;
        }
        start();
    }

    //Stampa la blockchain sul canale
    public void redisPublish(){
        String listaBlocchi = new Gson().toJson(blockChain.getListaBlocchi());
        publisher.publish(channel,listaBlocchi);
    }

    @Override
    public void run(){
        subscriber.subscribe(new JedisPubSub(){
            @Override
            public void onMessage(String channel, String message){

                JSONArray jsonArray = new JSONArray(message);
                ArrayList<Block> myBlockList = new ArrayList<>();
                //La stringa in input viene trasforma in una ArrayList<Block> per poterla inserire nel metodo "replaceChain"
                for(int i=0;i<jsonArray.length();i++){
                    ArrayList<String> data = new ArrayList<>();
                    JSONObject jObj = jsonArray.getJSONObject(i);
                    Block block = new Block();
                    block.setTimestamp(jObj.getLong("timestamp"));
                    block.setLastHash(jObj.getString("lastHash"));
                    block.setHash(jObj.getString("hash"));
                    for(int j=0;j<jObj.getJSONArray("data").length();j++)
                        data.add(jObj.getJSONArray("data").getString(j));
                    String[] array = data.toArray(new String[0]);
                    block.setData(array);
                    block.setNonce(jObj.getInt("nonce"));
                    block.setDifficulty(jObj.getInt("difficulty"));
                    myBlockList.add(block);
                }
                blockChain.replaceChain(myBlockList,myBlockList.get(0));
            }
        }, channel);
    }
}
