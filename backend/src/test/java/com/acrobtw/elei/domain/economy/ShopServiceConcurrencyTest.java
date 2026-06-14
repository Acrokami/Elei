package com.acrobtw.elei.domain.economy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.acrobtw.elei.domain.auth.enums.AuthProvider;
import com.acrobtw.elei.domain.economy.repository.InventoryItemRepository;
import com.acrobtw.elei.domain.economy.repository.ShopItemRepository;
import com.acrobtw.elei.domain.economy.repository.WalletRepository;
import com.acrobtw.elei.domain.user.User;
import com.acrobtw.elei.domain.user.UserRepository;

@DataJpaTest
@Transactional(propagation = Propagation.NOT_SUPPORTED)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ShopService.class)
@Testcontainers
public class ShopServiceConcurrencyTest {

   @Container
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private ShopService shopService;
    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private ShopItemRepository shopItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InventoryItemRepository inventoryRepository;

    private Long testItemId;
    private final String TEST_USER = "concurrency_tester";


    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername(TEST_USER);
        user.setEmail("test@elei.com");
        user.setPassword("hashed_password");
        user.setProvider(AuthProvider.LOCAL);
        userRepository.save(user);

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(500L);
        walletRepository.save(wallet);

        ShopItem item = new ShopItem();
        item.setTitle("Limited Title");
        item.setPrice(500L);
        item.setStock(1);
        item = shopItemRepository.save(item);
        testItemId = item.getId();
    }

    @AfterEach
    void tearDown() {
        inventoryRepository.deleteAll();
        shopItemRepository.deleteAll();
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void purchaseItem_ConcurrentAccess_ShouldOnlyAllowOnePurchase() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(threadCount);

        AtomicInteger successfulPurchases = new AtomicInteger(0);
        AtomicInteger failedPurchases = new AtomicInteger(0);

        for(int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    startLatch.await();
                    shopService.purchaseItem(TEST_USER, testItemId);
                    successfulPurchases.incrementAndGet();
                } catch (Exception e) {
                    failedPurchases.incrementAndGet();
                } finally {
                    endLatch.countDown();
                }
            });
        }
        startLatch.countDown();
        endLatch.await();
        assertEquals(1, successfulPurchases.get(), "Only one purchase should succeed");
        assertEquals(9, failedPurchases.get(), "Nine purchases should fail due to lack of stock/funds");

        Wallet wallet = walletRepository.findByUserUsername(TEST_USER).orElseThrow();

        assertEquals(0L, wallet.getBalance(), "Balance should be exactly 0, not negative");

        ShopItem item = shopItemRepository.findById(testItemId).orElseThrow();
        assertEquals(0, item.getStock(), "Stock should be exactly 0, not negative");

        long inventoryItems = inventoryRepository.count();
        assertEquals(1, inventoryItems, "User should have exactly 1 item in inventory");
    }
}
