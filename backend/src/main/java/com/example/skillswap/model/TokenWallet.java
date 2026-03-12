package com.example.skillswap.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "token_wallet")
public class TokenWallet {

    @Id
    private String id;

    private String userId;

    private int tokens;

    private int earnedTokens;

    private int spentTokens;

}