package Common;

import java.util.HashMap;
import java.util.Map;

import app.Complex;
import app.TestJson;

public class JsonClass {
	private Map<Integer, ArbiLine> arbiLines = new HashMap<Integer, ArbiLine>();

	public void addArbiLine(ArbiLine line) {
		arbiLines.put(line.hashCode(), line);
	}
	public Map<Integer, ArbiLine> getArbiLines() {
		return arbiLines;
	}

	public void setArbiLines(Map<Integer, ArbiLine> arbiLines) {
		this.arbiLines = arbiLines;
	}
	
}
