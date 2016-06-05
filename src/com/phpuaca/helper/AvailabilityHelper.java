package com.phpuaca.helper;

import com.intellij.openapi.project.DumbService;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.jetbrains.php.lang.psi.elements.ArrayCreationExpression;
import com.jetbrains.php.lang.psi.elements.ParameterList;
import com.jetbrains.php.lang.psi.elements.PhpPsiElement;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import com.jetbrains.php.phpunit.PhpUnitUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AvailabilityHelper {

    public boolean checkFile(@NotNull PsiFile psiFile) {
        return PhpUnitUtil.isPhpUnitTestFile(psiFile);
    }

    public boolean checkScope(@Nullable PsiElement psiElement) {
        if (!(psiElement instanceof StringLiteralExpression)) {
            return false;
        }

        if (DumbService.isDumb(psiElement.getProject())) {
            return false;
        }

        // $this->method('cursor');
        PsiElement stringLiteralExpressionContext = psiElement.getContext();
        if (stringLiteralExpressionContext instanceof ParameterList) {
            return true;
        }

        // $this->method(array('cursor'));
        if (stringLiteralExpressionContext instanceof PhpPsiElement && stringLiteralExpressionContext.toString().equals("Array value")) {
            PsiElement arrayValueContext = stringLiteralExpressionContext.getContext();
            if (arrayValueContext instanceof ArrayCreationExpression && arrayValueContext.getContext() instanceof ParameterList) {
                return true;
            }
        }

        return false;
    }
}
