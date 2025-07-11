package com.tanle.gRPC_Client;

import com.tanle.gRPC_Client.service.StockTradingClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GRpcClientApplication implements CommandLineRunner {

    private StockTradingClientService stockClientService;

    public GRpcClientApplication(StockTradingClientService stockClientService) {
        this.stockClientService = stockClientService;
    }
    public static void main(String[] args) {
        SpringApplication.run(GRpcClientApplication.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        System.out.println(stockClientService.getStockPrice("GOOGL"));
    }
}
