package net.okyildiz.parametre.utils;

import java.util.Date;

public abstract class BaseDTO {

    private String uid;
    private Date created;
    private Date updated;
    private String createUserId;
    private String updateUserUID;
    private Integer version;
    private Boolean status;

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
