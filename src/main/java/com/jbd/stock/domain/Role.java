package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnoreProperties(value = { "user", "paiements", "orders", "roles" }, allowSetters = true)
    private Set<Utilisateur> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Role name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Utilisateur> getUsers() {
        return this.users;
    }

    public void setUsers(Set<Utilisateur> utilisateurs) {
        if (this.users != null) {
            this.users.forEach(i -> i.removeRoles(this));
        }
        if (utilisateurs != null) {
            utilisateurs.forEach(i -> i.addRoles(this));
        }
        this.users = utilisateurs;
    }

    public Role users(Set<Utilisateur> utilisateurs) {
        this.setUsers(utilisateurs);
        return this;
    }

    public Role addUsers(Utilisateur utilisateur) {
        this.users.add(utilisateur);
        utilisateur.getRoles().add(this);
        return this;
    }

    public Role removeUsers(Utilisateur utilisateur) {
        this.users.remove(utilisateur);
        utilisateur.getRoles().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return id != null && id.equals(((Role) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
