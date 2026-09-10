     * LLMS are STATELESS and they dont save our data or chat history
     * so we use ChatMemory/ChatMemoryRepository
     * ChatMemory: represents a memory means
     * 1) what to store.
     * 2) tells about strategies:
     * a) keep last N msgs
     * b) time period
     * c) within that token limit
     * ChatMemoryRepository: is a data storage
     * 1) storage engine
     * 2) to store data
     * 3) saving and reading data
     

     ChatMemory -> MessageWindowChatMemory is impl class having max_messages size 20
     which implments get, add, clear, process methods and uses ChatMemoryRepository.
ChatMemoryRepository interface have some methods like findConversationIds(), saveAll etc.

InMemoryChatMemoryRepository impl ChatMemoryRepository to store msgs and deletes the msgs once the session is closed?

How to use?
we have advisors to use this:
1) MessageChatMemoryAdvisor and intercept and adds msgs with roles in prompt.
2) PromptChatMemoryAdvisor uses plain text memory 
3) VectorStoreChatMemoryAdvisor used for vector type memory storage

