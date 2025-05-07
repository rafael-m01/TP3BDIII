package org.example;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Embeddable
public class InfirmiereServiceId implements Serializable {

    @Column(name = "Infirmieres_id_infirmiere")
    private int infirmiereId;

    @Column(name = "Service_id_service")
    private int serviceId;

    public InfirmiereServiceId(int infirmiereId, int serviceId) {
        this.infirmiereId = infirmiereId;
        this.serviceId = serviceId;
    }

    public InfirmiereServiceId() {

    }
}
