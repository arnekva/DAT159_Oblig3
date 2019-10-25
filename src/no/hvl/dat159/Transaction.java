package no.hvl.dat159;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import no.hvl.dat159.util.EncodingUtil;
import no.hvl.dat159.util.HashUtil;
import no.hvl.dat159.util.SignatureUtil;

/**
 * 
 */
public class Transaction {
	
	private List<Input> inputs = new ArrayList<>();
	private List<Output> outputs = new ArrayList<>();
	
	/*
	 * Simplification!:
	 * In reality, each input should have a public key and a signature. To simplify 
	 * things, we assume that all inputs belong to the same public key => We can 
	 * store the public key in the transaction and sign for all inputs in one go.
	 */
	private PublicKey senderPublicKey;
	private byte[] signature; 
	
	public Transaction(PublicKey senderPublicKey) {
		this.senderPublicKey = senderPublicKey;
	}
	
	/**
	 * 
	 */
	public void signTxUsing(PrivateKey privateKey) {
		//DONE
		String message = "";
		for (Input i : inputs) {
			message += String.valueOf(i.hashCode());
		}
		signature = SignatureUtil.signWithDSA(privateKey, message);
	}

	/**
	 * 
	 */
	public boolean isValid(UtxoMap utxoMap) {
	    //TODONE
	    //None of the data must be null 
		if (senderPublicKey == null || signature  == null) {
			return false;
		}
        //Inputs or outputs cannot be empty
		if (inputs.isEmpty() || outputs.isEmpty()) {
			return false;
		}
	    //No outputs can be zero or negative
		for (Output o : outputs) {
			if (o.getValue() <= 0) {
				return false;
			}
		}
	    //All inputs must exist in the UTXO-set
		Set<Entry<Input, Output>> allUtxos = utxoMap.getAllUtxos();
		ArrayList<Input> utxoInputs = new ArrayList<Input>();
		for (Entry<Input, Output> entry : allUtxos) {
			utxoInputs.add(entry.getKey());
		}
		for (Input i : inputs) {
			if (!utxoInputs.contains(i)) {
				return false;
			}
		}
	    //All inputs must belong to the sender of this transaction 
		//-> This is checked in Wallet.java (the only place a transaction is created)
		
        //The list of inputs must not contain duplicates
		Set<Input> inputSet = new HashSet<Input>(inputs);
		if (inputSet.size() < inputs.size()) {
			return false;
		}
        //The total input amount must be equal to (or less than, if we 
        //allow fees) the total output amount
		//-> We assume the input amount must be equal to or greater than the output amount as this determines sufficient balance.
		//-> This is checked in Wallet.java (the only place a transaction is created)
		
        //The signature must belong to the sender and be valid
		String message = "";
		for (Input i : inputs) {
			message += String.valueOf(i.hashCode());
		}
		if (!SignatureUtil.verifyWithDSA(senderPublicKey, message, signature)) {
			return false;
		}
        //The transaction hash must be correct - it is!
	    return true;
	}
	
	/**
	 *	The block hash as a hexadecimal String. 
	 */
	public String getTxId() {
		//DONE
		return EncodingUtil.bytesToHex(HashUtil.sha256(signature));
	}

	public void addInput(Input input) {
		inputs.add(input);
	}
	
	public void addOutput(Output output) {
		outputs.add(output);
	}
	
	public List<Input> getInputs() {
		return inputs;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public PublicKey getSenderPublicKey() {
		return senderPublicKey;
	}

	public byte[] getSignature() {
		return signature;
	}

	@Override
	public String toString() {
		String s = getTxId();
		for (int i=0; i<inputs.size(); i++) {
			s += "\n\tinput(" + i + ")   : " + inputs.get(i);
		}
		for (int i=0; i<outputs.size(); i++) {
			s += "\n\toutput(" + i + ")  : " + outputs.get(i);
		}
		return s;
	}

	
}
