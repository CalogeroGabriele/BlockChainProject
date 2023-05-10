package com.ITSecurity.BlockChainProject;

import com.google.gson.Gson;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class APIs {

    BlockChain blockChain;
    PubSub pubSub;

    public APIs(BlockChain blockChain){
        this.blockChain = blockChain;
        this.blockChain.getGenesisBlock().setData(new String[]{"hello"});
        this.pubSub = new PubSub(blockChain);
    }

    @GetMapping("/")
    public String landingPage(){
        return ("Placeholder");
    }

    @GetMapping("/api/blocks")
    public String getBlockChain(){
         return  new Gson().toJson(blockChain.getListaBlocchi());
    }

    @PostMapping("/api/mine")
    public void mineBlock(@RequestBody InputData data){
        blockChain.addBlock(data.data);
        pubSub.redisPublish();
    }

    record InputData(String[] data){}
}
