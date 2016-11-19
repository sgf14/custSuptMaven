package com.prod.custSuptMaven.site;
/* class notes- for SQL- see JWA chap 21 pg 613.  this interface is used to setup normal CRUD
 * methods to interact with the DB.  Any class that houses persisted data will make use of this 
 * interface.  the alternative would be to include alot of redundant code in each different class
 * and that is often considered bad coding practice-note 1.  However, you need java generics to pass this data in.  
 * I= the Id and E= the entity (object) that houses the data for that unique id.
 * 
 * note 1- the obvious downside of this approach is that it is a more advanced method and could make it a 
 * little more difficult to diagnose later.  elements are kind of hidden behind interfaces unless you know 
 * how to interpret the implementations.  you could always repeat the required code in each class to make it more obvious, 
 * but then you run into the problems of violating the DRY approach to programming, and open up the code to more
 * chances of typos, errors, etc.
 */
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Validated
public interface GenericRepository<I extends Serializable, E extends Serializable>
{
    @NotNull
    Iterable<E> getAll();

    E get(@NotNull I id);

    void add(@NotNull E entity);

    void update(@NotNull E entity);

    void delete(@NotNull E entity);

    void deleteById(@NotNull I id);
}
