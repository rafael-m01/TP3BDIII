package org.example;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
public class InfirmiereServiceId implements Serializable {

    private int infirmiereId;
    private int serviceId;

    public InfirmiereServiceId(int infirmiereId, int serviceId) {
        this.infirmiereId = infirmiereId;
        this.serviceId = serviceId;
    }

    public InfirmiereServiceId() {

    }
}
