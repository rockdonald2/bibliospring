package edu.bbte.bibliospring.backend.model;

public abstract class BaseEntity extends AbstractModel { // NOSONAR

    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
