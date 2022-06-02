package org.apache.commons.jcs3.auxiliary.lateral;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.jcs3.access.exception.ObjectExistsException;
import org.apache.commons.jcs3.auxiliary.lateral.socket.tcp.LateralTCPService;
import org.apache.commons.jcs3.engine.CacheElement;
import org.apache.commons.jcs3.engine.CacheInfo;
import org.apache.commons.jcs3.engine.behavior.ICacheElement;
import org.apache.commons.jcs3.engine.behavior.IElementAttributes;
import org.apache.commons.jcs3.engine.behavior.IElementSerializer;
import org.apache.commons.jcs3.log.Log;
import org.apache.commons.jcs3.log.LogManager;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class LateralHttpRemoteService<K, V>
        extends LateralHttpNoOpService<K, V> {
    private static final Log log = LogManager.getLog(LateralHttpRemoteService.class);

    private final boolean allowPut;
    private final boolean issueRemoveOnPut;
    private final LateralHttpSender sender;

    private long listenerId = CacheInfo.listenerId;

    public LateralHttpRemoteService(final LateralHttpCacheAttributes lca) throws IOException {
        this.allowPut = lca.isAllowGet();
        this.issueRemoveOnPut = lca.isIssueRemoveOnPut();

        try {
            sender = new LateralHttpSender(lca);
            log.debug("Created senders to [{0}]", lca::getHttpServers);
        } catch (IOException e) {
            log.error("Could not create senders for {0}", lca::getHttpServers);
            throw e;
        }

    }

    @Override
    public void update(ICacheElement<K, V> item) throws ObjectExistsException, IOException {
        update(item, getListenerId());
    }

    @Override
    public void update(ICacheElement<K, V> item, long requesterId) throws IOException {
        // if we don't allow put, see if we should remove on put
        if (!this.allowPut &&
                // if we can't remove on put, and we can't put then return
                !this.issueRemoveOnPut) {
            return;
        }

        LateralHttpElementAttributes httpElementAttribues = null;
        if (item.getElementAttributes() instanceof LateralHttpElementAttributes) {
            httpElementAttribues = (LateralHttpElementAttributes) item.getElementAttributes();

        }else{
            httpElementAttribues = new LateralHttpElementAttributes();
        }
        if (Objects.isNull(httpElementAttribues.getRequestorId())) {
            httpElementAttribues.setRequestorId(requesterId);
            item.setElementAttributes(httpElementAttribues);

            // if we shouldn't remove on put, then put
            if (!this.issueRemoveOnPut) {
                final LateralElementDescriptor<K, V> led =
                        new LateralElementDescriptor<>(item, LateralCommand.UPDATE, requesterId);
                sender.sendAndReceive(led);
            }
            // else issue a remove with the hashcode for remove check on
            // on the other end, this will be a server config option
            else {
                log.debug("Issuing a remove for a put");

                final CacheElement<K, V> ce = new CacheElement<>(item.getCacheName(), item.getKey(), null);
                final LateralElementDescriptor<K, V> led =
                        new LateralElementDescriptor<>(ce, LateralCommand.REMOVE, requesterId);
                led.valHashCode = item.getVal().hashCode();
                sender.sendAndReceive(led);
            }
        }
    }

    @Override
    public void remove(String cacheName, K key) throws IOException {
        remove(cacheName, key, getListenerId());
    }

    @Override
    public void remove(String cacheName, K key, long requesterId) throws IOException {

        final CacheElement<K, V> ce = new CacheElement<>(cacheName, key, null);

        final LateralElementDescriptor<K, V> led =
                new LateralElementDescriptor<>(ce, LateralCommand.REMOVE, requesterId);
        sender.send(led);
    }

    @Override
    public void removeAll(String cacheName, long requesterId) throws IOException {
        final CacheElement<String, String> ce = new CacheElement<>(cacheName, "ALL", null);
        final LateralElementDescriptor<String, String> led =
                new LateralElementDescriptor<>(ce, LateralCommand.REMOVEALL, requesterId);
        sender.send(led);
    }

    @Override
    public void removeAll(String cacheName) throws IOException {
        removeAll(cacheName, getListenerId());
    }

    protected long getListenerId() {
        return listenerId;
    }
}
