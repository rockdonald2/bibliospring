package edu.bbte.bibliospring.backend.model;

import java.util.Objects;
import java.util.UUID;

public abstract class AbstractModel {

    protected String uuid;

    protected AbstractModel() {

    }

    public String getUuid() {
        if (Objects.isNull(uuid)) {
            this.uuid = UUID.randomUUID().toString();
        }

        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractModel that = (AbstractModel) o;
        return Objects.equals(getUuid(), that.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }

}
