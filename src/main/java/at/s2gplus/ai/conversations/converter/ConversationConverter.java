package at.s2gplus.ai.conversations.converter;

import com.fasterxml.jackson.core.type.TypeReference;

import at.s2gplus.ai.conversations.Conversation;
import at.s2gplus.ai.util.BaseConverter;

public class ConversationConverter extends BaseConverter<Conversation> {

  public ConversationConverter() {
    super(new TypeReference<>() {});
  }
}
