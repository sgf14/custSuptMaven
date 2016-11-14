package com.prod.custSuptMaven.site;

import java.io.Serializable;
import java.security.Principal;

import javax.servlet.http.HttpSession;

public class UserPrincipal implements Principal, Cloneable, Serializable {
	static final long serialVersionUID = 1L;
	private final String username;

    public UserPrincipal(String username)
    {
        this.username = username;
    }

    @Override
    public String getName()
    {
        return this.username;
    }

    @Override
    public int hashCode()
    {
        return this.username.hashCode();
    }

    @Override
    public boolean equals(Object other)
    {
        return other instanceof UserPrincipal &&
                ((UserPrincipal)other).username.equals(this.username);
    }

    @Override
    @SuppressWarnings("CloneDoesntDeclareCloneNotSupportedException")
    protected UserPrincipal clone()
    {
        try {
            return (UserPrincipal)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // not possible
        }
    }

    @Override
    public String toString()
    {
        return this.username;
    }

    public static Principal getPrincipal(HttpSession session)
    {
        return session == null ? null :
                (Principal)session.getAttribute("com.prod.custSuptMaven.user.principal");
    }

    public static void setPrincipal(HttpSession session, Principal principal)
    {
        session.setAttribute("com.prod.custSuptMaven.user.principal", principal);
    }

}
