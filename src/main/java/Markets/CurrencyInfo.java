package Markets;
public class CurrencyInfo {
	String name;
	Double txFee = 0.0;
	Double confirmationNumber = 0.0;
	WithdrawsState payoutState = WithdrawsState.Enabled;
	public CurrencyInfo(String name) {
		this.name = name;
	}
}
