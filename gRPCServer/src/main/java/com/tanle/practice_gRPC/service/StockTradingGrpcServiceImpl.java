package com.tanle.practice_gRPC.service;

import com.tanle.practice_gRPC.entity.Stock;
import com.tanle.practice_gRPC.grpc.*;
import com.tanle.practice_gRPC.repository.StockTradingRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@GrpcService
@RequiredArgsConstructor
public class StockTradingGrpcServiceImpl extends StockTradingServiceGrpc.StockTradingServiceImplBase {

    private final StockTradingRepository stockTradingRepository;

    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        Stock stock = stockTradingRepository.findByStockSymbol(request.getStockSymbol())
                .get();

        StockResponse response = StockResponse.newBuilder()
                .setPrice(stock.getPrice())
                .setStockSymbol(stock.getStockSymbol())
                .setTimestamp(stock.getLastUpdated().toString())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<StockOrder> bulkPlaceOrder(StreamObserver<StockSummary> responseObserver) {
        return new StreamObserver<StockOrder>() {
            int totalOrders = 0;
            double totalAmount = 0;
            int successCount = 0;

            @Override
            public void onNext(StockOrder stockOrder) {
                totalOrders++;
                totalAmount += stockOrder.getPrice() * stockOrder.getQuantity();
                successCount++;
                System.out.println("Received order : " + stockOrder);
            }

            @Override
            public void onError(Throwable throwable) {
                responseObserver.onError(throwable);
            }

            @Override
            public void onCompleted() {
                responseObserver.onNext(StockSummary.newBuilder()
                        .setTotalOrder(totalOrders)
                        .setSuccessCount(successCount)
                        .setTotalAmount(totalAmount)
                        .build());
                responseObserver.onCompleted();
            }
        };
    }

    @Override
    public void subscribeStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        String symbol = request.getStockSymbol();
        try {
            for (int i = 0; i < 10; i++) {
                StockResponse response = StockResponse.newBuilder()
                        .setTimestamp(Instant.now().toString())
                        .setStockSymbol(symbol)
                        .setPrice(new Random().nextDouble(200))
                        .build();

                responseObserver.onNext(response);
                TimeUnit.SECONDS.sleep(2);
            }
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(e.getCause());
        }
    }
}
