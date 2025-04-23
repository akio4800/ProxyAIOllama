package at.s2gplus.ai.actions.toolwindow;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;

import at.s2gplus.ai.actions.editor.EditorActionsUtil;
import at.s2gplus.ai.conversations.Conversation;
import at.s2gplus.ai.conversations.ConversationService;

public class MoveDownAction extends MoveAction {

  public MoveDownAction(Runnable onRefresh) {
    super("Move Down", "Move Down", AllIcons.Actions.MoveDown, onRefresh);
    EditorActionsUtil.registerAction(this);
  }

  @Override
  protected Optional<Conversation> getConversation(@NotNull Project project) {
    return ConversationService.getInstance().getPreviousConversation();
  }
}
