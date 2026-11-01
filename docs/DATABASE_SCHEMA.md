# PostgreSQL Database Schema & Relationships

## 1. Entity-Relationship (ER) Diagram

```mermaid
erDiagram
    STUDENTS ||--o{ QUIZ_ATTEMPTS : "takes"
    STUDENTS ||--o{ TOPIC_PERFORMANCES : "diagnosed_in"
    STUDENTS ||--o{ LEARNING_RECOMMENDATIONS : "receives"
    STUDENTS ||--o{ TOPIC_PROGRESSES : "tracks"

    TOPICS ||--o{ QUESTIONS : "categorizes"
    TOPICS ||--o{ LEARNING_RESOURCES : "contains"
    TOPICS ||--o{ TOPIC_PERFORMANCES : "evaluated_in"
    TOPICS ||--o{ TOPIC_PROGRESSES : "recorded_in"

    QUESTIONS ||--|{ QUESTION_OPTIONS : "has_choices"
    QUESTIONS ||--o{ STUDENT_ANSWERS : "selected_in"

    QUIZ_ATTEMPTS ||--|{ STUDENT_ANSWERS : "records"
    QUIZ_ATTEMPTS ||--o{ TOPIC_PERFORMANCES : "summarizes"
    QUIZ_ATTEMPTS ||--o{ LEARNING_RECOMMENDATIONS : "triggers"

    STUDENTS {
        bigint id PK
        varchar full_name
        varchar email UK
        varchar password
        varchar role
        timestamp created_at
    }

    TOPICS {
        bigint id PK
        varchar name
        text description
        text learning_objectives
        text beginner_explanation
        text common_mistakes
        text code_example
        int order_index
        varchar subject_name
    }

    QUESTIONS {
        bigint id PK
        bigint topic_id FK
        text question_text
        text explanation
        varchar difficulty_level
    }

    QUESTION_OPTIONS {
        bigint id PK
        bigint question_id FK
        varchar option_key
        text option_text
        boolean is_correct
    }

    QUIZ_ATTEMPTS {
        bigint id PK
        bigint student_id FK
        varchar subject_name
        int total_questions
        int correct_answers
        double score_percentage
        timestamp completed_at
    }

    STUDENT_ANSWERS {
        bigint id PK
        bigint attempt_id FK
        bigint question_id FK
        varchar selected_option_key
        boolean is_correct
    }

    TOPIC_PERFORMANCES {
        bigint id PK
        bigint attempt_id FK
        bigint student_id FK
        bigint topic_id FK
        int questions_attempted
        int correct_answers
        double accuracy_percentage
        varchar gap_status
        timestamp recorded_at
    }

    LEARNING_RESOURCES {
        bigint id PK
        bigint topic_id FK
        varchar title
        varchar content_type
        text resource_url
        text summary
        varchar difficulty_level
    }

    LEARNING_RECOMMENDATIONS {
        bigint id PK
        bigint student_id FK
        bigint topic_id FK
        bigint attempt_id FK
        text recommendation_text
        varchar action_type
        varchar priority
        text reason
        boolean is_completed
        timestamp created_at
    }

    TOPIC_PROGRESSES {
        bigint id PK
        bigint student_id FK
        bigint topic_id FK
        boolean is_completed
        timestamp completed_at
    }
```

---

## 2. Table Specifications & Constraints

### 2.1 `students`
Stores student authentication and profile credentials.
- `id` (BIGSERIAL, Primary Key)
- `email` (VARCHAR 150, Unique Index, Non-null)
- `password` (VARCHAR 255, BCrypt hashed, Non-null)
- `full_name` (VARCHAR 100, Non-null)
- `role` (VARCHAR 20, Default `ROLE_STUDENT`)
- `created_at` (TIMESTAMP, Non-null)

### 2.2 `topics`
Core Java curriculum modules.
- `id` (BIGSERIAL, Primary Key)
- `name` (VARCHAR 100, Non-null)
- `description` (TEXT)
- `learning_objectives` (TEXT)
- `beginner_explanation` (TEXT)
- `common_mistakes` (TEXT)
- `code_example` (TEXT)
- `order_index` (INT)
- `subject_name` (VARCHAR 50, Default `Core Java`)

### 2.3 `questions` & `question_options`
Question bank categorized by topic and difficulty level (`BEGINNER`, `INTERMEDIATE`, `ADVANCED`).
Options store the choice key (`A`, `B`, `C`, `D`) and `is_correct` boolean. The boolean is strictly kept on the server and never exposed in the quiz taking API.

### 2.4 `quiz_attempts` & `student_answers`
Maintains permanent audit history of every student submission. Each question answered is recorded alongside whether the student's selected key matched the verified key.

### 2.5 `topic_performances`
Maintains topic-level breakdown metrics for each attempt. Includes accuracy percentage and assigned gap status (`HIGH_PRIORITY_GAP`, `NEEDS_IMPROVEMENT`, `GOOD_UNDERSTANDING`, `STRONG_UNDERSTANDING`, `NOT_ASSESSED`).

### 2.6 `learning_recommendations`
Stores personalized remedial suggestions linked to specific weak topics. Contains human-readable `reason` explainability notes.

### 2.7 `topic_progresses`
Tracks student self-study completion status per topic with a unique constraint on `(student_id, topic_id)`.
