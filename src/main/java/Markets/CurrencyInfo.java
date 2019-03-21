package Markets;
public class CurrencyInfo {
	String name;
	Double txFee;
	Double confirmationNumber;
	WithdrawsState payoutState;
	public CurrencyInfo(String name) {
		this.name = name;
	}
}
