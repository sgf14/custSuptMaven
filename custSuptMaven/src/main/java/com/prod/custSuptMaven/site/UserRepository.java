package com.prod.custSuptMaven.site;

import com.prod.custSuptMaven.site.entities.UserPrincipal;

public interface UserRepository extends GenericRepository<Long, UserPrincipal>
{
    UserPrincipal getByUsername(String username);
}

