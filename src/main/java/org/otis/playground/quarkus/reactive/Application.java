package org.otis.playground.quarkus.reactive;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Application {
    public static void main(String ... args) {
        System.out.println("Running main method");
        Quarkus.run(args);
    }
}
