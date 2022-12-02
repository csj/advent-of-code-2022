# Day 1: Calorie Counting

[Problem Statement](https://adventofcode.com/2022/day/1) | [My solution](../src/main/kotlin/puzzles/Day01.kt) | [Next >>](Day02.md)

## Part 1

A nice, simple problem to help us dust off the old IDE and get back into the 
swing of things.

Not much to say here. Consider the groups of numbers, one at a time, add 
each one up, and report the sum of the highest one. Kotlin one-liner:

```kotlin
readGroups().maxOf { it.sumOf { it.toInt() } }.toString()
```
(all right, you do need to have written the `readGroups` utility first 😏)

## Part 2
This time, we need to collect the top 3 groups, and sum those all together. 
This is a little more tricky; a local variable holding the best group 
wouldn't cut it anymore.

I do something incredibly inefficient here: sort all the groups descending, 
take the top 3, and sum them. There are more efficient ways to do this.

Or are there? I guess it depends on your definition of "efficiency", eh? If 
it's: "how many CPU cycles does it take to run", then this is most 
definitely _not_ the most efficient. But if it's: "how much effort did I 
spend solving this problem", then it's definitely the most efficient.
