package org.apache.commons.jcs3.auxiliary.lateral;

import org.apache.commons.jcs3.auxiliary.lateral.socket.tcp.LateralTCPSender;
import org.apache.commons.jcs3.log.Log;
import org.apache.commons.jcs3.log.LogManager;
import org.apache.commons.jcs3.utils.serialization.StandardSerializer;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LateralHttpSender {
    /**
     * The logger
     */
    private static final Log log = LogManager.getLog(LateralTCPSender.class);

    public static final int DEFAULT_HTTP_PORT = 443;

    private final List<String> httpEndpoints = new ArrayList<>();

    private final String receiveServlet;
    private final CloseableHttpAsyncClient client;

    /**
     * Use to synchronize multiple threads that may be trying to get.
     */
    private final Lock lock = new ReentrantLock(true);

    public LateralHttpSender(final LateralHttpCacheAttributes lca)
            throws IOException {

        this.receiveServlet = lca.getHttpReceiveServlet();

        final String httpServers = lca.getHttpServers();
        if (Objects.isNull(httpServers)) {
            throw new IOException("Invalid servers (null)");
        }

        for (String httpServer : httpServers.split(",")) {
            log.info("Attempting connection to [{0}]", httpServer);

            final int port = httpServer.contains(":")
                    ? Integer.parseInt(httpServer.substring(httpServer.lastIndexOf(":") + 1))
                    : DEFAULT_HTTP_PORT;
            final String host = httpServer.contains(":")
                    ? httpServer.substring(0, httpServer.lastIndexOf(":"))
                    : httpServer;

            httpEndpoints.add(host + ":" + port);
        }

        client = HttpAsyncClients.createDefault();
        client.start();
    }

    /**
     * Sends commands to the lateral cache listener.
     * <p>
     *
     * @param led
     * @throws IOException
     */
    public <K, V> void send(final LateralElementDescriptor<K, V> led)
            throws IOException {

        log.debug("sending LateralElementDescriptor");

        if (led == null) {
            return;
        }

        lock.lock();
        try {

            final HttpEntity entity = buildEntity(led);

            httpEndpoints.forEach(t -> {
                String host = "http://" + t + receiveServlet;
                final HttpPost postRequest = new HttpPost(host);

                postRequest.setEntity(entity);

                client.execute(postRequest, null);
            });

        } finally {
            lock.unlock();
        }
    }

    public <K, V> Object sendAndReceive(final LateralElementDescriptor<K, V> led)
            throws IOException {
        if (led == null) {
            return null;
        }

        final HttpEntity entity = buildEntity(led);

        CountDownLatch latch = new CountDownLatch(1);

        httpEndpoints.stream().map(t -> {
                    HttpPost httpPost = new HttpPost("http://" + t + receiveServlet);
                    httpPost.setEntity(entity);
                    return httpPost;
                })
                .forEach(postRequest -> client.execute(postRequest, new FutureCallback<>() {

                    @Override
                    public void completed(HttpResponse result) {
                        latch.countDown();
                        log.info("Http call completed for {0} with status code {1}",
                                postRequest.getURI(), result.getStatusLine().getStatusCode());
                    }

                    @Override
                    public void failed(Exception ex) {
                        latch.countDown();
                        log.info("Http call failed for {0} with exception: {1}",
                                postRequest.getURI(), ex.getLocalizedMessage());
                    }

                    @Override
                    public void cancelled() {
                        latch.countDown();
                        log.info("Http call cancelled for {0}", postRequest.getURI());
                    }
                }));

        try {
            latch.await();
            return "COMPLETED";
        } catch (InterruptedException e) {
            log.error(e);
            throw new IOException(e);
        }
    }

    /**
     * Closes connection used by all LateralTCPSenders for this lateral connection. Dispose request
     * should come into the facade and be sent to all lateral cache services. The lateral cache
     * service will then call this method.
     * <p>
     *
     * @throws IOException
     */
    public void dispose()
            throws IOException {
        log.info("Dispose called");
        client.close();
    }

    private <K, V> HttpEntity buildEntity(LateralElementDescriptor<K, V> led) throws IOException {
        List<NameValuePair> params = new ArrayList<>(1);
        try {
            params.add(new BasicNameValuePair("data",
                    new String(new StandardSerializer().serialize(led))));
        } catch (IOException e) {
            log.error(e);
            throw e;
        }
        return new ByteArrayEntity(new StandardSerializer().serialize(led));
    }
}
