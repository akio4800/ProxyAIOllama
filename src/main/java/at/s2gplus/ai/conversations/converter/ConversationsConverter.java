package at.s2gplus.ai.conversations.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import at.s2gplus.ai.conversations.ConversationsContainer;
import at.s2gplus.ai.util.BaseConverter;

public class ConversationsConverter extends BaseConverter<ConversationsContainer> {

  public ConversationsConverter() {
    super(new TypeReference<>() {});
  }
}
