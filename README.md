# HORUS CODE | شِفرة حورس

> **“From the symbols of the past to the code of the future.”**  
> **«من رموز الماضي… إلى شيفرة المستقبل.»**

**Created & Engineered by:** Amr Ahmed | عمرو أحمد  
© 2026 Amr Ahmed. All Rights Reserved.

---

## 1. Project Overview & Core Philosophy

**HORUS CODE** is a premier educational coding application designed to teach computational thinking and programming logic to beginners. It bridges the artistic and cultural heritage of ancient Egyptian hieroglyphic symbols with the mathematical precision of modern computer science.

### Authentic Historical Distinction
HORUS CODE utilizes hieroglyph-inspired visual representations as a modern pedagogical design framework. The platform clearly distinguishes authentic historical facts (e.g., Gardiner sign catalog, sacred meanings of the Ankh and Wedjat) from the modern computational roles assigned by this project.

---

## 2. The HORUS CODE Language Specification

HORUS CODE operates on an authentic, deterministic programming language engine with zero dependencies on unsafe evaluation (no `eval()`):

| Symbol | Glyph | Name (AR / EN) | Modern CS Role | Example Syntax |
| :--- | :---: | :--- | :--- | :--- |
| **Ankh** | ☥ | عنخ (مفتاح الحياة) | Program Lifecycle Start / Stop | `START` ... `STOP` |
| **Djed Pillar** | 𓋹 | عمود جد | Variable Allocation & Memory Mutation | `SET score = 10` |
| **Eye of Horus** | 𓂀 | عين حورس (الوجات) | Conditional Logic & Decision Branches | `IF x > 5` ... `ELSE` ... `END_IF` |
| **Scarab** | 𓆣 | الجعران (خِبري) | Bounded Loops & Repetition Cycles | `REPEAT 3` ... `END_REPEAT` |
| **Papyrus Scroll** | 𓏛 | لفافة البردي | Standard Output (Console Display) | `PRINT "Result: " + (x * 2)` |
| **Feather of Ma'at** | 𓋖 | ريشة ماعت | Standard Input (User Input Prompt) | `INPUT "Enter energy" INTO e` |
| **Pyramidion** | 𓊗 | الهريم (بن بن) | Physical / Maze Navigation Actions | `ACTION MOVE_FORWARD` |

### Key Language Features
- **Arithmetic & Logic Evaluator:** Full support for `+`, `-`, `*`, `/`, `%`, `==`, `!=`, `<`, `<=`, `>`, `>=`.
- **String Concatenation & Interpolation:** `"Value: " + score`.
- **Infinite Loop Protection:** Enforces strict execution-step limits (max 1000 steps).
- **Execution Trace:** Records instruction-by-instruction memory state, variables watch table, and plain Arabic & English explanations.

---

## 3. Application Architecture & Modules

The application is built on modern Android with Kotlin and Jetpack Compose (Material Design 3):

1. **Visual Programming Studio (`StudioScreen`):**
   - Categorized Symbol Toolbox with search/filter tabs (Core, Variables, Conditions, Loops, I/O, Actions).
   - Visual Block Workspace with nesting indentation, parameter editors, reordering, and deletion.
   - Dual View: Seamless toggle between Visual Blocks and formatted Code text.
   - Stepper & Active Block Glowing Highlight during step-by-step execution.
   - Output Console with terminal prompt and clear controls.
   - Execution Trace log with variable snapshots.

2. **Interactive Learning Journey (`LevelsScreen`):**
   - 8 complete progressive levels from "The First Spark" to "The Final Trial".
   - Automated behavioral validation running against actual interpreter results.
   - Multi-tier hint system and victory dialog with XP rewards.

3. **Temple of Logic (`TempleMazeScreen`):**
   - 3 authentic grid-based puzzle maps: *The Gateway to Abydos*, *The Hall of Columns*, and *The Labyrinth of Karnak*.
   - Direction-aware Horus explorer character (North, East, South, West).
   - Sandstone column walls, keys/gems collection, and golden shrine objectives.
   - Animated step-by-step movement controlled by user code.

4. **HORUS Academy (`AcademyScreen`):**
   - 8 comprehensive computer science lessons (Algorithms, Variables, Decisions, Loops, I/O, Debugging, Problem Decomposition).
   - Interactive quizzes with instant feedback, explanations, and completion badges.

5. **Debugging Laboratory (`DebuggingScreen`):**
   - 5 broken programs with intentional bugs (syntax error, undefined variable, inverted condition, unclosed block, loop count mismatch).
   - Real interpreter error diagnosis and automated repair validation.

6. **Hieroglyphic Symbol Explorer (`SymbolsScreen`):**
   - Detailed encyclopedia comparing authentic ancient Egyptian history with modern programming mappings.

7. **Learner Dashboard (`DashboardScreen`):**
   - Live XP tracking, level completion counters, and 7 unlocks-based achievements (*First Inscription*, *Djed Guardian*, *Eye of Insight*, *Scarab Cycle*, *Truth Restorer*, *Sanctuary Master*, *Horus Grand Master*).
   - Persistent storage using local preferences.

8. **Judge Competition Presentation Mode (`JudgeDemoScreen`):**
   - 10-step guided walkthrough curated for student technology competition judges.
   - Bilingual presenter notes and instant live execution demonstrations.

9. **Learning Impact Evaluation (`EvaluationScreen`):**
   - Pre-Test baseline (5 questions) and Post-Test mastery assessment (5 questions).
   - Transparent, evidence-based score comparison and calculated learning gain percentage.

10. **Settings & Accessibility (`SettingsScreen`):**
    - Seamless Arabic (RTL) / English (LTR) language toggle without restart.
    - Midnight Dark / Light theme selector.
    - Verified authorship and credits card for Amr Ahmed.

---

## 4. Test Verification & Results

Automated unit tests are implemented in `app/src/test/java/com/example/HorusEngineUnitTest.kt`:
- `testParserAndExecution_sequentialPrint`: PASS
- `testVariablesAndArithmetic`: PASS
- `testConditionalBranching_IfElse`: PASS
- `testRepeatLoops`: PASS
- `testUndefinedVariableError`: PASS
- `testUnclosedIfBlockError`: PASS
- `testAllEightLevelSolutionsPassValidation`: PASS (All 8 level solutions validated)
- `testTempleMazeSimulation`: PASS (Full maze path and obstacle avoidance validated)

To run the tests:
```bash
gradle :app:testDebugUnitTest
```

---

## 5. Credits & Acknowledgments

- **Concept, Design & Development:** Amr Ahmed | عمرو أحمد
- **Platform:** HORUS CODE | شِفرة حورس
- **Copyright:** © 2026 Amr Ahmed. All Rights Reserved.
