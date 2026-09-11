 17, 18 an 19th video:

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
     

     ChatMemory -> MessageWindowChatMemory is impl class having max_messages size 20 and if more than 20 msgs are called by user
     then older msgs are evicted.
     which implments get, add, clear, process methods and uses ChatMemoryRepository.
ChatMemoryRepository interface have some methods like findConversationIds(), saveAll etc.

Memory storages:
1) InMemoryChatMemoryRepository stores messages in memory using a ConcurrentHashMap.
InMemoryChatMemoryRepository impl ChatMemoryRepository to store msgs and deletes the msgs once the session is closed.

2) JdbcChatMemoryRepository JdbcChatMemoryRepository is a built-in implementation that uses JDBC to store messages in a relational database. 
     It supports multiple databases out-of-the-box and is suitable for applications that require persistent storage of chat memory.
     <dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-chat-memory-repository-jdbc</artifactId>
</dependency>

3) CassandraChatMemoryRepository: uses Apache Cassandra to store messages. It is suitable for applications that require persistent storage of chat memory, especially for availability, durability, scale, and when taking advantage of time-to-live (TTL) feature.

4) Neo4j ChatMemoryRepository


How to use?
we have advisors to use this:
1) MessageChatMemoryAdvisor and intercept and adds msgs with roles in prompt.
2) PromptChatMemoryAdvisor uses plain text memory 
3) VectorStoreChatMemoryAdvisor used for vector type memory storage


How it remembers?

when we log the prompts by using advisors every time when we ask llm
 then all our previous conversaitons are sent to LLM so that from those conversations
 it remembers our data and sends our data.
Because of that more number of tokens are used which is drawback of InMemoryChatMemoryRepository
SO we use VectorDb to resolve this issue.


20th video:
 remembering chats forever: using JdbcChatMemoryRepository or cassandra or neo4j RedisChat can save our 
conversaitons in the DB when we configure them based on there dependencies that we define. Max per conversation 20 messages are used b default.
By using 
@Bean
public ChatMemory chatMemory(JdbcChatMemoryRepository jdbcRepo) {
     return MessageWindowChatMemory.builder()
     .ChatMemoryRepository(jdbcRepo)
     .max_messages(10).build();
}

Now it means only 10 max max_messages are used for that ConversationId



