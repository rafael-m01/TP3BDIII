package org.example;

import javax.persistence.*;
import java.sql.Time;
import java.time.LocalTime;
import java.util.Date;
import java.time.Duration;
@Entity
@Table(name = "infirmiere_service")

public class InfirmiereService {
    @EmbeddedId
    private InfirmiereServiceId infirmiereServiceId;

    @Column(name = "duree_personalisee", nullable = true)
    private Integer dureePersonalisee = 0;

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

    public Integer getDureePersonalisee() {
        return dureePersonalisee;
    }

    public void setDureePersonalisee(Integer dureePersonalisee) {
        this.dureePersonalisee = dureePersonalisee;
    }
}
