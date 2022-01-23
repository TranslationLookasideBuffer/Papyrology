load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "papyrus_antlr",
    srcs = ["Papyrus.g4"],
    package = "org.nullable.papyrology.grammar",
    visitor = True,
)

java_library(
    name = "grammar",
    srcs = [":papyrus_antlr"],
    visibility = ["//visibility:public"],
    deps = ["//src/main/third_party:antlr"],
)
