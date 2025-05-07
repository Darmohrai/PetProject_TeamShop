package org.example.teamshop.annotation.PermissionCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for performing access control checks before method execution.
 * <p>
 * <b>Important NOTE:</b> This annotation use {@code permissionHandler} bean,
 * which is expected to be an instance of {@link org.example.teamshop.security.PermissionHandler}.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @PermissionCheck("#permissionHandler.hasAccess(#userId)")
 * public void someMethod(String userId) {
 *     // business logic
 * }
 * }
 * </pre>
 *
 * @see org.example.teamshop.annotation.PermissionCheck.PermissionCheckAspect
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCheck {
    String value();
}