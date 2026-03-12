package com.example.skillswap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.skillswap.model.TokenWallet;
import com.example.skillswap.repository.TokenWalletRepository;

@Service
public class TokenWalletService {

    @Autowired
    private TokenWalletRepository walletRepository;

    public TokenWallet getWallet(String userId){
        return walletRepository.findByUserId(userId).orElse(null);
    }

    public void addTokens(String userId, int tokens){

        TokenWallet wallet = walletRepository
                .findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));

        wallet.setTokens(wallet.getTokens() + tokens);
        wallet.setEarnedTokens(wallet.getEarnedTokens() + tokens);

        walletRepository.save(wallet);
    }

}