package com.example.damianpytkowski.arbitrage_android.Common;

import java.util.HashMap;
import java.util.Map;



public class JsonClass {
    private Map<Integer, ArbiLine> arbiLines = new HashMap<Integer, ArbiLine>();

    public void addArbiLine(ArbiLine line) {
        arbiLines.put(line.hashCode(), line);
    }
    public void addArbiLine(ArbiLine line, Transaction transaction) {
        arbiLines.put(line.hashCode(), line);
        arbiLines.get(line.hashCode()).addTransaction(transaction);

    }
    public boolean hasArbiLine(ArbiLine line) {
        return arbiLines.containsKey(line.hashCode());
    }
    public Map<Integer, ArbiLine> getArbiLines() {
        return arbiLines;
    }

    public void setArbiLines(Map<Integer, ArbiLine> arbiLines) {
        this.arbiLines = arbiLines;
    }

}
