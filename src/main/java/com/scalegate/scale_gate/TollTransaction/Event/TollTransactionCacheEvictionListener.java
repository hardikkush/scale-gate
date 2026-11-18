package com.scalegate.scale_gate.TollTransaction.Event;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.cache.Cache;

@Component
@RequiredArgsConstructor
public class TollTransactionCacheEvictionListener {

    private final CacheManager cacheManager;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void evictTollHistoryCache(TollTransactionCreatedEvent event) {

        Cache cache = cacheManager.getCache("tollHistory");

        if (cache != null) {
            cache.evict(event.vehicleId());
        }
    }
}
