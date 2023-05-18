package com.jbd.stock.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jbd.stock.domain.enumeration.UserType;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Utilisateur.
 */
@Entity
@Table(name = "utilisateur")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Utilisateur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @Column(name = "status")
    private String status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "email")
    private String email;

    @Column(name = "information")
    private String information;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private UserType type;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private Set<Paiement> paiements = new HashSet<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties(value = { "orderItems", "user" }, allowSetters = true)
    private Set<Order> orders = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_utilisateur__roles",
        joinColumns = @JoinColumn(name = "utilisateur_id"),
        inverseJoinColumns = @JoinColumn(name = "roles_id")
    )
    @JsonIgnoreProperties(value = { "users" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utilisateur id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Utilisateur code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNom() {
        return this.nom;
    }

    public Utilisateur nom(String nom) {
        this.setNom(nom);
        return this;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return this.prenom;
    }

    public Utilisateur prenom(String prenom) {
        this.setPrenom(prenom);
        return this;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public LocalDate getDateCreation() {
        return this.dateCreation;
    }

    public Utilisateur dateCreation(LocalDate dateCreation) {
        this.setDateCreation(dateCreation);
        return this;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public String getStatus() {
        return this.status;
    }

    public Utilisateur status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return this.phone;
    }

    public Utilisateur phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return this.email;
    }

    public Utilisateur email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInformation() {
        return this.information;
    }

    public Utilisateur information(String information) {
        this.setInformation(information);
        return this;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public UserType getType() {
        return this.type;
    }

    public Utilisateur type(UserType type) {
        this.setType(type);
        return this;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Utilisateur user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<Paiement> getPaiements() {
        return this.paiements;
    }

    public void setPaiements(Set<Paiement> paiements) {
        if (this.paiements != null) {
            this.paiements.forEach(i -> i.setUser(null));
        }
        if (paiements != null) {
            paiements.forEach(i -> i.setUser(this));
        }
        this.paiements = paiements;
    }

    public Utilisateur paiements(Set<Paiement> paiements) {
        this.setPaiements(paiements);
        return this;
    }

    public Utilisateur addPaiement(Paiement paiement) {
        this.paiements.add(paiement);
        paiement.setUser(this);
        return this;
    }

    public Utilisateur removePaiement(Paiement paiement) {
        this.paiements.remove(paiement);
        paiement.setUser(null);
        return this;
    }

    public Set<Order> getOrders() {
        return this.orders;
    }

    public void setOrders(Set<Order> orders) {
        if (this.orders != null) {
            this.orders.forEach(i -> i.setUser(null));
        }
        if (orders != null) {
            orders.forEach(i -> i.setUser(this));
        }
        this.orders = orders;
    }

    public Utilisateur orders(Set<Order> orders) {
        this.setOrders(orders);
        return this;
    }

    public Utilisateur addOrder(Order order) {
        this.orders.add(order);
        order.setUser(this);
        return this;
    }

    public Utilisateur removeOrder(Order order) {
        this.orders.remove(order);
        order.setUser(null);
        return this;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Utilisateur roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public Utilisateur addRoles(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
        return this;
    }

    public Utilisateur removeRoles(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utilisateur)) {
            return false;
        }
        return id != null && id.equals(((Utilisateur) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utilisateur{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", nom='" + getNom() + "'" +
            ", prenom='" + getPrenom() + "'" +
            ", dateCreation='" + getDateCreation() + "'" +
            ", status='" + getStatus() + "'" +
            ", phone='" + getPhone() + "'" +
            ", email='" + getEmail() + "'" +
            ", information='" + getInformation() + "'" +
            ", type='" + getType() + "'" +
            "}";
    }
}
