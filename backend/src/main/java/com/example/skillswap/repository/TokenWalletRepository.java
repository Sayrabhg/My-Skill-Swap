package com.example.skillswap.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.skillswap.model.TokenWallet;

import java.util.Optional;

public interface TokenWalletRepository extends MongoRepository<TokenWallet, String> {

    Optional<TokenWallet> findByUserId(String userId);

}