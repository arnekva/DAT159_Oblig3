package no.hvl.dat159;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.naming.InsufficientResourcesException;

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
    	//DONE
        // 1. Calculate the balance
    	long balance = calculateBalance();
        // 2. Check if there are sufficient funds --- Exception?
    	if (balance < value) {
    		throw new InsufficientResourcesException();
    	}
        // 3. Choose a number of UTXO to be spent - We take ALL 
        //   (= the complete wallet balance)!
    	Set<Entry<Input, Output>> allUtxos = networkNode.getUtxoMap().getUtxosForAddress(getAddress());
        // 4. Calculate change
    	long change = balance - value;
        // 5. Create an "empty" transaction
    	Transaction transaction = new Transaction(getPublicKey());
        // 6. Add chosen inputs (=ALL)
    	for (Entry<Input, Output> entry : allUtxos) {
    		transaction.addInput(entry.getKey());
    	}
        // 7. Add 1 or 2 outputs, depending on change
    	transaction.addOutput(new Output(value, address));
    	if (change > 0) {
    		transaction.addOutput(new Output(change, getAddress()));
    	}
        // 8. Sign the transaction
    	transaction.signTxUsing(keyPair.getPrivate());
        return transaction;
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
