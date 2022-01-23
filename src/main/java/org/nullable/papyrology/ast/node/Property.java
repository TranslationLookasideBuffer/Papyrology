package org.nullable.papyrology.ast.node;

import static com.google.common.base.Preconditions.checkState;

import com.google.auto.value.AutoValue;
import java.util.Optional;

/** A {@link Declaration} that defines a property. */
@AutoValue
public abstract class Property implements Declaration {

  /** Returns the {@link Type} of this property. */
  public abstract Type getType();

  /** Returns the {@link Identifier} of this property. */
  public abstract Identifier getIdentifier();

  /** Returns the {@link Literal} that defines this property's default value, if present. */
  public abstract Optional<Literal> getDefaultValueLiteral();

  /**
   * Returns the {@link Function} that defines this property's setter, if present.
   *
   * <p>This {@code Function} will not have a comment or return {@code Type}, will have an {@code
   * Identifier} equivalent to "Set", and will have exactly one {@code Parameter} which in turn has
   * the same {@code Type} as this property.
   */
  public abstract Optional<Function> getSetFunction();

  /**
   * Returns the {@link Function} that defines this property's getter, if present.
   *
   * <p>This {@code Function} will not have a comment or any {@code Parameters} and will have an
   * {@code Identifier} equivalent to "Get" and a return {@code Type} that is the same as this
   * property.
   */
  public abstract Optional<Function> getGetFunction();

  /** Returns the documentation comment of this property, if present. */
  public abstract Optional<String> getComment();

  /**
   * Returns whether or not this property is an auto property.
   *
   * <p>If true, {@link #getSetFunction()} and {@link #getGetFunction()} will both return empty.
   * Likewise, {@link #isAutoReadOnly()} will return false.
   */
  public abstract boolean isAuto();

  /**
   * Returns whether or not this property is an auto read-only property.
   *
   * <p>If true, {@link #getSetFunction()} and {@link #getGetFunction()} will both return empty.
   * Likewise, {@link #isAuto()} and {@link #isConditional()} will return false. {@link
   * #getDefaultValueLiteral()} will return a present {@link Literal}.
   */
  public abstract boolean isAutoReadOnly();

  /** Returns whether or not this property is hidden. */
  public abstract boolean isHidden();

  /** Returns whether or not this property is hidden. */
  public abstract boolean isConditional();

  /** Returns a fresh {@code Property} builder. */
  static Builder builder() {
    return new AutoValue_Property.Builder();
  }

  /** A builder of {@code Properties}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setType(Type type);

    abstract Builder setIdentifier(Identifier id);

    abstract Builder setDefaultValueLiteral(Literal defaultValueLiteral);

    abstract Builder setSetFunction(Function function);

    abstract Builder setGetFunction(Function function);

    abstract Builder setComment(String comment);

    abstract Builder setAuto(boolean isAuto);

    abstract Builder setAutoReadOnly(boolean isAutoReadOnly);

    abstract Builder setHidden(boolean isHidden);

    abstract Builder setConditional(boolean isConditional);

    abstract Property autoBuild();

    final Property build() {
      Property property = autoBuild();
      if (property.isAuto()) {
        checkState(!property.isAutoReadOnly(), "Property cannot be both Auto and AutoReadOnly");
        checkState(
            property.getSetFunction().isEmpty(), "Auto Property cannot specify Set function");
        checkState(
            property.getGetFunction().isEmpty(), "Auto Property cannot specify Get function");
      } else if (property.isAutoReadOnly()) {
        checkState(!property.isConditional(), "AutoReadOnly Property cannot be conditional");
        checkState(
            property.getDefaultValueLiteral().isPresent(),
            "AutoReadOnly Property must specify a default value Literal");
        checkState(
            property.getSetFunction().isEmpty(),
            "AutoReadOnly Property cannot specify Set function");
        checkState(
            property.getGetFunction().isEmpty(),
            "AutoReadOnly Property cannot specify Get function");
      } else {
        checkState(
            property.getGetFunction().isPresent() || property.getSetFunction().isPresent(),
            "Full Property must specify at least a Get or Set function");
      }
      return property;
    }
  }
}
