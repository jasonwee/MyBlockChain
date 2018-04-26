import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Convert.Unit;

public class Test {

	private Web3j web3j;

	public static void main(String[] args) {
		new Test();
	}

	public Test() {

		String infuraMainnetToken = "<CHANGE ME, already register and got in mail, check infura.io>";
		web3j = Web3j.build(new HttpService("https://mainnet.infura.io/" + infuraMainnetToken));
		try {
			showProtocolInformation();
			//getWalletBalance("0x14ec153512224553365b391f7bef2914f62fadc6");
			//listTransactionsInBlock(5509541l);
			//showTransactionInfo("0x95b0441f0a6def3c5f6f0baafa62bc9cd9652bf2ef2401b4175f7182b8254ef2");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void showProtocolInformation() throws IOException {
		System.out.println("client version : " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
		System.out.println("network version : " + web3j.netVersion().send().getNetVersion());

	}
	
	public void getWalletBalance(String walletId) throws InterruptedException, ExecutionException {
		// send asynchronous requests to get balance
		// random wallet from
		// https://etherscan.io/address/0x32be343b94f860124dc4fee278fdcbd38c102d88
		EthGetBalance ethGetBalance = web3j
				.ethGetBalance(walletId, DefaultBlockParameterName.LATEST)
				.sendAsync().get();

		BigInteger wei = ethGetBalance.getBalance();
		

		System.out.println("current balance : " + wei);
	}
	
	public void listTransactionsInBlock(long blockHash) {

		  web3j.catchUpToLatestTransactionObservable(new
		  DefaultBlockParameterNumber(blockHash)).subscribe(block -> {
		  System.out.println("*+observer+getblock: " + block.getBlockNumber());
		  System.out.println("block getFrom: " + block.getFrom());
		  System.out.println("block getGasPriceRaw: " + block.getGasPriceRaw());
		  System.out.println("block getNonceRaw: " + block.getNonceRaw());
		  System.out.println("block getNonce: " + block.getNonce());
		  System.out.println("block getHash: " + block.getHash());
		  System.out.println("block getTo: " + block.getTo());
		  System.out.println("block getInput: " + block.getInput());
		  System.out.println("block getCreates: " + block.getCreates());
		  
		  
		  }, e -> { System.out.println("exception occurred in subscription: ");
		  System.out.println("exception occurred in subscription tx" + e); }, () -> {
		  System.out.println("block complete: "); } );
		 
	}
	
	public void showTransactionInfo(String txHash) throws Exception {
		EthTransaction transaction = web3j
				.ethGetTransactionByHash(txHash)
				.send();
		BigInteger amount = transaction.getResult().getValue();
		System.out.println("gas price : " + transaction.getResult().getGas());
		System.out.println("total ether : " +Convert.fromWei(amount.toString(), Unit.ETHER));
	}


}
