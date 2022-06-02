package org.apache.commons.jcs3.auxiliary.lateral;

import org.apache.commons.jcs3.access.exception.ObjectExistsException;
import org.apache.commons.jcs3.access.exception.ObjectNotFoundException;
import org.apache.commons.jcs3.auxiliary.lateral.socket.tcp.LateralTCPService;
import org.apache.commons.jcs3.engine.behavior.ICacheElement;
import org.apache.commons.jcs3.engine.behavior.ICacheServiceNonLocal;
import org.apache.commons.jcs3.log.Log;
import org.apache.commons.jcs3.log.LogManager;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class LateralHttpNoOpService<K, V> implements ICacheServiceNonLocal<K, V> {
    private static final Log log = LogManager.getLog(LateralHttpNoOpService.class);

    @Override
    public void update(ICacheElement<K, V> item, long requesterId) throws IOException {
        log.info("Consuming update");
    }

    @Override
    public void remove(String cacheName, K key, long requesterId) throws IOException {
        log.info("Consuming remove");
    }

    @Override
    public void removeAll(String cacheName, long requesterId) throws IOException {
        log.info("Consuming removeAll");
    }

    @Override
    public ICacheElement<K, V> get(String cacheName, K key, long requesterId) throws IOException {
        log.info("Consuming get");
        return null;
    }

    @Override
    public Map<K, ICacheElement<K, V>> getMultiple(String cacheName, Set<K> keys, long requesterId) {
        log.info("Consuming getMultiple");
        return null;
    }

    @Override
    public Map<K, ICacheElement<K, V>> getMatching(String cacheName, String pattern, long requesterId) throws IOException {
        log.info("Consuming getMatching");
        return null;
    }

    @Override
    public Set<K> getKeySet(String cacheName) throws IOException {
        log.info("Consuming getKeySet");
        return null;
    }

    @Override
    public void update(ICacheElement<K, V> item) throws ObjectExistsException, IOException {
        log.info("Consuming update");

    }

    @Override
    public ICacheElement<K, V> get(String cacheName, K key) throws ObjectNotFoundException, IOException {
        log.info("Consuming get");
        return null;
    }

    @Override
    public Map<K, ICacheElement<K, V>> getMultiple(String cacheName, Set<K> keys) throws ObjectNotFoundException, IOException {
        log.info("Consuming getMultiple");
        return null;
    }

    @Override
    public Map<K, ICacheElement<K, V>> getMatching(String cacheName, String pattern) throws IOException {
        log.info("Consuming getMatching");
        return null;
    }

    @Override
    public void remove(String cacheName, K key) throws IOException {
        log.info("Consuming remove");
    }

    @Override
    public void removeAll(String cacheName) throws IOException {
        log.info("Consuming removeAll");
    }

    @Override
    public void dispose(String cacheName) throws IOException {
        log.info("Consuming dispose");
    }

    @Override
    public void release() throws IOException {
        log.info("Consuming release");
    }
}
