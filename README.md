# Apriori Algorithm - Data Mining

A Java implementation of the **Apriori algorithm** for mining frequent itemsets and generating association rules from transactional data.

## About

The Apriori algorithm is a classic data mining technique used to discover interesting relationships (association rules) between items in large datasets. It is widely used in market basket analysis.

**Example:** If a customer buys `Milk` and `Bread`, how likely are they to also buy `Butter`?

## Features

- Generates all frequent itemsets above a minimum support threshold
- Produces association rules above a minimum confidence threshold
- Clean Java implementation with no external dependencies

## Dataset

| Transaction | Items |
|-------------|-------|
| T1 | Milk, Bread, Butter |
| T2 | Bread, Butter |
| T3 | Milk, Bread |
| T4 | Milk, Butter |
| T5 | Bread, Butter |
| T6 | Milk, Bread, Butter |

## Configuration

In `Apriori.java`:
```java
static double minSupport    = 0.5;  // 50%
static double minConfidence = 0.7;  // 70%
```

## How to Run

```bash
javac Apriori.java
java Apriori
```

## How It Works

1. Find all items meeting minimum support
2. Remove itemsets below the threshold (pruning)
3. Combine frequent itemsets to generate larger candidates
4. Extract association rules meeting minimum confidence

## Technologies

- Language: Java
- Dependencies: None (java.util.* only)

## Project Structure

```
├── Apriori.java
└── README.md
```
