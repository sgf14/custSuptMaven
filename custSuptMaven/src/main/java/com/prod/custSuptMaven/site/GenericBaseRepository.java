package com.prod.custSuptMaven.site;
/*class notes- jwa chap 21, pg 615. An abstract class that extends the GenericRepo interface (CRUD ops), and is then called by GenericJpaRepo..
 * it allows injection of Entity Manager and allows access to E and I as final
 * note Serializable interface is used for most database/persistence applications or transfers of data.  Stackoverflow has some good
 * explanations.  in short it converts an in memory (or live) object to bytes which can be stored to a file, database etc.  and deserialization 
 * converts bytes to an in-memory object.  JSON objects also use serialization for similar purposes- see LMPJS book- ajax section.  
 */
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class
        GenericBaseRepository<I extends Serializable, E extends Serializable>
    implements GenericRepository<I, E>
{
    //constructors to be implemented by GenericJpa.. that will extend this abstract class.  the JPA and its functions
	//will then be implemented by the end class
	protected final Class<I> idClass;
    protected final Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public GenericBaseRepository()
    {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        while(!(genericSuperclass instanceof ParameterizedType))
        {
            if(!(genericSuperclass instanceof Class))
                throw new IllegalStateException("Unable to determine type " +
                        "arguments because generic superclass neither " +
                        "parameterized type nor class.");
            if(genericSuperclass == GenericBaseRepository.class)
                throw new IllegalStateException("Unable to determine type " +
                        "arguments because no parameterized generic superclass " +
                        "found.");

            genericSuperclass = ((Class)genericSuperclass).getGenericSuperclass();
        }

        ParameterizedType type = (ParameterizedType)genericSuperclass;
        Type[] arguments = type.getActualTypeArguments();
        this.idClass = (Class<I>)arguments[0];
        this.entityClass = (Class<E>)arguments[1];
    }
}
