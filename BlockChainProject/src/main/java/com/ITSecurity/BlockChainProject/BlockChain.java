package com.ITSecurity.BlockChainProject;

import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class BlockChain {
    private ArrayList<Block> listaBlocchi = new ArrayList<>();
    private final Block genesisBlock;

    public BlockChain(String[] data){
        this.genesisBlock = Block.createGenesisBlock(data);
        this.listaBlocchi.add(genesisBlock);
    }

    public void addBlock(String[] data){
        Block blocco = Block.mineBlock(this.listaBlocchi.get(this.listaBlocchi.size()-1),data);
        this.listaBlocchi.add(blocco);
    }

    public ArrayList<Block> getListaBlocchi(){
        return listaBlocchi;
    }

    public Block getGenesisBlock(){ return genesisBlock; }

    public void printBlockChain(){
        String[] data;
        int i = 0;
        while(i<this.listaBlocchi.size()){
            data = this.listaBlocchi.get(i).getData().clone();
            System.out.println(
                    "Blocco: "+(i+1)+"\n"+
                            "Timestamp: "+this.listaBlocchi.get(i).getTimestamp()+"\n"+
                            "Last Hash: "+this.listaBlocchi.get(i).getLastHash()+"\n"+
                            "Hash: "+this.listaBlocchi.get(i).getHash()+"\n"+
                            "Data: "+Arrays.toString(data)+"\n"+
                            "Nonce: "+this.listaBlocchi.get(i).getNonce()+"\n"+
                            "Difficulty: "+this.listaBlocchi.get(i).getDifficulty()+"\n"
            );
            i++;
        }
    }

    //Metodo di validazione degli elementi all'interno della blockchain
    static Boolean chainValidation(ArrayList<Block> listaBlocchi,Block genesisBlock){
        long timestamp;
        String lastHash;
        String[] data;
        String validateHash;
        int nonce;
        int difficulty;
        int lastDifficulty;

        //Verifica la correttezza del Genesis Block
        if (!listaBlocchi.get(0).equals(genesisBlock)){
            System.out.println("Controllo genesisBlock");
            return false;
        }

        for(int i=1;i<listaBlocchi.size();i++){
            //Verifica che "l'hash" del blocco n-1 sia uguale al "lastHash" del blocco n (quello successivo) per ogni blocco
            //presente nella BlockChain
            lastHash = listaBlocchi.get(i-1).getHash();
            //Difficoltà del blocco precedente
            lastDifficulty = listaBlocchi.get(i-1).getDifficulty();
            timestamp = listaBlocchi.get(i).getTimestamp();
            data = listaBlocchi.get(i).getData();
            nonce = listaBlocchi.get(i).getNonce();
            difficulty = listaBlocchi.get(i).getDifficulty();

            if(!listaBlocchi.get(i).getLastHash().equals(lastHash)) {
                System.out.println("Controllo last hash");
                return false;
            }

            //Permette di evitare manipolazioni riguardanti la difficoltà di mining dei blocchi
            //La differenza di difficoltà tra l'ultimo blocco e quello attuale non può essere ne maggiore, ne inferiore di 1
            if(Math.abs(lastDifficulty - difficulty)>1) return false;

            //Rielabora il valore dell'hash del blocco n e lo confronta con il valore presente nel suo campo "hash"
            //per assicurarsi che non siano state effettuate modifiche a nessuno dei restanti campi all'interno del blocco
            validateHash = HashingClass.createHash(timestamp,lastHash,data,nonce,difficulty);
            if(!listaBlocchi.get(i).getHash().equals(validateHash)) {
                System.out.println("Controllo rielaborazione hash");
                return false;
            }
        }
        return true;
    }

    //Verifica di rimpiazzamento della catena di blocchi che si attiva quando la seconda blockchain ha
    //un maggior numero di blocchi rispetto alla prima.
    public void replaceChain(ArrayList<Block> listaBlocchi,Block genesisBlock){
        //Se la chain inserita come parametro (listaBlocchi) ha dimensioni inferiori o uguali non è necessario rimpiazzare, per cui
        //ritorna
        if(listaBlocchi.size()<=this.listaBlocchi.size()) {
            return;
        }

        //Se la chain inserita come parametro è di dimensioni maggiori alla prima, è necessario realizzare i test di validazione
        //per assicurarsi che funzioni correttamente
        if(!chainValidation(listaBlocchi,genesisBlock)){
            return;
        }

        //Rimpiazza la chain con quella di dimensioni maggiori
        this.listaBlocchi = listaBlocchi;
    }
}
