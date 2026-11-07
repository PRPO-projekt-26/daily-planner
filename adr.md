# ADR-001: Quarkus as a Service Framework
Status: Accepted

## Context
We are building the "Daily Planner" application, which consists of multiple services written in Java. The application will be deployed on Kubernetes. It utilizes CDI and Jakarta REST.

## Decision
We will build all services using the Quarkus framework.

## Consequences
+ Fast developer mode, standard APIs
+ Lower memory consumption on Kubernetes
- Fewer online examples compared to Spring