package com.tanle.gRPC_Client.service;

import com.tanle.practice_gRPC.grpc.*;
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

    public void bulkPlaceOrder() {
        StreamObserver<StockSummary> stockObserver = new StreamObserver<StockSummary>() {
            @Override
            public void onNext(StockSummary stockSummary) {
                System.out.println("Order Summary Received from Server:");
                System.out.println("Total Orders: " + stockSummary.getTotalOrder());
                System.out.println("Successful Orders: " + stockSummary.getSuccessCount());
                System.out.println("Total Amount: $" + stockSummary.getTotalAmount());
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                System.out.println("Completed");
            }
        };
        StreamObserver<StockOrder> requestObserver = serviceStub.bulkPlaceOrder(stockObserver);
        try {

            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("1")
                    .setStockSymbol("AAPL")
                    .setOrderType("BUY")
                    .setPrice(150.5)
                    .setQuantity(10)
                    .build());

            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("2")
                    .setStockSymbol("GOOGL")
                    .setOrderType("SELL")
                    .setPrice(2700.0)
                    .setQuantity(5)
                    .build());

            requestObserver.onNext(StockOrder.newBuilder()
                    .setOrderId("3")
                    .setStockSymbol("TSLA")
                    .setOrderType("BUY")
                    .setPrice(700.0)
                    .setQuantity(8)
                    .build());

            //done sending orders
            requestObserver.onCompleted();
        } catch (Exception ex) {
            requestObserver.onError(ex);
        }

    }

    public void subscribeStockPrice(String stockSymbol, StreamObserver<StockResponse> streamObserver) {
        serviceStub.subscribeStockPrice(StockRequest.newBuilder()
                .setStockSymbol(stockSymbol)
                .build(), streamObserver);
    }
}
