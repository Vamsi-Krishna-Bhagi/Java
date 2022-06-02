package org.apache.commons.jcs3.auxiliary.lateral;

import org.apache.commons.jcs3.auxiliary.AuxiliaryCacheAttributes;
import org.apache.commons.jcs3.auxiliary.lateral.behavior.ILateralCacheAttributes;
import org.apache.commons.jcs3.engine.behavior.ICacheElement;
import org.apache.commons.jcs3.engine.behavior.ICacheServiceNonLocal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

public class LateralHttpRemoteCache<K, V> extends LateralHttpNoOpCache<K, V> {
    private static Log log = LogFactory.getLog(LateralHttpRemoteCache.class);

    private final ICacheServiceNonLocal<K, V> lateralHttpRemoteService;
    private final String cacheName;

    public LateralHttpRemoteCache(ILateralCacheAttributes attr, ICacheServiceNonLocal<K, V> service) {
        this.lateralHttpRemoteService = service;
        this.cacheName = attr.getCacheName();
    }

    @Override
    public void update(ICacheElement<K, V> element) throws IOException {
        try {
            lateralHttpRemoteService.update(element);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean remove(K key) throws IOException {
        try {
            lateralHttpRemoteService.remove(cacheName, key);
            return true;
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void removeAll() throws IOException {
        try {
            lateralHttpRemoteService.removeAll(cacheName);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
