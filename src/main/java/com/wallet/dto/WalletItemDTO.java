package com.wallet.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class WalletItemDTO {
    private Long id;
    @NotNull(message = "Insira o id da carteira")
    private Long wallet;
    @NotNull(message = "Informe a data")
    private Date date;
    @NotNull(message = "Informe o tipo da carteira")
    @Pattern(regexp = "^(ENTRADA|SAÍDA)$", message = "Para o tipo, somente será aceito ENTRADA ou SAÍDA")
    private String type;
    @NotNull(message = "Informe uma descrição da carteira")
    private String description;
    @NotNull(message = "Informe um valor")
    private BigDecimal value;
}
