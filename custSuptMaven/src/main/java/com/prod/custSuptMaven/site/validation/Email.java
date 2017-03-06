package com.prod.custSuptMaven.site.validation;
/*class notes- Hibernate Validator interface.  Chap 16, pg454.  Validator in general implements business rules related to storing data in appropriate 
 * formats in the form of a new annotation that can be used by any class method needing the function.  See built in spring annotations pg 451.
 * this along with the others in this package create some new useful ones.  
 * see pg 453 for what an email validator would look like (regexp part) if used as a one-off.
 * In this particular app @email is no used, but @NotBlank is used on several fields in entity\package classes.
 * This specific validator checks email fields for proper format and like others throws an i18n error if data is non-compliant. implementation in code 
 * is done via annotations- note public @interface Email class name below. several of these are built in ie @NotNull.  this is a custom defined one
 * and as such is a demonstration of creating new/custom made annotations.
 * see pg 451 for several of the most commonly used ones that are defined by default.
 */
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Pattern;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//@SuppressWarnings("unused")
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^[a-z0-9`!#$%^&*'{}?/+=|_~-]+(\\.[a-z0-9`!#$%^&*'{}?/+=|" +
        "_~-]+)*@([a-z0-9]([a-z0-9-]*[a-z0-9])?)+(\\.[a-z0-9]" +
        "([a-z0-9-]*[a-z0-9])?)*$", flags = {Pattern.Flag.CASE_INSENSITIVE})
@ReportAsSingleViolation
public @interface Email
{
    String message() default "{com.prod.custSuptMaven.site.validation.Email.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
            ElementType.CONSTRUCTOR, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    static @interface List {
        Email[] value();
    }
}
