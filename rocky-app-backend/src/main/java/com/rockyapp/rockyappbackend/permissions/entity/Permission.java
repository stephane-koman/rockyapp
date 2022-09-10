package com.rockyapp.rockyappbackend.permissions.entity;

import com.rockyapp.rockyappbackend.common.entity.AbstractSocleEntity;
import com.rockyapp.rockyappbackend.roles.entity.Role;
import com.rockyapp.rockyappbackend.users.entity.User;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "permissions")
@Builder
public class Permission extends AbstractSocleEntity {

    private static final long serialVersionUID = -2763266356324716508L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissions_id_generator")
    @SequenceGenerator(name = "permissions_id_generator", sequenceName = "permissions_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Transient
    private List<Role> roles = new ArrayList<>();

    @Transient
    private List<User> users = new ArrayList<>();
}
