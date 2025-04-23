package at.s2gplus.ai.actions;

import javax.swing.*;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public abstract class TrackableAction extends AnAction {

  private final ActionType actionType;

  public TrackableAction(
      String text,
      String description,
      Icon icon,
      ActionType actionType) {
    super(text, description, icon);
    this.actionType = actionType;
  }

  public abstract void handleAction(@NotNull AnActionEvent e);

  @Override
  public void actionPerformed(@NotNull AnActionEvent e) {
      handleAction(e);
  }
}