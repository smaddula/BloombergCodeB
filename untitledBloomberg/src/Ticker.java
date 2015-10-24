import jdk.internal.util.xml.impl.Pair;

import java.util.ArrayList;

/**
 * Created by venkata on 10/23/15.
 */
public class Ticker {

    String Name;
    int units = 0;
    double netWorth = 0;
    double startDividend = 0;
    double curDividend = 0;
    double buyPrice = 0;
    double volatility = 0;
    Orders curOrders = null;
    int bidUnits = 0;
    double bidPrice = 0;

    public Ticker(String Name) {
        this.Name = Name;
    }

    public void addUnits(double price, int units) {
        this.buyPrice = (this.buyPrice*this.units + price*units)/(this.units + units);
        this.units += units;
    }

    public void removeUnits(double price, int units) {
        this.buyPrice = (this.buyPrice*this.units - price*units)/(this.units - units);
        this.units -= units;

    }

    public void updateCurDividend(double dividend) {
        this.curDividend = dividend;
    }
}
