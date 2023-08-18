# Scala

## Ecosystem

### SBT
- In scala we don't need a version manager like rvm or nvm, because sbt can handle that
- This happens because scala is _compiled_, so not every code will compile with every version of the lang

```
scalaVersion := "3.3.0"
```

- There is no install script, simply running `sbt run` downloads necessary deps

- To specify which sbt version, we add the following to a `project/build.properties` file. This will instruct whatever sbt version is installed to download the correct version - they're just jars :)

```properties
sbt.version=1.7.0
```

## Language

### Lists and Arrays
- Lists are implemented as _immutable linked lists_. Every operation on a list creates a new object.

```scala
// Builds a list one element at a time - note the trailing nil to "finish" the build
val list1: List[String] = "Hi" :: "World" :: "!" :: Nil
var list2: List[String] = ("Hi", "World", "!")
list1 == list2 // true
```

### Companion objects
- Are defined in the same file and has the same name as a class
- Used to store static methods, class attrs (aka @@METHODS from Ruby) and other class-level functions

> There is no `static` keyword on Scala!

### Class behaviours
- `class`:
  - normal classes, require custom impl. of methods such as `equals`, `toString`, etc
  - comparing objects means comparing refs.

`case-class`
  - automatically provide impl. of methods above based on constructor arguments.
  - comparing means comparing attrs.
  - provide a  so you don't have to use new to create an instance

```scala
class PersonA(name: String, age: Int)

case class PersonB(name: String, age: Int)

new PersonA("Alice", 25) == new PersonA("Alice", 25) // false, compares references
PersonB("Alice", 25) == PersonB("Alice", 25) // true, compares args
```

## Play Framework