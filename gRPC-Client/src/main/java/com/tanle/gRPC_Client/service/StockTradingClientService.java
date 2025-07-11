package com.tanle.gRPC_Client.service;

import com.tanle.practice_gRPC.grpc.StockRequest;
import com.tanle.practice_gRPC.grpc.StockResponse;
import com.tanle.practice_gRPC.grpc.StockTradingProto;
import com.tanle.practice_gRPC.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class StockTradingClientService {

    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceStub serviceStub;
    @GrpcClient("stockService")
    private StockTradingServiceGrpc.StockTradingServiceBlockingStub serviceBlockingStub;


    public StockResponse getStockPrice(String stockSymbol) {
        StockResponse response = serviceBlockingStub.getStockPrice(StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build());

        return response;
    }

    public void subscribeStockPrice(String stockSymbol, StreamObserver<StockResponse> streamObserver) {
        serviceStub.subscribeStockPrice(StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build(), streamObserver);
    }
}
