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
