package com.devsuperior.dscatolog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.devsuperior.dscatolog.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
