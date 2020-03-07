package com.wallet.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class UserWalletDTO {

    private Long id;
    @NotNull(message = "Informe o id do usuario")
    private Long user;
    @NotNull(message = "Informe o id da carteira")
    private Long wallet;
}
