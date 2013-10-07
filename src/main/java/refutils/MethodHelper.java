package refutils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class MethodHelper {
    private final Object instance;
    private final String methodName;
    private final Object[] invocationValues;

    private final Method method;

    public MethodHelper(Object instance, String methodName, Object... invocationValues) {
        this.instance = instance;
        this.methodName = methodName;
        this.invocationValues = invocationValues;

        method = getMatchingMethod();
    }

    private Method getMatchingMethod() {
        List<Method> methodMatches = getAllAvailableMethods();

        methodMatches = filterOnMethodNames(methodMatches);
        if (methodMatches.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find method named %s", methodName));
        }
        if (methodMatches.size() == 1) {
            return methodMatches.get(0);
        }

        methodMatches = filterOnArgumentNumberMatches(methodMatches);
        if (methodMatches.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find method named %s with correct number of parameters", methodName));
        }
        if (methodMatches.size() == 1) {
            return methodMatches.get(0);
        }

        methodMatches = filterOnArgumentTypes(methodMatches);
        if (methodMatches.size() == 0) {
            throw new IllegalArgumentException(String.format("Cannot find method named %s with correct parameter types ", methodName));
        }
        if (methodMatches.size() > 1) {
            throw new IllegalArgumentException(String.format("Found %s matches for method %s", methodMatches.size(),
                    methodName));
        }

        return methodMatches.get(0);
    }

    private List<Method> getAllAvailableMethods() {
        Method[] methods = instance.getClass().getDeclaredMethods();
        return Arrays.asList(methods);
    }

    private List<Method> filterOnMethodNames(List<Method> methodMatches) {
        List<Method> returnValue = new ArrayList<Method>();
        for (Method method : methodMatches) {
            if (method.getName().equals(methodName)) {
                returnValue.add(method);
            }
        }
        return returnValue;
    }

    private List<Method> filterOnArgumentNumberMatches(List<Method> methodNameMatches) {
        List<Method> returnValue = new ArrayList<Method>();
        for (Method methodNameMatch : methodNameMatches) {
            if (methodNameMatch.getParameterTypes().length == invocationValues.length) {
                returnValue.add(methodNameMatch);
            }
        }
        return returnValue;
    }

    private List<Method> filterOnArgumentTypes(List<Method> methodNearMatches) {
        List<Method> returnValue = new ArrayList<Method>();
        for (Method methodNearMatch : methodNearMatches) {
            int a = 0;
            boolean foundMatch = true;
            for (Class<?> parameterType : methodNearMatch.getParameterTypes()) {
                foundMatch = foundMatch && parameterType.isAssignableFrom(invocationValues[a].getClass());
                a++;
            }
            if (foundMatch) {
                returnValue.add(methodNearMatch);
            }
        }
        return returnValue;
    }

    public Object invoke() throws InvocationTargetException, IllegalAccessException {
        boolean accessibleState = method.isAccessible();
        method.setAccessible(true);

        Object returnValue = method.invoke(instance, invocationValues);
        
        method.setAccessible(accessibleState);
        
        return returnValue;
    }
}
