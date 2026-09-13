Retreival:Fetch the most relavant info from DB
Augmented: Put the context with prompt  
Generation: LLM generate the answer

-> Used to train on private company data as LLMs are trained on public data so if
    user wants to know about company policies etc then we train on pvt data and sends
    the response to User.


user -> app -> vector-db(where the pvt data stored) -> app -> LLM   
user <- app <- vector-db                            <- app <- LLM


<dependency>
   <groupId>org.springframework.ai</groupId>
   <artifactId>spring-ai-vector-store-advisor</artifactId>
</dependency>


used to store the data


why we need RAG?
1) limited memory -> trained up to a cutoff date (cant know latest info)
2) Context limit
3) Hallucinations -> sometimes LLM make up answers.

WHat it solves?
Fetching only most relavant pieces from db.
Feeding them into the prompt, so the LLM has the right context.
Reducing Hallucinations ad improving accuracy+trustworthiness.
