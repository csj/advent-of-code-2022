# Day 2: Rock Paper Scissors

[<< Previous](Day01.md) | [Problem Statement](https://adventofcode.com/2022/day/2) | [My solution](../src/main/kotlin/puzzles/Day02.kt) | [Next >>](Day03.md)

Ugh, this was a bit of an annoying one. Nothing much to do here except
simulate the game and tally the scores.

Every game gives two components to the score: one for the result you got (+0,
+3 or +6), and one for the thing you played (+1, +2 or +3).

## Part 1
In part 1, you are given what you played directly, so your play-bonus is 
directly given: 
```kotlin
val myScore = when (myMove) { 
    "X" -> 1  // rock
    "Y" -> 2  // paper
    "Z" -> 3  // scissors
}
```

But the result-bonus is a little trickier:

```kotlin
val resultScore = when (Pair(oppMove, myMove)) {
    Pair("A", "Y"), Pair("B", "Z"), Pair("C", "X") -> 6  // win
    Pair("A", "Z"), Pair("B", "X"), Pair("C", "Y") -> 0  // loss
    Pair("A", "X"), Pair("B", "Y"), Pair("C", "Z") -> 3  // tie
    else -> throw Exception("Invalid moves: $oppMove, $myMove")
}
```

## Part 2
This time, the result bonus is straightforward:
```kotlin
val resultScore = when (result) { 
    "X" -> 0  // loss 
    "Y" -> 3  // tie
    "Z" -> 6  // win
}
```

And the play bonus needs to be worked out from the result and the opponent's 
move:
```kotlin
val myScore = when (Pair(oppMove, result)) {
    Pair("A", "Y"), Pair("B", "X"), Pair("C", "Z") -> 1  // rock
    Pair("A", "Z"), Pair("B", "Y"), Pair("C", "X") -> 2  // paper
    Pair("A", "X"), Pair("B", "Z"), Pair("C", "Y") -> 3  // scissors
}
```

This problem was a bit tricky. I could see people getting tripped up and 
discouraged -- this is only day 2!!