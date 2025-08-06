package com.vaibh.controller;

import com.vaibh.model.User;
import com.vaibh.model.Wallet;
import com.vaibh.model.Withdrawal;
import com.vaibh.service.UserService;
import com.vaibh.service.WalletService;
import com.vaibh.service.WithdrawalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class WithdrawalController {
    @Autowired
    private WithdrawalService withdrawalService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletService walletService;

    @PostMapping("/api/withdrawal/{amount}")
    public ResponseEntity<?> withdrawalRequest(
            @PathVariable Long amount,
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);
        Wallet userWallet=walletService.getUserWallet(user);

        Withdrawal withdrawal=withdrawalService.requestWithdrawal(amount,user);
        walletService.addBalanceToWallet(userWallet, -withdrawal.getAmount());
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @PatchMapping("/api/admin/withdrawal/{id}/proceed/{accept}")
    public ResponseEntity<?> proceedWithdrawal(
            @PathVariable Long id,
            @PathVariable boolean accept,
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        Withdrawal withdrawal=withdrawalService.procedWithdrawal(id,accept);

        Wallet userWallet=walletService.getUserWallet(user);
        if(!accept){
            walletService.addBalanceToWallet(userWallet, withdrawal.getAmount());
        }
        
        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }

    @GetMapping("/api/withdrawal")
    public ResponseEntity<List<Withdrawal>> getWithdrawalHistory(

            @RequestHeader("Authorization")String jwt) throws Exception {
            List<Withdrawal> withdrawals=withdrawalService.getUsersWithdrawalHistory(userService.findUserProfileByJwt(jwt));
            return new ResponseEntity<>(withdrawals,HttpStatus.OK);
    }

    @GetMapping("/api/admin/withdrawal")
    public ResponseEntity<List<Withdrawal>> getAllWithdrawalRequest(

            @RequestHeader("Authorization")String jwt) throws Exception {
        List<Withdrawal> withdrawal=withdrawalService.getAllWithdrawalRequest();

        return new ResponseEntity<>(withdrawal, HttpStatus.OK);
    }
}
