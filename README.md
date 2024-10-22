# AI Financial Investment Advisor 

This project is a **prototype** backend service designed to assist users in understanding companies better through AI-driven financial insights. By asking questions about financial investments, users can get detailed analysis of a company’s strengths, weaknesses, and other relevant business metrics.

The purpose of this prototype is to act as a **playground** for development and experimentation. It runs entirely locally, utilizing an embedded H2 database and a local **SimpleVectorStore** to keep the setup lightweight and fast.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Requirements](#requirements)
- [Getting Started](#getting-started)
- [Running the Project](#running-the-project)
- [API Endpoints](#api-endpoints) (WIP)
- [Limitations](#limitations)
- [Contributing](#contributing) 

## Features

- **AI-Powered Analysis**: Leverage AI to ask questions about companies and receive investment insights.
- **RESTful API**: Exposes various endpoints for interaction with the AI and backend services.
- **Embedded H2 Database**: Stores financial and query data locally for fast development and prototyping.
- **SimpleVectorStore**: Handles vector-based data storage locally, simplifying the AI's ability to retrieve and manage insights.
- **Lightweight & Fast**: Designed to run locally with minimal setup.

## Architecture

- **Spring Boot**: Main framework for developing the backend API.
- **H2 Database**: Embedded relational database for local storage of financial and query data.
- **SimpleVectorStore**: Local storage for managing vectorized data from the AI model.
- **OpenAI API (or similar)**: Used for processing natural language queries related to financial investments.

### High-Level Architecture Diagram
```plaintext
Frontend (in progress)  <--->  [Spring Boot API]  <--->  AI Model
                                       |
                                       |---> [Embedded H2 Database]
                                       |
                                       |---> [SimpleVectorStore (local)]

```
## Requirements

Before you start, ensure you have the following:

- **Java 17+**: The application is built using Java 17 or higher. Please make sure you have the correct Java version installed.
- **OpenAI API Key**: This project integrates with OpenAI and requires an API key. You can obtain an API key by signing up at [OpenAI's official site](https://beta.openai.com/signup/).
- **Maven**: The project uses Maven for dependency management and running the application.


## Getting started
- Clone the Repository
```code
git clone https://github.com/sergiotorreslozano/my-invisor.git
cd my-invisor
```
- Install Dependencies
```code
mvn clean install
```
## Running the Project
```code
mvn spring-boot:run
```
## API Endpoints
- List all users
```code
curl --location 'http://localhost:7070/api/users'
```
- List all documents ingested in the Vector store
```code
curl --location 'http://localhost:7070/api/documents'
```
- Make a question
- Reload folder
```code
curl --location --request POST 'http://localhost:7070/documents/tep/reload'
```
## Limitations
- **Not Production-Ready**: This project is intended as a playground for AI image generation and editing. It is not ready for deployment in a production environment as is.
- **OpenAI API**: Since this project depends on OpenAI's API, ensure that your usage is in line with OpenAI's API limits and pricing structure.

## Contributing
We welcome contributions to the project! Feel free to submit issues or pull requests on the GitHub repository.
