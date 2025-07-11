package com.tanle.practice_gRPC.service;

import com.tanle.practice_gRPC.entity.Stock;
import com.tanle.practice_gRPC.grpc.StockRequest;
import com.tanle.practice_gRPC.grpc.StockResponse;
import com.tanle.practice_gRPC.grpc.StockTradingServiceGrpc;
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
