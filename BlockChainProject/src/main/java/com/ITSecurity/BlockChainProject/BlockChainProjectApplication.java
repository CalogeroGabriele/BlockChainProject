package com.ITSecurity.BlockChainProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BlockChainProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlockChainProjectApplication.class, args);
		BlockChain blockChain = new BlockChain(new String[]{""});
		APIs api = new APIs(blockChain);
	}
}
