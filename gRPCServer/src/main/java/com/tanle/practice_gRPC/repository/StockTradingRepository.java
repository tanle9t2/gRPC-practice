package com.tanle.practice_gRPC.repository;

import com.tanle.practice_gRPC.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockTradingRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByStockSymbol(String stockSymbol);
}
