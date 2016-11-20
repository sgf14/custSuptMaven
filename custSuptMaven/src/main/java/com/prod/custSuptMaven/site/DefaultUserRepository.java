package com.prod.custSuptMaven.site;

import org.springframework.stereotype.Repository;

import com.prod.custSuptMaven.site.entities.UserPrincipal;

@Repository
public class DefaultUserRepository
        extends GenericJpaRepository<Long, UserPrincipal>
        implements UserRepository
{
    @Override
    public UserPrincipal getByUsername(String username)
    {
        return this.entityManager.createQuery(
                "SELECT u FROM UserPrincipal u WHERE u.username = :username",
                UserPrincipal.class
        ).setParameter("username", username).getSingleResult();
    }
}
