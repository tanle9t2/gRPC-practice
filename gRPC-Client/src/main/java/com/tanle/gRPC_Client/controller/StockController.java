package com.tanle.gRPC_Client.controller;

import com.tanle.gRPC_Client.service.StockTradingClientService;
import com.tanle.practice_gRPC.grpc.StockRequest;
import com.tanle.practice_gRPC.grpc.StockResponse;
import com.tanle.practice_gRPC.grpc.StockTradingServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1")

public class StockController {
    @Autowired
    private StockTradingClientService clientService;


    @GetMapping("/subscribe/{symbol}")
    public SseEmitter streamStock(@PathVariable String symbol) {
        SseEmitter emitter = new SseEmitter();
        clientService.subscribeStockPrice(symbol, new StreamObserver<StockResponse>() {
            @Override
            public void onNext(StockResponse response) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("stock-price")
                            .data(Map.of("name", response.getStockSymbol(),
                                    "price", response.getPrice(),
                                    "timestamp", response.getTimestamp())));
                } catch (IOException e) {
                    emitter.completeWithError(e);
                }
            }

            @Override
            public void onError(Throwable t) {
                emitter.completeWithError(t);
            }

            @Override
            public void onCompleted() {
                emitter.complete();
            }
        });

        return emitter;
    }
}
