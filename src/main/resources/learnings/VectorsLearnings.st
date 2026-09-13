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

-> we can even store PDF inform of chunks and store in our DB instead of List of documents as well.
-> 