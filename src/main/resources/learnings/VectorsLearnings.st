Things we did in 23 Video:
 -> created vector service and implemented method to save inside the mariadb
 -> configired mariadb, docker-mariadb and added related dependencies.
 -> configured ollama pull mxbai-embed-large which is 1024 dimensions for embeddings
 -> downloaded DBeaver which is used to maintain all the DBs at one place.
 -> In one vectorserviceImpl implemented a method to stream list of string values into documents
 which is used to store in vector_store DB in form of embeddings.
 -> Using vectorStore.add(documents) we have created list of rows of data and its embeddings in db
 id | content | metadata | embeddings
 
in terminal:
//trigger docker image for mariadb then run DBeaver
docker run -d --name mariadb1 -p 3308:3306 -e MARIADB_ROOT_PASSWORD=root1234 -e MARIADB_DATABASE=springai mariadb:11.8



Video 24:

-> user -> app -> check similarity in db -> if found -> user
                                            not found-> "This query is not in db"
            List<Document> documents = 
            vectorStore.similaritySearch(SearchRequest.builder()
            .topK(5) //limited no. of top similar values need to retreive to reduce token size
            .similarityThreshold(0.6)  //matching value is not exact but similar 
            .query(message)
            .build());

            //similar result user query

            List<String> documentList = documents.stream().map(document -> document.getText()).toList();
            String context = String.join(", ", documentList);
            log.info("context {}", context);
            //pass in context

Video 25:
Instead of doing manual as above we can use 
QuestionAnswerAdvisor :A vector database stores data that the AI model is unaware of. When a user question is sent to the AI model, a QuestionAnswerAdvisor queries the vector database for documents related to the user question.

The response from the vector database is appended to the user text to provide context for the AI model to generate a response.
