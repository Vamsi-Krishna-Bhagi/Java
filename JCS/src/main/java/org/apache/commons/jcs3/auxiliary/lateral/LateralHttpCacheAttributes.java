package org.apache.commons.jcs3.auxiliary.lateral;

public class LateralHttpCacheAttributes extends LateralCacheAttributes {

    private static final boolean DEFAULT_ALLOW_PUT = true;
    private static final boolean DEFAULT_ISSUE_REMOVE_FOR_PUT = false;
    /** default - Only block for 1 second before timing out on a read.*/
    private static final int DEFAULT_SOCKET_TIME_OUT = 1000;

    /** default - Only block for 2 seconds before timing out on startup.*/
    private static final int DEFAULT_OPEN_TIMEOUT = 2000;

    private String httpServers;
    private String httpReceiveServlet;
    private String httpDeleteServlet;

    /**
     * can we go laterally for a get
     */
    private boolean allowGet = DEFAULT_ALLOW_PUT;

    /**
     * call remove when there is a put
     */
    private boolean issueRemoveOnPut = DEFAULT_ISSUE_REMOVE_FOR_PUT;

    /** Only block for openTimeOut seconds before timing out on startup. */
    private int openTimeOut = DEFAULT_OPEN_TIMEOUT;

    /** Only block for socketTimeOut seconds before timing out on a read.  */
    private int socketTimeOut = DEFAULT_SOCKET_TIME_OUT;
    private long requesterId;

    public String getHttpServers() {
        return httpServers;
    }

    public void setHttpServers(String httpServers) {
        this.httpServers = httpServers;
    }

    public String getHttpReceiveServlet() {
        return httpReceiveServlet;
    }

    public void setHttpReceiveServlet(String httpReceiveServlet) {
        this.httpReceiveServlet = httpReceiveServlet;
    }

    public String getHttpDeleteServlet() {
        return httpDeleteServlet;
    }

    public void setHttpDeleteServlet(String httpDeleteServlet) {
        this.httpDeleteServlet = httpDeleteServlet;
    }

    public boolean isAllowGet() {
        return allowGet;
    }

    public void setAllowGet(boolean allowGet) {
        this.allowGet = allowGet;
    }

    public boolean isIssueRemoveOnPut() {
        return issueRemoveOnPut;
    }

    public void setIssueRemoveOnPut(boolean issueRemoveOnPut) {
        this.issueRemoveOnPut = issueRemoveOnPut;
    }

    public int getOpenTimeOut() {
        return openTimeOut;
    }

    public void setOpenTimeOut(int openTimeOut) {
        this.openTimeOut = openTimeOut;
    }

    public int getSocketTimeOut() {
        return socketTimeOut;
    }

    public void setSocketTimeOut(int socketTimeOut) {
        this.socketTimeOut = socketTimeOut;
    }

    public void setRequestorId(long requesterId) {
        this.requesterId = requesterId;
    }

    public long getRequesterId() {
        return requesterId;
    }
}
