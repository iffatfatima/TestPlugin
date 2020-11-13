package org.intellij.sdk;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.intellij.sdk.toolWindow.MyToolWindowFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActionTest extends AnAction {
    public static ArrayList<PsiElement> psiElementList = new ArrayList<>();

    @Override
    public void update(@NotNull final AnActionEvent event) {
        Project project = event.getProject();
        event.getPresentation().setEnabledAndVisible(project!=null);
    }

    @Override
    public void actionPerformed(@NotNull final AnActionEvent event) {
        psiElementList.clear();
        MyToolWindowFactory.getMyToolWindow().clear();
        Project project = event.getProject();
        MyToolWindowFactory.getMyToolWindow().setProject(project);
        if (project != null) {
            AnalysisScope scope = new AnalysisScope(project);
            scope.accept(new PsiRecursiveElementVisitor() {
                @Override
                public void visitFile(final @NotNull PsiFile file) {
                    if (file instanceof PsiJavaFile) {
                        file.accept(new JavaRecursiveElementVisitor() {

                            @Override
                            public void visitMethod(PsiMethod method) {
                                super.visitMethod(method);
                                //add this on some condition later on
                                MyToolWindowFactory.getMyToolWindow().addMethod(method);

                            }
                        });
                    }
                }
            });
        }
    }
}