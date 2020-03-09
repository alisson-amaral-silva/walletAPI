package com.wallet.repository;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.util.enums.TypeEnum;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@DataJpaTest
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class WalletItemRepositoryTest {

    private static final Date DATE = new Date();
    private static final TypeEnum TYPE = TypeEnum.EN;
    private static final String DESCRIPTION = "Conta de Luz";
    private static final BigDecimal VALUE = BigDecimal.valueOf(65);
    private Long savedWalletItemId = null;
    private Long savedWalletId = null;

    @Autowired
    WalletItemsRepository repository;

    @Autowired
    WalletRepository walletRepository;

    @Before
    public void setUp() {
        Wallet w = new Wallet();
        w.setName("Carteira Teste");
        w.setValue(BigDecimal.valueOf(250));
        walletRepository.save(w);

        WalletItem wi = new WalletItem(null, w, TYPE, DESCRIPTION, VALUE, DATE);
        repository.save(wi);

        savedWalletItemId = wi.getId();
        savedWalletId = w.getId();
    }

    @After
    public void tearDown() {
        repository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    public void testSave() {
        Wallet w = new Wallet();
        w.setName("Carteira 1");
        w.setValue(BigDecimal.valueOf(500));
        walletRepository.save(w);

        WalletItem wi = new WalletItem(null, w, TYPE, DESCRIPTION, VALUE, DATE);

        WalletItem response = repository.save(wi);

        assertNotNull(response);
        assertEquals(response.getDescription(), DESCRIPTION);
        assertEquals(response.getType(), TYPE);
        assertEquals(response.getValue(), VALUE);
        assertEquals(response.getWallet().getId(), w.getId());
        assertEquals(response.getDate(), DATE);
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveInvalidWalletItem() {
        WalletItem wi = new WalletItem(null, null, null, DESCRIPTION, null, DATE);
        repository.save(wi);
    }

    @Test
    public void testUpdate() {

        Optional<WalletItem> wir = repository.findById(savedWalletItemId);

        String description = "Descrição alterada";

        WalletItem changed = wir.get();
        changed.setDescription(description);

        repository.save(changed);

        Optional<WalletItem> newWalletItem = repository.findById(savedWalletItemId);

        assertEquals(description, newWalletItem.get().getDescription());
    }

    @Test
    public void deleteWalletItem() {
        Optional<Wallet> wallet = walletRepository.findById(savedWalletId);
        WalletItem wi = new WalletItem(null, wallet.get(), TYPE, DESCRIPTION, VALUE, DATE);

        repository.save(wi);

        repository.deleteById(wi.getId());

        Optional<WalletItem> response = repository.findById(wi.getId());

        assertFalse(response.isPresent());
    }

    public void testFindBetweenDates() {
        Optional<Wallet> w = walletRepository.findById(savedWalletId);

        LocalDateTime localDateTime = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Date currentDatePlusFiveDays = Date.from(localDateTime.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
        Date currentDatePlusSevenDays = Date.from(localDateTime.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());


        repository.save(new WalletItem(null, w.get(), TYPE, DESCRIPTION, VALUE, currentDatePlusFiveDays));
        repository.save(new WalletItem(null, w.get(), TYPE, DESCRIPTION, VALUE, currentDatePlusSevenDays));

        PageRequest pg = PageRequest.of(0, 10);
        Page<WalletItem> response = repository.findAllByWalletId(savedWalletId, DATE, currentDatePlusFiveDays, pg);

        assertEquals(response.getContent().size(), 2);
        assertEquals(response.getTotalElements(), 2);
        assertEquals(response.getContent().get(0).getWallet().getId(), savedWalletId);
    }

    @Test
    public void testFindByType() {
        List<WalletItem> response = repository.findByWalletIdAndType(savedWalletId, TYPE);

        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getType(), TYPE);
    }

    @Test
    public void testFindByTypeSd() {

        Optional<Wallet> w = walletRepository.findById(savedWalletId);

        repository.save(new WalletItem(null, w.get(), TypeEnum.SD, DESCRIPTION, VALUE, DATE));

        List<WalletItem> response = repository.findByWalletIdAndType(savedWalletId, TypeEnum.SD);

        assertEquals(response.size(), 1);
        assertEquals(response.get(0).getType(), TypeEnum.SD);
    }

    @Test
    public void testSumByWallet() {
        Optional<Wallet> w = walletRepository.findById(savedWalletId);

        repository.save(new WalletItem(1L, w.get(),TYPE, DESCRIPTION, BigDecimal.valueOf(150.80),  DATE));

        BigDecimal response = repository.sumByWalletId(savedWalletId);

        assertEquals(response.compareTo(BigDecimal.valueOf(215.8)), 0);
    }
}