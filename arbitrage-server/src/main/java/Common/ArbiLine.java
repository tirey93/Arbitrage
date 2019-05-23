package Common;

import java.util.ArrayList;

public class ArbiLine {
	private String asset;
	private String denominator;
	private String marketAsk;
	private String marketBid;
	private Transaction latest;
	private ArrayList<Transaction> transactions = new ArrayList<>();
	

	public ArbiLine(String asset, String denominator, String marketAsk, String marketBid) {
		super();
		this.asset = asset;
		this.denominator = denominator;
		this.marketAsk = marketAsk;
		this.marketBid = marketBid;
	}
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
		latest = transaction;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((asset == null) ? 0 : asset.hashCode());
		result = prime * result + ((denominator == null) ? 0 : denominator.hashCode());
		result = prime * result + ((marketAsk == null) ? 0 : marketAsk.hashCode());
		result = prime * result + ((marketBid == null) ? 0 : marketBid.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArbiLine other = (ArbiLine) obj;
		if (asset == null) {
			if (other.asset != null)
				return false;
		} else if (!asset.equals(other.asset))
			return false;
		if (denominator == null) {
			if (other.denominator != null)
				return false;
		} else if (!denominator.equals(other.denominator))
			return false;
		if (marketAsk == null) {
			if (other.marketAsk != null)
				return false;
		} else if (!marketAsk.equals(other.marketAsk))
			return false;
		if (marketBid == null) {
			if (other.marketBid != null)
				return false;
		} else if (!marketBid.equals(other.marketBid))
			return false;
		return true;
	}
	public String getAsset() {
		return asset;
	}
	public void setAsset(String asset) {
		this.asset = asset;
	}
	public String getDenominator() {
		return denominator;
	}
	public void setDenominator(String denominator) {
		this.denominator = denominator;
	}
	public String getMarketAsk() {
		return marketAsk;
	}
	public void setMarketAsk(String marketAsk) {
		this.marketAsk = marketAsk;
	}
	public String getMarketBid() {
		return marketBid;
	}
	public void setMarketBid(String marketBid) {
		this.marketBid = marketBid;
	}
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(ArrayList<Transaction> transactions) {
		this.transactions = transactions;
	}
	public Transaction getLatest() {
		return latest;
	}
	public void setLatest(Transaction latest) {
		this.latest = latest;
	}
	
}
