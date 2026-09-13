package com.practice.firstspringai.dummyData;

import java.util.List;

public class Helper {
    public static List<String> getData() {
        return List.of(
                "The Java Memory Stack stores local variables and primitive types per thread, while the Heap stores all instantiated objects.",
                "Java provides eight primitive data types: byte, short, int, long, float, double, char, and boolean.",
                "Wrapper classes convert primitive data types into object instances, enabling them to be stored inside Java collection frameworks.",
                "Autoboxing is the automatic conversion the Java compiler performs between primitive types and their corresponding object wrapper classes.",
                "Strings in Java are immutable; once instantiated, their character contents cannot be altered, ensuring thread safety and security.",
                "The String Pool is a dedicated memory region in the Heap that caches unique String literals to optimize overall memory allocation.",
                "StringBuilder provides mutable character sequences for single-threaded operations, whereas StringBuffer offers synchronized thread-safe modifications.",
                "The Java Collections Framework provides unified architecture interfaces such as List, Set, and Map for storing and manipulating data structures.",
                "ArrayList relies on a dynamically resizing array offering fast random access, while LinkedList uses a doubly-linked list optimized for frequent insertions.",
                "HashMap stores key-value pairs using a hashing algorithm, offering average constant-time complexity O(1) for lookup operations.",
                "HashSet stores unique elements backed internally by a HashMap instance, preventing duplicate values from being inserted.",
                "ConcurrentHashMap provides high-concurrency operations by locking bucket segments without locking the entire map during concurrent reads and writes.",
                "Generics enable types to be parameters when defining classes and interfaces, providing compile-time type safety and eliminating explicit casting.",
                "Upper-bounded wildcards (? extends T) restrict unknown types to a specific super type or its subclasses, facilitating safe read-only operations.",
                "A functional interface in Java contains exactly one abstract method and serves as the target type for lambda expressions.",
                "Lambda expressions provide a clear and concise syntax to represent anonymous inner classes implementing functional interfaces.",
                "The Java Stream API processes sequences of elements declaratively through pipeline operations like filter, map, and reduce.",
                "The Optional container object explicitly indicates whether a non-null value is present, mitigating NullPointerException occurrences.",
                "A thread is the smallest unit of execution within a Java process, allowing concurrent execution of multiple tasks.",
                "The synchronized keyword prevents multiple threads from concurrently accessing a shared critical section to prevent race conditions.",
                "The volatile keyword guarantees that updates to a variable are immediately visible across all CPU caches to other active threads.",
                "The Executors framework manages thread pools, separating task submission logic from execution and thread lifecycle management.",
                "CompletableFuture provides asynchronous, non-blocking programming capabilities by chaining multi-stage computations and callbacks.",
                "Java Records (Java 14+) are immutable data carrier classes that automatically generate constructors, accessors, equals, hashCode, and toString methods.",
                "Sealed classes (Java 17+) restrict which specific subclasses or interfaces are permitted to extend or implement them.",
                "Virtual Threads (Java 21) are lightweight, JVM-managed threads designed to scale high-throughput concurrent applications with minimal OS overhead.",
                "Pattern matching for switch allows conditional type extraction and multi-branch matching directly inside switch expressions.",
                "The Java Platform Module System (JPMS) encapsulates packages and explicitly declares component dependencies via module-info.java files.",
                "The Reflection API enables inspection and dynamic invocation of classes, fields, and methods at runtime regardless of access modifiers.",
                "Annotations provide structural metadata about code components that can be processed at compile time or evaluated dynamically at runtime.");
    }
}
