package org.nullable.papyrology.ast.node;

import com.google.auto.value.AutoValue;
import java.util.Optional;
import org.nullable.papyrology.grammar.PapyrusParser.HeaderContext;

/** Metadata about a single script. */
@AutoValue
public abstract class Header implements Construct {

  /** Returns this script's {@code Identifier}. */
  public abstract Identifier getScriptIdentifier();

  /**
   * Returns the {@code Identifier} of the script that this script extends, if present (i.e. this
   * script's parent).
   */
  public abstract Optional<Identifier> getParentScriptIdentifier();

  /** Returns whether or not this script is hidden. */
  public abstract boolean isHidden();

  /** Returns whether or not this script is conditional. */
  public abstract boolean isConditional();

  /** Returns the content of this script's documentation comment if present. */
  public abstract Optional<String> getScriptComment();

  /** Returns a new {@code Header} based on the given {@link HeaderContext}. */
  public static Header create(HeaderContext ctx) {
    Builder header =
        builder()
            .setScriptIdentifier(Identifier.create(ctx.ID(0)))
            .setHidden(!ctx.F_HIDDEN().isEmpty())
            .setConditional(!ctx.F_CONDITIONAL().isEmpty());
    if (ctx.ID().size() > 1) {
      header.setParentScriptIdentifier(Identifier.create(ctx.ID(1)));
    }
    if (ctx.docComment() != null) {
      header.setScriptComment(ctx.docComment().DOC_COMMENT().getSymbol().getText());
    }
    return header.build();
  }

  /** Returns a fresh {@code Header} builder. */
  static Builder builder() {
    return new AutoValue_Header.Builder();
  }

  /** A builder of {@code Headers}. */
  @AutoValue.Builder
  abstract static class Builder {
    abstract Builder setScriptIdentifier(Identifier id);

    abstract Builder setParentScriptIdentifier(Identifier id);

    abstract Builder setHidden(boolean isHidden);

    abstract Builder setConditional(boolean isConditional);

    abstract Builder setScriptComment(String comment);

    abstract Header build();
  }
}
