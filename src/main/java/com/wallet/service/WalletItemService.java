package com.wallet.service;

import com.wallet.entity.WalletItem;
import com.wallet.util.enums.TypeEnum;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface WalletItemService {
    WalletItem save (WalletItem i);

    Page<WalletItem> findBetweenDates(Long walletItem, Date start, Date end, int page);

    List<WalletItem> findByWalletIdAndType(Long wallet, TypeEnum type);

    Optional<WalletItem> findById(Long id);

    BigDecimal sumByWalletId(Long wallet);

    void deleteById(Long id);
}
