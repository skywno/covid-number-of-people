package com.example.covid.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
@Table(indexes = {
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class AdminLocationMap {

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @ManyToOne(optional = false)
    private Admin admin;

    @Setter
    @ManyToOne(optional = false)
    private Location location;


    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "datetime default CURRENT_TIMESTAMP on update " +
                    "CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime modifiedAt;


    protected AdminLocationMap() {
    }

    protected AdminLocationMap(Admin admin, Location location) {
        this.admin = admin;
        this.location = location;
    }

    public static AdminLocationMap of(Admin admin, Location location) {
        return new AdminLocationMap(admin, location);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((AdminLocationMap) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, admin, createdAt, modifiedAt);
    }

}
