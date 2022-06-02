package org.apache.commons.jcs3.auxiliary.lateral;

import org.apache.commons.jcs3.auxiliary.AuxiliaryCache;
import org.apache.commons.jcs3.auxiliary.AuxiliaryCacheAttributes;
import org.apache.commons.jcs3.engine.CacheStatus;
import org.apache.commons.jcs3.engine.behavior.ICacheElement;
import org.apache.commons.jcs3.engine.behavior.IElementSerializer;
import org.apache.commons.jcs3.engine.logging.behavior.ICacheEventLogger;
import org.apache.commons.jcs3.engine.match.behavior.IKeyMatcher;
import org.apache.commons.jcs3.engine.stats.behavior.IStats;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class LateralHttpNoOpCache<K, V> implements AuxiliaryCache<K, V> {
    private static final Log log = LogFactory.getLog(LateralHttpNoOpCache.class);

    @Override
    public Set<K> getKeySet() {
        log.info("Consuming getKeySet");
        return null;
    }

    @Override
    public IStats getStatistics() {
        log.info("Consuming getStatistics");
        return null;
    }

    @Override
    public AuxiliaryCacheAttributes getAuxiliaryCacheAttributes() {
        log.info("Consuming getAuxiliaryCacheAttributes");
        return null;
    }

    @Override
    public void setElementSerializer(IElementSerializer elementSerializer) {
        log.info("Consuming setElementSerializer");
    }

    @Override
    public void setCacheEventLogger(ICacheEventLogger cacheEventLogger) {
        log.info("Consuming setCacheEventLogger");
    }

    @Override
    public void update(ICacheElement<K, V> element) throws IOException {
        log.info("Consuming update");
    }

    @Override
    public ICacheElement<K, V> get(K key) {
        log.info("Consuming get");
        return null;
    }

    @Override
    public Map<K, ICacheElement<K, V>> getMultiple(Set<K> keys) {
        log.info("Consuming getMultiple");
        return null;
    }

    @Override
    public Map<K, ICacheElement<K, V>> getMatching(String pattern) {
        log.info("Consuming getMatching");
        return null;
    }

    @Override
    public boolean remove(K key) throws IOException {
        log.info("Consuming remove");
        return false;
    }

    @Override
    public void removeAll() throws IOException {
        log.info("Consuming removeAll");
    }

    @Override
    public void dispose() {
        log.info("Consuming dispose");
    }

    @Override
    public int getSize() {
        log.info("Consuming getSize");
        return 0;
    }

    @Override
    public CacheStatus getStatus() {
        log.info("Consuming getStatus");
        return null;
    }

    @Override
    public String getStats() {
        log.info("Consuming getStats");
        return null;
    }

    @Override
    public String getCacheName() {
        log.info("Consuming getCacheName");
        return null;
    }

    @Override
    public void setKeyMatcher(IKeyMatcher<K> keyMatcher) {
        log.info("Consuming setKeyMatcher");
    }

    @Override
    public CacheType getCacheType() {
        log.info("Consuming getCacheType");
        return CacheType.LATERAL_CACHE;
    }
}
