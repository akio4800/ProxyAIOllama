package at.s2g.ai.ui;

import java.util.NoSuchElementException;

import javax.swing.*;

import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBFont;

import at.s2g.ai.Icons;
import ee.carlrobert.llm.client.openai.completion.OpenAIChatCompletionModel;

public class ModelIconLabel extends JBLabel {

  public ModelIconLabel(String clientCode, String modelCode) {
    if ("you.chat.completion".equals(clientCode)) {
      setIcon(Icons.You);
      return;
    }

    if ("chat.completion".equals(clientCode)) {
      setIcon(Icons.OpenAI);
    }
    if ("anthropic.chat.completion".equals(clientCode)) {
      setIcon(Icons.Anthropic);
    }
    if ("azure.chat.completion".equals(clientCode)) {
      setIcon(Icons.Azure);
    }
    if ("llama.chat.completion".equals(clientCode)) {
      setIcon(Icons.Llama);
    }
    if ("google.chat.completion".equals(clientCode)) {
      setIcon(Icons.Google);
    }
    setText(formatModelName(modelCode));
    setFont(JBFont.small());
    setHorizontalAlignment(SwingConstants.LEADING);
  }

  private String formatModelName(String modelCode) {
    try {
      return OpenAIChatCompletionModel.findByCode(modelCode).getDescription();
    } catch (NoSuchElementException e) {
      return modelCode;
    }
  }
}
