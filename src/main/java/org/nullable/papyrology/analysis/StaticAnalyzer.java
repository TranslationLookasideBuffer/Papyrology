package org.nullable.papyrology.analysis;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.google.errorprone.annotations.FormatMethod;
import com.google.errorprone.annotations.FormatString;
import org.nullable.papyrology.ast.node.Script;
import org.nullable.papyrology.common.SourceReference;

/** Analyzes a {@link Script} for a particular set of issues. */
public interface StaticAnalyzer {

  /** Run this static analyzer on the given {@link Script}. */
  ImmutableList<Issue> analyze(Script script);

  /** An issue identified by a static analyzer. */
  @AutoValue
  public abstract class Issue {

    /** Identifies the "class" of this {@code Issue}. */
    public enum Category {
      /** Identifies that this {@code Issue} was caused a flaw that prevents compilation. */
      ERROR,

      /**
       * Identifies that this {@code Issue} was caused a flaw doesn't prevent compilation outright,
       * but which probably should be addressed.
       */
      WARNING,

      /**
       * Identifies that this {@code Issue} was caused a flaw (or pattern) that doesn't prevent
       * compilation, but which some developers may want to address.
       */
      PEDANTIC
    }

    /** A particular reference to source that caused this {@code Issue}. */
    public abstract SourceReference getSourceReference();

    /** A message describing the {@code Issue}. */
    public abstract String getMessage();

    /** The "class" of this {@code Issue}. */
    public abstract Category getCategory();

    /**
     * Returns a new {@link Category#ERROR} {@code Issue} with the given {@code SourceReference} and
     * {@code message}.
     */
    @FormatMethod
    public static Issue error(
        SourceReference reference, @FormatString String message, Object... args) {
      return new AutoValue_StaticAnalyzer_Issue(
          reference, String.format(message, args), Category.ERROR);
    }

    /**
     * Returns a new {@link Category#WARNING} {@code Issue} with the given {@code SourceReference}
     * and {@code message}.
     */
    @FormatMethod
    public static Issue warning(
        SourceReference reference, @FormatString String message, Object... args) {
      return new AutoValue_StaticAnalyzer_Issue(
          reference, String.format(message, args), Category.ERROR);
    }

    /**
     * Returns a new {@link Category#PEDANTIC} {@code Issue} with the given {@code SourceReference}
     * and {@code message}.
     */
    @FormatMethod
    public static Issue pedantic(
        SourceReference reference, @FormatString String message, Object... args) {
      return new AutoValue_StaticAnalyzer_Issue(
          reference, String.format(message, args), Category.ERROR);
    }
  }
}
