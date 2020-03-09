package com.wallet.repository;

import com.wallet.entity.Wallet;
import com.wallet.entity.WalletItem;
import com.wallet.service.WalletItemService;
import com.wallet.util.enums.TypeEnum;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
//@SpringBootTest
public class WalletItemRepositoryTest {

    private static final Date DATE = new Date();
    private static final TypeEnum TYPE = TypeEnum.EN;
    private static final String DESCRIPTION = "Conta de luz";
    private static final BigDecimal VALUE = BigDecimal.valueOf(65);
    private Long savedWalletItemId = null;
    private Long savedWalletId = null;

    @MockBean
    WalletItemsRepository repository;

    @Autowired
    WalletItemService service;

    @MockBean
    WalletRepository walletRepository;

    @BeforeEach()
    public void setUp(){
        Wallet w = new Wallet();
        w.setName("Carteira 2");
        w.setValue(BigDecimal.valueOf(600));

        walletRepository.save(w);

        WalletItem wi = new WalletItem(null, w,TYPE,DESCRIPTION,VALUE, DATE);
        WalletItem response = repository.save(wi);

        savedWalletId = w.getId();
        savedWalletItemId= wi.getId();
    }

    @AfterEach()
    public void tearDown(){
        repository.deleteAll();
        walletRepository.deleteAll();
    }

    @Test
    public void testSave(){
    Wallet w = new Wallet();
    w.setName("Carteira 2");
    w.setValue(BigDecimal.valueOf(500));

    walletRepository.save(w);

    WalletItem wi = new WalletItem(null, w,TYPE,DESCRIPTION,VALUE, DATE);
    WalletItem response = repository.save(wi);
    assertNotNull(response);
    assertEquals(response.getDescription(), DESCRIPTION);
    assertEquals(response.getType(), TYPE);
    assertEquals(response.getValue(), VALUE);
    assertEquals(response.getDate(), DATE);
    savedWalletId = w.getId();
    savedWalletItemId= wi.getId();
    }

    @Test(expected = ConstraintViolationException.class)
    public void testSaveInvalidWalletItem(){
        WalletItem wi = new WalletItem(null,null, null, DESCRIPTION, null, DATE);
        repository.save(wi);
    }

    @Test
    public void testFindBetweenDates(){
        List<WalletItem> list = new ArrayList<>();
        list.add(getMockWalletItem());
        Page<WalletItem> page = new PageImpl(list);
        BDDMockito.given(repository.findAllByWalletId(Mockito.anyLong(), DATE, Mockito.any(Date.class), Mockito.any(PageRequest.class))).willReturn(page);
        Page<WalletItem> response = service.findBetweenDates(1L, new Date(), new Date(), 0);

        assertNotNull(response);
//        Optional<Wallet> wallet = walletRepository.findById(1L);
//
//
//        LocalDateTime localDateTime = DATE.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//        Date currentDatePlusFiveDays = Date.from(localDateTime.plusDays(5).atZone(ZoneId.systemDefault()).toInstant());
//        Date currentDatePlusSevenDays = Date.from(localDateTime.plusDays(7).atZone(ZoneId.systemDefault()).toInstant());
//
//        repository.save(new WalletItem(null,wallet.get(),TYPE,DESCRIPTION,VALUE, currentDatePlusFiveDays));
//        repository.save(new WalletItem(null,wallet.get(),TYPE,DESCRIPTION,VALUE, currentDatePlusSevenDays));
//
//        Sort sort = Sort.unsorted();
//        PageRequest pg = PageRequest.of(0, 0);
//        Page<WalletItem> response = repository.findAllByWalletId(1L, DATE, currentDatePlusSevenDays, pg);
//        assertEquals(response.getContent().size(), 2);
    }

    private WalletItem getMockWalletItem(){
        Wallet w = new Wallet();
        w.setName("Carteira 2");
        w.setValue(BigDecimal.valueOf(500));
        walletRepository.save(w);

        WalletItem wi = new WalletItem(1L, w, TYPE, DESCRIPTION,VALUE, DATE);
        return wi;
    }

    @Test
    public void testFindByType(){
        Wallet w = new Wallet();
        w.setName("Carteira 2");
        w.setValue(BigDecimal.valueOf(500));

        walletRepository.save(w);

        WalletItem wi = new WalletItem(null, w,TYPE,DESCRIPTION,VALUE, DATE);
        WalletItem wis = new WalletItem(null, w,TYPE,DESCRIPTION,VALUE, DATE);

        repository.save(wi);
        repository.save(wis);

        List<WalletItem> response = repository.findByWalletIdAndType(1L, TYPE);

        assertEquals(response.size(), 2);
    }

    @Test
    public void testFindByTypeSd(){
        Wallet w = new Wallet();
        w.setName("Carteira 2");
        w.setValue(BigDecimal.valueOf(500));

        walletRepository.save(w);

        WalletItem wi = new WalletItem(null, w,TypeEnum.SD,DESCRIPTION,VALUE, DATE);

        repository.save(wi);

        List<WalletItem> response = repository.findByWalletIdAndType(1L, TypeEnum.SD);

        assertEquals(response.size(), 1);
    }
    @Test
    public void testSumByWallet(){
        Wallet w = new Wallet();
        w.setName("Carteira 2");
        w.setValue(BigDecimal.valueOf(500));
        walletRepository.save(w);

        Optional<Wallet> walletResponseId = walletRepository.findById(1L);

        WalletItem wi = repository.save(new WalletItem(null, walletResponseId.get(), TYPE, DESCRIPTION, BigDecimal.valueOf(150.80), DATE));
        BigDecimal response = repository.sumByWalletId(1L);
        assertEquals(response.compareTo(BigDecimal.valueOf(215.8)),-1);
    }

    @Test
    public void testUpdate(){
        Wallet w = new Wallet();
        w.setName("Carteira 2");
        w.setValue(BigDecimal.valueOf(500));

        walletRepository.save(w);
        Optional<Wallet> wallet = walletRepository.findById(1L);
        WalletItem wi = new WalletItem(null, wallet.get(), TYPE, DESCRIPTION, VALUE, DATE);

        repository.save(wi);

        Optional<WalletItem> findWi = repository.findById(1L);
        String changedDescription = "Descrição Alterada";
        WalletItem changed = findWi.get();
        changed.setDescription(changedDescription);

        repository.save(changed);

        Optional<WalletItem> newWalletItem = repository.findById(1L);
        assertEquals(changedDescription, newWalletItem.get().getDescription());
    }

    @Test
    public void deleteWalletItem(){
        Wallet w = new Wallet();
        w.setName("Carteira 2");
        w.setValue(BigDecimal.valueOf(500));

        walletRepository.save(w);
        Optional<Wallet> wallet = walletRepository.findById(1L);
        WalletItem wi = new WalletItem(null, wallet.get(), TYPE, DESCRIPTION, VALUE, DATE);

        repository.save(wi);

        repository.deleteById(wi.getId());

        Optional<WalletItem> response = repository.findById(wi.getId());
        assertFalse(response.isPresent());
    }
}
