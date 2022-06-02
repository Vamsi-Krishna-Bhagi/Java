package org.apache.commons.jcs3.auxiliary.lateral;

import org.apache.commons.jcs3.auxiliary.AbstractAuxiliaryCacheFactory;
import org.apache.commons.jcs3.auxiliary.AuxiliaryCache;
import org.apache.commons.jcs3.auxiliary.AuxiliaryCacheAttributes;
import org.apache.commons.jcs3.auxiliary.lateral.behavior.ILateralCacheAttributes;
import org.apache.commons.jcs3.engine.behavior.ICacheServiceNonLocal;
import org.apache.commons.jcs3.engine.behavior.ICompositeCacheManager;
import org.apache.commons.jcs3.engine.behavior.IElementSerializer;
import org.apache.commons.jcs3.engine.logging.behavior.ICacheEventLogger;

import java.util.ArrayList;
import java.util.Objects;

public class LateralCacheFactory extends AbstractAuxiliaryCacheFactory {
    @Override
    public <K, V> AuxiliaryCache<K, V> createCache(AuxiliaryCacheAttributes attr,
                                                   ICompositeCacheManager cacheMgr,
                                                   ICacheEventLogger cacheEventLogger,
                                                   IElementSerializer elementSerializer) throws Exception {
        LateralHttpCacheAttributes lac = (LateralHttpCacheAttributes) attr;

        switch (lac.getTransmissionType()) {
            case HTTP -> {
                if (Objects.nonNull(lac.getHttpServers())) {
                    ICacheServiceNonLocal<K, V> service = new LateralHttpRemoteService<>(lac);
                    return new LateralHttpRemoteCache<>(lac, service);
                }
            }
            case TCP -> {
                throw new UnsupportedOperationException("TCP is yet to implement");
            }
            case UDP -> {
                throw new UnsupportedOperationException("UDP is yet to implement");
            }
            case XMLRPC -> {
                throw new UnsupportedOperationException("XMLRPC is yet to implement");
            }
        }
        return null;
    }
}
