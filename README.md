# ✈️ AI Travel Agent Backend

An AI-powered travel agent backend built with **Java, Spring Boot, Spring AI, OpenAI, and MongoDB**.
Chat with the agent in natural language to search and book flights and hotels, and check
destination weather — all through a single conversational interface.

## Features

- 🗨️ **Conversational AI chat** — powered by Spring AI (`ChatClient`) with OpenAI (`gpt-4o-mini`)
- 🧠 **Session-based memory** — remembers context within a conversation (`ChatMemory`)
- ✈️ **Flight tools** — search, check availability, book, cancel, and view bookings by passenger
- 🏨 **Hotel tools** — search by city, check availability, book, cancel, and view bookings by guest
- 🌦️ **Weather tool** — fetches live temperature, wind, humidity, precipitation, and air quality
  (via Open-Meteo) to help with trip planning
- 💾 **MongoDB persistence** — flights, hotels, bookings, and chat history are all stored in MongoDB
- 🌐 **Web chat UI** — simple Thymeleaf-based chat page in addition to the REST API

## Tech Stack

| Layer      | Technology                          |
|------------|---------------------------------------|
| Language   | Java 17                                |
| Framework  | Spring Boot, Spring AI                 |
| AI Model   | OpenAI (gpt-4o-mini via Spring AI)     |
| Database   | MongoDB                                |
| Build Tool | Maven                                  |
| Frontend   | Thymeleaf (server-rendered chat page)  |

## Getting Started

### Prerequisites
- Java 17+
- Maven
- MongoDB running locally or accessible via URI
- An OpenAI API key

### Configuration

```bash
export MONGODB_URI=mongodb://localhost:27017/travel_agent_db
export OPENAI_API_KEY=your_openai_api_key
export CHAT_MODEL=gpt-4o-mini
```

### Run the app

```bash
./mvnw spring-boot:run
```

App runs on **port 8082**. Web chat UI: http://localhost:8082/

## Example Chat Prompts
