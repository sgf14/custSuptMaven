package com.prod.custSuptMaven.site.repositories;

import org.springframework.data.repository.CrudRepository;

import com.prod.custSuptMaven.site.entities.UserPrincipal;

public interface UserRepository extends CrudRepository<UserPrincipal, Long>
{
    UserPrincipal getByUsername(String username);
}

