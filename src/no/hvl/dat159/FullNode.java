package no.hvl.dat159;

import no.hvl.dat159.util.DateTimeUtil;

/**
 * Contains both the full Blockchain and the full UtxoMap.
 * Also contains the wallet for the mining rewards + fees.
 * 
 * Services:
 * - Receive a transaction, validate the transaction, create and mine 
 * 	 a new block, validate and append the mined block to the 
 *   blockchain + update the UxtoMap.
 * - Provide a limited UtxoMap that matches one particular address 
 *   to wallets.
 */
public class FullNode {
	
	private Blockchain blockchain;
	private UtxoMap utxoMap;
	private Wallet wallet;

	/**
	 * Initializes a node containing a blockchain, a UTXO-map and a wallet
	 * for block rewards. Then initializes the blockchain by mining and
	 * adding a genesis block.
	 */
	public FullNode(String walletId) {
		//DONE?
		this.wallet = new Wallet(walletId, this);
		this.blockchain = new Blockchain();
		this.utxoMap = new UtxoMap();
		mineAndAddGenesisBlock();
	}

	/**
	 * Does what it says.
	 */
	public void mineAndAddGenesisBlock() {
		//DONE
		//1. Create the coinbase transaction
		CoinbaseTx coinbasetx = new CoinbaseTx(0, DateTimeUtil.getTimestamp() + " - Trump sucks.", wallet.getAddress());
		if (coinbasetx.isValid()) {
			//2. Add the coinbase transaction to a new block and mine the block
			Block genesisBlock = new Block("0", coinbasetx, null);
			genesisBlock.mine();
			//3. Validate the block. If valid:
			if (genesisBlock.isValidAsGenesisBlock()) {
				//4. Add the block to the blockchain
				blockchain.setGenesisBlock(genesisBlock);
				//5. Update the utxo set
				utxoMap.addOutput(new Input("0", 0), coinbasetx.getOutput());
			} else {
				System.out.println("Block is invalid as genesisblock");
			}
		}
		
	}
	
	/**
	 * Does what it says.
	 */
	public void mineAndAppendBlockContaining(Transaction tx) {
		//TODO
		//1. Create the coinbase transaction
		//2. Add the two transactions to a new block and mine the block
		//3. Validate the block. If valid:
			//4. Add the block to the blockchain
			//5. Update the utxo set with the new coinbaseTx
			//6. Update the utxo set with the new tx
		//else
			//up to you
	}

	public Blockchain getBlockchain() {
		return blockchain;
	}

	public UtxoMap getUtxoMap() {
		return utxoMap;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void printOverview() {
		System.out.println();
		System.out.println("Full node overview");
		System.out.println("------------------");
		System.out.println("   Associated wallet");
		wallet.printOverviewIndented();
		System.out.println("   Associated blockchain");
		blockchain.printOverview();
		wallet.printOverview();
	}
	
}
