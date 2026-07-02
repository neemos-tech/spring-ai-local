# Building a Personal AI Concierge with Spring AI & MongoDB Atlas Vector Search

# My Resouces

## Code Walkthrough

https://youtu.be/A45__gELXyA?si=fJoSseD0PcM6UeQy

## Vector Embeddings Blog

https://medium.com/@neemo/easiest-way-to-understand-vector-embeddings-and-vector-search-01648bedd034?sharedUserId=neemo

## Sprring AI Blog
https://medium.com/@neemo/free-free-free-building-a-high-performance-local-ai-stack-spring-boot-4-spring-ai-ollama-and-71452cd3b4c6?sharedUserId=neemo

## Introduction

A Retrieval-Augmented Generation (RAG) application built using Spring Boot, Spring AI, Ollama, and MongoDB Atlas Vector Search and/or Redis Vector DB.

This project demonstrates how to build an AI assistant capable of answering questions using your own documents instead of relying solely on the knowledge contained within a Large Language Model.

---

# Overview

The application demonstrates how to:

* Build a Retrieval-Augmented Generation (RAG) pipeline
* Generate embeddings using a local embedding model
* Store and retrieve vectors using MongoDB Atlas Vector Search
* Ingest PDF and CSV documents
* Stream AI responses using Spring AI
* Reduce hallucinations by grounding responses with retrieved context
* Switch between different knowledge bases with minimal configuration changes

---

# Technology Stack

* Java 25
* Spring Boot 4.1.0
* Spring AI 2.0
* MongoDB Atlas
* MongoDB Atlas Vector Search
* Ollama - Qwen and Llama 3:Latest
* Apache Tika
* Maven

---

# Architecture

```text
                User Question
                      │
                      ▼
            Embedding Model
                      │
                      ▼
      MongoDB Atlas Vector Search
                      │
                      ▼
        Retrieve Relevant Context
                      │
                      ▼
          Large Language Model
                      │
                      ▼
               Grounded Response
```

---

# Project Structure

```text
src/main
 ├── configs
 ├── controller
 ├── services
 ├── utils
 ├── constants
 └── resources - to keep documents and configurations
```

---

# Prerequisites

Before running the application, ensure the following are available:

* Java 11 or later
* Maven
* MongoDB Atlas Cluster
* MongoDB Atlas Vector Search Index
* Ollama Cloud or Local Models

---

# Installing Ollama

Download and install Ollama from:

https://ollama.com

Pull the required models:

```bash
ollama pull qwen2.5:14b
or
ollama pull llama3:latest

ollama pull qwen3-embedding:8b
```

---

# MongoDB Atlas Configuration

Create the following resources:

* Cluster - M0 for Free (Limited to 3 Vecto Indices)
* Database
* Collection
* Atlas Vector Search Index

Configure the MongoDB connection:

```properties
spring.data.mongodb.uri=<YOUR_MONGODB_URI>
```

---

# Document Ingestion

The application currently supports:

* PDF documents
* CSV datasets

The ingestion workflow consists of:

1. Reading the source document
2. Splitting the content into chunks - (Default Configuration)
3. Generating embeddings
4. Storing vectors in MongoDB Atlas Vector Search

---

# Running the Application

Using Maven:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=redis && -DREDIS_URI=<REDIS_URI>

mvn spring-boot:run -Dspring-boot.run.profiles=story && -DDB_URL=<DB_URL> && -DDB_USER=<DB_USER> &&  -DDB_PASSWORD=<DB_PASSWORD>
```

Or run the Spring Boot application directly from your preferred IDE.

---

# Sample Query

```
Suggest a hotel that has a rooftop restaurant and a swimming pool.
```

Example response:

```
Golden Sands Resort offers a rooftop restaurant overlooking the city skyline, an outdoor swimming pool, and consistently positive guest reviews for both dining and hospitality.
```

---

# RAG Workflow

```text
User Question
      │
      ▼
Embedding Generation
      │
      ▼
Vector Search
      │
      ▼
Relevant Context
      │
      ▼
Prompt Construction
      │
      ▼
Large Language Model
      │
      ▼
Grounded Response
```

---

# Concepts Demonstrated

This project covers the following concepts:

* Retrieval-Augmented Generation (RAG)
* Vector Embeddings
* Semantic Search
* Vector Databases
* Prompt Engineering
* Context Windows
* Hallucination Reduction
* Local AI Development
* Spring AI Advisors

---

# Presentation

This repository accompanies the presentation:

**Building a Personal AI Concierge: From Documents to Answers Using RAG**

Topics covered include:

* Why Large Language Models hallucinate
* How Retrieval-Augmented Generation works
* Understanding vector embeddings
* Semantic search using MongoDB Atlas Vector Search
* Building a practical RAG application with Spring AI
* Live demonstration

---

# Learning Resources

## Spring AI

https://docs.spring.io/spring-ai/reference/

## MongoDB Atlas

https://www.mongodb.com/atlas

## MongoDB Atlas Vector Search

https://www.mongodb.com/docs/vector-search/

https://www.mongodb.com/docs/vector-search/tutorials/quick-start/?deployment-type=atlas&embedding=auto&interface=driver&language=java-sync

## Ollama

https://ollama.com

## Apache Tika

https://tika.apache.org/

## pgvector

https://github.com/pgvector/pgvector

## Redis Vector Search

https://redis.io/docs/interact/search-and-query/search/vectors/

## LangChain Concepts

https://python.langchain.com/docs/concepts/

## Retrieval-Augmented Generation

https://research.ibm.com/blog/retrieval-augmented-generation-RAG

---

# Future Enhancements

Potential enhancements include:

* Hybrid Search (Keyword + Vector Search)
* Metadata-based Filtering
* Multi-document Retrieval
* Chat Memory
* Multi-turn Conversations
* Tool Calling
* Agentic AI
* Support for Multiple Embedding Models
* Redis Vector Store
* pgvector
* Milvus
* Weaviate
* Pinecone
* Streaming User Interface

---

# Contributing

Contributions, suggestions, and improvements are welcome. Feel free to fork the repository, experiment with different vector databases or embedding models, and submit pull requests.

---

# License

This project is intended for educational and demonstration purposes. Please review the licenses of the individual frameworks, models, and tools used within the project before using them in production.
 
