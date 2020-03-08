package com.wallet.repository;

import com.wallet.entity.WalletItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletItemsRepository extends JpaRepository<WalletItem, Long> {
}
