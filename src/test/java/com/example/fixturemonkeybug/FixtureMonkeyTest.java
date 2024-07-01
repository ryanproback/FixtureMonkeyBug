package com.example.fixturemonkeybug;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import com.navercorp.fixturemonkey.customizer.Values;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.RepeatedTest;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class FixtureMonkeyTest {


    @RepeatedTest(100)
    void testFail() {
        FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                                                   .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                                                   .build();

        // 간혈적인 FixedValueFilterMissException 발생
        TestFailObject actual = fixtureMonkey.giveMeBuilder(TestFailObject.class)
                                             .set("elements", Values.just(new HashSet<>()))
                                             .setNull("referenceTestObjectElement")
                                             .sample();

        assertThat(actual).isNotNull();
    }

    @RepeatedTest(100)
    void testSuccess() {
        FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                                                   .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                                                   .build();

        TestSuccessObject actual = fixtureMonkey.giveMeBuilder(TestSuccessObject.class)
                                             .set("elements", Values.just(new HashSet<>()))
                                             .sample();

        assertThat(actual).isNotNull();
    }


    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class TestFailObject {
        private Set<TestElement> elements;
        private ReferenceTestObjectElement referenceTestObjectElement;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class TestSuccessObject {
        private Set<TestElement> elements;
    }





    @Getter
    @Builder
    static class TestElement {
        private String value;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    static class ReferenceTestObjectElement {
        private String value;
        private TestFailObject testFailObject;
    }
}
