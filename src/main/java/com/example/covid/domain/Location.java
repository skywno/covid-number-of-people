package com.example.covid.domain;

import com.example.covid.constant.LocationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Getter
@ToString
@EqualsAndHashCode
@Table(indexes = {
        @Index(columnList = "locationName"),
        @Index(columnList = "address"),
        @Index(columnList = "phoneNumber"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Setter
    @Column(nullable = false, columnDefinition = "varchar(20) default 'COMMON'")
    @Enumerated(EnumType.STRING)
    private LocationType locationType;

    @Setter
    @Column(nullable = false)
    private String locationName;

    @Setter
    @Column(nullable = false)
    private String address;

    @Setter
    @Column(nullable = false)
    private String phoneNumber;

    @Setter
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer capacity;


    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @CreatedDate
    private LocalDateTime createdAt;

    @Column(nullable = false, insertable = false, updatable = false,
            columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    @LastModifiedDate
    private LocalDateTime modifiedAt;

    @ToString.Exclude
    @OrderBy("id")
    @OneToMany(mappedBy = "location")
    private final Set<Event> events = new LinkedHashSet<>();
    
    @ToString.Exclude
    @OneToMany(mappedBy = "location", cascade=CascadeType.REMOVE)
    private final Set<AdminLocationMap> adminLocationMaps = new LinkedHashSet<>();


    protected Location() {
    }

    protected Location(
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        this.locationType = locationType;
        this.locationName = locationName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.capacity = capacity;
    }

    public static Location of(
            LocationType locationType,
            String locationName,
            String address,
            String phoneNumber,
            Integer capacity
    ) {
        return new Location(locationType, locationName, address, phoneNumber,
                capacity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        return id != null && id.equals(((Location) obj).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationName, address, phoneNumber, createdAt, modifiedAt);
    }
}