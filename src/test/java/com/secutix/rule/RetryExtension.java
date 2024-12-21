package com.secutix.rule;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

public class RetryExtension implements TestExecutionExceptionHandler {
    private final int retryCount = 3;

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        for (int i = 0; i < retryCount; i++) {
            try {
                context.getTestMethod().get().invoke(context.getRequiredTestInstance());
                return; // Success, exit
            } catch (Throwable t) {
                if (i == retryCount-1) {
                    throw t; // Final retry, rethrow the exception
                }
            }
        }
    }
}
