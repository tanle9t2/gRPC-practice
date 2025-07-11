package com.tanle.gRPC_Client.service;

import com.tanle.practice_gRPC.grpc.StockRequest;
import com.tanle.practice_gRPC.grpc.StockResponse;
import com.tanle.practice_gRPC.grpc.StockTradingProto;
import com.tanle.practice_gRPC.grpc.StockTradingServiceGrpc;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service

public class StockTradingClientService {

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub serviceBlockingStub;


    public StockResponse getStockPrice(String stockSymbol) {
        StockResponse response = serviceBlockingStub.getStockPrice(StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build());

        return response;
    }
}
