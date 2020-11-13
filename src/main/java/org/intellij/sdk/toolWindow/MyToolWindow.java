package org.intellij.sdk.toolWindow;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.pom.Navigatable;
import com.intellij.psi.*;

import javax.swing.*;
import java.awt.*;

public class MyToolWindow{

  private JPanel myToolWindowContent;
  private JList<PsiElement> methodsJList;
  DefaultListModel<PsiElement> methodModel;
  private Project myProject;

  public MyToolWindow(ToolWindow toolWindow) {
    methodsJList.setCellRenderer(new MyCellRenderer());
  }

  public JPanel getContent() {
    return myToolWindowContent;
  }


  public void setProject(Project project) {
    this.myProject = project;
  }

  //clears all models and lists for new list
  public void clear() {
    if (methodModel != null) {
      methodModel.clear();
    }
    methodsJList.clearSelection();

  }

  public void addMethod(PsiMethod method) {
    if (methodModel == null){
      methodModel = new DefaultListModel<>();
    }
    methodModel.addElement(method);
    methodsJList.setModel(methodModel);
  }


  //Defines how lists will be rendered in UI and actions that will be performed on its selection
  private class MyCellRenderer extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
      super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
      if (value instanceof PsiMethod){
        PsiMethod psiMethod = ((PsiMethod) value);
        setText(psiMethod.getName());
        if (isSelected) {
          PsiElement navigationElement = (psiMethod).getNavigationElement();
          try {
            if (navigationElement != null && navigationElement instanceof Navigatable && ((Navigatable) navigationElement).canNavigate()) {
              ApplicationManager.getApplication()
                      .invokeLater(new Runnable() {
                        @Override
                        public void run() {
                          ((Navigatable) navigationElement).navigate(false);
                        }
                      }, ModalityState.defaultModalityState(), myProject.getDisposed());
            }
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      return this;
    }
  }
}
