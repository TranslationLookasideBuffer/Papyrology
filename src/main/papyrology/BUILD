load("@rules_antlr//antlr:antlr4.bzl", "antlr")

antlr(
    name = "grammar",
    srcs = ["Papyrus.g4"],
    package = "org.nullable.papyrology",
    visibility = ["//visibility:private"],
)

java_library(
    name = "papyrus",
    srcs = [":grammar"],
    visibility = ["//visibility:public"],
    deps = ["@antlr4_runtime//jar"],
)
