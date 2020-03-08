package com.wallet.repository;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
public class WalletItemRepositoryTest {

    private static final Date DATE = new Date();
    private static final String TYPE = "EN";
    private static final String DESCRIPTION = "Conta de luz";
    private static final BigDecimal VALUE = BigDecimal.valueOf(65);

    @Autowired
    WalletItemsRepository repository;

    @Autowired
    WalletRepository walletRepository;
    @Test
    public void testSave(){
    Wallet w = new Wallet();
    w.setName("Carteira 1");
    w.setValue(BigDecimal.valueOf(500));

    walletRepository.save(w);

    WalletItem wi = new WalletItem(1L, w,DATE,TYPE,DESCRIPTION,VALUE);
    WalletItem response = repository.save(wi);
    assertNotNull(response);
    assertEquals(response.getDescription(), DESCRIPTION);
    assertEquals(response.getType(), TYPE);
    assertEquals(response.getValue(), VALUE);
    assertEquals(response.getDate(), DATE);
    }
}
