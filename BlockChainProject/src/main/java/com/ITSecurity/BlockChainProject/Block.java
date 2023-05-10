package com.ITSecurity.BlockChainProject;

import java.util.Arrays;

public class Block {

    private long timestamp;
    private String lastHash;
    private String hash;
    private String[] data;
    private int nonce;
    //La difficoltà indica il numero di zeri che realizzano il prefisso dell'hash
    private int difficulty;

    public Block(){}

    public Block(long timestamp, String lastHash, String hash, String[] data,int nonce,int difficulty){
        this.timestamp = timestamp;
        this.lastHash = lastHash;
        this.hash = hash;
        this.data = data.clone();
        this.nonce = nonce;
        this.difficulty = difficulty;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLastHash() {
        return lastHash;
    }

    public void setLastHash(String lastHash) {
        this.lastHash = lastHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void printBlockContent(){
        //Stampa il contenuto del blocco
        System.out.println(
                "Timestamp: "+timestamp+"\n"+
                        "Last Hash: "+lastHash+"\n"+
                        "Hash: "+hash+"\n"+
                        "Data: "+Arrays.toString(data)+"\n"+
                        "Nonce: "+nonce+"\n"+
                        "Difficulty: "+difficulty+"\n"
        );
    }

    //Classe stativca per la creazione del blocco di genesi con "last hash" nullo
    public static Block createGenesisBlock(String[] data){
        int nonce = 0;
        final int difficulty = 3;
        //Timestamp di creazione del blocco
        long timestamp = System.currentTimeMillis();
        //Ultimo hash settato con valore di default
        String lastHash = "000000000000000000000000000000000000000000000000000000000000000";
        //Hash del blocco corrente
        String hash = HashingClass.createHash(timestamp,lastHash,data,nonce,difficulty);
        //Crea e ritorna un oggetto con gli attributi contententi i valori soprastanti
        return new Block(timestamp,lastHash,hash,data,nonce,difficulty);
    }

    public static Block mineBlock(Block lastBlock,String[] data){
        int nonce = 0;
        String hash;
        String zeroesNumber = "0";
        long timestamp;
        int difficulty;

        //Hash dell ultimo blocco nella blockchain
        String lastHash = lastBlock.getHash();

        //Esegui il loop finchè i caratterri dell'hash generato non corrispondono alla difficoltà scelta
        //(se la difficoltà è 3 allora sarà necessario che l'hash contenga tre 0 come prefisso)
        do{
            nonce++;
            timestamp = System.currentTimeMillis();
            //La difficoltà viene settata in modo differente ogni volta che viene minato un blocco
            difficulty = adjustDifficulty(lastBlock,timestamp);
            //Hash del blocco corrente
            hash = HashingClass.createHash(timestamp,lastHash,data,nonce,difficulty);
        }while(!hash.substring(0,difficulty).equals(zeroesNumber.repeat(difficulty)));

        //Crea e ritorna un oggetto con gli attributi contententi i valori soprastanti
        return new Block(timestamp,lastHash,hash,data,nonce,difficulty);
    }

    public static int adjustDifficulty(Block block,long timestamp){
        //Velocità di mining dei blocchi
        final long mineRate = 1000;

        //Se la quantità di tempo necessaria a minare il blocco precedente è superiore al minerate
        //allora è necessario diminuire la difficoltà
        //altrimenti è necessario aumentarla

        //Questo controllo permette di evitare che la difficoltà possa scendere sotto il valore 1,
        //se così non fosse, la difficoltà potrebbe assumere il valore 0 o inferiore, rendendo il processo di mining istantaneo (senza dover
        //passare il test del prefisso)

        if((timestamp - block.getTimestamp())>mineRate && (block.getDifficulty()-1)==0){
            return block.getDifficulty();
        }else if ((timestamp - block.getTimestamp())>mineRate){
            return block.getDifficulty()-1;
        }

        return block.getDifficulty()+1;
    }
}

