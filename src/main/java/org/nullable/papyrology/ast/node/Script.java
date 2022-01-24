package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

/** The top-level construct - an entire Papyrus script. */
@AutoValue
public abstract class Script implements Construct {

  /** Returns this script's {@link Header}. */
  public abstract Header getHeader();

  /** Returns this script's {@link Declaration Declarations} in order. */
  public abstract ImmutableList<Declaration> getDeclarations();

  /** Returns a fresh {@code Script} builder. */
  public static Builder builder() {
    return new AutoValue_Script.Builder();
  }

  /** A builder of {@code ScriptNodes}. */
  @AutoValue.Builder
  public abstract static class Builder {
    public abstract Builder setHeader(Header header);

    public abstract Builder setDeclarations(ImmutableList<Declaration> declarations);

    public abstract Script build();
  }
}
