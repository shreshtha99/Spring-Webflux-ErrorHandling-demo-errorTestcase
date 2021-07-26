package com.errorTesting.SpringWebfluxErrorHandlingdemo;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.io.IOException;

public class ErrorHandlingTest {


@Test
    public void testMono(){
        Mono<?> monoString;
        monoString = Mono.just("ABC")
                .then(Mono.error(new RuntimeException("Error Occured"))).log();
        monoString.subscribe(System.out::println);
    }

    @Test
    public void testFlux(){
        Flux<?> fluxString;
        fluxString=Flux.just("ABC","EFG","PQR")
                .concatWithValues("XYZ")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .concatWithValues("UVW")
                .log();
        fluxString.subscribe(System.out::println);
    }
    @Test
    public void doOnErrorTest(){
        Flux<?> fluxString;
        fluxString=Flux.just("ABC","EFG","PQR")
                .concatWithValues("XYZ")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .doOnError(error-> error.printStackTrace())
                .concatWithValues("UVW")
                .log();
        fluxString.subscribe(System.out::println);
    }
    @Test
    public void onErrorReturn()
    {
        Flux<?> fluxString;
        fluxString=Flux.just("ABC","EFG","PQR")
                .concatWithValues("XYZ")
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .onErrorReturn("Default Value")
                .concatWithValues("UVW")
                .log();
        fluxString.subscribe(System.out::println);

    }

    @Test
    public void onErrorResumeflux(){
        Flux<Integer> fluxInt;
        fluxInt=Flux.just(123,456,789)
                .concatWithValues(Integer.valueOf(012))
                .concatWith(Flux.error(new RuntimeException("Exception Ocurred")))
                .onErrorResume(ex->Flux.range(1,3))
                .concatWithValues(Integer.valueOf(342))
                .log();
        fluxInt.subscribe(System.out::println);


    }
    @Test
     public void onErrorContinueflux(){
         Flux<Integer> fluxInt;
         fluxInt=Flux.just(123,456,789)
                 .concatWithValues(Integer.valueOf(012))
                 .onErrorContinue((throwable, o) -> {System.out.println("Error for an item");})
                 .concatWithValues(Integer.valueOf(342))
                 .log();
         fluxInt.subscribe(System.out::println);


     }

     @Test
     public void errorRetry(){
         Flux<Integer> fluxInt;
         fluxInt=Flux.just(123,456,789)
                 .concatWithValues(Integer.valueOf(012))
                 .onErrorMap(e-> new IOException(e))
                 .retry(10)
                 .concatWithValues(Integer.valueOf(342))
                 .log();
         fluxInt.subscribe(System.out::println);

     }
     @Test
     public void onErrorStopflux(){
         Flux<Integer> fluxInt;
         fluxInt=Flux.just(123,456,789)
                 .concatWithValues(Integer.valueOf(675))
                 .concatWithValues(Integer.valueOf(342))
                 .onErrorStop()
                 .log();
         fluxInt.subscribe(System.out::println);
     }
     @Test
     public void testdofinally(){

         Flux<Integer> fluxInt;
         fluxInt=Flux.just(123,456,789)
                 .concatWith((Flux.error(new RuntimeException("error test"))))
                 .doFinally(i->{
                     if (SignalType.ON_ERROR.equals(i)){
                         System.out.println("Completed with error");
                     }
                     if (SignalType.ON_COMPLETE.equals(i)){
                         System.out.println("Completed without error");
                     }
                 })
                 .log();
         fluxInt.subscribe(System.out::println);

     }
      


    }

