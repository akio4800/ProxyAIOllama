package at.s2g.ai.actions.toolwindow;

import java.util.Optional;

import org.jetbrains.annotations.NotNull;

import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;

import at.s2g.ai.actions.editor.EditorActionsUtil;
import at.s2g.ai.conversations.Conversation;
import at.s2g.ai.conversations.ConversationService;

public class MoveUpAction extends MoveAction {

  public MoveUpAction(Runnable onRefresh) {
    super("Move Up", "Move up", AllIcons.Actions.MoveUp, onRefresh);
    EditorActionsUtil.registerAction(this);
  }

  @Override
  protected Optional<Conversation> getConversation(@NotNull Project project) {
    return ConversationService.getInstance().getNextConversation();
  }
}
