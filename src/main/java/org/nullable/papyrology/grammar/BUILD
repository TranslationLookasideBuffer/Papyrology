genrule(
    name = "papyrus_antlr",
    srcs = ["Papyrus.g4"],
    outs = [
        "PapyrusBaseListener.java",
        "PapyrusBaseVisitor.java",
        "PapyrusLexer.java",
        "PapyrusListener.java",
        "PapyrusParser.java",
        "PapyrusVisitor.java",
    ],
    cmd = "$(JAVA) -jar $(execpath @antlr//jar) " +
          "-Werror -visitor -listener -long-messages " +
          "-package org.nullable.papyrology.grammar " +
          "-o $(BINDIR) $<",
    toolchains = ["@bazel_tools//tools/jdk:current_java_runtime"],
    tools = ["@antlr//jar"],
)

java_library(
    name = "grammar",
    srcs = [
        "package-info.java",
        ":papyrus_antlr",
    ],
    visibility = ["//visibility:public"],
    deps = [
        "//src/main/third_party:antlr",
        "//src/main/third_party:errorprone",
    ],
)
