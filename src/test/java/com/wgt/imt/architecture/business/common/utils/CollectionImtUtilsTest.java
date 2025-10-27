package com.wgt.imt.architecture.business.common.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CollectionImtUtils")
class CollectionImtUtilsTest {

    @Nested
    @DisplayName("append(Collection<T>, T)")
    class AppendSingleElement {

        @Test
        @DisplayName("Devrait ajouter un élément à une collection non vide")
        void shouldAppendElementToNonEmptyCollection() {
            // Given
            Collection<String> source = Arrays.asList("element1", "element2");
            String newElement = "element3";

            // When
            Collection<String> result = CollectionImtUtils.append(source, newElement);

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains("element1"));
            assertTrue(result.contains("element2"));
            assertTrue(result.contains("element3"));
        }

        @Test
        @DisplayName("Devrait ajouter un élément à une collection vide")
        void shouldAppendElementToEmptyCollection() {
            // Given
            Collection<String> source = Collections.emptyList();
            String newElement = "element1";

            // When
            Collection<String> result = CollectionImtUtils.append(source, newElement);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.contains("element1"));
        }

        @Test
        @DisplayName("Devrait ajouter un élément à une collection nulle")
        void shouldAppendElementToNullCollection() {
            // Given
            Collection<String> source = null;
            String newElement = "element1";

            // When
            Collection<String> result = CollectionImtUtils.append(source, newElement);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.contains("element1"));
        }
    }

    @Nested
    @DisplayName("append(Collection<T>, T...)")
    class AppendVarArgs {

        @Test
        @DisplayName("Devrait ajouter plusieurs éléments à une collection non vide")
        void shouldAppendMultipleElementsToNonEmptyCollection() {
            // Given
            Collection<String> source = Arrays.asList("element1");
            String element2 = "element2";
            String element3 = "element3";

            // When
            Collection<String> result = CollectionImtUtils.append(source, element2, element3);

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains("element1"));
            assertTrue(result.contains("element2"));
            assertTrue(result.contains("element3"));
        }

        @Test
        @DisplayName("Devrait ajouter plusieurs éléments à une collection vide")
        void shouldAppendMultipleElementsToEmptyCollection() {
            // Given
            Collection<String> source = Collections.emptyList();

            // When
            Collection<String> result = CollectionImtUtils.append(source, "element1", "element2");

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains("element1"));
            assertTrue(result.contains("element2"));
        }

        @Test
        @DisplayName("Devrait gérer une collection nulle avec plusieurs éléments")
        void shouldHandleNullCollectionWithMultipleElements() {
            // Given
            Collection<String> source = null;

            // When
            Collection<String> result = CollectionImtUtils.append(source, "element1", "element2", "element3");

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
        }
    }

    @Nested
    @DisplayName("append(Collection<T>, Collection<T>)")
    class AppendCollection {

        @Test
        @DisplayName("Devrait ajouter une collection d'éléments à une collection non vide")
        void shouldAppendCollectionToNonEmptyCollection() {
            // Given
            Collection<String> source = Arrays.asList("element1");
            Collection<String> toAdd = Arrays.asList("element2", "element3");

            // When
            Collection<String> result = CollectionImtUtils.append(source, toAdd);

            // Then
            assertNotNull(result);
            assertEquals(3, result.size());
            assertTrue(result.contains("element1"));
            assertTrue(result.contains("element2"));
            assertTrue(result.contains("element3"));
        }

        @Test
        @DisplayName("Devrait ajouter une collection vide à une collection non vide")
        void shouldAppendEmptyCollectionToNonEmptyCollection() {
            // Given
            Collection<String> source = Arrays.asList("element1");
            Collection<String> toAdd = Collections.emptyList();

            // When
            Collection<String> result = CollectionImtUtils.append(source, toAdd);

            // Then
            assertNotNull(result);
            assertEquals(1, result.size());
            assertTrue(result.contains("element1"));
        }

        @Test
        @DisplayName("Devrait gérer une source nulle avec une collection à ajouter")
        void shouldHandleNullSourceWithCollection() {
            // Given
            Collection<String> source = null;
            Collection<String> toAdd = Arrays.asList("element1", "element2");

            // When
            Collection<String> result = CollectionImtUtils.append(source, toAdd);

            // Then
            assertNotNull(result);
            assertEquals(2, result.size());
            assertTrue(result.contains("element1"));
            assertTrue(result.contains("element2"));
        }
    }
}

