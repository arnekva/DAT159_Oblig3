package no.hvl.dat159;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import no.hvl.dat159.util.HashUtil;
import no.hvl.dat159.util.SignatureUtil;

/**
 * A Wallet keeps the keys and creates signed transactions to be
 * sent to the "network"/node.
 * A wallet also has a name/id to make it easier to identify.
 */
public class Wallet {
	
	private String id;
    private KeyPair keyPair;

    /*
     * The single node in this "network" that the wallets knows about.
     */
	private FullNode networkNode;
	
	/**
	 * 
	 */
	public Wallet(String id, FullNode node) {
		//DONE
		this.id = id;
		this.networkNode = node;
		this.keyPair = SignatureUtil.generateRandomDSAKeyPair();
	}
	
	/**
	 * 
	 */
    public Transaction createTransaction(long value, String address) throws Exception {
    	//TODO
        // 1. Calculate the balance
        // 2. Check if there are sufficient funds --- Exception?
        // 3. Choose a number of UTXO to be spent - We take ALL 
        //   (= the complete wallet balance)!
        // 4. Calculate change
        // 5. Create an "empty" transaction
        // 6. Add chosen inputs (=ALL)
        // 7. Add 1 or 2 outputs, depending on change
        // 8. Sign the transaction
        return null;
    }

    public String getId() {
		return id;
	}

    /**
     * 
     */
	public PublicKey getPublicKey() {
		//DONE
		return keyPair.getPublic();
    }

	/**
	 * 
	 */
    public String getAddress() {
		//DONE
		return HashUtil.pubKeyToAddress(getPublicKey());
    }
    
    /**
     * 
     */
    public long calculateBalance() {
    	//DONE
    	long balance = 0;
    	ArrayList<Integer> ledger = new ArrayList<Integer>();
    	Set<Entry<Input, Output>> utxos = networkNode.getUtxoMap().getUtxosForAddress(getAddress());
    	utxos.forEach(x -> ledger.add((int)x.getValue().getValue()));
    	balance = ledger.stream().collect(Collectors.summingInt(Integer::intValue));
    	return balance;
    }

    /**
     * 
     */
    public int getNumberOfUtxos() {
    	//DONE
    	Set<Entry<Input, Output>> utxos = networkNode.getUtxoMap().getUtxosForAddress(getAddress());
    	return utxos.size();
    }
    
	public void printOverview() {
		System.out.println();
		System.out.println(id + " overview");
		System.out.println("----------------------");
		System.out.println("   Address    : " + getAddress());
		System.out.println("   Balance    : " + calculateBalance());
		System.out.println("   # of UTXOs : " + getNumberOfUtxos());
		
	}
	
	public void printOverviewIndented() {
		System.out.println("      " + id + " with address : " + getAddress());
	}
	
}
