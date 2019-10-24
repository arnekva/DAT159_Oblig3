package no.hvl.dat159;

/**
 * 
 */
public class CoinbaseTx {
	
	/*
	 * The block number wherein this coinbase tx is located.
	 * This is to ensure unique txIds, see BIP34
	 */
	private int blockHeight; 
	
	/*
	 * The message for this coinbase tx.
	 */
	private String message;
	
	/*
	 * The out put for this coinbase tx.
	 */
	private Output output;

	/* --------------------------------------------------------------------- */

	public CoinbaseTx(int blockHeight, String message, String walletAddress) {
		//TODO
	}
	
	public boolean isValid(UtxoMap utxoMap) {
		//TODO
		return true;
	}

	public String getMessage() {
		return message;
	}

	public Output getOutput() {
		return output;
	}

	/**
	 *	The block hash as a hexadecimal String. 
	 */
	public String getTxId() {
		//TODO
		return null;
	}
	
	@Override
	public String toString() {
		return getTxId() + "\n\tmessage    : " + message + "\n\toutput(0)  : " + output;
	}
	
}
