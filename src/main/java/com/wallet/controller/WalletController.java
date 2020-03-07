package com.wallet.controller;


import com.wallet.dto.WalletDTO;
import com.wallet.entity.Wallet;
import com.wallet.response.Response;
import com.wallet.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("wallet")
public class WalletController {
    @Autowired
    private WalletService service;

    @PostMapping
    public ResponseEntity<Response<WalletDTO>> create(@Valid @RequestBody WalletDTO dto, BindingResult result){
        Response<WalletDTO> response = new Response<WalletDTO>();

        if(result.hasErrors()){
            result.getAllErrors().forEach(r -> response.getErrors().add(r.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Wallet w = service.save(this.convertDtoToEntity(dto));

        response.setData(this.convertEntityToDto(w));

        return ResponseEntity.ok(response);
    }

    private Wallet convertDtoToEntity(WalletDTO dto){
        Wallet wallet = new Wallet();

        wallet.setId(dto.getId());
        wallet.setName(dto.getName());
        wallet.setValue(dto.getValue());

        return wallet;
    }

    private WalletDTO convertEntityToDto(Wallet wallet){
        WalletDTO dto = new WalletDTO();

        dto.setId(wallet.getId());
        dto.setName(wallet.getName());
        dto.setValue(wallet.getValue());

        return dto;
    }

}
