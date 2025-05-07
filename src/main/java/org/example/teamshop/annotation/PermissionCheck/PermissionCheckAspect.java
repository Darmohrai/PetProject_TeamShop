package org.example.teamshop.annotation.PermissionCheck;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.teamshop.Exception.InternalSecurityLogicException;
import org.example.teamshop.Exception.PermissionDeniedException;
import org.example.teamshop.security.PermissionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

/**
 * Aspect responsible for evaluating access permissions using SpEL expressions
 * defined in the {@link PermissionCheck} annotation.
 *
 * <p>This aspect is triggered before the execution of any method annotated with {@code @PermissionCheck}.
 * It evaluates the specified SpEL expression in the context of the method's parameters.
 *
 * <p>The expression can access:
 * <ul>
 *   <li>Method arguments using {@code #parameterName} syntax</li>
 *   <li>The {@code permissionHandler} bean via {@code #permissionHandler}</li>
 * </ul>
 *
 * <p><b>Note:</b> The {@code permissionHandler} is a required bean of type
 * {@link org.example.teamshop.security.PermissionHandler} injected into the aspect and made available in the SpEL context.
 *
 * @see PermissionCheck
 * @see org.springframework.expression.spel.standard.SpelExpressionParser
 */

@Aspect
@Component
public class PermissionCheckAspect {

    private final PermissionHandler permissionHandler;

    private final ExpressionParser parser = new SpelExpressionParser();

    @Autowired
    public PermissionCheckAspect(PermissionHandler permissionHandler) {
        this.permissionHandler = permissionHandler;
    }

    @Before("@annotation(permissionCheck)")
    public void checkPermission(JoinPoint joinPoint, PermissionCheck permissionCheck) throws PermissionDeniedException {
        String spelExpression = permissionCheck.value();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        EvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        // TODO: Improve logic, cause @ shouldn't work in this way
        String correctedSpelExpression = spelExpression;
        if(spelExpression.contains("@permissionHandler"))
            correctedSpelExpression = spelExpression.replace("@permissionHandler", "#permissionHandler");

        context.setVariable("permissionHandler", permissionHandler);

        Expression expression = parser.parseExpression(correctedSpelExpression);
        Boolean result = expression.getValue(context, Boolean.class);

        if (Boolean.FALSE.equals(result))
            throw new PermissionDeniedException("Access Denied");
        else if (result == null)
            throw new InternalSecurityLogicException("SpEl evaluation failed. Its server error");
    }
}
