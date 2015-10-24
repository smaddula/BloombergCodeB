/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;


public class ExchangeClient {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */


    String host = new String("codebb.cloudapp.net");
    int port = 17429;
    String username = "teamWarrenBuffet";
    String password = "qwertyu";
    Socket socket;
    PrintWriter pout;
    BufferedReader bin;


    public ExchangeClient() throws IOException {

        socket = new Socket(host, port);
        this.pout = new PrintWriter(socket.getOutputStream());
        this.pout.println(username + " " + password);
        this.bin = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    public Orders getOrders(String ticker) throws IOException {
        Orders myOrder = new Orders();
        StringBuilder sb = new StringBuilder();
        sb.append("ORDERS ").append(ticker);
        this.pout.println(sb.toString());
        this.pout.flush();
        String line;
        if ((line = this.bin.readLine()) != null) {

            String[] s = line.split(" ");
            for (int i=1; i <s.length; i++) {
                if (s[i].equals("BID")) {

                }
            }
        }
        return myOrder;
    }


    public void placeBid(String ticker, double price, int shares) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("BID ").append(ticker).append(" ").append(price).append(" ").append(shares);
        this.pout.println(sb.toString());
        this.pout.flush();
        String line;
        double cash = 0.0;
        if ((line = this.bin.readLine()) != null) {
            System.out.println(line);
        }
    }


    public void placeAsk(String ticker, double price, int shares) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("ASK ").append(ticker).append(" ").append(price).append(" ").append(shares);
        this.pout.println(sb.toString());
        this.pout.flush();
        String line;
        if ((line = this.bin.readLine()) != null) {
            System.out.println(line);
        }
    }


    public void clearBid(String ticker) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("CLEAR_BID ").append(ticker);
        this.pout.println(sb.toString());
        this.pout.flush();
        String line;
        if ((line = this.bin.readLine()) != null) {
            System.out.println(line);
        }
    }


    public void subscribe(TradingSystem mySystem) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("SUBSCRIBE");
        this.pout.println(sb.toString());
        this.pout.flush();
        String line;
        double cash = 0.0;
        while ((line = this.bin.readLine()) != null) {
            mySystem.updateTrade(line);
        }
    }

    public void unSubscribe() throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("UNSUBSCRIBE");
        this.pout.println(sb.toString());
        this.pout.flush();
    }


    public HashMap<String,Double> getDividends() throws IOException {
        HashMap<String,Double> result = new HashMap<String,Double>();
        StringBuilder sb = new StringBuilder();
        sb.append("MY_SECURITIES");
        this.pout.println(sb.toString());
        this.pout.flush();
        String line;
        if ((line = this.bin.readLine()) != null) {
            String[] s = line.split(" ");
            int i=0;
            while(i < s.length -2) {
                result.put(s[i], Double.parseDouble(s[i+2]));
            }
        }
        return result;
    }


    public void clearAsk(String ticker) throws IOException {

        StringBuilder sb = new StringBuilder();
        sb.append("CLEAR_ASK ").append(ticker);
        this.pout.println(sb.toString());
        this.pout.flush();
        String line;
        if ((line = this.bin.readLine()) != null) {
            System.out.println(line);
        }
    }



    public double getCash() throws IOException {
        this.pout.println("MY_CASH");
        this.pout.flush();
        String line;
        double cash = 0.0;
        if ((line = this.bin.readLine()) != null) {
            System.out.println(line);
            cash = Double.parseDouble(line.split(" ")[1]);
        }
        return cash;
    }
}
