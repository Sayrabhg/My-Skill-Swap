package com.example.skillswap.controller;

import com.example.skillswap.model.TokenWallet;
import com.example.skillswap.repository.TokenWalletRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/wallet")
@CrossOrigin("*")
public class TokenWalletController {

    @Autowired
    private TokenWalletRepository walletRepository;


    // Get wallet by userId
    @GetMapping("/{userId}")
    public TokenWallet getWallet(@PathVariable String userId) {
        Optional<TokenWallet> wallet = walletRepository.findByUserId(userId);
        return wallet.orElse(null);
    }

    @PostMapping("/create/{userId}")
    public TokenWallet createWallet(@RequestBody TokenWallet wallet, @PathVariable String userId) {

        wallet.setUserId(userId);

        return walletRepository.save(wallet);
    }

    // Add tokens (mentor earns tokens)
    @PutMapping("/add/{userId}")
    public TokenWallet addTokens(@PathVariable String userId, @RequestParam int amount) {

        TokenWallet wallet = walletRepository.findByUserId(userId).orElseThrow();

        wallet.setTokens(wallet.getTokens() + amount);
        wallet.setEarnedTokens(wallet.getEarnedTokens() + amount);

        return walletRepository.save(wallet);
    }


    // Spend tokens (student uses tokens)
    @PutMapping("/spend/{userId}")
    public TokenWallet spendTokens(@PathVariable String userId, @RequestParam int amount) {

        TokenWallet wallet = walletRepository.findByUserId(userId).orElseThrow();

        wallet.setTokens(wallet.getTokens() - amount);
        wallet.setSpentTokens(wallet.getSpentTokens() + amount);

        return walletRepository.save(wallet);
    }

}