/**
 * Created by venkata on 10/24/15.
 */
public class Trade {

    public Trade(double price, int units) {
        this.price = price;
        this.units = units;
    }

    public int getUnits() {
        return units;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    int units;
    double price;
}