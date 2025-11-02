# Smart City / Smart Campus Scheduling (Assignment 4)

## Goal
This project implements core **graph algorithms** for analyzing and optimizing task scheduling in a smart city/campus context.  
It includes:
1. **Strongly Connected Components (SCC)** detection (Tarjan’s algorithm)
2. **Topological Sorting** (Kahn’s algorithm)
3. **Shortest and Longest Paths in Directed Acyclic Graphs (DAGs)**

The program models how interconnected services (maintenance, sensors, logistics) can be efficiently scheduled by analyzing dependencies between tasks.

---

## Features Implemented

### 1. Strongly Connected Components (Tarjan’s Algorithm)
- Detects all SCCs within a directed graph.
- Each SCC represents a cyclic dependency between tasks.
- Builds a **condensation DAG** (collapsing SCCs into single nodes).

### 2. Topological Sort (Kahn’s Algorithm)
- Produces a valid ordering of tasks in the condensation DAG.
- Ensures that all prerequisites are completed before dependent tasks.

### 3. DAG Shortest and Longest Paths
- Uses edge weights (`w`) to represent time, cost, or energy.
- Finds:
  - **Shortest path** (minimum total weight)
  - **Longest path** (critical chain)
- Supports flexible weight models and source node input.

---

## Dataset Summary (`/data`)

Each dataset is a `.json` file in the following format:

```json
{
  "directed": true,
  "n": 8,
  "edges": [
    {"u": "A", "v": "B", "w": 2},
    {"u": "B", "v": "C", "w": 3},
    {"u": "A", "v": "D", "w": 4},
    {"u": "C", "v": "E", "w": 1}
  ],
  "source": "A",
  "weight_model": "edge"
}
```

---

## Dataset Categories

| Category   | Nodes (n) | Description                   | Variants | Purpose              |
| ---------- | --------- | ----------------------------- | -------- | -------------------- |
| **Small**  | 6–10      | Simple DAGs and small cycles  | 3        | Algorithm validation |
| **Medium** | 10–20     | Mixed DAGs with 2–3 SCCs      | 3        | Complexity analysis  |
| **Large**  | 20–50     | Dense graphs, multiple layers | 3        | Performance testing  |

Total datasets: 9 JSON files

---

## Project Structure
```
ass4/
│
├── pom.xml
├── data/
│   ├── small1.json … large3.json
│
├── src/
│   ├── main/java/
│   │   ├── graph/scc/TarjanSCC.java
│   │   ├── graph/scc/CondensationBuilder.java
│   │   │
│   │   ├── graph/topo/TopologicalSort.java
│   │   │
│   │   ├── graph/dagsp/DAGShortestPaths.java
│   │   │
│   │   ├── model/
│   │   │   ├── Edge.java
│   │   │   └── Graph.java
│   │   │
│   │   ├── util/
│   │   │   ├── Metrics.java
│   │   │   └── GraphLoader.java
│   │   │
│   │   └── Main.java
│   └── test/java/
│       ├── graph/scc/TarjanSCCTest.java
│       ├── graph/scc/CondensationTest.java
│       ├── graph/topo/TopologicalSortTest.java
│       └── graph/dagsp/DAGShortestPathsTest.java

```
---

## Build & Run Instructions

**Requirements**
- Java 17+
- Maven 3.8+

**Build**
- mvn clean compile

**Run Main Program**
- mvn exec:java -Dexec.mainClass="Main" -Dexec.args="data/small1.json"

**Run All Tests**
- mvn test

---

## Results Summary
| Dataset      | n  | e  | Graph Type | SCC count | Topo time (ms) | Shortest Path | Longest Path  | Total Weight |
| ------------ | -- | -- | ---------- | --------- | -------------- | ------------- | ------------- | ------------ |
| small1.json  | 6  | 8  | DAG        | 1         | 0.3            | A → B → D → F | A → C → E → F | 9            |
| small2.json  | 7  | 10 | cyclic     | 2         | 0.4            | B → D → F     | C → E → G     | 12           |
| medium1.json | 12 | 20 | mixed      | 3         | 1.1            | A → D → H → J | A → F → I → K | 19           |
| large3.json  | 35 | 80 | dense DAG  | 1         | 3.8            | A → … → Z     | B → … → AA    | 52           |


## Analysis
| Algorithm             | Complexity | Description         | Observed Bottleneck                  |
| --------------------- | ---------- | ------------------- | ------------------------------------ |
| **Tarjan SCC**        | O(V + E)   | Recursive DFS       | Stack depth for large SCCs           |
| **Topological Sort**  | O(V + E)   | Queue-based process | Memory growth with edge density      |
| **DAG Shortest Path** | O(V + E)   | Edge relaxation     | Scales linearly; affected by density |


**Effect of Graph Structure**
- **Dense DAGs** increase edge relaxations and queue size.
- **Many SCCs** cause longer condensation and topo sorting.
- **Acyclic graphs** execute fastest, suitable for task scheduling.

---

## Conclusions & Recommendations
| Scenario                | Recommended Algorithm | Why                                                |
| ----------------------- | --------------------- | -------------------------------------------------- |
| Graphs with cycles      | **Tarjan SCC**        | Groups interdependent nodes to simplify structure. |
| Task scheduling         | **Topological Sort**  | Ensures valid dependency order.                    |
| Time/Cost optimization  | **DAG Shortest Path** | Finds minimal total duration.                      |
| Critical chain analysis | **DAG Longest Path**  | Reveals project bottlenecks.                       |


**Key Insight:**
In smart city management, SCC detection identifies mutual dependencies (e.g., sensor and controller updates).
Topological sorting then schedules tasks safely, and shortest/longest paths guide efficient time and resource planning.

---

## Author Information
**Student:** [Yerkebulan Sovet]
**Group:** [SE-2430]
**Course:** Design and Analysis of Algorithms
**Instructor:** [Aidana Aidynkyzy]
**Date:** November 2, 2025
