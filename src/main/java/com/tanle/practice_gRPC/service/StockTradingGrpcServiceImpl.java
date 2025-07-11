package com.tanle.practice_gRPC.service;

import com.tanle.practice_gRPC.entity.Stock;
import com.tanle.practice_gRPC.grpc.StockRequest;
import com.tanle.practice_gRPC.grpc.StockResponse;
import com.tanle.practice_gRPC.grpc.StockTradingServiceGrpc;
import com.tanle.practice_gRPC.repository.StockTradingRepository;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.grpc.server.service.GrpcService;

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
}
