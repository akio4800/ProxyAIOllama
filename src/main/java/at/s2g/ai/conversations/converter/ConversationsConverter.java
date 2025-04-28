package at.s2g.ai.conversations.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import at.s2g.ai.conversations.ConversationsContainer;
import at.s2g.ai.util.BaseConverter;

public class ConversationsConverter extends BaseConverter<ConversationsContainer> {

  public ConversationsConverter() {
    super(new TypeReference<>() {});
  }
}
