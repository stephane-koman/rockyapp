package com.rockyapp.rockyappbackend.roles.entity;

import com.rockyapp.rockyappbackend.common.entity.AbstractSocleEntity;
import com.rockyapp.rockyappbackend.permissions.entity.Permission;
import com.rockyapp.rockyappbackend.users.entity.User;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
@Builder
public class Role extends AbstractSocleEntity {

    private static final long serialVersionUID = -2317045897686365984L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_generator")
    @SequenceGenerator(name = "roles_id_generator", sequenceName = "roles_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Transient
    private List<User> users = new ArrayList<>();

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany()
    @JoinTable(
            name = "roles_permissions",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    private List<Permission> permissions = new ArrayList<>();
}
