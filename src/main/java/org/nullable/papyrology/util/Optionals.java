package org.nullable.papyrology.util;

import java.util.Optional;
import java.util.function.Supplier;

/** Utilities for working with {@link Optional Optionals}. */
public final class Optionals {

  /**
   * Returns an {@link Optional} that contains the result of the given {@link Supplier} if the given
   * {@code condition} is {@code true} or an empty {@code Optional} otherwise.
   */
  public static <T> Optional<T> of(boolean condition, Supplier<T> supplier) {
    return condition ? Optional.of(supplier.get()) : Optional.empty();
  }

  private Optionals() {}
}
