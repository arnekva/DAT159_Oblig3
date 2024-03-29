package no.hvl.dat159.application;

import java.util.List;

import javax.naming.InsufficientResourcesException;

import no.hvl.dat159.Block;
import no.hvl.dat159.FullNode;
import no.hvl.dat159.Transaction;
import no.hvl.dat159.Wallet;

public class DemoApplication {
	
	public static void main(String... blablabla) throws Exception {
		
        /*
         * In this assignment, we are going to look at how to represent and record
         * monetary transactions. We will use Bitcoin as the basis for the assignment,
         * but there will be a lot of simplifications!
         */
		
		/*
		 * 1. First, you should create the one and only FullNode.
		 * 		The full node will create it's internal Wallet, create the 
		 *      (centralized) Blockchain and UtxoMap, mine the genesis Block 
		 *      and add it to the blockchain (and update the UtxoMap).
		 */
    	//DONE
		FullNode fullnode = new FullNode("Sven-Olai");
		/*
		 * 2. Next, you should create two additional wallets and reference
		 * 		the three wallets from three variables.
		 */
    	//DONE
		Wallet LPwallet = new Wallet("Lars-Petter", fullnode);
		Wallet TOwallet = new Wallet("Tosin", fullnode);
		Wallet SOwallet = fullnode.getWallet();
		/*
		 * 3. Next, you should create a Transaction to transfer some money
		 * 		from the miner's (full node's) wallet address to one of the 
		 * 		other wallet addresses. The full node should receive this 
		 * 		transaction, validate the transaction, mine a new block and 
		 * 		append it to the blockchain.
		 */
    	//DONE
		Transaction tx = SOwallet.createTransaction(500, LPwallet.getAddress());
		fullnode.mineAndAppendBlockContaining(tx);

		
		/*
		 * 4. Repeat the above (transfer some money from one wallet address
		 * 		to another and record this in the blockchain ledger). 
		 */
    	//DONE
		Transaction tx2 = LPwallet.createTransaction(300, TOwallet.getAddress());
		fullnode.mineAndAppendBlockContaining(tx2);

		/*
		 * 5. Repeat the above (transfer some money from one wallet address
		 * 		to another and record this in the blockchain ledger). 
		 */
    	//DONE
		Transaction tx3 = SOwallet.createTransaction(1200, LPwallet.getAddress());
		fullnode.mineAndAppendBlockContaining(tx3);
		
		Transaction txFail;
		try {
			System.out.println("Testing a transaction with insufficient funds:\n");
			txFail = SOwallet.createTransaction(3200, LPwallet.getAddress());
			fullnode.mineAndAppendBlockContaining(txFail);
		} catch (InsufficientResourcesException e) {
			System.out.println("\n----------Insufficient funds.----------\n\n");
		}
		
		
		/*
		 * 6. Now, it is time to look at the finished result. Print out:
		 * 		- An overview of the full node
		 * 		- An overview of each of the three wallets
		 * 		- An overview of each of the four blocks in the blockchain
		 */
    	//DONE
		fullnode.printOverview();
		LPwallet.printOverview();
		TOwallet.printOverview();
		List<Block> blocks = fullnode.getBlockchain().getBlocks();
		blocks.forEach(x -> x.printOverview());
	}
	
}
