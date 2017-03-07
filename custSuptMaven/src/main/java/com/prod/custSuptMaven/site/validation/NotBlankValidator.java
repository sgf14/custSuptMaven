package com.prod.custSuptMaven.site.validation;
/* class notes- helper class to NotBlank validator class.  kind of the guts of no empty string validation.  see isValid method below
 * 
 */
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotBlankValidator
        implements ConstraintValidator<NotBlank, CharSequence>
{
    @Override
    public void initialize(NotBlank annotation)
    {

    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context)
    {
        if(value instanceof String)
            return ((String) value).trim().length() > 0;
        return value.toString().trim().length() > 0;
    }
}
