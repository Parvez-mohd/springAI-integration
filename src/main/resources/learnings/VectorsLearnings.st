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



Next we learnt about Modules pre-retreval, retrevial and post-retreval and generater ang impl moduleRAG


ETL pipeline( Extract Transform Load)
source(PDF/WORD etc) -> documentReader -> DocumentList -> DocumentTransformer -> Transformed data -> DocumentWriter -> Database.


The ParagraphPdfDocumentReader uses the PDF catalog (e.g. TOC) information to split the input PDF into text paragraphs and output a single Document per paragraph. NOTE: Not all PDF documents contain the PDF catalog.

The TikaDocumentReader uses Apache Tika to extract text from a variety of document formats, such as PDF, DOC/DOCX, PPT/PPTX, and HTML. For a comprehensive list of supported formats, refer to the Tika documentation.

The TokenTextSplitter is an implementation of TextSplitter that splits text into chunks based on token count. It supports configurable encoding types (e.g., CL100K_BASE, P50K_BASE, O200K_BASE) and defaults to CL100K_BASE.

Transformers
TextSplitter
The TextSplitter an abstract base class that helps divides documents to fit the AI model’s context window.

ContentFormatTransformer
Ensures uniform content formats across all documents.

KeywordMetadataEnricher
The KeywordMetadataEnricher is a DocumentTransformer that uses a generative AI model to extract keywords from document content and add them as metadata.

The SummaryMetadataEnricher is a DocumentTransformer that uses a generative AI model to create summaries for documents and add them as metadata. It can generate summaries for the current document, as well as adjacent documents (previous and next).




Tool calling: tool calling helps in giving the real time data as LLM has the stale and old data, we create a class having currentTimeZone then that .tool is passed to LLM and based on the tool it generates the response