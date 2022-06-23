package sn.thiane.mefpai.ent.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Contenu.
 */
@Entity
@Table(name = "contenu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Contenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom_contenu")
    private String nomContenu;

    @Lob
    @Column(name = "contenu")
    private byte[] contenu;

    @Column(name = "contenu_content_type")
    private String contenuContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Contenu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomContenu() {
        return this.nomContenu;
    }

    public Contenu nomContenu(String nomContenu) {
        this.setNomContenu(nomContenu);
        return this;
    }

    public void setNomContenu(String nomContenu) {
        this.nomContenu = nomContenu;
    }

    public byte[] getContenu() {
        return this.contenu;
    }

    public Contenu contenu(byte[] contenu) {
        this.setContenu(contenu);
        return this;
    }

    public void setContenu(byte[] contenu) {
        this.contenu = contenu;
    }

    public String getContenuContentType() {
        return this.contenuContentType;
    }

    public Contenu contenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
        return this;
    }

    public void setContenuContentType(String contenuContentType) {
        this.contenuContentType = contenuContentType;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Contenu)) {
            return false;
        }
        return id != null && id.equals(((Contenu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Contenu{" +
            "id=" + getId() +
            ", nomContenu='" + getNomContenu() + "'" +
            ", contenu='" + getContenu() + "'" +
            ", contenuContentType='" + getContenuContentType() + "'" +
            "}";
    }
}
