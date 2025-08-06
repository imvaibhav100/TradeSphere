package com.vaibh.controller;

import com.vaibh.domain.WalletTransactionType;
import com.vaibh.model.*;
import com.vaibh.response.PaymentResponse;
import com.vaibh.service.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class WalletController {
    @Autowired
    private WalletService walletService;
    @Autowired
    private WalletTransactionService walletTransactionService;
    @Autowired
    private UserService userService;


    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;


    @GetMapping("/api/wallet")
    public ResponseEntity<?> getUserWallet(@RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        return new ResponseEntity<>(wallet, HttpStatus.OK);
    }

    @GetMapping("/api/wallet/transactions")
    public ResponseEntity<List<WalletTransaction>> getWalletTransaction(
            @RequestHeader("Authorization")String jwt) throws Exception {
        User user=userService.findUserProfileByJwt(jwt);

        Wallet wallet = walletService.getUserWallet(user);

        List<WalletTransaction> transactions=walletTransactionService.getTransactions(wallet,null);

        return new ResponseEntity<>(transactions, HttpStatus.OK);
    }

    @PutMapping("/api/wallet/deposit/amount/{amount}")
    public ResponseEntity<PaymentResponse> depositMoney(@RequestHeader("Authorization")String jwt,
                                                        @PathVariable Long amount) throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);
//        PaymentResponse res = walleteService.depositFunds(user,amount);
        PaymentResponse res = new PaymentResponse();
        res.setPayment_url("deposite success");
        walletService.addBalanceToWallet(wallet, amount);

        return new ResponseEntity<>(res,HttpStatus.OK);

    }

    @PutMapping("/api/wallet/deposit")
    public ResponseEntity<Wallet> addMoneyToWallet(
            @RequestHeader("Authorization")String jwt,
            @RequestParam(name="order_id") Long orderId,
            @RequestParam(name="payment_id")String paymentId
            ) throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        Wallet wallet = walletService.getUserWallet(user);


        PaymentOrder order = paymentService.getPaymentOrderById(orderId);
        Boolean status=paymentService.ProccedPaymentOrder(order,paymentId);
        PaymentResponse res = new PaymentResponse();
        res.setPayment_url("deposite success");

        if(status){
            wallet=walletService.addBalanceToWallet(wallet, order.getAmount());
        }


        return new ResponseEntity<>(wallet,HttpStatus.OK);

    }

//    @PutMapping("/api/wallet/withdraw/amount/{amount}/user/{userId}")
//    public ResponseEntity<PaymentResponse> withdrawMoney(@PathVariable Long userId, @PathVariable Long amount) throws Exception {
//
//        String wallet = walleteService.depositFunds(userId,amount);
//
//        return new ResponseEntity<>(wallet,HttpStatus.OK);
//    }

    @PutMapping("/api/wallet/{walletId}/transfer")
    public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization")String jwt,
                                                        @PathVariable Long walletId,
                                                         @RequestBody WalletTransaction req
    ) throws Exception {
        User senderUser =userService.findUserProfileByJwt(jwt);


        Wallet reciverWallet = walletService.findWalletById(walletId);

        Wallet wallet = walletService.walletToWalletTransfer(senderUser,reciverWallet, req.getAmount());
        Long transferAmount = req.getAmount();
        // FIX: Use correct arguments for createTransaction (Wallet, WalletTransactionType, String, String, Long)
        walletTransactionService.createTransaction(
                wallet, WalletTransactionType.WALLET_TRANSFER,
                String.valueOf(transferAmount), "Wallet Transfer", System.currentTimeMillis());
        return new ResponseEntity<>(wallet, HttpStatus.OK);

    }


    @PutMapping("/api/wallet/order/{orderId}/pay")
    public ResponseEntity<Wallet> payOrderPayment(@PathVariable Long orderId,
                                                  @RequestHeader("Authorization")String jwt) throws Exception {
        User user =userService.findUserProfileByJwt(jwt);
        System.out.println("-------- "+orderId);
        Order order=orderService.getOrderById(orderId);

        Wallet wallet = walletService.payOrderPayment(order,user);

        return new ResponseEntity<>(wallet,HttpStatus.OK);

    }



}

