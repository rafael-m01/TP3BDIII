package org.example;

import javax.persistence.*;
import java.util.Date;
import java.time.Duration;

public class InfirmiereService {

    @EmbeddedId
    private InfirmiereServiceId infirmiereServiceId;

    @Column(name = "duree_personalitee", nullable = false)
    private Duration dureePersonalisee;

    public InfirmiereService(InfirmiereServiceId infirmiereServiceId) {
        this.infirmiereServiceId = infirmiereServiceId;
    }

    public InfirmiereService() {}

    public InfirmiereServiceId getInfirmiereServiceId() {
        return infirmiereServiceId;
    }

    public void setInfirmiereServiceId(InfirmiereServiceId infirmiereServiceId) {
        this.infirmiereServiceId = infirmiereServiceId;
    }

    public Duration getDureePersonalisee() {
        return dureePersonalisee;
    }

    public void setDureePersonalisee(Duration dureePersonalisee) {
        this.dureePersonalisee = dureePersonalisee;
    }
}
