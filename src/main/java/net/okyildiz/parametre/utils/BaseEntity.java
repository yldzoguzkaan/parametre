package net.okyildiz.parametre.utils;


import jakarta.persistence.*;

import java.util.Date;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uid")
    private String uid;

    @Column(name = "created_date", nullable = false)
    private Date created;

    @Column(name = "updated_date")
    private Date updated;

    @Column(name = "create_user_uid", nullable = false)
    private String createUserId;

    @Column(name = "update_user_uid")
    private String updateUserUID;

    @Column(name = "version", columnDefinition = "INTEGER DEFAULT 1")
    @Version
    private Integer version;

    @Column(name = "status", columnDefinition = "TINYINT(1) DEFAULT 1")
    private Boolean status = true;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(String createUserId) {
        this.createUserId = createUserId;
    }

    public String getUpdateUserUID() {
        return updateUserUID;
    }

    public void setUpdateUserUID(String updateUserUID) {
        this.updateUserUID = updateUserUID;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
