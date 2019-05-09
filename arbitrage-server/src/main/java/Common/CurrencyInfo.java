package Common;

public class CurrencyInfo {
	String name;
	public Double txFee = 0.0;
	public Double confirmationNumber = 0.0;
	public WithdrawsState payoutState = WithdrawsState.Enabled;
	public CurrencyInfo(String name) {
		this.name = name;
	}
}
