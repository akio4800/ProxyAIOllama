package at.s2g.ai.conversations.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import at.s2g.ai.conversations.Conversation;
import at.s2g.ai.util.BaseConverter;

public class ConversationConverter extends BaseConverter<Conversation> {

  public ConversationConverter() {
    super(new TypeReference<>() {});
  }
}
