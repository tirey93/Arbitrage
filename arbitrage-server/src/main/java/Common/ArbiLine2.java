package Common;
import java.util.ArrayList;

public class ArbiLine2 {
	public Long startTime;
	public Long endTime = Long.parseLong("0");
	public String asset;
	public String marketAsk;
	public String marketBid;
	public int nShowNegative;
	public ArrayList<Double> rois = new ArrayList<>();
	public ArbiLine2(Long startTime, String asset, String marketAsk, String marketBid) {
		super();
		this.startTime = startTime;
		this.asset = asset;
		this.marketAsk = marketAsk;
		this.marketBid = marketBid;
		this.nShowNegative = 5;
	}
}
