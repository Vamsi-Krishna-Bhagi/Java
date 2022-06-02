package org.apache.commons.jcs3.auxiliary.lateral;

import org.apache.commons.jcs3.engine.ElementAttributes;

public class LateralHttpElementAttributes extends ElementAttributes {
    private Long requestorId;

    public Long getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(Long requestorId) {
        this.requestorId = requestorId;
    }
}
