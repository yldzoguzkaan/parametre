package net.okyildiz.parametre.item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import net.okyildiz.parametre.utils.BaseEntity;

@Entity
@Table(name = "tbl_parametre")
public class ParameterEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "type", nullable = false, length = 100)
    private String type;

    @Column(name = "description", length = 250)
    private String description;

    public ParameterEntity() {}

    public ParameterEntity(String name, String type, String description) {
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
