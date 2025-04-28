package at.s2g.ai.ui.checkbox;

import java.util.List;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.CheckboxTree;
import com.intellij.ui.CheckedTreeNode;
import com.intellij.ui.ColoredTreeCellRenderer;

import at.s2g.ai.util.file.FileUtil;

public abstract class FileCheckboxTree extends CheckboxTree {

  public FileCheckboxTree(FileCheckboxTreeCellRenderer cellRenderer, CheckedTreeNode node) {
    super(cellRenderer, node);
  }

  public abstract List<VirtualFile> getReferencedFiles();

  protected static void updateFilePresentation(
      ColoredTreeCellRenderer textRenderer,
      @NotNull VirtualFile virtualFile) {
    var fileType = FileTypeManager.getInstance().getFileTypeByFile(virtualFile);
    textRenderer.setIcon(fileType.getIcon());
    textRenderer.append(virtualFile.getName());
    textRenderer.append(" - " + FileUtil.convertFileSize(virtualFile.getLength()));
  }
}
